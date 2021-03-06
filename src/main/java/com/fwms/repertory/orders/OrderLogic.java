package com.fwms.repertory.orders;

import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;

public interface OrderLogic {
    Record getAllGysOrderPageList(Context ctx, String PARTNER_NO,String PRO_TYPE_ID, String SJ_ID,String GYS_ID, String START_TIME, String END_TIME, String STATE, int PAY_DONE, int page, int count, String ORDER_ID,String OUT_ORDER_ID, String INBOUND_STATUS_BEGIN, String INBOUND_STATUS_END);
    boolean deleteOrder(Context ctx, String ORDER_ID);
    boolean updateOrderState(String ORDER_ID,int STATE);
    boolean updateOrderVerify(String ORDER_ID,String USER_ID);


    boolean saveGysOrder(Context ctx,String USER_ID,String SJ_ID, String ORDER_ID,String OUT_ORDER_ID, String GYS_ID, String GYS_NAME, String SEND_PRICE, String OTHER_PRICE, String PAY_TYPE, String MEMO, String JH_TIME, String JH_TYPE, String JH_ADDR, String IFKP, String KP_TYPE, String TAX, String FK_YD, int isBack,int status,String PARTNER_NO,String KW_ID,String PROVINCE,String CITY,String AREA,String ADDR,String FULL_ADDR,String CONTACT,String MOBILE);
    boolean saveGysOrderImport(Context ctx,String USER_ID,String SJ_ID, String ORDER_ID,String OUT_ORDER_ID, String GYS_ID, String GYS_NAME, String SEND_PRICE, String OTHER_PRICE, String PAY_TYPE, String MEMO, String JH_TIME, String JH_TYPE, String JH_ADDR,String INBOUND_TIME,String OUTBOUND_TIME, String IFKP, String KP_TYPE, String TAX, String FK_YD, int isBack,int status,String PARTNER_NO,String KW_ID,String PROVINCE,String CITY,String AREA,String ADDR,String FULL_ADDR,String CONTACT,String MOBILE);
    boolean saveGysOrderPro(Context ctx, String ORDER_ID, String PRO_ID, String PRO_SPEC_ID,String PRO_COUNT, String PRO_PRICE, String TAX_PRICE, String PRO_CODE_NUMBER, String IFSF, String PRO_TYPE, String PRO_TYPE_ID, String PRO_NAME, String totalPrice, String ORDER_ITEM_ID, String WS_PRO_PRICE, String TAX_RATE, String WSTotalPrice);
    Record getSingleOrder(String ORDER_ID);
    boolean updateGysOrder(Context ctx,String ORDER_ID,String OUT_ORDER_ID, String SEND_PRICE, String OTHER_PRICE, String PAY_TYPE, String MEMO, String JH_TIME,String INBOUND_TIME, String JH_TYPE, String JH_ADDR, String IFKP, String KP_TYPE, String TAX, String FK_YD,String PARTNER_NO,String PROVINCE,String CITY,String AREA,String ADDR,String FULL_ADDR,String CONTACT,String MOBILE);
    Record getSingleOrderPrint(String ORDER_ID);
    boolean deleteGysOrderProducts(String ORDER_ID);
    boolean printOrderInbound(String ORDER_ID);

    boolean saveOrderPackage(String ORDER_ID,String PACKAGE_CODE);
    boolean saveOrderPackageProduct(String ORDER_ID,String PACKAGE_PRODUCT_ID, String PACKAGE_CODE,String SPEC_ID,String PRO_NAME,int PRO_COUNT);
    boolean deletePackageCode(String PACKAGE_CODE);
    RecordSet getOrderPackages(String ORDER_ID);
    String getNowPackageCode(String ORDER_ID);
    Record getSingleOrderForPackage(String ORDER_ID);
    boolean saveInbound(Context ctx,String INBOUND_ID,String ORDER_ID,String KW_ID, String GYS_ID, String GYS_NAME,String INBOUND_TIME);
    Record getSingleOrderBase(String ORDER_ID);
    boolean updateOrderStatusInbound(Context ctx, String ORDER_ID, int STATUS);
    RecordSet getGysOrderDailyReport(String SJ_ID,String GYS_ID,String START_TIME,String END_TIME);
    RecordSet getGysOrderDailyReportDH(String SJ_ID,String GYS_ID,String START_TIME,String END_TIME);
    RecordSet getGysOrderDailyReport2(String SJ_ID,String GYS_ID,String START_TIME,String END_TIME);
    Record getAllInboundPageList(Context ctx, String SJ_ID,String GYS_ID, String START_TIME, String END_TIME,  int page, int count, String ORDER_ID, int STATUS);
    Record getSingleInbound(String ORDER_ID, String INBOUND_ID);

