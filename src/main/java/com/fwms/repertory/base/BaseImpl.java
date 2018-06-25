package com.fwms.repertory.base;

import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.log.Logger;
import com.fwms.basedevss.base.sql.ConnectionFactory;
import com.fwms.basedevss.base.sql.SQLExecutor;
import com.fwms.basedevss.base.util.DateUtils;
import com.fwms.basedevss.base.util.Initializable;

import java.util.ArrayList;
import java.util.List;


public class BaseImpl implements BaseLogic, Initializable {
    private static final Logger L = Logger.getLogger(BaseImpl.class);

    private ConnectionFactory connectionFactory;
    private String db;
    private String provinceTable = "t_sys_province";
    private String cityTable = "t_sys_city";
    private String areaTable = "t_sys_area";
    private String dwTable = "t_sys_dw";
    private String kwTable = "t_sys_kw";
    private String gysNewTable = "t_sys_gys";

    private String productTable = "t_sys_product";
    private String productSpecTable = "t_sys_product_spec";
    private String productLineTable = "t_sys_product_line";
    private String gysWlTable = "t_sys_user_gys_wl";
    private String sequenceTable = "t_sys_natural_sequence";
    private String partnerKwTable = "t_sys_partner_kw";
    private String sjPartnerTable = "t_sys_user_partner";
    private String specFullBoxTable = "t_sys_product_spec_fullbox";
    private String logTable = "t_sys_log";

    public BaseImpl() {
    }

    @Override
    public void init() {
        Configuration conf = GlobalConfig.get();
        this.connectionFactory = ConnectionFactory.getConnectionFactory("dbcp");
        this.db = conf.getString("service.db", null);
    }

    @Override
    public void destroy() {
        this.connectionFactory = ConnectionFactory.close(connectionFactory);
        this.db = null;
    }
    private SQLExecutor getSqlExecutor() {
        return new SQLExecutor(connectionFactory, db);
    }
    private SQLExecutor read_getSqlExecutor() {
        return new SQLExecutor(connectionFactory, db);
    }

    public void saveLog(String LOGS) {
        String sql = "INSERT INTO " + logTable + " (LOGS) VALUES" +
                " ('"+LOGS+"') ";
        SQLExecutor se = getSqlExecutor();
        se.executeUpdate(sql);
    }

