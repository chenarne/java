package com.fwms.quartz;

import com.fwms.basedevss.base.util.Initializable;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class JobScheduler implements Initializable {
    private Scheduler scheduler = null;
    public void init() {
        //System.setProperty(StdSchedulerFactory.PROPERTIES_FILE,"")

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();


        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }


    public void destroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}