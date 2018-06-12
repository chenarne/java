package com.fwms.basedevss.base.auth;


import com.fwms.basedevss.ServiceResult;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.log.Logger;
import com.fwms.basedevss.base.util.Encoders;
import com.fwms.basedevss.base.web.QueryParams;
import com.fwms.common.GlobalLogics;
import com.fwms.common.StringFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class WebSignatures {
    private static final Logger L = Logger.getLogger(WebSignatures.class);
    public static String md5Sign(QueryParams qp) {
        TreeSet<String> set = new TreeSet<String>(qp.keySet());
        set.remove("app_type");
        set.remove("user_type");
        set.remove("device_id");
        set.remove("location");
        set.remove("language");
        set.remove("version_code");
        set.remove("user_agent");
        set.remove("app_platform");
        set.remove("call_id");
        set.remove("ticket");
        set.remove("sign");
        set.remove("sign_method");
        set.remove("callback");
        set.remove("fresh");
        set.remove("_");
        set.remove("channel_id");
        set.remove("ip_addr");
        set.remove("url");
        set.remove("frompage");
        set.remove("USER_IMG");
        set.remove("USER_FILE");

        Iterator i = set.iterator();
        String p = "";
        while (i.hasNext()) {
            String i0 = i.next().toString();
            p += i0 + "=" + qp.getString(i0, "") + "|";
        }
        if (p.lastIndexOf("|") > 0)
            p = p.substring(0, p.length() - 1);

        return md5Sign(p);
    }

    public static String md5Sign(String s) {
        Validate.notNull(s);
        return Encoders.md5Base64(s);
    }
    private static String[]  systemParams=new String[]{"_","app_platform","app_type","call_id","callback","callbackType","channel_id",
            "device_id","fresh","frompage","ip_addr","language","location","sign","sign_method","ticket",
            "url","user_agent","USER_FILE",
            "USER_IMG","user_type","version_code"};
    public static ServiceResult checkUserParams(QueryParams qp){

        Iterator i = qp.keySet().iterator();
        ServiceResult result=new ServiceResult();
        while (i.hasNext()) {
            String i0 = i.next().toString();
            if(Arrays.binarySearch(systemParams,i0)<0){
                String q=qp.getString(i0, "").trim();
                q=q.replaceAll("'","’");
                q=q.replaceAll("\"","”");
                q=q.replaceAll("--","——");
                q=q.replaceAll("#","＃");
                q=q.replaceAll("&amp;","＆");
                q=q.replaceAll("&","＆");
                qp.setString(i0.trim(),q);
                if(!StringFilter.validSql(qp.getString(i0, "").trim())){
                    result.addErrorMessage(i0.trim()+":"+qp.getString(i0, "").trim()+"，含有非法字符");
                    return result;
                }
                qp.setString(i0.trim(),StringFilter.validXss(qp.getString(i0, "").trim()));
            }
        }
        return result;
    }
    public static String md5MallSign(QueryParams qp,String key) {
        TreeSet<String> set = new TreeSet<String>(qp.keySet());
        set.remove("app_type");
        set.remove("user_type");
        set.remove("device_id");
        set.remove("location");
        set.remove("language");
        set.remove("version_code");
        set.remove("user_agent");
        set.remove("app_platform");
        set.remove("call_id");
        set.remove("ticket");
        set.remove("sign");
        set.remove("sign_method");
        set.remove("callback");
        set.remove("fresh");
        set.remove("_");
        set.remove("channel_id");
        set.remove("ip_addr");
        set.remove("url");
        set.remove("frompage");
        set.remove("USER_IMG");
        set.remove("USER_FILE");
        set.remove("callbackType");
        set.remove("testId");
        Iterator i = set.iterator();
        String p = "";
        while (i.hasNext()) {
            String i0 = i.next().toString();
            p += i0.trim() + ":" + qp.getString(i0, "").trim().replace(" ","").replace("&","＆") + ",";
        }
        if (p.lastIndexOf(",") > 0)
            p = p.substring(0, p.length() - 1);
        //GlobalLogics.getHistory().saveHistory(null, "1121221212121","","","","","","","","", p,"","","","");
        return Encoders.md5Hex(p+"_"+key).toLowerCase();
    }
}
