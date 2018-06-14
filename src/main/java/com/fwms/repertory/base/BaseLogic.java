package com.fwms.repertory.base;

import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;

public interface BaseLogic {
    int genNaturalSequence(String table);

    RecordSet getProvince();
    RecordSet getCity(String PROVINCE_ID);
    RecordSet getArea(String CITY_ID);
    Record getProvinceById(String PROVINCE_ID);
    Record getCityById(String CITY_ID);
    Record getAreaById(String AREA_ID);

    boolean saveDw( String DW_SX, String DW);
    boolean deleteDw( String DW_SX);
    Record getDW(String DW_SX);
    RecordSet getAllDW();

    RecordSet getAllKwBaseByLevel(int KF_FLAG,int factId);
    Record getSingleKw(String KW_ID);
    boolean deleteKw(String KW_ID);

    Record getSingleKwBase(String KW_ID);
    boolean saveKw(int factId,String KW_ID,String KW_NAME, String KW_ADDR, String KW_PEROID,String USER_ID,int KF_FLAG,int LEVEL,String FID,String CONSIGNEE_NAME,String CONSIGNEE_PHONE,String CONSIGNEE_ADDR,String PROVINCE,String CITY,String AREA,String AREA_ID);
    boolean updateKw(int factId,String KW_ID, String KW_NAME, String KW_ADDR, String KW_PEROID, int KF_FLAG,String CONSIGNEE_NAME,String CONSIGNEE_PHONE,String CONSIGNEE_ADDR,String PROVINCE,String CITY,String AREA,String AREA_ID,String USER_ID);

    //======
    RecordSet getAllProLine();
    boolean saveUserProduct(String GYS_ID,String PRO_ID,String PRO_CODE,String PRO_TYPE, String PRO_TYPE_ID, String PRO_NAME,String PRO_NAME_SX,String MEMO,int TRANSPORT_TYPE,String DW);
    boolean updateProduct(String PRO_ID,String PRO_CODE,String PRO_TYPE, String PRO_TYPE_ID, String PRO_NAME,String PRO_NAME_SX,String MEMO,int TRANSPORT_TYPE,String DW);
    boolean deleteProduct(String PRO_ID);
    boolean saveProductSpec(String PRO_ID,String SPEC_ID,String PRO_CODE,String PRO_SPEC, String PRO_COLOR, String PRO_PRICE,String PRO_PRICE_1,String PRO_NAME,String PRO_NAME_SX,int PERIOD,String MEMO,String BAR_CODE);
    boolean updateProductSpec(String SPEC_ID,String PRO_CODE,String PRO_SPEC, String PRO_COLOR, String PRO_PRICE,String PRO_PRICE_1,String PRO_NAME,String PRO_NAME_SX,int PERIOD,String MEMO,String BAR_CODE);
    boolean deleteProductSpec(String SPEC_ID);
    Record getSingleProSpec(String SPEC_ID);
    Record getSinglePro(String PRO_ID);
    RecordSet getAllGysPro(String GYS_ID);
    RecordSet getAllGysProSpec(String GYS_ID);
    RecordSet getAllPartnerKw(String SJ_ID);

    Record getPartnerKw(String PARTNER_NO);
    RecordSet getAllParentKw(int KF_FLAG,int factId);
    RecordSet getAllSonKw(int KF_FLAG,int factId,String FID);
    RecordSet getAllKW();
}
