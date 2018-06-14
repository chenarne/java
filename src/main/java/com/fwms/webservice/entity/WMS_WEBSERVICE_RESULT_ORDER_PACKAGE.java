package com.fwms.webservice.entity;
public class WMS_WEBSERVICE_RESULT_ORDER_PACKAGE implements java.io.Serializable {

    private String ORDER_ID;
    private String OUT_ORDER_ID;
    private String GYS_ID;
    private String JH_TIME;
    private String KW_ID;
    private String PARTNER_NO;
    private String FULL_ADDR;
    private String CONTACT;
    private String MOBILE;

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getOUT_ORDER_ID() {
        return OUT_ORDER_ID;
    }

    public void setOUT_ORDER_ID(String OUT_ORDER_ID) {
        this.OUT_ORDER_ID = OUT_ORDER_ID;
    }

    public String getGYS_ID() {
        return GYS_ID;
    }

    public void setGYS_ID(String GYS_ID) {
        this.GYS_ID = GYS_ID;
    }

    public String getJH_TIME() {
        return JH_TIME;
    }

    public void setJH_TIME(String JH_TIME) {
        this.JH_TIME = JH_TIME;
    }

    public String getKW_ID() {
        return KW_ID;
    }

    public void setKW_ID(String KW_ID) {
        this.KW_ID = KW_ID;
    }

    public String getPARTNER_NO() {
        return PARTNER_NO;
    }

    public void setPARTNER_NO(String PARTNER_NO) {
        this.PARTNER_NO = PARTNER_NO;
    }

    public String getFULL_ADDR() {
        return FULL_ADDR;
    }

    public void setFULL_ADDR(String FULL_ADDR) {
        this.FULL_ADDR = FULL_ADDR;
    }

    public String getCONTACT() {
        return CONTACT;
    }

    public void setCONTACT(String CONTACT) {
        this.CONTACT = CONTACT;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getINBOUND_TIME() {
        return INBOUND_TIME;
    }

    public void setINBOUND_TIME(String INBOUND_TIME) {
        this.INBOUND_TIME = INBOUND_TIME;
    }

    public String getKW_NAME() {
        return KW_NAME;
    }

    public void setKW_NAME(String KW_NAME) {
        this.KW_NAME = KW_NAME;
    }

    public String getPARENT_KW_NAME() {
        return PARENT_KW_NAME;
    }

    public void setPARENT_KW_NAME(String PARENT_KW_NAME) {
        this.PARENT_KW_NAME = PARENT_KW_NAME;
    }

    public String getGYS_NAME() {
        return GYS_NAME;
    }

    public void setGYS_NAME(String GYS_NAME) {
        this.GYS_NAME = GYS_NAME;
    }

    public String getPARTNER_NAME() {
        return PARTNER_NAME;
    }

    public void setPARTNER_NAME(String PARTNER_NAME) {
        this.PARTNER_NAME = PARTNER_NAME;
    }

    public String getSJ_NAME() {
        return SJ_NAME;
    }

    public void setSJ_NAME(String SJ_NAME) {
        this.SJ_NAME = SJ_NAME;
    }

    private String INBOUND_TIME;
    private String KW_NAME;
    private String PARENT_KW_NAME;
    private String GYS_NAME;
    private String PARTNER_NAME;
    private String SJ_NAME;

    public String getPACKAGE_CODE() {
        return PACKAGE_CODE;
    }

    public void setPACKAGE_CODE(String PACKAGE_CODE) {
        this.PACKAGE_CODE = PACKAGE_CODE;
    }

    private String PACKAGE_CODE;

    public String getPRO_DETAIL() {
        return PRO_DETAIL;
    }

    public void setPRO_DETAIL(String PRO_DETAIL) {
        this.PRO_DETAIL = PRO_DETAIL;
    }

    private String PRO_DETAIL;


    public WMS_WEBSERVICE_RESULT_ORDER_PACKAGE() {
    }
    public static WMS_WEBSERVICE_RESULT_ORDER_PACKAGE dummy() {
        WMS_WEBSERVICE_RESULT_ORDER_PACKAGE o = new WMS_WEBSERVICE_RESULT_ORDER_PACKAGE();
        o.setORDER_ID("");
        o.setCONTACT("");
        o.setFULL_ADDR("");
        o.setGYS_ID("");
        o.setGYS_NAME("");
        o.setINBOUND_TIME("");
        o.setJH_TIME("");
        o.setKW_ID("");
        o.setKW_NAME("");
        o.setMOBILE("");
        o.setOUT_ORDER_ID("");
        o.setPARENT_KW_NAME("");
        o.setPARTNER_NAME("");
        o.setSJ_NAME("");
        o.setPARTNER_NO("");
        o.setPACKAGE_CODE("");
        o.setPRO_DETAIL("");
        return o;
    }
}
