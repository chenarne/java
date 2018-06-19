package com.fwms.service.user;

import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;

import java.util.List;

public interface UserLogic {
    //===================user  ==================
    Record getUserByUserName(String USER_NAME);
    String SaveGys(Context ctx, String GYS_ID, String USER_ID, String SJ_ID, String GYS_NAME, String GYS_NAME_SX, int GYS_TYPE, String ADDR, String CONTACT_USER, String PHONE, String KEYWORD, String SUPPORT_TYPE, String MEMO);
    boolean lock_user(String USER_ID);
    boolean updateGys(String GYS_ID, String USER_ID, String GYS_NAME, String GYS_NAME_SX, String ADDR, String CONTACT_USER, String PHONE, String KEYWORD, String SUPPORT_TYPE, String MEMO, String DISPLAY_NAME, String DISPLAY_NAME_SX);
    //获取某种类型的全部用户
    RecordSet getAllUserAndDeleteByUserType(int USER_TYPE, String DISPLAY_NAME);
    Record getSinglePartnerByNoBaseOrder(String PARTNER_NO);

    boolean adminSavePartner(Context ctx, String SJ_ID, String PARTNER_NO, String PARTNER_NAME,String TRADE, String TYPE, String MOBILE, String CONTACT, String PROVINCE,String CITY,String AREA,String ADDR);
    boolean adminUpdatePartner(String PARTNER_NO, String PARTNER_NAME,String TRADE, String TYPE, String MOBILE, String CONTACT, String PROVINCE,String CITY,String AREA,String ADDR);
    Record getSinglePartnerByNo(String PARTNER_NO);
    RecordSet getUserPartnerByUser(String SJ_ID);
    boolean adminDeletePartner(String PARTNER_NO);
    boolean saveUserGoods(String USER_ID,String PRO_ID);
    RecordSet getAllSJGys(String SEARCH_STR,String SJ_ID);
    RecordSet getAllSJGysSel(String SEARCH_STR,String SJ_ID);
    boolean updateUserImg(Context ctx, String USER_ID, String USER_IMG);

    boolean updateUserPassword(Context ctx, String USER_ID, String USER_PASSWORD);

    boolean resetPassword(String USER_NAME);

    boolean deleteUser(Context ctx, String USER_ID);
    RecordSet getAllUserSel(int USER_TYPE);
    Record getSingleUserStrong(Context ctx, String USER_ID);
    RecordSet getSjPartnerBase(String SJ_ID);

    Record getSingleUserSimple(String USER_ID);

    RecordSet getAllUserByUserTypeArea(int USER_TYPE, String WORK_AREA);

    String getSingleUserPassword(Context ctx, String USER_ID);

    RecordSet getAllUserByUserType(int USER_TYPE, String DISPLAY_NAME);

    Record getAllUserPageList(Context ctx, String SEARCH_STR, int USER_TYPE,int VERIFY,int page, int count);


    Record getSingleUserBaseSingleAndPwd(String USER_ID);
    Record getUserById(String USER_ID);
    boolean saveUser_ticket(Context ctx, String USER_ID, String USER_TICKET, String EXPIRE_TIME, int USER_TYPE, String AUTH_FROM, String OPEN_ID);

    Record getExistsUserTicket(String USER_ID, String USER_TICKET);

    boolean updateUser_ticket(Context ctx, String USER_ID, String USER_TICKET, String EXPIRE_TIME, int USER_TYPE, String AUTH_FROM, String OPEN_ID);

    boolean deleteUser_ticket(Context ctx, String USER_TICKET);

    boolean deleteUser_ticket_type(Context ctx, String USER_TICKET);

    Record getUserIdByTicket(Context ctx, String ticket);

    //==========================
    boolean deleteGys(String GYS_ID);
    String saveUser(Context ctx, String USER_ID, String USER_NAME, String DISPLAY_NAME,String DISPLAY_NAME_SX, String USER_PASSWORD, int USER_TYPE);
    boolean deleteUser(String USER_ID);
    Record getAllGysPageList( String SEARCH_STR, String SJ_ID,int page, int count);
    Record getSingleGysBase(String GYS_ID);

    Record getSingleGys(String GYS_ID);

    boolean deleteSj(String SJ_ID);
    Record getAllSjPageList(Context ctx, String SEARCH_STR, int SJ_TYPE,int page, int count);
    String SaveSj(Context ctx, String SJ_ID, String USER_ID,String SJ_NAME, String SJ_NAME_SX, int SJ_TYPE,String MOBILE, String CONTACT, String EMAIL, String MEMO, String ADDR);
    boolean updateSj(Context ctx, String SJ_ID, String USER_ID,String SJ_NAME, String SJ_NAME_SX, int SJ_TYPE,String MOBILE, String CONTACT, String EMAIL, String MEMO, String ADDR,String DISPLAY_NAME,String DISPLAY_NAME_SX);
    Record getSingleSj(String SJ_ID);
    RecordSet getAllSj();
    Record getSingleSjBase(String SJ_ID);
    Record getSinglePartnerByNoBase(String PARTNER_NO);

    boolean savePartnerKw(Context ctx, String PARTNER_NO, String KW_ID);
    Record existsPartnerKw(String PARTNER_NO, String KW_ID);
    Record getPartnerKw(String PARTNER_NO);
    RecordSet getAllGysSj(String GYS_ID);
    RecordSet getAllUserPartners();

    Record getGysByUser(String USER_ID);
    Record getSjByUser(String USER_ID);



    //===========================
    Record check_partner_name(String PARTNER_NAME);
    Record check_pro_name_spec(String PRO_NAME,String PRO_SPEC);
}

