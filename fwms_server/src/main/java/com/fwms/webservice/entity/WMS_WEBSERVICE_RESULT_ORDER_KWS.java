package com.fwms.webservice.entity;
public class WMS_WEBSERVICE_RESULT_ORDER_KWS implements java.io.Serializable {

    private String KW_ID;

    public String getKW_ID() {
        return KW_ID;
    }

    public void setKW_ID(String KW_ID) {
        this.KW_ID = KW_ID;
    }

    public String getKW_NAME() {
        return KW_NAME;
    }

    public void setKW_NAME(String KW_NAME) {
        this.KW_NAME = KW_NAME;
    }

    public int getALL_PACKAGE_COUNT() {
        return ALL_PACKAGE_COUNT;
    }

    public void setALL_PACKAGE_COUNT(int ALL_PACKAGE_COUNT) {
        this.ALL_PACKAGE_COUNT = ALL_PACKAGE_COUNT;
    }

    public int getLESS_PACKAGE_COUNT() {
        return LESS_PACKAGE_COUNT;
    }

    public void setLESS_PACKAGE_COUNT(int LESS_PACKAGE_COUNT) {
        this.LESS_PACKAGE_COUNT = LESS_PACKAGE_COUNT;
    }

    private String KW_NAME;
    private int ALL_PACKAGE_COUNT;
    private int LESS_PACKAGE_COUNT;

    public int getHAS_PACKAGE_COUNT() {
        return HAS_PACKAGE_COUNT;
    }

    public void setHAS_PACKAGE_COUNT(int HAS_PACKAGE_COUNT) {
        this.HAS_PACKAGE_COUNT = HAS_PACKAGE_COUNT;
    }

    private int HAS_PACKAGE_COUNT;


    public WMS_WEBSERVICE_RESULT_ORDER_KWS() {
    }
    public static WMS_WEBSERVICE_RESULT_ORDER_KWS dummy() {
        WMS_WEBSERVICE_RESULT_ORDER_KWS o = new WMS_WEBSERVICE_RESULT_ORDER_KWS();
        o.setKW_ID("");
        o.setKW_NAME("");
        o.setALL_PACKAGE_COUNT(0);
        o.setLESS_PACKAGE_COUNT(0);
        o.setHAS_PACKAGE_COUNT(0);
        return o;
    }
}
