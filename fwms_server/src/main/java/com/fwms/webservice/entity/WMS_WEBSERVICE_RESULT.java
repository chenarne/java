package com.fwms.webservice.entity;
public class WMS_WEBSERVICE_RESULT implements java.io.Serializable {

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    private int STATUS ;
    private String MESSAGE ;






    public WMS_WEBSERVICE_RESULT() {
    }
    public static WMS_WEBSERVICE_RESULT dummy() {
        WMS_WEBSERVICE_RESULT o = new WMS_WEBSERVICE_RESULT();
        o.setSTATUS(0);
        o.setMESSAGE("");
        return o;
    }
}
