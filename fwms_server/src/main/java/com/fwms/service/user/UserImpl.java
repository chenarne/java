package com.fwms.service.user;

import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.log.Logger;
import com.fwms.basedevss.base.sql.ConnectionFactory;
import com.fwms.basedevss.base.sql.SQLExecutor;
import com.fwms.basedevss.base.util.RandomUtils;

import com.fwms.basedevss.base.util.DateUtils;
import com.fwms.basedevss.base.util.Encoders;
import com.fwms.basedevss.base.util.Initializable;
import com.fwms.common.Constants;
import com.fwms.common.GlobalLogics;


public class UserImpl implements UserLogic, Initializable {
    private static final Logger L = Logger.getLogger(UserImpl.class);

    private ConnectionFactory connectionFactory;
    private String db;
    private String userTable = "t_users";
    private String user_ticketTable = "t_users_ticket";
    private String sjPartnerTable = "t_sys_user_partner";
    private String gysWlTable = "t_sys_user_gys_wl";
    private String gysNewTable = "t_sys_gys";
    private String sjTable = "t_sys_sj";
    private String partnerKwTable = "t_sys_partner_kw";
    private String productSpecTable = "t_sys_product_spec";



    public UserImpl() {
    }

    @Override
    public void init() {
        Configuration conf = GlobalConfig.get();
        this.connectionFactory = ConnectionFactory.getConnectionFactory("dbcp");
        this.db = conf.getString("service.db", null);
    }

    @Override
    public void destroy() {
        this.userTable = null;
        this.connectionFactory = ConnectionFactory.close(connectionFactory);
        this.db = null;
    }

    private SQLExecutor getSqlExecutor() {
        return new SQLExecutor(connectionFactory, db);
    }
    private SQLExecutor read_getSqlExecutor() {
        return new SQLExecutor(connectionFactory, db);
    }

