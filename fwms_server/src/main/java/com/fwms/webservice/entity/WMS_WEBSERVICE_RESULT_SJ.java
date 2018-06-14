package com.fwms.webservice.entity;
public class WMS_WEBSERVICE_RESULT_SJ implements java.io.Serializable {

    private String SJ_ID;

    public String getSJ_ID() {
        return SJ_ID;
    }

    public void setSJ_ID(String SJ_ID) {
        this.SJ_ID = SJ_ID;
    }

    public String getSJ_NAME() {
        return SJ_NAME;
    }

    public void setSJ_NAME(String SJ_NAME) {
        this.SJ_NAME = SJ_NAME;
    }

    public String getSJ_NAME_SX() {
        return SJ_NAME_SX;
    }

    public void setSJ_NAME_SX(String SJ_NAME_SX) {
        this.SJ_NAME_SX = SJ_NAME_SX;
    }

    private String SJ_NAME;
    private String SJ_NAME_SX;

    public WMS_WEBSERVICE_RESULT_SJ() {
    }
    public static WMS_WEBSERVICE_RESULT_SJ dummy() {
        WMS_WEBSERVICE_RESULT_SJ o = new WMS_WEBSERVICE_RESULT_SJ();
        o.setSJ_NAME("");
        o.setSJ_NAME_SX("");
        o.setSJ_ID("");

        return o;
    }
}
