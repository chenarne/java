package com.fwms.webservice;

import com.fwms.webservice.entity.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface OrderServiceLogic {

    @WebMethod
    List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getOrder(String userId,String PRINTED,String SJ_ID,String PARTNER_NO,String INBOUND_TIME);
    @WebMethod
    WMS_WEBSERVICE_RESULT_BOOLEAN updatePackagePrint(String package_code,String userId);

    @WebMethod
    WMS_WEBSERVICE_RESULT_BOOLEAN updatePackageInbound(String package_code,String userId);
    @WebMethod
    WMS_WEBSERVICE_RESULT_BOOLEAN updatePackageOutbound(String package_code,String userId);
    @WebMethod
    List<WMS_WEBSERVICE_RESULT_ORDER_INBOUND> updatePackageInbound(String kwId);
    @WebMethod
    List<WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND> updatePackageOutbound(String kwId);

    @WebMethod
    WMS_WEBSERVICE_RESULT_USER packageUserLogin(String user_name,String password);
}
