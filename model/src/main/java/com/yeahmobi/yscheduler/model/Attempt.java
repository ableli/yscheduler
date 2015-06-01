package com.yeahmobi.yscheduler.model;

import com.yeahmobi.yscheduler.model.type.AttemptStatus;
import java.util.Date;

public class Attempt {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.task_id
     *
     * @mbggenerated
     */
    private Long taskId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.instance_id
     *
     * @mbggenerated
     */
    private Long instanceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.agent_id
     *
     * @mbggenerated
     */
    private Long agentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.transaction_id
     *
     * @mbggenerated
     */
    private Long transactionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.status
     *
     * @mbggenerated
     */
    private AttemptStatus status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.active
     *
     * @mbggenerated
     */
    private Boolean active;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.return_value
     *
     * @mbggenerated
     */
    private Integer returnValue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.start_time
     *
     * @mbggenerated
     */
    private Date startTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.end_time
     *
     * @mbggenerated
     */
    private Date endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.duration
     *
     * @mbggenerated
     */
    private Long duration;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attempt.output
     *
     * @mbggenerated
     */
    private String output;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.id
     *
     * @return the value of attempt.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.id
     *
     * @param id the value for attempt.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.task_id
     *
     * @return the value of attempt.task_id
     *
     * @mbggenerated
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.task_id
     *
     * @param taskId the value for attempt.task_id
     *
     * @mbggenerated
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.instance_id
     *
     * @return the value of attempt.instance_id
     *
     * @mbggenerated
     */
    public Long getInstanceId() {
        return instanceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.instance_id
     *
     * @param instanceId the value for attempt.instance_id
     *
     * @mbggenerated
     */
    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.agent_id
     *
     * @return the value of attempt.agent_id
     *
     * @mbggenerated
     */
    public Long getAgentId() {
        return agentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.agent_id
     *
     * @param agentId the value for attempt.agent_id
     *
     * @mbggenerated
     */
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.transaction_id
     *
     * @return the value of attempt.transaction_id
     *
     * @mbggenerated
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.transaction_id
     *
     * @param transactionId the value for attempt.transaction_id
     *
     * @mbggenerated
     */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.status
     *
     * @return the value of attempt.status
     *
     * @mbggenerated
     */
    public AttemptStatus getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.status
     *
     * @param status the value for attempt.status
     *
     * @mbggenerated
     */
    public void setStatus(AttemptStatus status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.active
     *
     * @return the value of attempt.active
     *
     * @mbggenerated
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.active
     *
     * @param active the value for attempt.active
     *
     * @mbggenerated
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.return_value
     *
     * @return the value of attempt.return_value
     *
     * @mbggenerated
     */
    public Integer getReturnValue() {
        return returnValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.return_value
     *
     * @param returnValue the value for attempt.return_value
     *
     * @mbggenerated
     */
    public void setReturnValue(Integer returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.start_time
     *
     * @return the value of attempt.start_time
     *
     * @mbggenerated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.start_time
     *
     * @param startTime the value for attempt.start_time
     *
     * @mbggenerated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.end_time
     *
     * @return the value of attempt.end_time
     *
     * @mbggenerated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.end_time
     *
     * @param endTime the value for attempt.end_time
     *
     * @mbggenerated
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.duration
     *
     * @return the value of attempt.duration
     *
     * @mbggenerated
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.duration
     *
     * @param duration the value for attempt.duration
     *
     * @mbggenerated
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.create_time
     *
     * @return the value of attempt.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.create_time
     *
     * @param createTime the value for attempt.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.update_time
     *
     * @return the value of attempt.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.update_time
     *
     * @param updateTime the value for attempt.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attempt.output
     *
     * @return the value of attempt.output
     *
     * @mbggenerated
     */
    public String getOutput() {
        return output;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attempt.output
     *
     * @param output the value for attempt.output
     *
     * @mbggenerated
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table attempt
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", taskId=").append(taskId);
        sb.append(", instanceId=").append(instanceId);
        sb.append(", agentId=").append(agentId);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", status=").append(status);
        sb.append(", active=").append(active);
        sb.append(", returnValue=").append(returnValue);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", duration=").append(duration);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", output=").append(output);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table attempt
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Attempt other = (Attempt) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
            && (this.getInstanceId() == null ? other.getInstanceId() == null : this.getInstanceId().equals(other.getInstanceId()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getTransactionId() == null ? other.getTransactionId() == null : this.getTransactionId().equals(other.getTransactionId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getActive() == null ? other.getActive() == null : this.getActive().equals(other.getActive()))
            && (this.getReturnValue() == null ? other.getReturnValue() == null : this.getReturnValue().equals(other.getReturnValue()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getOutput() == null ? other.getOutput() == null : this.getOutput().equals(other.getOutput()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table attempt
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getInstanceId() == null) ? 0 : getInstanceId().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getTransactionId() == null) ? 0 : getTransactionId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getActive() == null) ? 0 : getActive().hashCode());
        result = prime * result + ((getReturnValue() == null) ? 0 : getReturnValue().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getOutput() == null) ? 0 : getOutput().hashCode());
        return result;
    }
}