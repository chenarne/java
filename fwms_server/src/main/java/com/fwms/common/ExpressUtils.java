package com.fwms.common;

import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.log.Logger;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Created by chenarne on 15/12/9.
 */
public class ExpressUtils {
    private static final Logger L = Logger.getLogger(ExpressUtils.class);

    public static Record getKuaidi100Str(String EXPRESS_COMP, String EXPRESS_NUM) throws URISyntaxException, URIException {
        return new Record();
    }

}
