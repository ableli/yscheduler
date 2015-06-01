package com.yeahmobi.yscheduler.model;

import java.util.Date;

public class ScheduleProgress {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column schedule_progress.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column schedule_progress.current_schedule_time
     *
     * @mbggenerated
     */
    private Date currentScheduleTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column schedule_progress.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column schedule_progress.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column schedule_progress.id
     *
     * @return the value of schedule_progress.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column schedule_progress.id
     *
     * @param id the value for schedule_progress.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column schedule_progress.current_schedule_time
     *
     * @return the value of schedule_progress.current_schedule_time
     *
     * @mbggenerated
     */
    public Date getCurrentScheduleTime() {
        return currentScheduleTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column schedule_progress.current_schedule_time
     *
     * @param currentScheduleTime the value for schedule_progress.current_schedule_time
     *
     * @mbggenerated
     */
    public void setCurrentScheduleTime(Date currentScheduleTime) {
        this.currentScheduleTime = currentScheduleTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column schedule_progress.create_time
     *
     * @return the value of schedule_progress.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column schedule_progress.create_time
     *
     * @param createTime the value for schedule_progress.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column schedule_progress.update_time
     *
     * @return the value of schedule_progress.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column schedule_progress.update_time
     *
     * @param updateTime the value for schedule_progress.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table schedule_progress
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
        sb.append(", currentScheduleTime=").append(currentScheduleTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table schedule_progress
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
        ScheduleProgress other = (ScheduleProgress) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCurrentScheduleTime() == null ? other.getCurrentScheduleTime() == null : this.getCurrentScheduleTime().equals(other.getCurrentScheduleTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table schedule_progress
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCurrentScheduleTime() == null) ? 0 : getCurrentScheduleTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}