package com.fwms.common;

import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.util.json.JsonUtils;
import com.fwms.common.cache.SpyMemcachedUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.LinkedHashMap;

/**
 * Created by liqun on 2016/6/4.
 */
public class LocalUtils {
    protected static Logger log = LoggerFactory.getLogger(LocalUtils.class);

    public static String getLocalIp(){
        InetAddress ia=null;
        String localip="";
        try {
            ia=ia.getLocalHost();

            String localname=ia.getHostName();
            localip=ia.getHostAddress();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return localip;
    }
}
