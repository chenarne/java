package com.fwms.basedevss.base.web;


import com.fwms.basedevss.ServerException;
import com.fwms.basedevss.base.BaseErrors;
import com.fwms.basedevss.base.io.Charsets;
import com.fwms.basedevss.base.util.*;
import com.fwms.basedevss.base.util.json.JsonUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParams extends StringMap implements Copyable<QueryParams> {
    private static final int IN_MEMORY_FILE_SIZE_THRESHOLD = 1024 * 20; // 20KB
    private static final int MAX_UPLOAD_FILE_SIZE = 1024 * 1024 * 500; // 500MB


    private static final String TMP_DIR = getTempDir();

    private static String getTempDir() {
        try {
            String tempDir = FileUtils.getTempDirectoryPath() + "/upload_" + DateUtils.nowNano();
            FileUtils.forceMkdir(new File(tempDir));
            return tempDir;
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_IO_ERROR, e);
        }
    }

    public QueryParams() {
    }


    public static QueryParams create(HttpServletRequest req) {
        QueryParams qp = new QueryParams();
        qp.parseParams(req);
        return qp;
    }

    protected static String decodeHeader(HttpServletRequest req, String name, String def) throws UnsupportedEncodingException {
        String v = req.getHeader(name);
        return StringUtils.isNotEmpty(v) ? java.net.URLDecoder.decode(v, "UTF-8") : def;
    }

    protected static String parseWutongUserAgent(String ua, String key) {
        List<String> l = StringUtils2.splitList(ua, ";", true);
        for (String str : l) {
            if (str.contains(key)) {
                return StringUtils.substringAfter(str, "=");
            }
        }

        return "";
    }

    private static String getLanguageFromUserAgent(HttpServletRequest req) throws UnsupportedEncodingException {
        String ua = decodeHeader(req, "User-Agent", "");
        if (StringUtils.startsWith(ua, "os=")) {
            return parseWutongUserAgent(ua, "lang").equalsIgnoreCase("CN") ?  "zh" : "en";
        } else if (StringUtils.startsWith(ua, "Mozilla")) {
            String acceptLanguage = decodeHeader(req, "Accept-Language", "en");
            return StringUtils.substringBefore(acceptLanguage, ",");
        } else {
            return "en";
        }
    }

    @SuppressWarnings("unchecked")
    private void parseParams(HttpServletRequest req) {
        try {

            // header
            setString("app_type", decodeHeader(req, "app_type", ""));
            setString("device_id", decodeHeader(req, "device_id", ""));
            setString("language", decodeHeader(req, "language", ""));
            if (decodeHeader(req, "ticket", "").equals("null") || decodeHeader(req, "ticket", "").equals("undefined")){
                setString("ticket", "");
            } else {
                setString("ticket", decodeHeader(req, "ticket", ""));
            }
            setString("location", decodeHeader(req, "location", ""));
            setString("app_platform", decodeHeader(req, "app_platform", ""));
            setString("user_agent", decodeHeader(req, "User-Agent", ""));
            setString("call_id", decodeHeader(req, "call_id", ""));
            setString("channel_id", decodeHeader(req, "channel_id", ""));
            if (decodeHeader(req, "url", "").equals("null")){
                setString("url", "");
            }else{
                byte[] a = Encoders.fromBase64(decodeHeader(req, "url", ""));
                String urlReally =  new String(a);
                setString("url", urlReally);
            }
            setString("url", decodeHeader(req, "url", ""));
            if (decodeHeader(req, "frompage", "").equals("null")){
                setString("url", "");
            }else{
                byte[] a = Encoders.fromBase64(decodeHeader(req, "frompage", ""));
                String urlReally =  new String(a);
                setString("frompage", urlReally);
            }
            // get params
//            if ("post".equals(req.getMethod().toLowerCase())) {
//                String contentType = req.getContentType();
//                if (contentType != null &&
//                        contentType.contains("application/json")) {//参数值是json
//                    BufferedReader br = req.getReader();
//                    String str, wholeStr = "";
//                    while ((str = br.readLine()) != null) {
//                        wholeStr += str;
//                    }
//                    if (!wholeStr.isEmpty()) {
//                        try {
//                            Map bodyParam = JsonUtils.fromJson(wholeStr, Map.class);
//                            for (Object e0 : bodyParam.entrySet()) {
//                                Map.Entry<String, ?> e = (Map.Entry<String, ?>) e0;
//                                Object v = e.getValue();
//                                String vs = v instanceof String[]
//                                        ? StringUtils.join((String[]) v, "")
//                                        : ObjectUtils.toString(v, "");
//                                setString(e.getKey(), vs);
//                            }
//                        } catch (ServerException e) {
//
//                        }
//                    }
//                }
//            }
            Map m = req.getParameterMap();
            for (Object e0 : m.entrySet()) {
                Map.Entry<String, ?> e = (Map.Entry<String, ?>) e0;
                Object v = e.getValue();
                String vs = v instanceof String[]
                        ? StringUtils.join((String[]) v, "")
                        : ObjectUtils.toString(v, "");
                setString(e.getKey(), vs);
            }

            if (ServletFileUpload.isMultipartContent(req)) {
                ServletFileUpload upload = createFileUpload();
                List<FileItem> fileItems = upload.parseRequest(req);
                for (FileItem fileItem : fileItems) {
                    if (fileItem.isFormField()) {
                        setString(fileItem.getFieldName(), fileItem.getString(Charsets.DEFAULT));
                    } else {
                        put(fileItem.getFieldName(), fileItem);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServerException(BaseErrors.PLATFORM_PARSE_QUERY_PARAMS_ERROR, e);
        }
    }


    private static ServletFileUpload createFileUpload() {
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory(IN_MEMORY_FILE_SIZE_THRESHOLD, new File(TMP_DIR)));
        upload.setSizeMax(MAX_UPLOAD_FILE_SIZE);
        return upload;
    }

    public FileItem getFile(String k) {
        Object v = get(k);
        return (v != null && v instanceof FileItem) ? (FileItem) v : null;
    }

    public FileItem checkGetFile(String k) {
        FileItem v = getFile(k);
        if (v == null)
            throw new ServerException(BaseErrors.PLATFORM_ILLEGAL_PARAM, "Missing file upload parameter '%s'", k);
        return v;
    }

    public Value<String> getSequentialString(String... keys) {
        for (String key : keys) {
            if (containsKey(key)) {
                String v = checkGetString(key);
                return new Value<String>(key, v);
            }
        }
        return null;
    }


    @Override
    public QueryParams copy() {
        QueryParams qp = new QueryParams();
        qp.putAll(this);
        return qp;
    }

    public QueryParams removeKeys(String... keys) {
        for (String key : keys)
            remove(key);
        return this;
    }

    public void close() {
        if (!isEmpty()) {
            for (Object v : values()) {
                if (v instanceof FileItem)
                    ((FileItem) v).delete();
            }
            clear();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public static class Value<T> {
        public final String key;
        public final T value;

        public Value(String key, T value) {
            this.key = key;
            this.value = value;
        }
    }
    public QueryParams getUserParams(){
        QueryParams params=this.copy();
        params.removeKeys("app_type","device_id","language","ticket","location","app_platform","user_agent","call_id","channel_id","url","frompage");
        return params;
    }

}
