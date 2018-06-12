package com.fwms.service.history;

import com.fwms.common.Constants;
import com.fwms.common.GlobalLogics;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.util.StringUtils2;
import com.fwms.common.PortalContext;
import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.web.QueryParams;
import com.fwms.basedevss.base.web.webmethod.WebMethod;
import com.fwms.basedevss.base.web.webmethod.WebMethodServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class HistoryServlet extends WebMethodServlet {
    @Override
    public void init() throws ServletException {
        Configuration conf = GlobalConfig.get();
        super.init();
    }

    @WebMethod("history/history_get_single")
    public Record History_get_single(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        long ID = qp.checkGetInt("ID");
        return GlobalLogics.getHistory().getSingleHistory(ctx, ID);
    }

    @WebMethod("history/history_get_page_list")
    public Record History_get_page_list(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_ID = qp.getString("USER_ID", "0");
        String DEVICE_CODE = qp.getString("DEVICE_CODE", "");
        String PLATFORM = qp.getString("PLATFORM", "");
        String USER_AGENT = qp.getString("USER_AGENT", "");
        String DEVICE_LANGUAGE = qp.getString("DEVICE_LANGUAGE", "");
        String DEVICE_IP = qp.getString("DEVICE_IP", "");
        String URI = qp.getString("URI", "");
        String VERSION_CODE = qp.getString("VERSION_CODE", "");
        String CHANNEL_ID = qp.getString("CHANNEL_ID", "");

        int PAGE = (int) qp.getInt("PAGE", 0);
        int COUNT = (int) qp.getInt("COUNT", 20);
        return GlobalLogics.getHistory().getAllHistoryPageList(ctx, USER_ID, DEVICE_CODE, PLATFORM, USER_AGENT, DEVICE_LANGUAGE, DEVICE_IP, URI, PAGE, COUNT, VERSION_CODE, CHANNEL_ID);
    }

    @WebMethod("history/get_api_report")
    public RecordSet History_get_visit_report(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);

        String API_NAME = qp.checkGetString("API_NAME");

        String START_TIME = qp.getString("START_TIME", "");
        String END_TIME = qp.getString("END_TIME", "");

        long lt = 0;
        long et = 0;
        try {
            if (!START_TIME.equals("")) {
                lt = Constants.dateString2long(START_TIME + " 00:00:00");
            }
            if (!END_TIME.equals("")) {
                et = Constants.dateString2long(END_TIME + " 23:59:59");
            }
        } catch (Exception e) {

        }
        return GlobalLogics.getHistory().getReportApi(ctx, API_NAME, lt, et);
    }

}

