package com.fwms.webservice.entity;
public class WMS_WEBSERVICE_RESULT_USER implements java.io.Serializable {

    private String USER_ID ;

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }


    private String USER_NAME ;
    private String DISPLAY_NAME ;

    public WMS_WEBSERVICE_RESULT_USER() {
    }
    public static WMS_WEBSERVICE_RESULT_USER dummy() {
        WMS_WEBSERVICE_RESULT_USER o = new WMS_WEBSERVICE_RESULT_USER();
        o.setUSER_ID("");
        o.setDISPLAY_NAME("");
        o.setUSER_NAME("");
        return o;
    }
}
