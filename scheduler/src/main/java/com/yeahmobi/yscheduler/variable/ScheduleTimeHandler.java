package com.yeahmobi.yscheduler.variable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.yeahmobi.yscheduler.common.variable.VariableContext;
import com.yeahmobi.yscheduler.common.variable.VariableException;
import com.yeahmobi.yscheduler.common.variable.VariableHandler;
import com.yeahmobi.yscheduler.model.WorkflowInstance;

public class ScheduleTimeHandler implements VariableHandler {

    public static final String VARIABLE_NAME = "scheduleTime";

    public String process(String[] params, VariableContext variableContext) throws VariableException {
        try {
            DefaultVariableContext context = (DefaultVariableContext) variableContext;
            Date scheduleTime = context.getTaskIntance().getScheduleTime();
            if (scheduleTime == null) {
                WorkflowInstance workflowInstance = context.getWorkflowInstance();
                scheduleTime = workflowInstance == null ? null : workflowInstance.getScheduleTime();
            }

            if (scheduleTime != null) {
                String format = "";
                String offset = "";
                if (params.length >= 1) {
                    format = params[0].trim();
                    if (params.length == 2) {
                        offset = params[1].trim();
                        if (StringUtils.isNotBlank(offset)) {
                            scheduleTime = taskOffset(scheduleTime, offset);
                        }
                    }
                }
                if (StringUtils.isNotBlank(format)) {
                    SimpleDateFormat formatter = new SimpleDateFormat(format);
                    return formatter.format(scheduleTime);
                } else {
                    return String.valueOf(scheduleTime.getTime() / 1000);
                }
            } else {
                return StringUtils.EMPTY;
            }
        } catch (Exception e) {
            throw new VariableException(e);
        }
    }

    private Date taskOffset(Date scheduleTime, String offset) throws VariableException {
        if (offset.length() < 2) {
            throw new VariableException("Offset error： " + offset);
        }
        int offsetInt = 0;
        String offsetString = offset.substring(0, offset.length() - 1);
        if (offsetString.startsWith("+")) {
            offsetString = offsetString.substring(1);
        }
        try {
            offsetInt = Integer.parseInt(offsetString);
        } catch (Exception e) {
            throw new VariableException("Offset error： " + offset);
        }
        char type = offset.charAt(offset.length() - 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(scheduleTime);
        if (type == 'h') {
            calendar.add(Calendar.HOUR_OF_DAY, offsetInt);
        } else if (type == 'd') {
            calendar.add(Calendar.DAY_OF_MONTH, offsetInt);
        } else if (type == 'w') {
            calendar.add(Calendar.WEEK_OF_YEAR, offsetInt);
        } else if (type == 'm') {
            calendar.add(Calendar.MONTH, offsetInt);
        } else if (type == 'y') {
            calendar.add(Calendar.YEAR, offsetInt);
        }
        scheduleTime = calendar.getTime();
        return scheduleTime;
    }
}