    public String SaveGys(Context ctx, String GYS_ID, String USER_ID, String SJ_ID, String GYS_NAME, String GYS_NAME_SX, int GYS_TYPE, String ADDR, String CONTACT_USER, String PHONE, String KEYWORD, String SUPPORT_TYPE, String MEMO) {
        String sql = "INSERT INTO " + gysNewTable + " ( GYS_ID, USER_ID, SJ_ID,  GYS_NAME,  GYS_NAME_SX, GYS_TYPE, ADDR,  CONTACT_USER,  PHONE,  KEYWORD, SUPPORT_TYPE, MEMO,CREATE_TIME)" +
                " VALUES ('"+GYS_ID+"','" + USER_ID + "','" + SJ_ID + "','" + GYS_NAME + "','" + GYS_NAME_SX + "','" + GYS_TYPE + "','" + ADDR + "','"+CONTACT_USER+"','" + PHONE + "'," +
                "'" + KEYWORD + "','" + SUPPORT_TYPE + "' ,'"+MEMO+"','"+DateUtils.now()+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n > 0) {
            return GYS_ID;
        }
        return "";
    }

    public boolean updateGys(String GYS_ID, String USER_ID, String GYS_NAME, String GYS_NAME_SX, String ADDR, String CONTACT_USER, String PHONE, String KEYWORD, String SUPPORT_TYPE, String MEMO, String DISPLAY_NAME, String DISPLAY_NAME_SX) {
        String sql = "UPDATE " + gysNewTable + " SET GYS_NAME='" + GYS_NAME + "',GYS_NAME_SX='" + GYS_NAME_SX + "',ADDR='" + ADDR + "',CONTACT_USER='" + CONTACT_USER + "',PHONE='" + PHONE + "',KEYWORD='" + KEYWORD + "',SUPPORT_TYPE='" + SUPPORT_TYPE + "'," +
                "MEMO='" + MEMO + "' WHERE GYS_ID='" + GYS_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n > 0) {
            se.executeUpdate("UPDATE " + userTable + " SET DISPLAY_NAME='" + DISPLAY_NAME + "',DISPLAY_NAME_SX='" + DISPLAY_NAME_SX + "' WHERE USER_ID='" + USER_ID + "'");
        }
        return n > 0;
    }

    public boolean adminSavePartner(Context ctx, String SJ_ID, String PARTNER_NO, String PARTNER_NAME,String TRADE, String TYPE, String MOBILE, String CONTACT, String PROVINCE,String CITY,String AREA,String ADDR) {
        if (existsPartner(SJ_ID,PARTNER_NAME).isEmpty()){
            String sql = "INSERT INTO " + sjPartnerTable + " (SJ_ID, PARTNER_NO, PARTNER_NAME, TRADE, TYPE,  MOBILE, CONTACT,  CREATE_TIME,PROVINCE, CITY, AREA, ADDR)" +
                    " VALUES ('"+SJ_ID+"','" + PARTNER_NO + "','" + PARTNER_NAME + "','" + TRADE + "','" + TYPE + "','" + MOBILE + "','"+CONTACT+"'," +
                    "'" + DateUtils.now() + "' ,'"+PROVINCE+"','"+CITY+"','"+AREA+"','"+ADDR+"') ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n>0;
        } else{
            String sql = "UPDATE " + sjPartnerTable + " SET PARTNER_NAME='" + PARTNER_NAME + "',TRADE='" + TRADE + "',TYPE='"+TYPE+"',MOBILE='" + MOBILE + "',CONTACT='"+CONTACT+"'," +
                    "PROVINCE='"+PROVINCE+"',CITY='"+CITY+"',AREA='"+AREA+"',ADDR='"+ADDR+"' WHERE PARTNER_NO='"+PARTNER_NO+"' ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n > 0;
        }
    }

    public Record existsPartner(String SJ_ID, String PARTNER_NAME){
        String sql = "SELECT * FROM " + sjPartnerTable + "  WHERE (SJ_ID='" + SJ_ID + "' AND PARTNER_NAME='"+PARTNER_NAME+"') AND DELETE_TIME IS NULL ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    public boolean adminUpdatePartner(String PARTNER_NO, String PARTNER_NAME,String TRADE, String TYPE, String MOBILE, String CONTACT, String PROVINCE,String CITY,String AREA,String ADDR) {
        String sql = "UPDATE " + sjPartnerTable + " SET PARTNER_NAME='" + PARTNER_NAME + "',TRADE='" + TRADE + "',TYPE='"+TYPE+"',MOBILE='" + MOBILE + "',CONTACT='"+CONTACT+"'," +
                "PROVINCE='"+PROVINCE+"',CITY='"+CITY+"',AREA='"+AREA+"',ADDR='"+ADDR+"' WHERE PARTNER_NO='"+PARTNER_NO+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    public Record getSinglePartnerByNo(String PARTNER_NO) {
        String sql = "SELECT * FROM " + sjPartnerTable + "  WHERE (PARTNER_NO='" + PARTNER_NO + "') ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        if (!rec.isEmpty()) {
            rec = formatUserAddr(rec);
        }
        return rec;
    }
    public Record getSinglePartnerByNoBase(String PARTNER_NO) {
        String sql = "SELECT * FROM " + sjPartnerTable + "  WHERE (PARTNER_NO='" + PARTNER_NO + "') ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
    public Record getSinglePartnerByNoBaseOrder(String PARTNER_NO) {
        String sql = "SELECT PARTNER_NO,PARTNER_NAME,SJ_ID FROM " + sjPartnerTable + "  WHERE (PARTNER_NO='" + PARTNER_NO + "') ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
    public RecordSet getUserPartnerByUser(String SJ_ID) {
        String sql = "SELECT * FROM " + sjPartnerTable + "  WHERE (SJ_ID='" + SJ_ID + "') AND DELETE_TIME IS NULL ORDER BY PARTNER_NAME ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs){
            formatUserAddr(rec);
        }
        return recs;
    }
    public RecordSet getSjPartnerBase(String SJ_ID) {
        String sql = "SELECT * FROM " + sjPartnerTable + "  WHERE (SJ_ID='" + SJ_ID + "') AND DELETE_TIME IS NULL ORDER BY PARTNER_NAME ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    public boolean adminDeletePartner(String PARTNER_NO) {
        String sql = "UPDATE " + sjPartnerTable + " SET DELETE_TIME='" + DateUtils.now() + "' WHERE PARTNER_NO='"+PARTNER_NO+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    //根据用户名，获取用户信息
    public Record getUserByUserName(String USER_NAME) {
        String sql = "SELECT * FROM " + userTable + "  WHERE (USER_NAME='" + USER_NAME + "') AND DELETE_TIME IS NULL";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    public Record getSjByUser(String USER_ID) {
        String sql = "SELECT * FROM " + sjTable + "  WHERE (USER_ID='" + USER_ID + "')";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    public RecordSet getAllSj() {
        String sql = "SELECT * FROM " + sjTable + "  WHERE DELETE_TIME IS NULL ORDER BY SJ_NAME_SX";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet rec = se.executeRecordSet(sql, null);
        return rec;
    }
    public RecordSet getAllGysSj(String GYS_ID) {
        String sql = "SELECT * FROM " + sjTable + "  WHERE SJ_ID IN (SELECT SJ_ID FROM "+gysNewTable+" WHERE GYS_ID='"+GYS_ID+"') AND DELETE_TIME IS NULL ORDER BY SJ_NAME_SX";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet rec = se.executeRecordSet(sql, null);
        return rec;
    }

    public Record getGysByUser(String USER_ID) {
        String sql = "SELECT * FROM " + gysNewTable + "  WHERE (USER_ID='" + USER_ID + "')";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    //根据用户名，获取用户信息
    public Record getUserById(String USER_ID) {
        if (USER_ID.length()<=0)
            return new Record();
        String sql = "SELECT * FROM " + userTable + "  WHERE USER_ID='" + USER_ID + "' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    //创建管理员
    public String saveUser(Context ctx, String USER_ID, String USER_NAME, String DISPLAY_NAME,String DISPLAY_NAME_SX, String USER_PASSWORD, int USER_TYPE) {
        String sql = "INSERT INTO " + userTable + " (USER_ID,USER_NAME,DISPLAY_NAME,DISPLAY_NAME_SX,USER_PASSWORD,USER_TYPE,VERIFY,CREATE_TIME) VALUES" +
                " ('"+USER_ID+"','" + USER_NAME + "','" + DISPLAY_NAME + "','"+DISPLAY_NAME_SX+"','" + USER_PASSWORD + "','" + USER_TYPE + "',1,'" + DateUtils.now() + "') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n > 0) {
            Record user = getUserByUserName(USER_NAME);
            return user.getString("USER_ID");
        }
        return "0";
    }

    //用户修改头像
    public boolean updateUserImg(Context ctx, String USER_ID, String USER_IMG) {
        String sql = "UPDATE " + userTable + " SET USER_IMG='" + USER_IMG + "' WHERE USER_ID='" + USER_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }
    //获取用户 PASSWORD
    public Record getSingleUserBaseSingleAndPwd(String USER_ID) {
        String sql = "SELECT USER_ID,USER_NAME,USER_PASSWORD FROM " + userTable + "  WHERE USER_ID='" + USER_ID + "' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    //用户修改密码
    public boolean updateUserPassword(Context ctx, String USER_ID, String USER_PASSWORD) {
        String sql = "UPDATE " + userTable + " SET USER_PASSWORD='" + USER_PASSWORD + "' WHERE USER_ID='" + USER_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    //重置用户密码
    public boolean resetPassword(String USER_NAME) {
        String sql = "UPDATE " + userTable + " SET  USER_PASSWORD='" + Encoders.md5Hex("123456").toUpperCase() + "' WHERE USER_NAME='" + USER_NAME + "' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    //删除用户  或者恢复删除用户
    public boolean deleteUser(Context ctx, String USER_ID) {
        SQLExecutor se = getSqlExecutor();
        String sql0 = "SELECT DELETE_TIME FROM " + userTable + " WHERE USER_ID='" + USER_ID + "' ";
        Record rec = se.executeRecord(sql0, null);
        if (rec.isEmpty())
            return false;
        String nt = rec.getString("DELETE_TIME");
        String sql = "";
        if (nt.length() > 4) {
            sql = "UPDATE " + userTable + " SET DELETE_TIME=NULL  WHERE USER_ID='" + USER_ID + "' ";
        } else {
            sql = "UPDATE " + userTable + " SET DELETE_TIME='" + DateUtils.now() + "'  WHERE USER_ID='" + USER_ID + "' ";
        }
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    public Record getSingleUserStrong(Context ctx, String USER_ID) {
        String sql = "SELECT * FROM " + userTable + "  WHERE USER_ID='" + USER_ID + "' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        if (!rec.isEmpty()) {
            Record ug = Record.fromJson(rec.getString("USER_IMG"));
            if (ug.getString("IMG_S").length() > 0) {
                rec.put("IMG_S", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_S")));
                rec.put("IMG_M", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern","/userPhotoImg/%s"), ug.getString("IMG_M")));
                rec.put("IMG_L", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_L")));
                rec.put("IMG_T", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_T")));
            } else {
                rec.put("IMG_S", "");
                rec.put("IMG_M", "");
                rec.put("IMG_L", "");
                rec.put("IMG_T", "");
            }
            rec.remove("USER_IMG");
        }

        return rec;
    }


    public Record getSingleUserSimple(String USER_ID) {
        String sql = "SELECT * FROM " + userTable + "  WHERE USER_ID='" + USER_ID + "' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        if (!rec.isEmpty()) {
            Record ug = Record.fromJson(rec.getString("USER_IMG"));
            if (ug.getString("IMG_S").length() > 0) {
                rec.put("IMG_S", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_S")));
                rec.put("IMG_M", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern","/userPhotoImg/%s"), ug.getString("IMG_M")));
                rec.put("IMG_L", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_L")));
                rec.put("IMG_T", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_T")));
            } else {
                rec.put("IMG_S", "");
                rec.put("IMG_M", "");
                rec.put("IMG_L", "");
                rec.put("IMG_T", "");
            }
            rec.remove("USER_IMG");
        }

        return rec;
    }


    //获取用户USER_ID是否存在
    public boolean checkUser(String USER_ID) {
        String sql = "SELECT ID FROM " + userTable + "  WHERE USER_ID='" + USER_ID + "' AND DELETE_TIME IS NULL";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return !rec.isEmpty() ? true : false;
    }

    //获取用户 PASSWORD  ,字符串
    public String getSingleUserPassword(Context ctx, String USER_ID) {
        String sql = "SELECT USER_PASSWORD FROM " + userTable + "  WHERE USER_ID='" + USER_ID + "' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        if (rec.isEmpty()) {
            return "";
        }
        return rec.getString("USER_PASSWORD");
    }


    //获取某种类型的全部用户
    public RecordSet getAllUserByUserType(int USER_TYPE,String DISPLAY_NAME) {
        String sql = "SELECT * FROM " + userTable + "  WHERE USER_TYPE='" + USER_TYPE + "' AND DELETE_TIME IS NULL ";
        if (DISPLAY_NAME.length()>0)
            sql+=" AND DISPLAY_NAME LIKE '%"+DISPLAY_NAME+"%'";
        sql+=" ORDER BY DISPLAY_NAME ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    //获取某种类型的全部用户
    @Override
    public RecordSet getAllUserAndDeleteByUserType(int USER_TYPE,String DISPLAY_NAME) {
        String sql = "SELECT * FROM " + userTable + "  WHERE USER_TYPE='" + USER_TYPE + "' ";
        if (DISPLAY_NAME.length()>0)
            sql+=" AND DISPLAY_NAME LIKE '%"+DISPLAY_NAME+"%'";
        sql+=" ORDER BY DISPLAY_NAME ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }



    public RecordSet getAllUserByUserTypeArea(int USER_TYPE,String DEPARTMENT_ID) {
        String sql = "SELECT * FROM " + userTable + "  WHERE USER_TYPE='" + USER_TYPE + "' AND DELETE_TIME IS NULL AND DEPARTMENT_ID IN ("+DEPARTMENT_ID+") ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    public RecordSet getAllUserPartners() {
        String sql = "SELECT * FROM " + sjPartnerTable + "  WHERE DELETE_TIME IS NULL ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        RecordSet allSj = getAllSj();
        for (Record r : recs){
            String SJ_ID = r.getString("SJ_ID");
            Record sj = allSj.findEq("SJ_ID",SJ_ID);
            r.put("SJ_INFO",sj);
        }
        return recs;
    }

    public RecordSet getAllSJGys(String SEARCH_STR,String SJ_ID) {
        String sql = "SELECT * FROM " + gysNewTable + "  WHERE SJ_ID='" + SJ_ID + "' AND DELETE_TIME IS NULL ";
        if (SEARCH_STR.length()>0)
            sql +=" AND (GYS_NAME LIKE '%"+SEARCH_STR+"%' OR GYS_NAME_SX LIKE '%"+SEARCH_STR+"%')";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs){
            rec = formatGYS(rec);
        }
        return recs;
    }
    public RecordSet getAllSJGysSel(String SEARCH_STR,String SJ_ID) {
        String sql = "SELECT * FROM " + gysNewTable + "  WHERE DELETE_TIME IS NULL ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            sql +=" AND SJ_ID='" + SJ_ID + "' ";
        if (SEARCH_STR.length()>0)
            sql +=" AND (GYS_NAME LIKE '%"+SEARCH_STR+"%' OR GYS_NAME_SX LIKE '%"+SEARCH_STR+"%')";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public RecordSet getAllUserSel(int USER_TYPE) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT USER_ID,DISPLAY_NAME,DISPLAY_NAME_SX FROM " + userTable + " WHERE DELETE_TIME IS NULL  ";
        sql += " AND USER_TYPE='"+USER_TYPE+"' ";
        sql += " ORDER BY DISPLAY_NAME ASC ";
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    //分页获取全部用户
    public Record getAllUserPageList(Context ctx, String SEARCH_STR, int USER_TYPE,int VERIFY,int page, int count) {
        SQLExecutor se = read_getSqlExecutor();
        String searchFilter = "";
        if (SEARCH_STR.length() > 0)
            searchFilter += " AND (DISPLAY_NAME LIKE '%" + SEARCH_STR + "%' OR DISPLAY_NAME_SX LIKE '%"+SEARCH_STR+"%') ";
        if (USER_TYPE != 999)
            searchFilter += " AND USER_TYPE='"+USER_TYPE+"' ";
        if (VERIFY != 999 && VERIFY != 9)
            searchFilter += " AND VERIFY='"+VERIFY+"' ";

        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM " + userTable + "  WHERE DELETE_TIME IS NULL ";
        sql0 += searchFilter;
        int rowNum = (int) se.executeRecord(sql0, null).getInt("COUNT1");
        int page_count = 0;
        if (rowNum > 0) {
            if ((rowNum % count) == 0) {
                page_count = (int) (rowNum / count);
            } else {
                page_count = (int) (rowNum / count) + 1;
            }
        }
        String sql = "SELECT * FROM " + userTable + " WHERE DELETE_TIME IS NULL  ";
        sql += searchFilter;

        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY DISPLAY_NAME_SX DESC LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            if (!rec.isEmpty()) {
                Record ug = Record.fromJson(rec.getString("USER_IMG"));
                if (ug.getString("IMG_S").length() > 0) {
                    rec.put("IMG_S", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_S")));
                    rec.put("IMG_M", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern","/userPhotoImg/%s"), ug.getString("IMG_M")));
                    rec.put("IMG_L", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_L")));
                    rec.put("IMG_T", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_T")));
                } else {
                    rec.put("IMG_S", "");
                    rec.put("IMG_M", "");
                    rec.put("IMG_L", "");
                    rec.put("IMG_T", "");
                }
            }
            rec = formatUserAddr(rec);

        }
        Record out_rec = new Record();
        out_rec.put("ROWS_COUNT", rowNum);
        out_rec.put("PAGE_COUNT", page_count);
        if (page == 0 || page == 1) {
            out_rec.put("CURRENT_PAGE", 1);
        } else {
            out_rec.put("CURRENT_PAGE", page);
        }
        out_rec.put("PAGE_SIZE", count);
        out_rec.put("DATAS", recs);
        return out_rec;
    }

    public Record formatUserAddr(Record rec){
           if (rec.isEmpty())
               return rec;
        if (rec.has("PROVINCE")){
            rec.put("PROVINCE_NAME", GlobalLogics.getBaseLogic().getProvinceById(rec.getString("PROVINCE")).getString("PROVINCE_NAME")) ;
        } else{
            rec.put("PROVINCE_NAME","");
        }
        if (rec.has("CITY")){
            rec.put("CITY_NAME", GlobalLogics.getBaseLogic().getCityById(rec.getString("CITY")).getString("CITY_NAME")) ;
        } else{
            rec.put("CITY_NAME","");
        }
        if (rec.has("AREA")){
            rec.put("AREA_NAME", GlobalLogics.getBaseLogic().getAreaById(rec.getString("AREA")).getString("AREA_NAME"));
        } else{
            rec.put("AREA_NAME","");
        }
        rec.put("FULL_ADDR",rec.getString("PROVINCE_NAME")+rec.getString("CITY_NAME")+rec.getString("AREA_NAME")+rec.getString("ADDR"));
        return rec;
    }
    public boolean lock_user(String USER_ID) {
        String sql = "UPDATE " + userTable + " SET VERIFY= ABS(VERIFY-1) WHERE USER_ID='" + USER_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    public synchronized long getMaxID(String tableName) {
        return RandomUtils.generateId();
//        String sql = "SELECT MAX(ID) AS MAXID FROM " + tableName + " ";
//        SQLExecutor se = read_getSqlExecutor();
//        Record rec = se.executeRecord(sql, null);
//        if (rec.isEmpty())
//            return 1;
//        int maxID = (int) rec.getInt("MAXID") + 1;
//        return maxID;
    }

    public boolean saveUserGoods(String GYS_ID,String PRO_ID) {
        String sql = "INSERT INTO  " + gysWlTable + " (GYS_ID,PRO_ID) VALUES ('"+GYS_ID+"','"+PRO_ID+"')";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }







    //==========================ticket======================================================
    public boolean saveUser_ticket(Context ctx, String USER_ID, String USER_TICKET, String EXPIRE_TIME, int USER_TYPE, String AUTH_FROM, String OPEN_ID) {
        Record rec = getExistsUserTicket(USER_ID, USER_TICKET);
        if (rec.isEmpty()) {
            String sql = "INSERT INTO " + user_ticketTable + " ( ID,USER_ID,USER_TICKET,CREATE_TIME,EXPIRE_TIME,USER_TYPE,AUTH_FROM,OPEN_ID) VALUES ( '" + getMaxID(user_ticketTable) + "','" + USER_ID + "','" + USER_TICKET + "','" + DateUtils.now() + "','" + EXPIRE_TIME + "','" + USER_TYPE + "','" + AUTH_FROM + "','" + OPEN_ID + "' ) ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n > 0;
        } else {
            String sql = "UPDATE " + user_ticketTable + " SET EXPIRE_TIME = '" + EXPIRE_TIME + "' WHERE  USER_ID='" + USER_ID + "' ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n > 0;
        }
    }

    public Record getExistsUserTicket(String USER_ID, String USER_TICKET) {
        String sql = "SELECT * FROM " + user_ticketTable + "  WHERE USER_ID='" + USER_ID + "' AND USER_TICKET='" + USER_TICKET + "'";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    public boolean updateUser_ticket(Context ctx, String USER_ID, String USER_TICKET, String EXPIRE_TIME, int USER_TYPE, String AUTH_FROM, String OPEN_ID) {
        Record rec = getExistsUserTicket(USER_ID, USER_TICKET);
        if (!rec.isEmpty()) {
            String sql = "UPDATE " + user_ticketTable + " SET EXPIRE_TIME='" + EXPIRE_TIME + "' WHERE USER_ID='" + USER_ID + "' AND USER_TYPE='" + USER_TYPE + "' AND AUTH_FROM='" + AUTH_FROM + "' AND OPEN_ID='" + OPEN_ID + "' ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n > 0;
        } else {
            String sql = "INSERT INTO " + user_ticketTable + " ( ID,USER_ID,USER_TICKET,CREATE_TIME,EXPIRE_TIME,USER_TYPE,AUTH_FROM,OPEN_ID ) VALUES ( '" + getMaxID(user_ticketTable) + "','" + USER_ID + "','" + USER_TICKET + "','" + DateUtils.now() + "','" + EXPIRE_TIME + "','" + USER_TYPE + "','" + AUTH_FROM + "','" + OPEN_ID + "' ) ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n > 0;
        }
    }

    public boolean deleteUser_ticket(Context ctx, String USER_TICKET) {
        String sql = "DELETE FROM " + user_ticketTable + "  WHERE USER_TICKET='" + USER_TICKET + "' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    public boolean deleteUser_ticket_type(Context ctx, String USER_TICKET) {
        String sql = "DELETE FROM " + user_ticketTable + "  WHERE USER_TICKET='" + USER_TICKET + "' AND USER_TYPE=2 ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    public Record getUserIdByTicket(Context ctx, String ticket) {
        String sql = "SELECT * FROM " + user_ticketTable + "  WHERE USER_TICKET='" + ticket.replace("'","''") + "' AND EXPIRE_TIME > '" + DateUtils.now() + "' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    //===========================================================

    public Record formatGYS(Record rec) {
        String USER_ID = rec.getString("USER_ID") ;
        Record user =  getSingleUserSimple(USER_ID);
        if (!user.isEmpty()){
            rec.put("USER_NAME",user.getString("USER_NAME"));
            rec.put("DISPLAY_NAME_SX",user.getString("DISPLAY_NAME_SX"));
            rec.put("DISPLAY_NAME",user.getString("DISPLAY_NAME"));
            rec.put("USER_TYPE",user.getString("USER_TYPE"));
            rec.put("VERIFY",user.getString("VERIFY"));
        }
        RecordSet PRO_ALL = GlobalLogics.getBaseLogic().getAllGysPro(rec.getString("GYS_ID")) ;
        rec.put("PRO_NAMES",PRO_ALL.joinColumnValues("PRO_NAME_SX", ","));
        rec.put("PRO_COUNT",PRO_ALL.size());
        String SJ_ID = rec.getString("SJ_ID");
        if (SJ_ID.length()>0){
            rec.put("SJ_INFO",getSingleSj(SJ_ID));
        }else{
            rec.put("SJ_INFO",new Record());
        }
        return rec;
    }

    //获取所有,分页
    public Record getAllGysPageList( String SEARCH_STR, String SJ_ID,int page, int count) {
        SQLExecutor se = read_getSqlExecutor();

        String filter = "";
        if (SEARCH_STR.length() > 0)
            filter += " AND (GYS_NAME LIKE '%" + SEARCH_STR + "%' OR GYS_NAME_SX LIKE '%" + SEARCH_STR + "%') ";
        if (SJ_ID.length() > 0)
            filter += " AND (SJ_ID='" + SJ_ID + "') ";

        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM " + gysNewTable + "  WHERE 1=1 ";
        sql0+=filter;
        int rowNum = (int) se.executeRecord(sql0, null).getInt("COUNT1");
        int page_count = 0;
        if (rowNum > 0) {
            if ((rowNum % count) == 0) {
                page_count = (int) (rowNum / count);
            } else {
                page_count = (int) (rowNum / count) + 1;
            }
        }
        String sql = "SELECT * FROM " + gysNewTable + " WHERE 1=1 ";
        sql+=filter;
        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY GYS_NAME LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            rec.put("STR_CREATE_TIME", Constants.dateLongToString(rec.getInt("CREATE_TIME")));
        }
        Record out_rec = new Record();
        out_rec.put("ROWS_COUNT", rowNum);
        out_rec.put("PAGE_COUNT", page_count);
        if (page == 0 || page == 1) {
            out_rec.put("CURRENT_PAGE", 1);
        } else {
            out_rec.put("CURRENT_PAGE", page);
        }
        out_rec.put("PAGE_SIZE", count);
        out_rec.put("DATAS", recs);
        return out_rec;
    }

    //单条记录
    public Record singleGys(String GYS_ID) {
        String sql ="SELECT * FROM " + gysNewTable + "  WHERE GYS_ID='"+GYS_ID+"' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        rec = formatGYS(rec);
        return rec;
    }

    //单条记录
    public boolean deleteGys(String GYS_ID) {
        Record gys = singleGys( GYS_ID) ;
        String USER_ID = gys.getString("USER_ID");
        String sql ="UPDATE " + gysNewTable + " SET DELETE_TIME='"+DateUtils.now()+"' WHERE GYS_ID='"+GYS_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n>0){
            n = se.executeUpdate("UPDATE " + userTable + " SET DELETE_TIME='"+DateUtils.now()+"',VERIFY=0 WHERE USER_ID='"+USER_ID+"' ") ;
        }
        return n>0;
    }

    //单条记录
    public boolean deleteUser(String USER_ID) {
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate("UPDATE " + userTable + " SET DELETE_TIME='" + DateUtils.now() + "',VERIFY=0 WHERE USER_ID='" + USER_ID + "' ");
        return n > 0;
    }


    public Record getSingleGysBase(String GYS_ID){
        String sql = "SELECT * FROM " + gysNewTable + "  WHERE GYS_ID='" + GYS_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
    public RecordSet getAllGys(){
        String sql = "SELECT GYS_ID,GYS_NAME,GYS_NAME_SX FROM " + gysNewTable + " ";
        SQLExecutor se = getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public RecordSet getAllPartners(){
        String sql = "SELECT PARTNER_NO,PARTNER_NAME,SJ_ID FROM " + sjPartnerTable + " ";
        SQLExecutor se = getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public Record getSingleGys(String GYS_ID){
        String sql = "SELECT * FROM " + gysNewTable + "  WHERE GYS_ID='" + GYS_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        if (!rec.isEmpty())
            rec= formatGYS(rec);
        return rec;
    }

    public Record getSingleSj(String SJ_ID){
        String sql = "SELECT * FROM " + sjTable + "  WHERE SJ_ID='" + SJ_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        if (!rec.isEmpty())
            rec = formatSjUser(rec);
        return rec;
    }
    public Record getSingleSjBase(String SJ_ID){
        String sql = "SELECT * FROM " + sjTable + "  WHERE SJ_ID='" + SJ_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }


    //========sj==========================
    //分页获取全部用户
    public Record getAllSjPageList(Context ctx, String SEARCH_STR, int SJ_TYPE,int page, int count) {
        SQLExecutor se = read_getSqlExecutor();
        String searchFilter = "";
        if (SEARCH_STR.length() > 0)
            searchFilter += " AND (SJ_NAME LIKE '%" + SEARCH_STR + "%' OR SJ_NAME_SX LIKE '%"+SEARCH_STR+"%') ";
        if (SJ_TYPE != 999 && SJ_TYPE != 9)
            searchFilter += " AND SJ_TYPE='"+SJ_TYPE+"' ";

        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM " + sjTable + "  WHERE DELETE_TIME IS NULL ";
        sql0 += searchFilter;
        int rowNum = (int) se.executeRecord(sql0, null).getInt("COUNT1");
        int page_count = 0;
        if (rowNum > 0) {
            if ((rowNum % count) == 0) {
                page_count = (int) (rowNum / count);
            } else {
                page_count = (int) (rowNum / count) + 1;
            }
        }
        String sql = "SELECT * FROM " + sjTable + " WHERE DELETE_TIME IS NULL  ";
        sql += searchFilter;

        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY SJ_NAME LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            rec = formatSjUser(rec);

        }
        Record out_rec = new Record();
        out_rec.put("ROWS_COUNT", rowNum);
        out_rec.put("PAGE_COUNT", page_count);
        if (page == 0 || page == 1) {
            out_rec.put("CURRENT_PAGE", 1);
        } else {
            out_rec.put("CURRENT_PAGE", page);
        }
        out_rec.put("PAGE_SIZE", count);
        out_rec.put("DATAS", recs);
        return out_rec;
    }

    public Record formatSjUser(Record rec){
        if (rec.isEmpty())
            return rec;
        String USER_ID = rec.getString("USER_ID") ;
        Record user =  getSingleUserSimple(USER_ID);
        if (!user.isEmpty()){
             rec.put("USER_NAME",user.getString("USER_NAME"));
             rec.put("DISPLAY_NAME_SX",user.getString("DISPLAY_NAME_SX"));
             rec.put("DISPLAY_NAME",user.getString("DISPLAY_NAME"));
             rec.put("USER_TYPE",user.getString("USER_TYPE"));
             rec.put("VERIFY",user.getString("VERIFY"));
        }
        return rec;
    }

    //单条记录
    public boolean deleteSj(String SJ_ID) {
        Record gys = getSingleSj(SJ_ID);
        String USER_ID = gys.getString("USER_ID");
        String sql ="UPDATE " + sjTable + " SET DELETE_TIME='"+DateUtils.now()+"' WHERE SJ_ID='"+SJ_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n>0){
            n = se.executeUpdate("UPDATE " + userTable + " SET DELETE_TIME='"+DateUtils.now()+"',VERIFY=0 WHERE USER_ID='"+USER_ID+"' ") ;
        }
        return n>0;
    }
    public String SaveSj(Context ctx, String SJ_ID, String USER_ID,String SJ_NAME, String SJ_NAME_SX, int SJ_TYPE,String MOBILE, String CONTACT, String EMAIL, String MEMO, String ADDR) {
        String sql = "INSERT INTO " + sjTable + " (SJ_ID, USER_ID,SJ_NAME,  SJ_NAME_SX, SJ_TYPE, MOBILE,  CONTACT,  EMAIL,  MEMO, ADDR,CREATE_TIME)" +
                " VALUES ('"+SJ_ID+"','" + USER_ID + "','" + SJ_NAME + "','" + SJ_NAME_SX + "','" + SJ_TYPE + "','" + MOBILE + "','" + CONTACT + "','"+EMAIL+"','" + MEMO + "','" + ADDR + "','"+DateUtils.now()+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n > 0) {
            return SJ_ID;
        }
        return "";
    }

    public boolean updateSj(Context ctx, String SJ_ID, String USER_ID,String SJ_NAME, String SJ_NAME_SX, int SJ_TYPE,String MOBILE, String CONTACT, String EMAIL, String MEMO, String ADDR,String DISPLAY_NAME,String DISPLAY_NAME_SX) {
        String sql = "UPDATE " + sjTable + " SET SJ_NAME='" + SJ_NAME + "',SJ_NAME_SX='" + SJ_NAME_SX + "',SJ_TYPE='" + SJ_TYPE + "',MOBILE='" + MOBILE + "',CONTACT='" + CONTACT + "',EMAIL='"+EMAIL+"',MEMO='" + MEMO + "',ADDR='" + ADDR + "' WHERE SJ_ID='"+SJ_ID+"'";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n > 0) {
            se.executeUpdate("UPDATE " + userTable + " SET DISPLAY_NAME='" + DISPLAY_NAME + "',DISPLAY_NAME_SX='" + DISPLAY_NAME_SX + "' WHERE USER_ID='" + USER_ID + "'");
        }
        return n>0;
    }

    public boolean savePartnerKw(Context ctx, String PARTNER_NO, String KW_ID) {
        SQLExecutor se = getSqlExecutor();
        se.executeUpdate("DELETE FROM " + partnerKwTable + " WHERE PARTNER_NO='" + PARTNER_NO + "' ");
        se.executeUpdate("INSERT INTO " + partnerKwTable + " (PARTNER_NO, KW_ID) VALUES ('"+PARTNER_NO+"','" + KW_ID + "') ");
        return true;
    }
    public Record existsPartnerKw(String PARTNER_NO, String KW_ID) {
        String sql = "SELECT * FROM " + partnerKwTable + "  WHERE PARTNER_NO='" + PARTNER_NO + "' AND KW_ID='"+KW_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
    public Record getPartnerKw(String PARTNER_NO) {
        String sql = "SELECT * FROM " + partnerKwTable + "  WHERE PARTNER_NO='" + PARTNER_NO + "' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecordSet(sql, null).getFirstRecord();
        return rec;
    }


    //==============check_import======================
    public Record check_partner_name(String PARTNER_NAME) {
        String sql = "SELECT * FROM " + sjPartnerTable + "  WHERE PARTNER_NAME='" + PARTNER_NAME + "' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecordSet(sql, null).getFirstRecord();
        return rec;
    }
    public Record check_pro_name_spec(String PRO_NAME,String PRO_SPEC) {
        String sql = "SELECT * FROM " + productSpecTable + "  WHERE 1=1 ";
        if (PRO_NAME.length()>0)
            sql +=" AND PRO_NAME='" + PRO_NAME + "' ";
        if (PRO_SPEC.length()>0)
            sql +=" AND PRO_SPEC='" + PRO_SPEC + "' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecordSet(sql, null).getFirstRecord();
        return rec;
    }

}

