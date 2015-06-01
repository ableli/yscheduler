package com.yeahmobi.yscheduler.executor.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.agentframework.AgentRequest;
import com.yeahmobi.yscheduler.agentframework.AgentResponse;
import com.yeahmobi.yscheduler.agentframework.AgentResponseCode;
import com.yeahmobi.yscheduler.agentframework.agent.event.shell.ShellTaskSubmitionEventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.event.task.CalloutEventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.event.task.TaskCancellationEventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.event.task.TaskStatusEventHandler;
import com.yeahmobi.yscheduler.agentframework.agent.task.AbstractAgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskStatus;
import com.yeahmobi.yscheduler.agentframework.agent.task.TaskTransactionStatus;
import com.yeahmobi.yscheduler.agentframework.agent.task.async.CalloutAgentTask;
import com.yeahmobi.yscheduler.agentframework.agent.task.shell.ShellAgentTask;
import com.yeahmobi.yscheduler.agentframework.client.DefaultAgentClient;
import com.yeahmobi.yscheduler.agentframework.exception.AgentClientException;
import com.yeahmobi.yscheduler.common.Constants;
import com.yeahmobi.yscheduler.common.variable.VariableException;
import com.yeahmobi.yscheduler.common.variable.VariableManager;
import com.yeahmobi.yscheduler.executor.AttemptExecutor;
import com.yeahmobi.yscheduler.loadbalance.AgentLoadbalance;
import com.yeahmobi.yscheduler.model.Agent;
import com.yeahmobi.yscheduler.model.Attempt;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowInstance;
import com.yeahmobi.yscheduler.model.service.AgentService;
import com.yeahmobi.yscheduler.model.service.AttemptService;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.UserService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.AttemptStatus;
import com.yeahmobi.yscheduler.notice.NoticeService;
import com.yeahmobi.yscheduler.variable.DefaultVariableContext;

@Service
public class DefaultAttemptExecutor implements AttemptExecutor {

    private static final Logger     LOGGER          = LoggerFactory.getLogger(DefaultAttemptExecutor.class);

    ExecutorService                 executorService = Executors.newCachedThreadPool(new CustomizableThreadFactory(
                                                                                                                  "attempt-pool-"));

    /** 以<instanceId,Pair>键值对在内存中存放运行中的Attempt，便于InstanceExecutor查询Attempt状态 */
    private Map<Long, Pair>         attemptMap      = new ConcurrentHashMap<Long, Pair>();

    @Autowired
    private TaskService             taskService;

    @Autowired
    private TaskInstanceService     taskInstanceService;

    @Autowired
    private AttemptService          attemptService;

    @Autowired
    private WorkflowService         workflowService;

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Autowired
    private AgentService            agentService;

    @Autowired
    private VariableManager         variableManager;

    @Autowired
    private AgentLoadbalance        agentLoadbalance;

    @Autowired
    private UserService             userService;

    @Autowired
    private NoticeService           noticeService;

    private AtomicBoolean           closed          = new AtomicBoolean(false);

    private String                  attachmentServerUri;

    @Value("#{confProperties['storageServerUri']}")
    private String                  storageServerUri;

    @PostConstruct
    public void init() {
        this.attachmentServerUri = this.storageServerUri + "/download";
        // 从db中加载上次未结束的attempt
        List<Attempt> attemptList = this.attemptService.getAllUncompleteds();
        for (Attempt attempt : attemptList) {
            Pair pair = buildPair(attempt);
            if (pair != null) {
                putPair(pair);
                this.executorService.submit(new InnerTask(pair));
            }
        }
    }

    private TaskStatus checkStatus(Pair pair) throws AgentClientException {
        DefaultAgentClient agentClient = pair.agentClient;
        String host = pair.host;
        long txId = pair.txId;

        Map<String, String> params = new HashMap<String, String>();
        params.put("txId", String.valueOf(txId));

        AgentResponse<TaskStatus> res = agentClient.call(host, new AgentRequest(TaskStatusEventHandler.EVENT_TYPE,
                                                                                params));

        if (AgentResponseCode.SUCCESS.equals(res.getResponseCode())) {
            TaskStatus taskStatus = res.getResponseData();
            return taskStatus;
        } else {
            throw new AgentClientException("Agent return failed when check status, response code is "
                                           + res.getResponseCode() + ", errorMsg is " + res.getErrorMsg());
        }
    }

