package com.fwms.webservice.entity;
public class WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND implements java.io.Serializable {

    private String ORDER_ID;
    private String KW_ID;

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

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

    public String getPARENT_KW_NAME() {
        return PARENT_KW_NAME;
    }

    public void setPARENT_KW_NAME(String PARENT_KW_NAME) {
        this.PARENT_KW_NAME = PARENT_KW_NAME;
    }

    public String getOUTBOUND_TIME() {
        return OUTBOUND_TIME;
    }

    public void setOUTBOUND_TIME(String OUTBOUND_TIME) {
        this.OUTBOUND_TIME = OUTBOUND_TIME;
    }

    public String getOUTBOUND_ID() {
        return OUTBOUND_ID;
    }

    public void setOUTBOUND_ID(String OUTBOUND_ID) {
        this.OUTBOUND_ID = OUTBOUND_ID;
    }

    public String getPARTNER_NAME() {
        return PARTNER_NAME;
    }

    public void setPARTNER_NAME(String PARTNER_NAME) {
        this.PARTNER_NAME = PARTNER_NAME;
    }

    private String KW_NAME;
    private String PARENT_KW_NAME;
    private String OUTBOUND_TIME;
    private String OUTBOUND_ID;
    private String PARTNER_NAME;


    public WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND() {
    }
    public static WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND dummy() {
        WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND o = new WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND();
        o.setORDER_ID("");
        o.setOUTBOUND_TIME("");
        o.setKW_ID("");
        o.setKW_NAME("");
        o.setPARENT_KW_NAME("");
        o.setPARTNER_NAME("");
        o.setOUTBOUND_ID("");
        return o;
    }
}
