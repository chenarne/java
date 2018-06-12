package com.fwms.service.history;

import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface HistoryLogic {
    boolean saveHistory(Context ctx, String USER_ID, String DEVICE_CODE, String APP_TYPE, String PLATFORM, String USER_AGENT, String DEVICE_LANGUAGE, String DEVICE_IP, String LOCATION, String URI, String URL, String QSTR, String VERSION_CODE, String CHANNEL_ID,String FROM_PAGE);


    Record getSingleHistory(Context ctx, long ID);
    Record getAllHistoryPageList(Context ctx, String USER_ID, String DEVICE_CODE, String PLATFORM, String USER_AGENT, String DEVICE_LANGUAGE, String DEVICE_IP, String URI, int page, int count, String VERSION_CODE, String CHANNEL_ID);

    RecordSet getReportApi(Context ctx, String API_NAME, long startTime, long endTime);


}

