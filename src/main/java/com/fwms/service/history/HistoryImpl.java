package com.fwms.service.history;

import com.fwms.common.*;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.log.Logger;
import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.sql.ConnectionFactory;
import com.fwms.basedevss.base.sql.SQLExecutor;
import com.fwms.basedevss.base.util.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryImpl implements HistoryLogic, Initializable {
    private static final Logger L = Logger.getLogger(HistoryImpl.class);

    private ConnectionFactory connectionFactory;
    private String db;
    private String historyTable = "t_history";
    public HistoryImpl() {
    }

    @Override
    public void init() {
        Configuration conf = GlobalConfig.get();
        this.connectionFactory = ConnectionFactory.getConnectionFactory("dbcp");
        this.db = conf.getString("service.db", null);
    }


    @Override
    public void destroy() {
        this.historyTable = null;
        this.connectionFactory = ConnectionFactory.close(connectionFactory);
        this.db = null;
    }

    private SQLExecutor getSqlExecutor() {
        return new SQLExecutor(connectionFactory, db);
    }
    private SQLExecutor read_getSqlExecutor() {
        return new SQLExecutor(connectionFactory, db);
    }
    public boolean saveHistory(Context ctx, String USER_ID, String DEVICE_CODE, String APP_TYPE, String PLATFORM, String USER_AGENT,
                               String DEVICE_LANGUAGE, String DEVICE_IP, String LOCATION, String URI, String URL, String QSTR,
                               String VERSION_CODE, String CHANNEL_ID,String FROM_PAGE) {
        long ID = RandomUtils.generateId();
        String sql = "INSERT INTO " + historyTable + " ( ID,USER_ID,DEVICE_CODE,APP_TYPE,PLATFORM,USER_AGENT,DEVICE_LANGUAGE,DEVICE_IP,LOCATION,CREATE_TIME,URI,URL,QSTR ,VERSION_CODE,CHANNEL_ID,FROM_PAGE) VALUES ( '" + ID + "','" + USER_ID + "','" + DEVICE_CODE + "','" + APP_TYPE + "','" + PLATFORM + "','" + USER_AGENT + "','" + DEVICE_LANGUAGE + "','" + DEVICE_IP + "','" + LOCATION + "','" + DateUtils.now() + "','" + URI + "','" + URL + "','" + QSTR + "','" + VERSION_CODE + "','" + CHANNEL_ID + "','"+FROM_PAGE+"' ) ";
        long n =getSqlExecutor().executeUpdate(sql);
        return n > 0;
    }


    public Record getSingleHistory(Context ctx, long ID) {
        String sql = "SELECT * FROM " + historyTable + "  WHERE ID='" + ID + "' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        rec.put("STR_ID", rec.getString("ID"));
        rec.put("STR_CREATE_TIME", rec.getString("CREATE_TIME"));
        if (!rec.getString("USER_ID").equals("0")) {
            Record u = GlobalLogics.getUser().getSingleUserSimple( rec.getString("USER_ID"));
            rec.put("USER_NAME", u.getString("USER_NAME"));
        } else {
            rec.put("USER_NAME", "");
        }
        rec.put("STR_CREATE_TIME2", Constants.dateLongToString(rec.getInt("CREATE_TIME")));
        return rec;
    }


    public Record getAllHistoryPageList(Context ctx, String USER_ID, String DEVICE_CODE, String PLATFORM, String USER_AGENT, String DEVICE_LANGUAGE, String DEVICE_IP, String URI, int page, int count, String VERSION_CODE, String CHANNEL_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM " + historyTable + "  WHERE 1=1 ";
        if (USER_ID.length() > 0)
            sql0 += " AND USER_ID = '" + USER_ID + "' ";
        if (DEVICE_CODE.length() > 0)
            sql0 += " AND INSTR(DEVICE_CODE,'" + DEVICE_CODE + "')>0 ";
        if (PLATFORM.length() > 0)
            sql0 += " AND PLATFORM = '" + PLATFORM + "' ";
        if (USER_AGENT.length() > 0)
            sql0 += " AND INSTR(PLATFORM,'" + PLATFORM + "')>0 ";
        if (DEVICE_LANGUAGE.length() > 0)
            sql0 += " AND INSTR(DEVICE_LANGUAGE,'" + DEVICE_LANGUAGE + "')>0 ";
        if (DEVICE_IP.length() > 0)
            sql0 += " AND DEVICE_IP = '" + DEVICE_IP + "' ";
        if (URI.length() > 0)
            sql0 += " AND INSTR(URI,'" + URI + "')>0 ";
        if (VERSION_CODE.length() > 0)
            sql0 += " AND INSTR(VERSION_CODE,'" + VERSION_CODE + "')>0 ";
        if (CHANNEL_ID.length() > 0)
            sql0 += " AND CHANNEL_ID = '" + CHANNEL_ID + "' ";

        int rowNum = (int) se.executeRecord(sql0, null).getInt("COUNT1");
        int page_count = 0;
        if (rowNum > 0) {
            if ((rowNum % count) == 0) {
                page_count = (int) (rowNum / count);
            } else {
                page_count = (int) (rowNum / count) + 1;
            }
        }
        String sql = "SELECT * FROM " + historyTable + " WHERE 1=1 ";

        if (USER_ID.length() > 0)
            sql += " AND USER_ID = '" + USER_ID + "' ";
        if (DEVICE_CODE.length() > 0)
            sql += " AND INSTR(DEVICE_CODE,'" + DEVICE_CODE + "')>0 ";
        if (PLATFORM.length() > 0)
            sql += " AND PLATFORM = '" + PLATFORM + "' ";
        if (USER_AGENT.length() > 0)
            sql += " AND INSTR(PLATFORM,'" + PLATFORM + "')>0 ";
        if (DEVICE_LANGUAGE.length() > 0)
            sql += " AND INSTR(DEVICE_LANGUAGE,'" + DEVICE_LANGUAGE + "')>0 ";
        if (DEVICE_IP.length() > 0)
            sql += " AND DEVICE_IP = '" + DEVICE_IP + "' ";
        if (URI.length() > 0)
            sql += " AND INSTR(URI,'" + URI + "')>0 ";
        if (VERSION_CODE.length() > 0)
            sql += " AND INSTR(VERSION_CODE,'" + VERSION_CODE + "')>0 ";
        if (CHANNEL_ID.length() > 0)
            sql += " AND CHANNEL_ID = '" + CHANNEL_ID + "' ";

        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY CREATE_TIME DESC LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            rec.put("STR_ID", rec.getString("ID"));
            rec.put("STR_CREATE_TIME", rec.getString("CREATE_TIME"));
            rec.put("STR_CREATE_TIME_F", rec.getString("CREATE_TIME"));
            //找一个version_name
            rec.put("VERSION_NAME", "");
        }
        Record out_rec = new Record();
        out_rec.put("ROWS_COUNT", rowNum);
        out_rec.put("PAGE_COUNT", page_count);
        if (page == 0 || page == 1) {
            out_rec.put("CURRENT_PAGE", 1);
        } else {
            out_rec.put("CURRENT_PAGE", page);
        }
        out_rec.put("PAGE_SIZE", count);
        out_rec.put("DATAS", recs);
        return out_rec;
    }

    public RecordSet getReportApi(Context ctx, String API_NAME, long startTime, long endTime) {
        SQLExecutor se = read_getSqlExecutor();
        String sql0 = "SELECT * FROM " + historyTable + "  WHERE 1=1 ";
        if (startTime > 0)
            sql0 += " AND CREATE_TIME > '" + startTime + "' ";
        if (endTime > 0)
            sql0 += " AND CREATE_TIME < '" + endTime + "'";
        sql0 += " AND URI='" + API_NAME + "' AND USER_ID='" + ctx.getUser_id() + "' ORDER BY CREATE_TIME";
        RecordSet recs = se.executeRecordSet(sql0, null);
        return recs;
    }




}

