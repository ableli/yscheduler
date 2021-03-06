package com.yeahmobi.yscheduler.web.controller.task;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.CrontabUtils;
import com.yeahmobi.yscheduler.model.Agent;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.AgentService;
import com.yeahmobi.yscheduler.model.type.DependingStatus;
import com.yeahmobi.yscheduler.model.type.TaskStatus;
import com.yeahmobi.yscheduler.model.type.TaskType;

/**
 * @author Ryan Sun
 */
@Service
public class TaskHelper {

    @Autowired
    private AgentService agentService;

    public Task extractTaskFromRequest(HttpServletRequest request, boolean isNew) {
        Task result = new Task();

        String retry = request.getParameter("retryTimes");
        if (StringUtils.isNotBlank(retry) && StringUtils.isNumeric(retry)) {
            int retryTimes = Integer.parseInt(retry);
            if (retryTimes < 0) {
                result.setRetryTimes(0);
            } else {
                result.setRetryTimes(retryTimes);
            }
        } else {
            result.setRetryTimes(0);
        }

        String timeoutStr = request.getParameter("timeout");
        if (StringUtils.isNotBlank(timeoutStr) && StringUtils.isNumeric(timeoutStr)) {
            int timeout = Integer.parseInt(timeoutStr);
            if (timeout < 0) {
                result.setTimeout(0);
            } else {
                result.setTimeout(timeout);
            }
        } else {
            result.setTimeout(0);
        }

        int type = Integer.parseInt(request.getParameter("type"));
        result.setType(TaskType.valueOf(type));

        if (isNew) {
            String name = request.getParameter("name");
            Validate.isTrue(StringUtils.isNotBlank(name), "作业名称不能为空！");
            Validate.isFalse(StringUtils.startsWith(name, "_"), "作业名称不能以下划线开头！");
            Validate.isTrue(Pattern.matches("^[0-9a-zA-Z|_|\\.|\\-|(|)]*$", name),
                            "名称命名不合法！名称中可使用的字符包括：数字、字母、下划线、横线、点和括号,不能以下划线开头！");
            result.setName(name);
            long owner = UserContextHolder.getUserContext().getId();
            result.setOwner(owner);
        } else {
            result.setId(Long.parseLong(request.getParameter("id")));
        }
        String crontab = request.getParameter("crontab");
        String schedule = request.getParameter("schedule");
        // 打开调度
        if ("clock".equals(schedule)) {
            crontab = CrontabUtils.normalize(crontab, false);
            result.setCrontab(crontab);
            result.setStatus(TaskStatus.OPEN);
        } else {// 暂停调度
            result.setStatus(TaskStatus.PAUSED);
        }

        String canSkip = request.getParameter("canSkip");
        if (StringUtils.equals("true", canSkip)) {
            result.setCanSkip(true);
            result.setLastStatusDependency(DependingStatus.NONE);
        } else {
            result.setCanSkip(false);
            String concurrent = request.getParameter("concurrent");
            if (StringUtils.equals("true", concurrent)) {
                result.setLastStatusDependency(DependingStatus.NONE);
            } else {
                String condition = request.getParameter("condition");
                if (DependingStatus.COMPLETED.name().equalsIgnoreCase(condition)) {
                    result.setLastStatusDependency(DependingStatus.COMPLETED);
                } else if (DependingStatus.SUCCESS.name().equalsIgnoreCase(condition)) {
                    result.setLastStatusDependency(DependingStatus.SUCCESS);
                }
            }
        }

        switch (result.getType()) {
            case HTTP: {
                // agent
                List<Agent> inPlatform = this.agentService.listInPlatform();
                Validate.isTrue(inPlatform.size() > 0, "platform agent 为空，无法创建http任务，请联系管理员！");
                result.setAgentId(inPlatform.get(0).getId());
                // command
                String calloutUrl = request.getParameter("calloutUrl");
                Validate.isTrue(StringUtils.isNotBlank(calloutUrl), "'调用url'不能为空！");
                String cancelUrl = StringUtils.trimToNull(request.getParameter("cancelUrl"));
                if (cancelUrl == null) {
                    cancelUrl = "";
                }
                boolean needCallback = BooleanUtils.toBoolean(request.getParameter("needCallback"));
                result.setCommand(calloutUrl + ';' + needCallback + ';' + cancelUrl);
            }
                break;
            case SHELL: { // agent
                String agent = request.getParameter("agent");
                if (agent != null) {
                    long agentId = Long.parseLong(agent);
                    result.setAgentId(agentId);
                }

                // command
                String command = request.getParameter("command");
                Validate.isTrue(StringUtils.isNotBlank(command), "命令不能为空！");
                result.setCommand(command);
            }
                break;
        }

        result.setDescription(request.getParameter("description"));
        return result;
    }
}
