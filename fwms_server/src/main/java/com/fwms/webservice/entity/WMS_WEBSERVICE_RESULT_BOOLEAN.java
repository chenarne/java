package com.fwms.webservice.entity;
public class WMS_WEBSERVICE_RESULT_BOOLEAN implements java.io.Serializable {

    private boolean result ;
    public void setResult(boolean result) {
        this.result = result;
    }
    public boolean getResult() {
        return result;
    }

    public WMS_WEBSERVICE_RESULT_BOOLEAN() {
    }
    public static WMS_WEBSERVICE_RESULT_BOOLEAN dummy() {
        WMS_WEBSERVICE_RESULT_BOOLEAN o = new WMS_WEBSERVICE_RESULT_BOOLEAN();
        o.setResult(false);
        return o;
    }
}