    private long submitToAgent(Pair pair, Map<String, String> params) throws AgentClientException {
        DefaultAgentClient agentClient = pair.agentClient;
        String host = pair.host;

        String eventType = getEventType(pair.task);
        AgentResponse<Long> res = agentClient.call(host, new AgentRequest(eventType, params));

        if (AgentResponseCode.SUCCESS.equals(res.getResponseCode())) {
            long txId = res.getResponseData();
            return txId;
        } else {
            throw new AgentClientException("Agent return failed when submit shellcmd, response code is "
                                           + res.getResponseCode() + ", errorMsg is " + res.getErrorMsg());
        }

    }

    private String getEventType(Task task) {
        switch (task.getType()) {
            case HTTP:
                return CalloutEventHandler.EVENT_TYPE;
            case SHELL:
            default:
                return ShellTaskSubmitionEventHandler.EVENT_TYPE;
        }
    }

    private Map<String, String> getParams(Pair pair) throws VariableException {
        Map<String, String> params = new HashMap<String, String>();
        String command = pair.task.getCommand();
        DefaultVariableContext context = new DefaultVariableContext();
        context.setTask(pair.task);
        context.setTaskIntance(pair.instance);
        context.setWorkflowInstance(pair.workflowInstance);
        context.setWorkflow(pair.workflow);
        command = this.variableManager.process(command, context);
        params.put(AbstractAgentTask.PARAM_TASKNAME, pair.task.getName());
        String host = (StringUtils.contains(pair.host, ':')) ? pair.host : pair.host + ":"
                                                                           + Constants.DEFAULT_AGENT_PORT;
        params.put(AbstractAgentTask.PARAM_AGENT_HOST, host);
        params.put(AbstractAgentTask.PARAM_ATTMEPT_ID, String.valueOf(pair.attempt.getId()));
        if (pair.workflow != null) {
            params.put(AbstractAgentTask.PARAM_WORKFLOW_NAME, pair.workflow.getName());
        }
        switch (pair.task.getType()) {
            case HTTP:
                // calloutUrl;needCallback;cancelUrl
                // 含calloutUrl,needCallback,agentHost,cancelUrl
                String[] splits = StringUtils.split(command, ';');
                String calloutUrl = splits[0];
                params.put(CalloutAgentTask.PARAM_CALLOUT_URL, calloutUrl);
                if (splits.length > 1) {
                    String needCallback = splits[1];
                    params.put(CalloutAgentTask.PARAM_NEED_CALLBACK, needCallback);
                }
                if (splits.length > 2) {
                    String cancelUrl = splits[2];
                    params.put(CalloutAgentTask.PARAM_CANCEL_URL, cancelUrl);
                }
            case SHELL:
            default:
                if ((pair.task.getAttachmentVersion() != null) && (pair.task.getAttachmentVersion() > 0)) {
                    params.put(ShellAgentTask.PARAM_ATTACHMENT_SERVER_URI, this.attachmentServerUri);
                    params.put(ShellAgentTask.PARAM_ATTACHMENT_VERSION,
                               String.valueOf(pair.task.getAttachmentVersion()));
                }
                params.put(ShellAgentTask.PARAM_SHELL_COMMAND, command);
        }

        return params;
    }