    public RecordSet getProvince() {
        String sql = "SELECT * FROM " + provinceTable + "  WHERE 1=1 ORDER BY PROVINCE_ID";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public Record getProvinceById(String PROVINCE_ID) {
        String sql = "SELECT * FROM " + provinceTable + "  WHERE PROVINCE_ID='"+PROVINCE_ID+"'";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
    public RecordSet getCity(String PROVINCE_ID) {
        String sql = "SELECT * FROM " + cityTable + "  WHERE 1=1 ";
        if (PROVINCE_ID.length()>0)
            sql += " AND PROVINCE_ID='"+PROVINCE_ID+"'";
        sql += " ORDER BY CITY_ID ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public Record getCityById(String CITY_ID) {
        String sql = "SELECT * FROM " + cityTable + "  WHERE CITY_ID='"+CITY_ID+"'";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
    public RecordSet getAreaByName(String AREA_NAME) {
        String sql = "SELECT * FROM " + areaTable + "  WHERE 1=1 ";
        if (AREA_NAME.length()>0)
            sql += " AND AREA_NAME='"+AREA_NAME+"'";
        sql += " ORDER BY AREA_ID ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    public RecordSet getArea(String CITY_ID) {
        String sql = "SELECT * FROM " + areaTable + "  WHERE 1=1 ";
        if (CITY_ID.length()>0)
            sql += " AND CITY_ID='"+CITY_ID+"'";
        sql += " ORDER BY AREA_ID ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public Record getAreaById(String AREA_ID) {
        String sql = "SELECT * FROM " + areaTable + "  WHERE AREA_ID='"+AREA_ID+"'";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    public boolean saveDw( String DW_SX, String DW) {
        String sql = "INSERT INTO " + dwTable + " (DW_SX, DW)" +
                " VALUES ('"+DW_SX+"','" + DW + "') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean deleteDw( String DW_SX) {
        String sql = "DELETE FROM " + dwTable + " WHERE DW_SX='"+DW_SX+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public Record getDWBYSX(String DW_SX) {
        String sql = "SELECT * FROM " + dwTable + "  WHERE DW_SX='"+DW_SX+"'";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecordSet(sql, null).getFirstRecord();
        return rec;
    }

    public RecordSet getAllDW() {
        String sql = "SELECT * FROM " + dwTable + "  ORDER BY DW";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public RecordSet getAllKW() {
        String sql = "SELECT * FROM " + kwTable + " ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public RecordSet getAllKWSel() {
        String sql = "SELECT * FROM " + kwTable + " WHERE DELETE_TIME IS NULL ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public RecordSet getAllKwBaseByLevel(int KF_FLAG,int factId) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + kwTable + " WHERE LEVEL=1 AND DELETE_TIME IS NULL ";
        if(factId!=999){
            sql+=" and FACTID="+factId;
        }
        if (KF_FLAG != 9 && KF_FLAG != 999){
            sql+=" AND KF_FLAG='"+KF_FLAG+"'";
        }
        sql += " ORDER BY SORT  ";
        RecordSet recs = se.executeRecordSet(sql, null);
        if (recs.size() > 0){
            for (Record record : recs){
                String sql1 = "SELECT * FROM " + kwTable + " WHERE LEVEL=2 AND DELETE_TIME IS NULL AND FID='"+record.getString("FID")+"' ";
                if (KF_FLAG != 9 && KF_FLAG != 999){
                    sql1+=" AND KF_FLAG='"+KF_FLAG+"'";
                }
                sql1 += " ORDER BY SORT  ";
                RecordSet recs1 = se.executeRecordSet(sql1, null);
                record.put("SON",recs1);
            }
        }
        return recs;
    }

    public RecordSet getAllParentKw(int KF_FLAG,int factId) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + kwTable + " WHERE LEVEL=1 AND DELETE_TIME IS NULL ";
        if(factId!=999){
            sql+=" and FACTID="+factId;
        }
        if (KF_FLAG != 9 && KF_FLAG != 999){
            sql+=" AND KF_FLAG='"+KF_FLAG+"'";
        }
        sql += " ORDER BY SORT  ";
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    public RecordSet getAllSonKw(int KF_FLAG,int factId,String FID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + kwTable + " WHERE LEVEL=2 AND DELETE_TIME IS NULL ";
        if(factId!=999){
            sql+=" and FACTID="+factId;
        }
        if (KF_FLAG != 9 && KF_FLAG != 999){
            sql+=" AND KF_FLAG='"+KF_FLAG+"'";
        }
        sql +=" AND FID='"+FID+"'";
        sql += " ORDER BY SORT  ";
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    public boolean deleteKw(String KW_ID) {
        Record k = getSingleKw(KW_ID);
        SQLExecutor se = getSqlExecutor();
        if (k.getInt("LEVEL")==1){
            se.executeUpdate("UPDATE " + kwTable + " SET DELETE_TIME='" + DateUtils.now() + "' WHERE FID='"+KW_ID+"'");
        }
        long n = se.executeUpdate("UPDATE " + kwTable + " SET DELETE_TIME='" + DateUtils.now() + "' WHERE KW_ID='" + KW_ID + "'");
        return n>0;
    }

    public String getSingleKwName(String KW_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT KW_NAME FROM " + kwTable + " WHERE KW_ID='"+KW_ID+"' ";
        Record rec = se.executeRecord(sql, null);
        return rec.isEmpty()?"":rec.getString("KW_NAME");
    }
    public Record getSingleKw(String KW_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + kwTable + " WHERE KW_ID='"+KW_ID+"' ";
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
    public Record getSingleKwBase(String KW_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + kwTable + " WHERE KW_ID='"+KW_ID+"' ";
        Record rec = se.executeRecord(sql, null);

        if (rec.isEmpty())
            return rec;
        //再把省、市的ID查出来
        String PROVINCE_ID = "";
        String CITY_ID = "";
        String AREA_ID = "";
        if (rec.getInt("LEVEL") == 1){
            AREA_ID = rec.getString("AREA_ID") ;
            CITY_ID = getAreaById(rec.getString("AREA_ID")).getString("CITY_ID");
            if (CITY_ID.length() > 0)
                PROVINCE_ID = getCityById(CITY_ID).getString("PROVINCE_ID");
        }

        rec.put("PROVINCE_ID",PROVINCE_ID);
        rec.put("CITY_ID",CITY_ID);

        return rec;
    }

    public boolean saveKw(int factId,String KW_ID,String KW_NAME, String KW_ADDR, String KW_PEROID,String USER_ID,int KF_FLAG,int LEVEL,String FID,String CONSIGNEE_NAME,String CONSIGNEE_PHONE,String CONSIGNEE_ADDR,String PROVINCE,String CITY,String AREA,String AREA_ID) {
        String sql = "INSERT INTO " + kwTable + " (FACTID,KW_ID, KW_NAME, KW_ADDR, KW_PEROID, USER_ID,KF_FLAG,CREATE_TIME,LEVEL,FID,CONSIGNEE_NAME, CONSIGNEE_PHONE, CONSIGNEE_ADDR, PROVINCE, CITY, AREA,AREA_ID) VALUES" +
                " ("+factId+",'" + KW_ID + "','" + KW_NAME + "','"+KW_ADDR+"','"+KW_PEROID+"','"+USER_ID+"','"+KF_FLAG+"','"+DateUtils.now()+"','"+LEVEL+"','"+FID+"','"+CONSIGNEE_NAME+"','"+CONSIGNEE_PHONE+"','"+CONSIGNEE_ADDR+"','"+PROVINCE+"','"+CITY+"','"+AREA+"','"+AREA_ID+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateKw(int factId,String KW_ID, String KW_NAME, String KW_ADDR, String KW_PEROID, int KF_FLAG,String CONSIGNEE_NAME,String CONSIGNEE_PHONE,String CONSIGNEE_ADDR,String PROVINCE,String CITY,String AREA,String AREA_ID,String USER_ID) {
        String sql = "UPDATE " + kwTable + " SET FACTID="+factId+", KW_NAME='" + KW_NAME + "',KW_ADDR='" + KW_ADDR + "',KW_PEROID='" + KW_PEROID + "',KF_FLAG='"+KF_FLAG+"',CONSIGNEE_NAME='"+CONSIGNEE_NAME+"',CONSIGNEE_PHONE='"+CONSIGNEE_PHONE+"',CONSIGNEE_ADDR='"+CONSIGNEE_ADDR+"',PROVINCE='"+PROVINCE+"',CITY='"+CITY+"',AREA='"+AREA+"',AREA_ID='"+AREA_ID+"',USER_ID='"+USER_ID+"' WHERE KW_ID='" + KW_ID + "'";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }


    //=======PRO=================
    public RecordSet getAllProLine() {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + productLineTable + " WHERE STATUS=1 AND DELETE_TIME IS NULL ORDER BY PRO_TYPE_ID ";
        RecordSet rec = se.executeRecordSet(sql, null);
        return rec;
    }

    public boolean saveUserProduct(String GYS_ID, String PRO_ID, String PRO_CODE, String PRO_TYPE, String PRO_TYPE_ID, String PRO_NAME, String PRO_NAME_SX, String MEMO,int TRANSPORT_TYPE,String PRO_DW,String PRO_DW_NAME) {
        SQLExecutor se = getSqlExecutor();
        String sql = "INSERT INTO " + productTable + " (PRO_ID, PRO_CODE, PRO_TYPE,  PRO_TYPE_ID,  PRO_NAME, PRO_NAME_SX, MEMO,TRANSPORT_TYPE, PRO_DW,GYS_ID,PRO_DW_NAME) VALUES" +
                " (" + PRO_ID + ",'" + PRO_CODE + "','" + PRO_TYPE + "','" + PRO_TYPE_ID + "','" + PRO_NAME + "','" + PRO_NAME_SX + "','" + MEMO + "','"+TRANSPORT_TYPE+"','"+PRO_DW+"','"+GYS_ID+"','"+PRO_DW_NAME+"') ";

        String sql2 = "INSERT INTO  " + gysWlTable + " (GYS_ID,PRO_ID) VALUES ('" + GYS_ID + "','" + PRO_ID + "')";
        List<String> ls = new ArrayList<String>();
        ls.add(sql);
        ls.add(sql2);
        long m = se.executeUpdate(ls);
        return m > 0;
    }

    public boolean updateProduct(String PRO_ID,String PRO_CODE,String PRO_TYPE, String PRO_TYPE_ID, String PRO_NAME,String PRO_NAME_SX,String MEMO,int TRANSPORT_TYPE,String PRO_DW,String PRO_DW_NAME) {
        String sql = "UPDATE " + productTable + " SET PRO_CODE='" + PRO_CODE + "',PRO_TYPE='" + PRO_TYPE + "',PRO_TYPE_ID='"+PRO_TYPE_ID+"',PRO_NAME='"+PRO_NAME+"',PRO_NAME_SX='"+PRO_NAME_SX+"',MEMO='"+MEMO+"',TRANSPORT_TYPE='"+TRANSPORT_TYPE+"',PRO_DW='"+PRO_DW+"',PRO_DW_NAME='"+PRO_DW_NAME+"' WHERE PRO_ID='"+PRO_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n>0)
            se.executeUpdate("UPDATE " + productSpecTable + " SET PRO_DW_NAME='"+PRO_DW_NAME+"' WHERE PRO_ID='" + PRO_ID + "' ");
        return n>0;
    }

    public boolean deleteProduct(String PRO_ID) {
        List<String> sql = new ArrayList<String>();
        String sql1 = "UPDATE " + productTable + "  SET DELETE_TIME='"+DateUtils.now()+"' WHERE PRO_ID='" + PRO_ID + "' ";
        String sql2 = "UPDATE " + productSpecTable + "  SET DELETE_TIME='"+DateUtils.now()+"' WHERE PRO_ID='" + PRO_ID + "' ";
        sql.add(sql1);
        sql.add(sql2);
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }


    public boolean saveProductSpec(String PRO_ID,String SPEC_ID,String PRO_CODE,String PRO_SPEC, String PRO_COLOR, String PRO_PRICE,String PRO_PRICE_1,String PRO_NAME,String PRO_NAME_SX,int PERIOD,String MEMO,String BAR_CODE,String PRO_DW_NAME,int SINGLE_BOX) {
        String sql = "INSERT INTO " + productSpecTable + " (PRO_ID, SPEC_ID,PRO_CODE, PRO_SPEC,  PRO_COLOR,PRO_PRICE, PRO_PRICE_1, PRO_NAME, PRO_NAME_SX,PERIOD, MEMO,BAR_CODE,PRO_DW_NAME,SINGLE_BOX) VALUES" +
                " ("+PRO_ID+",'"+SPEC_ID+"','" + PRO_CODE + "','" + PRO_SPEC + "','"+PRO_COLOR+"','"+PRO_PRICE+"','"+PRO_PRICE_1+"','"+PRO_NAME+"','"+PRO_NAME_SX+"','"+PERIOD+"','"+MEMO+"','"+BAR_CODE+"','"+PRO_DW_NAME+"','"+SINGLE_BOX+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean updateProductSpec(String SPEC_ID,String PRO_CODE,String PRO_SPEC, String PRO_COLOR, String PRO_PRICE,String PRO_PRICE_1,String PRO_NAME,String PRO_NAME_SX,int PERIOD,String MEMO,String BAR_CODE,int SINGLE_BOX) {
        String sql = "UPDATE " + productSpecTable + " SET PRO_CODE='" + PRO_CODE + "',PRO_SPEC='" + PRO_SPEC + "',PRO_COLOR='"+PRO_COLOR+"',PRO_PRICE='"+PRO_PRICE+"',PRO_PRICE_1='"+PRO_PRICE_1+"',PRO_NAME='"+PRO_NAME+"',PRO_NAME_SX='"+PRO_NAME_SX+"',PERIOD='"+PERIOD+"',MEMO='"+MEMO+"',BAR_CODE='"+BAR_CODE+"',SINGLE_BOX='"+SINGLE_BOX+"' WHERE SPEC_ID='"+SPEC_ID+"'";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean deleteProductSpec(String SPEC_ID) {
        List<String> sql = new ArrayList<String>();
        String sql2 = "DELETE FROM " + productSpecTable + "  WHERE SPEC_ID='" + SPEC_ID + "' ";
        sql.add(sql2);
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n > 0;
    }


    public Record getSinglePro(String PRO_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + productTable + " WHERE PRO_ID='"+PRO_ID+"' ";
        Record rec = se.executeRecord(sql, null);
        if (!rec.isEmpty()) {
            rec.put("PRO_SPEC",getAllProSpec(PRO_ID)) ;
//            Record dw = getDWBYSX(rec.getString("PRO_DW"));
//            rec.put("PRO_DW_NAME",dw.getString("DW"));
        }


        return rec;
    }
    public Record getSingleProBaseByProCode(String PRO_CODE) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + productSpecTable + " WHERE PRO_CODE='"+PRO_CODE+"' ";
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    public Record getSingleProBase(String PRO_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + productTable + " WHERE PRO_ID='"+PRO_ID+"' ";
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
    public RecordSet getAllProSpec(String PRO_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + productSpecTable + " WHERE PRO_ID='"+PRO_ID+"' AND DELETE_TIME IS NULL ORDER BY SORT,PRO_NAME";
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    public RecordSet getAllGysPro(String GYS_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT g.*,p.* FROM " + productTable + " p INNER JOIN "+ gysNewTable +" g ON g.GYS_ID=p.GYS_ID WHERE p.GYS_ID='"+GYS_ID+"' AND p.DELETE_TIME IS NULL ORDER BY p.SORT,p.PRO_NAME";
        RecordSet recs = se.executeRecordSet(sql, null);
//        RecordSet allDw = getAllDW();
        for (Record rec : recs){
            rec.put("PRO_SPEC",getAllProSpec(rec.getString("PRO_ID"))) ;
//            Record dw = allDw.findEq("DW_SX",rec.getString("PRO_DW"));
//            rec.put("PRO_DW_NAME",dw.getString("DW"));
        }
        return recs;
    }

    public RecordSet getAllGysProSpec(String GYS_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql ="";
        if (GYS_ID.length()>0 && !GYS_ID.equals("999") && !GYS_ID.equals("9") && !GYS_ID.equals("0"))
            sql = "SELECT p.*,pro.GYS_ID,pro.PRO_DW,pro.PRO_DW_NAME,pro.TRANSPORT_TYPE,pro.PRO_CODE AS BIG_PRO_CODE,pro.PRO_TYPE_ID,pro.PRO_TYPE FROM " + productSpecTable + " p INNER JOIN "+productTable+" pro ON pro.PRO_ID=p.PRO_ID WHERE pro.GYS_ID='"+GYS_ID+"' AND p.DELETE_TIME IS NULL AND pro.DELETE_TIME IS NULL ORDER BY SORT,PRO_NAME";
        else
            sql = "SELECT p.*,pro.GYS_ID,pro.PRO_DW,pro.PRO_DW_NAME,pro.TRANSPORT_TYPE,pro.PRO_CODE AS BIG_PRO_CODE,pro.PRO_TYPE_ID,pro.PRO_TYPE FROM " + productSpecTable + " p INNER JOIN "+productTable+" pro ON pro.PRO_ID=p.PRO_ID WHERE p.DELETE_TIME IS NULL AND pro.DELETE_TIME IS NULL ORDER BY SORT,PRO_NAME";
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }
    //第一次新增的时候用的
    public RecordSet getAllGysProSpecCanFullBox(String GYS_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT p.*,pro.PRO_DW,pro.PRO_DW_NAME,pro.TRANSPORT_TYPE,pro.PRO_CODE AS BIG_PRO_CODE,pro.PRO_TYPE_ID,pro.PRO_TYPE FROM " + productSpecTable + " p INNER JOIN "+productTable+" pro ON pro.PRO_ID=p.PRO_ID WHERE p.DELETE_TIME IS NULL AND pro.DELETE_TIME IS NULL AND p.SINGLE_BOX=0 AND pro.GYS_ID='"+GYS_ID+"' ORDER BY SORT,PRO_NAME";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record r : recs){
            //看看这个 SPEC_ID,有没有被 供应商 选择过  .不管哪个规则
            Record HAS = existsSpecFullBoxThisGys(GYS_ID, r.getString("SPEC_ID"));
            if (HAS.isEmpty()){
                r.put("HAS",0);
            }else{
                r.put("HAS",1);
            }
        }
        return recs;
    }
    //第二次修改的时候用的
    public RecordSet getAllGysProSpecCanFullBoxUpdate(String GYS_ID,String BOX_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT p.*,pro.PRO_DW,pro.PRO_DW_NAME,pro.TRANSPORT_TYPE,pro.PRO_CODE AS BIG_PRO_CODE,pro.PRO_TYPE_ID,pro.PRO_TYPE FROM " + productSpecTable + " p INNER JOIN "+productTable+" pro ON pro.PRO_ID=p.PRO_ID WHERE p.DELETE_TIME IS NULL AND pro.DELETE_TIME IS NULL AND p.SINGLE_BOX=0 AND pro.GYS_ID='"+GYS_ID+"' ORDER BY SORT,PRO_NAME";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record r : recs){
            //看看这个 SPEC_ID,有没有被其他的规则选择过 ,必须是这个供应商的
            Record OTHER_HAS = existsOtherGzSelThisSpec(GYS_ID, BOX_ID, r.getString("SPEC_ID"));
            if (OTHER_HAS.isEmpty()){
                r.put("OTHER_HAS",0);
                //没有了,这个规则下,自己选中了没有
                Record MY_HAS = existsSpecFullBox(BOX_ID, r.getString("SPEC_ID"));
                if (MY_HAS.isEmpty()){
                    r.put("MY_HAS",0);
                }else{
                    r.put("MY_HAS",1);
                }
            }else{
                r.put("OTHER_HAS",1);
                r.put("MY_HAS",0);
            }




        }
        return recs;
    }
    public Record getSingleProSpec(String SPEC_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + productSpecTable + " WHERE SPEC_ID='"+SPEC_ID+"' ";
        Record rec = se.executeRecord(sql, null);
        return rec;
    }

    public synchronized int genNaturalSequence(String table){
        SQLExecutor se = getSqlExecutor();
        Record old = se.executeRecord("SELECT CURRENT FROM " + sequenceTable + " WHERE SEQ_ID='" + table + "'");
        if(old.isEmpty()){
            se.executeUpdate("INSERT INTO " + sequenceTable + " (CURRENT,SEQ_ID) VALUES (0,'" + table + "')  ");
        }
        long current = old.getInt("CURRENT");
        long next = current + 1;
        se.executeUpdate("UPDATE "+sequenceTable+" SET CURRENT="+next+" WHERE SEQ_ID='"+table+"' ");
        return (int) next;
    }

    public RecordSet getAllPartnerKw1(String SJ_ID) {
        String sql = "SELECT k.*,partner.* FROM " + kwTable + " k INNER JOIN "+partnerKwTable+" p ON p.KW_ID=k.KW_ID INNER JOIN "+sjPartnerTable+" partner ON partner.PARTNER_NO=p.PARTNER_NO WHERE partner.SJ_ID='" + SJ_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    public RecordSet getAllPartnerKw(String SJ_ID) {
        String sql = "SELECT * FROM  "+sjPartnerTable+" partner WHERE partner.SJ_ID='" + SJ_ID + "' ";
        SQLExecutor se = getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        RecordSet allKw = getAllKW();
        for (Record r : recs){
            String PARTNER_NO = r.getString("PARTNER_NO");
            Record record = se.executeRecord("SELECT * FROM "+kwTable+" WHERE KW_ID IN (SELECT KW_ID FROM "+partnerKwTable+" WHERE PARTNER_NO='"+PARTNER_NO+"')");
            if (!record.isEmpty()){
                String FID = record.getString("FID");
                Record parent = allKw.findEq("KW_ID",FID);
                r.put("KW_ID",record.getString("KW_ID"));
                r.put("KW_NAME",record.getString("KW_NAME"));
                r.put("PARENT_KW_ID",FID);
                r.put("PARENT_KW_NAME",parent.getString("KW_NAME"));
            } else {
                r.put("KW_ID","");
                r.put("KW_NAME","");
                r.put("PARENT_KW_ID","");
                r.put("PARENT_KW_NAME","");
            }
        }
        return recs;
    }

    public Record getPartnerKw(String PARTNER_NO) {
        String sql = "SELECT * FROM " + kwTable + "  WHERE KW_ID IN ( SELECT KW_ID FROM "+partnerKwTable+" WHERE PARTNER_NO='"+PARTNER_NO+"')";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        if (!rec.isEmpty())  {
            String FID = rec.getString("FID");
            Record parent = se.executeRecord("SELECT * FROM "+kwTable+" WHERE KW_ID='"+FID+"' ");
            if (!parent.isEmpty()) {
                rec.put("PARENT_KW_ID",FID);
                rec.put("PARENT_KW_NAME",parent.getString("KW_NAME"));
            }else{
                rec.put("PARENT_KW_ID","");
                rec.put("PARENT_KW_NAME","");
            }
        }
        return rec;
    }


    public boolean saveSpecFullBox( String BOX_ID,String GYS_ID, String SPEC_ID,String PRO_NAME,String PRO_SPEC) {
        String sql = "INSERT INTO " + specFullBoxTable + " (BOX_ID,GYS_ID, SPEC_ID,PRO_NAME,PRO_SPEC,CREATE_TIME)" +
                " VALUES ('"+BOX_ID+"','"+GYS_ID+"','" + SPEC_ID + "','"+PRO_NAME+"','"+PRO_SPEC+"','"+DateUtils.now()+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean deleteSpecFullBox( String BOX_ID, String SPEC_ID) {
        String sql = "DELETE FROM " + specFullBoxTable + " WHERE BOX_ID='"+BOX_ID+"' AND SPEC_ID='"+SPEC_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean deleteSpecFullBoxAll(String BOX_ID) {
        String sql = "DELETE FROM " + specFullBoxTable + " WHERE BOX_ID='"+BOX_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }


    //是否存在这个箱规则,这个货品的规则
    public Record existsSpecFullBox(String BOX_ID, String SPEC_ID) {
        String sql = "SELECT * FROM " + specFullBoxTable + "  WHERE BOX_ID='"+BOX_ID+"' AND SPEC_ID='"+SPEC_ID+"'";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecordSet(sql, null).getFirstRecord();
        return rec;
    }

    //是否存在被当成合箱的选了
    public RecordSet existsSpecFullBoxThisGys(String SPEC_ID) {
        String sql = "SELECT * FROM " + specFullBoxTable + "  WHERE SPEC_ID='"+SPEC_ID+"'";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    //这个供应商,有没有选过这个
    public Record existsSpecFullBoxThisGys(String GYS_ID, String SPEC_ID) {
        String sql = "SELECT * FROM " + specFullBoxTable + "  WHERE GYS_ID='"+GYS_ID+"' AND SPEC_ID='"+SPEC_ID+"'";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecordSet(sql, null).getFirstRecord();
        return rec;
    }


    //这个供应商,有没有其他规则   选过这个货品
    public Record existsOtherGzSelThisSpec(String GYS_ID,String BOX_ID, String SPEC_ID) {
        String sql = "SELECT * FROM " + specFullBoxTable + "  WHERE SPEC_ID='"+SPEC_ID+"' AND GYS_ID='"+GYS_ID+"' AND BOX_ID!='"+BOX_ID+"' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecordSet(sql, null).getFirstRecord();
        return rec;
    }

    public RecordSet getSingleSpecFullBox(String BOX_ID) {
        String sql = "SELECT * FROM " + specFullBoxTable + "  WHERE BOX_ID='"+BOX_ID+"' ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        return recs;
    }

    public RecordSet getAllSpecFullBox(String GYS_ID) {
        String sql = "SELECT BOX_ID,CREATE_TIME FROM " + specFullBoxTable + "  WHERE GYS_ID='"+GYS_ID+"' GROUP BY BOX_ID ";
        SQLExecutor se = read_getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record r : recs){
            String  BOX_ID = r.getString("BOX_ID");
            RecordSet full_box = getSingleSpecFullBox( BOX_ID) ;
            r.put("ALL_FULL_BOX",full_box);
        }
        return recs;
    }

    public boolean saveSpecFullBox2( String BOX_ID,String GYS_ID, String SPEC_ID,String PRO_NAME,String PRO_SPEC) {
        Record exists = existsSpecFullBox(BOX_ID,SPEC_ID);
        if (exists.isEmpty()){
            String sql = "INSERT INTO " + specFullBoxTable + " (BOX_ID,GYS_ID, SPEC_ID,PRO_NAME,PRO_SPEC,CREATE_TIME)" +
                    " VALUES ('"+BOX_ID+"','"+GYS_ID+"','" + SPEC_ID + "','"+PRO_NAME+"','"+PRO_SPEC+"','"+DateUtils.now()+"') ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n>0;
        }else{
            return deleteSpecFullBox(BOX_ID,SPEC_ID);
        }
    }
}