    boolean confirmInbound(Context ctx,String PACKAGE_CODE);
    boolean saveOutbound(Context ctx,String OUTBOUND_ID,String ORDER_ID,String KW_ID, String GYS_ID, String GYS_NAME,String OUTBOUND_TIME);
    boolean updateOrderStatusOutbound(Context ctx, String ORDER_ID,int STATUS);

    Record getAllOutboundPageList(Context ctx, String SJ_ID, String GYS_ID, String START_TIME, String END_TIME,  int page, int count, String ORDER_ID, int STATUS);

    Record getSingleOutbound(String ORDER_ID, String OUTBOUND_ID);
    boolean confirmOutbound(Context ctx,String PACKAGE_CODE);

    RecordSet getNowRepoPackage(String SJ_ID,String GYS_ID,String F_KW_ID,String KW_ID,String START_TIME,String END_TIME);
    RecordSet getGysOrderDailyGoods(String SJ_ID,String GYS_ID,String F_KW_ID,String KW_ID,String START_TIME,String END_TIME);
    boolean deletePackageAll(String ORDER_ID);
    RecordSet getAllCanPrintMd(Context ctx, String GYS_ID,int isPrinted,String SJ_ID,String PARTNER_NO,String INBOUND_TIME);
    RecordSet getOrderPackageStatus(String ORDER_ID);
    RecordSet getInboundPrintKw(String GYS_ID,String KW_ID, String START_TIME, String END_TIME);
    RecordSet getInboundPrintBox(String GYS_ID,String KW_ID,String START_TIME, String END_TIME);
    RecordSet getOutboundPrintKw(String GYS_ID,String KW_ID, String START_TIME, String END_TIME);
    RecordSet getOutboundPrintBox(String GYS_ID,String KW_ID,String START_TIME, String END_TIME);
    Record getSinglePackage(String PACKAGE_CODE);
    RecordSet getOrderInbound(String ORDER_ID);
    RecordSet getOrderOutbound(String ORDER_ID);
    boolean deleteAllOrderImport(String GYS_ID,String USER_ID);
    boolean saveOrderImport(String IMPORT_ID,String GYS_ID,String USER_ID,String OUT_ORDER_ID,String PARTNER_NAME,String PARTNER_NO,String SPEC_ID,String PRO_NAME,String PRO_SPEC,int PRO_COUNT,String INBOUND_TIME,String JH_TIME,String ERR_STR,String PRO_CODE);
    RecordSet getAllImportsByIDS(String IMPORT_IDS);
    Record getSingleInboundBase(String INBOUND_ID);
    Record getSingleOutboundBase(String OUTBOUND_ID);

    RecordSet getInboundPrintBoxForExcel(String GYS_ID,String KW_ID,String KW_NAME,String START_TIME, String END_TIME);
    RecordSet getOutboundPrintBoxForExcel(String GYS_ID,String KW_ID,String KW_NAME,String START_TIME, String END_TIME);
    //========================
    Record getSingleOrderByPackageCode(String PACKAGE_CODE);

    void test(String outbound_id,String package_code);
    RecordSet getOrderPackagesBase(String ORDER_ID);

    ///===========webservice 用=============
    RecordSet webService_getAllInbound(String KW_ID);
    RecordSet webService_getAllOutbound(String KW_ID);
    boolean webService_printOrderPackage(Context ctx, String PACKAGE_CODE);
    RecordSet webService_getInboundPackage(String INBOUND_ID);
    RecordSet webService_getOutboundPackage(String OUTBOUND_ID);

    RecordSet getOrderProductsSpec(String ORDER_ID);
    RecordSet webService_getAllInboundKws(String START_TIME,String END_TIME);
    RecordSet webService_getAllOutboundKws(String START_TIME,String END_TIME);
    RecordSet webService_getAllInboundKwPackages(String START_TIME,String END_TIME,String KW_ID);
    RecordSet webService_getAllOutboundKwPackages(String START_TIME,String END_TIME,String KW_ID);
}