    @PreDestroy
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.executorService.shutdownNow();
        }
    }

    private Pair buildPair(Attempt attempt) {
        long instanceId = attempt.getInstanceId();
        long taskId = attempt.getTaskId();
        TaskInstance instance = this.taskInstanceService.get(instanceId);
        Task task = this.taskService.get(taskId);

        // 有可能这个attempt对应的task已经删除
        if (task == null) {
            return null;
        }

        Workflow workflow = null;
        WorkflowInstance workflowInstance = null;
        if (instance.getWorkflowInstanceId() != null) {
            workflowInstance = this.workflowInstanceService.get(instance.getWorkflowInstanceId());
            workflow = this.workflowService.get(workflowInstance.getWorkflowId());
        }

        Pair pair = new Pair(task, instance, attempt, workflow, workflowInstance);
        return pair;
    }

    private void putPair(Pair pair) {
        TaskInstance instance = pair.instance;
        this.attemptMap.put(instance.getId(), pair);
    }

    public void submit(final Attempt attempt) {
        Pair pair = buildPair(attempt);
        if (pair != null) {
            this.attemptService.save(attempt);
            putPair(pair);
            this.executorService.submit(new InnerTask(pair));
        }
    }

    private static class Pair {

        Task               task;
        Attempt            attempt;
        TaskInstance       instance;
        Workflow           workflow;
        WorkflowInstance   workflowInstance;
        DefaultAgentClient agentClient;
        String             host;
        long               txId;

        public Pair(Task task, TaskInstance instance, Attempt attempt, Workflow workflow,
                    WorkflowInstance workflowInstance) {
            super();
            this.task = task;
            this.instance = instance;
            this.attempt = attempt;
            this.workflow = workflow;
            this.workflowInstance = workflowInstance;
        }
    }

    public boolean isRunning(long instanceId) {
        Pair pair = this.attemptMap.get(instanceId);
        return (pair != null) && (pair.attempt.getStatus() == AttemptStatus.RUNNING);
    }

    /**
     * 负责执行一个attempt
     */
    private class InnerTask implements Runnable {

        private static final int RETRY_INTERVAL_RATIO_ON_SUBMIT_AGENT_FAILED = 5;

        private static final int FETCH_STATUS_INTERVAL_SECONDS               = 5;

        private static final int SUBMIT_AGENT_MAX_TRY_COUNT                  = 3;

        private final Pair       pair;

        public InnerTask(Pair pair) {
            this.pair = pair;
        }

        // 保存log到本地数据库
        private void appendLog(String msg, boolean appendLineSeparator) {
            writeLog(msg, appendLineSeparator, true);
        }

        // 保存log到本地数据库
        private void writeLog(String msg, boolean appendLineSeparator, boolean append) {
            if (appendLineSeparator) {
                msg += IOUtils.LINE_SEPARATOR;
            }
            if (append) {
                String output = this.pair.attempt.getOutput() == null ? "" : this.pair.attempt.getOutput();
                this.pair.attempt.setOutput(output + msg);
            } else {
                this.pair.attempt.setOutput(msg);
            }
            DefaultAttemptExecutor.this.attemptService.update(this.pair.attempt);
        }

        public void run() {

            try {
                // 执行attempt
                Attempt attempt = this.pair.attempt;

                try {
                    Agent agent = getAgent(attempt);
                    buildAgantClient(agent);

                    // 写一个文件头进去log中
                    writeLog(String.format("Attempt(taskId:%s,attemptId:%s) started at %s, submit to agent(id=%s, host=%s)",
                                           this.pair.task.getId(), attempt.getId(),
                                           DateFormatUtils.format(new Date(), Constants.DATE_FORMAT_PATTERN),
                                           agent.getId(), agent.getIp()), true, false);

                    // 没有txId，说明是新提交(不是从db load的attempt)，需要提交
                    if (attempt.getTransactionId() == null) {
                        Map<String, String> params = getParams(this.pair);

                        int tryCount = 1;
                        do {
                            try {
                                long transactionId = submitToAgent(this.pair, params);
                                attempt.setTransactionId(transactionId);
                            } catch (AgentClientException e) {
                                String msg = "Submit to agent failed for " + tryCount + " times.";
                                if (tryCount >= SUBMIT_AGENT_MAX_TRY_COUNT) {
                                    attempt.setStatus(AttemptStatus.FAILED);
                                    throw new AgentClientException(msg + " It is considered failed.", e);
                                } else {
                                    appendLog(msg, true);
                                    TimeUnit.SECONDS.sleep(tryCount * RETRY_INTERVAL_RATIO_ON_SUBMIT_AGENT_FAILED);
                                }
                                tryCount++;
                            }
                        } while (attempt.getTransactionId() == null);

                        DefaultAttemptExecutor.this.attemptService.update(attempt);
                    }

                    this.pair.txId = attempt.getTransactionId();

                    TaskStatus agentStatus = null;

                    long failedStartTime = -1;

                    // 提交任务之后delay一下再去checkStatus
                    TimeUnit.SECONDS.sleep(1);

                    do {
                        try {
                            agentStatus = checkStatus(this.pair);

                            attempt.setDuration(agentStatus.getDuration());
                            DefaultAttemptExecutor.this.attemptService.update(attempt);
                            failedStartTime = -1;
                        } catch (Exception e) {
                            if (failedStartTime == -1) {
                                failedStartTime = System.currentTimeMillis();
                            } else {
                                if ((System.currentTimeMillis() - failedStartTime) > (5 * 60 * 1000L)) {
                                    throw new AgentClientException("Get status/log from agent failed for 5 minutes.", e);
                                }
                            }
                        } finally {
                            // sleep park
                            if ((agentStatus != null) && !agentStatus.getStatus().isCompleted()) {
                                TimeUnit.SECONDS.sleep(FETCH_STATUS_INTERVAL_SECONDS);
                            }
                        }

                    } while ((agentStatus == null) || ((agentStatus != null) && !agentStatus.getStatus().isCompleted()));

                    // 正常结束
                    attempt.setStatus(tranformStatus(agentStatus.getStatus()));
                    attempt.setReturnValue(agentStatus.getReturnValue());

                } catch (VariableException e) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    appendLog(StringUtils.trimToEmpty(e.getMessage()), true);
                    appendLog(baos.toString(), true);
                    if ((attempt.getStatus() == null) || !attempt.getStatus().isCompleted()) {
                        attempt.setStatus(AttemptStatus.COMPLETE_WITH_UNKNOWN_STATUS);
                    }
                } catch (AgentClientException e) {
                    // 异常结束
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    appendLog(StringUtils.trimToEmpty(e.getMessage()), true);
                    appendLog(baos.toString(), true);
                    if ((attempt.getStatus() == null) || !attempt.getStatus().isCompleted()) {
                        attempt.setStatus(AttemptStatus.COMPLETE_WITH_UNKNOWN_STATUS);
                    }
                } catch (IllegalStateException e) {
                    // (获取不到agent)异常结束
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    appendLog(StringUtils.trimToEmpty(e.getMessage()), true);
                    appendLog(baos.toString(), true);
                    attempt.setStatus(AttemptStatus.FAILED);
                } finally {
                    attempt.setEndTime(new Date());
                }

                // 将结果写回db
                DefaultAttemptExecutor.this.attemptService.update(attempt);
                // 移除
                DefaultAttemptExecutor.this.attemptMap.remove(attempt.getInstanceId());

            } catch (InterruptedException e) {
                // 一般是关闭程序时才会关闭线程池，才会中断，故不做任何事情，让线程结束
            }
        }

        private void buildAgantClient(Agent agent) {
            String ip = agent.getIp();
            String[] split = StringUtils.split(ip, ':');
            String host = split[0];
            int port = -1;
            if (split.length > 1) {
                port = Integer.parseInt(split[1]);
            }
            DefaultAgentClient agentClient = new DefaultAgentClient(port, Constants.AGENT_CONTEXT);
            if (port > 0) {
                agentClient = new DefaultAgentClient(port, Constants.AGENT_CONTEXT);
            } else {
                agentClient = new DefaultAgentClient(Constants.AGENT_CONTEXT);
            }

            this.pair.agentClient = agentClient;
            this.pair.host = host;
        }

        private Agent getAgent(Attempt attempt) {
            Agent agent = null;
            if (attempt.getAgentId() != null) {
                agent = DefaultAttemptExecutor.this.agentService.get(attempt.getAgentId());
            } else {
                // 如果不存在agentId，则从team内通过lb获取
                User user = DefaultAttemptExecutor.this.userService.get(this.pair.task.getOwner());
                agent = DefaultAttemptExecutor.this.agentLoadbalance.getActiveAgent(user.getTeamId());
                attempt.setAgentId(agent.getId());
            }
            return agent;
        }

    }

    public void cancel(long instanceId) {
        Pair pair = this.attemptMap.get(instanceId);

        DefaultAgentClient agentClient = pair.agentClient;
        if (agentClient == null) {
            return;
        }
        String host = pair.host;
        long txId = pair.txId;

        Map<String, String> params = new HashMap<String, String>();
        params.put("txId", String.valueOf(txId));

        AgentResponse<TaskStatus> res;
        try {
            res = agentClient.call(host, new AgentRequest(TaskCancellationEventHandler.EVENT_TYPE, params));
            if (!AgentResponseCode.SUCCESS.equals(res.getResponseCode())) {
                throw new AgentClientException("Agent return failed when cancel, response code is "
                                               + res.getResponseCode() + ", errorMsg is " + res.getErrorMsg());
            }
        } catch (AgentClientException e) {
            // 网络异常
            LOGGER.error("Error when cancelling agent", e);
        }

    }

    public AttemptStatus tranformStatus(TaskTransactionStatus status) {
        switch (status) {
            case CANCEL:
                return AttemptStatus.CANCELLED;
            case SUCCESS:
                return AttemptStatus.SUCCESS;
            case FAIL:
                return AttemptStatus.FAILED;
            case COMPLETE_WITH_UNKNOWN_STATUS:
                return AttemptStatus.COMPLETE_WITH_UNKNOWN_STATUS;
            case RUNNING:
                return AttemptStatus.RUNNING;
            default:
                return AttemptStatus.RUNNING;
        }
    }
}
