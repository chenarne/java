package com.fwms.common;

import com.fwms.basedevss.ServerException;
import com.fwms.basedevss.ServiceResult;
import com.fwms.basedevss.base.auth.WebSignatures;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.log.Logger;
import com.fwms.basedevss.base.util.Encoders;
import com.fwms.basedevss.base.web.Cookies;
import com.fwms.basedevss.base.web.QueryParams;
import com.fwms.common.cache.SpyMemcachedUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLDecoder;

public class PortalContext {
    private static final Logger L = Logger.getLogger(PortalContext.class);

    public static String getTicket(HttpServletRequest req){
        String ticket = "ticket";

        String oldTicket = req.getParameter(ticket),
                newTicket = oldTicket;

        try {
            if(null == newTicket || newTicket.isEmpty() || newTicket.equals("null") || newTicket.equals("undefined")) {
                Cookie cookie = Cookies.getCookie(req, ticket);
                if(null != cookie)
                    newTicket = cookie.getValue();

                if(null == newTicket || newTicket.isEmpty() || newTicket.equals("null") || newTicket.equals("undefined"))
                    newTicket = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(newTicket == null)
            newTicket = "";
        return newTicket;
    }
    public static Context getContext(HttpServletRequest req, QueryParams qp, boolean needTicket,boolean needSign) {
        Context ctx = new Context();

//        try {
//            long sendT = qp.getInt("call_id",0);
//            if ((DateUtils.nowMillis() - sendT)> 1 * 2 * 60 * 60 * 1000L)
//                throw new ServerException(ErrorCodes.SYSTEM_HTTP_REQUEST_DELAY, "time out");
//        }catch (Exception e){}

        //需要ticket
        String ticket = qp.getString("ticket");
        if(ticket.isEmpty())
            ticket = getTicket(req);
        if (needTicket) {
            if (ticket.isEmpty() || "null".equals(ticket) || "undefined".equals(ticket))
                throw new ServerException(ErrorCodes.AUTH_NEED_TICKET, "need ticket");
        }


        long user_type = 0;
        Record ut;
        //如果上传了ticket，不管是不是合法，都需要验证
        if (!ticket.isEmpty() && !"null".equals(ticket) && !"undefined".equals(ticket)) {
            ut = GlobalLogics.getUser().getUserIdByTicket(null, ticket);
            if (ut.isEmpty()) {
                throw new ServerException(ErrorCodes.AUTH_TICKET_INVALID, "ticket not exists");
            } else {
                user_type = ut.getInt("USER_TYPE");
                ctx.setUser_id(ut.getString("USER_ID"));
                Record users=GlobalLogics.getUser().getUserById(ut.getString("USER_ID"));
                if(users!=null){
                    ctx.setUserName(users.isEmpty()?"":users.getString("DISPLAY_NAME"));
                }
                ctx.setUser_type(String.valueOf(user_type));
                String rurl=req.getHeader("referer");
            }
        }

        if (needSign) {
            //再检查签名是不是正确   q
            String sign = qp.checkGetString("sign");
            String expectantSign = WebSignatures.md5Sign(qp);
            if (!StringUtils.equals(sign, expectantSign)) {
                sign = StringUtils.replace(sign, " ", "+");
                if (!StringUtils.equals(sign, expectantSign)){
                    throw new ServerException(ErrorCodes.AUTH_SIGNATURE_ERROR, "Invalid md5 signatures");
                }
            }
        }

        String IP = req.getRemoteAddr() ;
        String URI = req.getRequestURI();
        String URL = ctx.getUrl();
        String QSTR =req.getQueryString();
        ctx.setApp_type(qp.getString("app_type", "0"));
        ctx.setDevice_id(qp.getString("device_id", ""));
        ctx.setLanguage(qp.getString("language", ""));
        ctx.setTicket(qp.getString("ticket", ""));
        ctx.setUser_agent(qp.getString("user_agent", ""));
        ctx.setApp_platform(qp.getString("app_platform", ""));
        ctx.setCall_id(qp.getString("call_id", "0"));
        ctx.setLocation(qp.getString("location", ""));
        ctx.setVersionCode(qp.getString("version_code", ""));
        ctx.setIp_addr(req.getRemoteAddr());

        if (!qp.getString("url", "").equals("")){
            byte[] a = Encoders.fromBase64(qp.getString("url", ""));
            String urlReally =  new String(a);
            ctx.setUrl(URLDecoder.decode(urlReally));
        } else {
            ctx.setUrl("");
        }
        if (!qp.getString("frompage", "").equals("")){
            byte[] a = Encoders.fromBase64(qp.getString("frompage", ""));
            String urlReally =  new String(a);
            ctx.setFrom_page(URLDecoder.decode(urlReally));
        } else {
            ctx.setFrom_page("");
        }

        ctx.setChannel_id(qp.getString("channel_id", ""));
        String deviceId = qp.getString("device_id","");
        String UAU =  ctx.getUser_agent().toUpperCase();
        if (UAU.contains("ANDROID")
                || UAU.contains("IPHONE")
                || UAU.contains("IPAD")) {
            deviceId = "mobile";
        }

        GlobalLogics.getHistory().saveHistory(ctx, ctx.getUser_id(), deviceId, ctx.getApp_type(), ctx.getApp_platform(), ctx.getUser_agent(), ctx.getLanguage(), IP, ctx.getLocation(), URI, URL, QSTR, ctx.getVersionCode(), ctx.getChannel_id(),ctx.getFrom_page());

        return ctx;
    }
}
