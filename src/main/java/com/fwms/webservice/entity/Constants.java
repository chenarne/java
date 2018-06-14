package com.fwms.webservice.entity;

import com.fwms.basedevss.base.conf.GlobalConfig;

public class Constants implements java.io.Serializable {
    //使用Endpoint类提供的publish方法发布WebService，发布时要保证使用的端口号没有被其他应用程序占用
    public static String host = GlobalConfig.get().getString("server.host.webservice", "localhost");
    public static String port = GlobalConfig.get().getString("server.port.webservice", "8089");
    public static String address = "http://" + host + ":" + port + "/OrderServer/Webservice";

}
