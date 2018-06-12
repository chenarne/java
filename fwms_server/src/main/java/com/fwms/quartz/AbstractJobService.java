package com.fwms.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 抽象Job 类，如果某个类需要实现定时作业功能，可以继承此类并重写抽象方法
 */
public abstract class AbstractJobService implements Job {

    public final void execute(JobExecutionContext context) throws JobExecutionException {
	    String logId=beforeExecute(context);
	    String result=doExecute(context);
	    afterExecute(context,logId,result);
    }

    // 执行前的操作
    protected String beforeExecute(JobExecutionContext context) {
//
        return "";
    }

    // 执行后的操作
    protected void afterExecute(JobExecutionContext context,String logId,String message) {

    }

    // 具体执行逻辑交由子类实现
    protected abstract String doExecute(JobExecutionContext context) throws JobExecutionException;

}
