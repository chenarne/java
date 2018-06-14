package com.fwms.webservice.entity;
public class WMS_WEBSERVICE_RESULT_PARTNER implements java.io.Serializable {

    private String PARTNER_NO;

    public String getPARTNER_NAME() {
        return PARTNER_NAME;
    }

    public void setPARTNER_NAME(String PARTNER_NAME) {
        this.PARTNER_NAME = PARTNER_NAME;
    }

    public String getPARTNER_NO() {
        return PARTNER_NO;
    }

    public void setPARTNER_NO(String PARTNER_NO) {
        this.PARTNER_NO = PARTNER_NO;
    }

    private String PARTNER_NAME;

    public WMS_WEBSERVICE_RESULT_PARTNER() {
    }
    public static WMS_WEBSERVICE_RESULT_PARTNER dummy() {
        WMS_WEBSERVICE_RESULT_PARTNER o = new WMS_WEBSERVICE_RESULT_PARTNER();
        o.setPARTNER_NO("");
        o.setPARTNER_NAME("");
        return o;
    }
}
