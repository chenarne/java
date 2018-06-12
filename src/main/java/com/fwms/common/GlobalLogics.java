package com.fwms.common;

import com.fwms.basedevss.base.util.InitUtils;
import com.fwms.basedevss.base.web.JettyServer;

import com.fwms.quartz.JobScheduler;

import com.fwms.repertory.base.BaseImpl;
import com.fwms.repertory.base.BaseLogic;
import com.fwms.repertory.orders.OrderImpl;
import com.fwms.repertory.orders.OrderLogic;
import com.fwms.service.history.HistoryImpl;
import com.fwms.service.history.HistoryLogic;

import com.fwms.service.user.UserImpl;
import com.fwms.service.user.UserLogic;


public class GlobalLogics {
    private static HistoryLogic history = new HistoryImpl();
    private static UserLogic user = new UserImpl();
    private static BaseLogic baseLogic = new BaseImpl();
    private static JobScheduler jobScheduler = new JobScheduler();
    private static OrderLogic orderLogic = new OrderImpl();

    public static void init() {
        InitUtils.batchInit(orderLogic, jobScheduler, history, user,baseLogic);
    }

    public static void destroy() {
        InitUtils.batchDestroy(orderLogic, jobScheduler, history, user, baseLogic);
    }

    public static HistoryLogic getHistory() {
        return history;
    }
    public static UserLogic getUser() {
        return user;
    }
    public static BaseLogic getBaseLogic() {
        return baseLogic;
    }
    public static OrderLogic getOrderLogic() {
        return orderLogic;
    }




    public static class ServerLifeCycle implements JettyServer.LifeCycle {
        @Override
        public void before() throws Exception {
            GlobalLogics.init();
        }

        @Override
        public void after() throws Exception {
            GlobalLogics.destroy();
        }
    }


}
