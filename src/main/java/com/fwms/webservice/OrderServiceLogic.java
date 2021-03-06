package com.fwms.webservice;

import com.fwms.webservice.entity.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface OrderServiceLogic {

    @WebResult(name="getInboundMdList")
    List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getInboundMdList(
            @WebParam(name = "userId", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String userId,
            @WebParam(name = "PRINTED", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String PRINTED,
            @WebParam(name = "SJ_ID", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String SJ_ID,
            @WebParam(name = "PARTNER_NO", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String PARTNER_NO,
            @WebParam(name = "INBOUND_TIME", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String INBOUND_TIME);

    @WebResult(name="getAllGysSj")
    List<WMS_WEBSERVICE_RESULT_SJ> getAllGysSj(@WebParam(name = "userId", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
                                               String userId);
    @WebResult(name="getSjPartner")
    List<WMS_WEBSERVICE_RESULT_PARTNER> getSjPartner(@WebParam(name = "SJ_ID", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
                                                     String SJ_ID);

    @WebResult(name="updatePackagePrint")
    WMS_WEBSERVICE_RESULT_BOOLEAN updatePackagePrint(
            @WebParam(name = "package_code", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String package_code,
            @WebParam(name = "userId", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String userId);

    @WebResult(name="updatePackageInbound")
    WMS_WEBSERVICE_RESULT updatePackageInbound(
            @WebParam(name = "inbound_id", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String inbound_id,
            @WebParam(name = "package_code", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String package_code,
            @WebParam(name = "userId", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String userId);
    @WebResult(name="updatePackageOutbound")
    WMS_WEBSERVICE_RESULT updatePackageOutbound(
            @WebParam(name = "outbound_id", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String outbound_id,
            @WebParam(name = "package_code", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String package_code,
            @WebParam(name = "userId", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String userId);
    @WebResult(name="getAllPackageInbound")
    List<WMS_WEBSERVICE_RESULT_ORDER_INBOUND> getAllPackageInbound(
            @WebParam(name = "kw_id", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String kw_id);
    @WebResult(name="getAllPackageOutbound")
    List<WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND> getAllPackageOutbound(
            @WebParam(name = "kw_id", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String kw_id);

    @WebResult(name="packageUserLogin")
    WMS_WEBSERVICE_RESULT_USER packageUserLogin(
            @WebParam(name = "user_name", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String user_name,
            @WebParam(name = "password", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String password);
    @WebResult(name="getScanInboundList")
    List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getScanInboundList(
            @WebParam(name = "user_name", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String INBOUND_ID);
    @WebResult(name="getScanOutboundList")
    List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getScanOutboundList(
            @WebParam(name = "user_name", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String OUTBOUND_ID);

    //==============以下是改成按照库位收货的==============
    @WebResult(name="getAllInboundKws")
    List<WMS_WEBSERVICE_RESULT_ORDER_KWS> getAllInboundKws(
            @WebParam(name = "start_time", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String start_time,
            @WebParam(name = "end_time", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String end_time
    );
    @WebResult(name="getAllOutboundKws")
    List<WMS_WEBSERVICE_RESULT_ORDER_KWS> getAllOutboundKws(
            @WebParam(name = "start_time", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String start_time,
            @WebParam(name = "end_time", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String end_time
    );
    @WebResult(name="getScanInboundListThisKw")
    List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getScanInboundListThisKw(
            @WebParam(name = "start_time", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String start_time,
            @WebParam(name = "end_time", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String end_time,
            @WebParam(name = "kw_id", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String kw_id
    );
    @WebResult(name="getAllOutboundKws")
    List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getScanOutboundListThisKw(
            @WebParam(name = "start_time", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String start_time,
            @WebParam(name = "end_time", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String end_time,
            @WebParam(name = "kw_id", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)String kw_id
    );

    @WebResult(name="updatePackageInboundNew")
    WMS_WEBSERVICE_RESULT updatePackageInboundNew(
            @WebParam(name = "in_kw_id", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String in_kw_id,
            @WebParam(name = "package_code", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String package_code,
            @WebParam(name = "userId", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String userId);
    @WebResult(name="updatePackageOutboundNew")
    WMS_WEBSERVICE_RESULT updatePackageOutboundNew(
            @WebParam(name = "in_kw_id", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String in_kw_id,
            @WebParam(name = "package_code", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String package_code,
            @WebParam(name = "userId", targetNamespace = "http://39.107.86.181/", mode = WebParam.Mode.IN)
            String userId);
    //==============以上是改成按照库位收货的==============
}
