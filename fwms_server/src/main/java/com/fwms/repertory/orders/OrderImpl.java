package com.fwms.repertory.orders;

import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.log.Logger;
import com.fwms.basedevss.base.sql.ConnectionFactory;
import com.fwms.basedevss.base.sql.SQLExecutor;
import com.fwms.basedevss.base.util.*;
import com.fwms.common.Constants;
import com.fwms.common.GlobalLogics;
import com.fwms.service.user.UserLogic;

import java.util.ArrayList;
import java.util.List;


public class OrderImpl implements OrderLogic, Initializable {
    private static final Logger L = Logger.getLogger(OrderImpl.class);

    private ConnectionFactory connectionFactory;
    private String db;

    private String gysOrderTable = "t_sys_order";
    private String orderProductTable = "t_sys_order_product";
    private String productTable = "t_sys_product";
    private String productSpecTable = "t_sys_product_spec";
    private String productLineTable = "t_sys_product_line";
    private String gysWlTable = "t_sys_user_gys_wl";
    private String orderInboundTable = "t_sys_inbound";
    private String orderOutboundTable = "t_sys_outbound";
    private String orderInboundItemTable = "t_sys_inbound_item";

    private String packageTable = "t_sys_order_package";
    private String packageProductTable = "t_sys_order_package_product";
    private String gysNewTable = "t_sys_gys";
    private String kwTable = "t_sys_kw";

    public OrderImpl() {
    }

    @Override
    public void init() {
        Configuration conf = GlobalConfig.get();
        this.connectionFactory = ConnectionFactory.getConnectionFactory("dbcp");
        this.db = conf.getString("service.db", null);
    }

    @Override
    public void destroy() {
        this.gysOrderTable = null;
        this.connectionFactory = ConnectionFactory.close(connectionFactory);
        this.db = null;
    }

    private SQLExecutor getSqlExecutor() {
        return new SQLExecutor(connectionFactory, db);
    }
    private SQLExecutor read_getSqlExecutor() {
        return new SQLExecutor(connectionFactory, db);
    }

    public boolean deleteOrder(Context ctx, String ORDER_ID) {
        String sql = "UPDATE  " + gysOrderTable + " SET DELETE_TIME='"+DateUtils.now()+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateOrderStatusInbound(Context ctx, String ORDER_ID, int STATUS, String INBOUND_TIME) {
        String sql = "UPDATE  " + gysOrderTable + " SET STATUS='"+STATUS+"',INBOUND_TIME='"+INBOUND_TIME+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateOrderStatusOutbound(Context ctx, String ORDER_ID,int STATUS,String OUTBOUND_TIME) {
        String sql = "UPDATE  " + gysOrderTable + " SET STATUS='"+STATUS+"',OUTBOUND_TIME='"+OUTBOUND_TIME+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateOrderState(String ORDER_ID,int STATE) {
        String sql ="UPDATE " + gysOrderTable + " SET STATE='"+STATE+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateOrderVerify(String ORDER_ID,String USER_ID) {
        String sql ="UPDATE " + gysOrderTable + " SET STATUS='"+OrderConstants.ORDER_STATUS_CONFIRMED+"',VERIFY_STATUS='1',VERIFY_USER_ID='"+USER_ID+"',VERIFY_TIME='"+DateUtils.now()+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean deletePackageCode(String PACKAGE_CODE) {
        String sql1 = "DELETE FROM " + packageProductTable + "  WHERE PACKAGE_CODE='" + PACKAGE_CODE + "' ";
        String sql2 = "DELETE FROM " + packageTable + "  WHERE PACKAGE_CODE='" + PACKAGE_CODE + "' ";
        List<String> ls = new ArrayList<String>();
        ls.add(sql1);
        ls.add(sql2);

        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(ls);
        return n > 0;
    }
    public boolean printOrderInbound(String ORDER_ID) {
        String sql1 = "UPDATE " + gysOrderTable + " SET PRINT_INBOUND=PRINT_INBOUND+1  WHERE ORDER_ID='" + ORDER_ID + "' ";
        List<String> ls = new ArrayList<String>();
        ls.add(sql1);
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(ls);
        return n > 0;
    }

        //获取所有,分页
    public Record getAllGysOrderPageList(Context ctx, String PRO_TYPE_ID, String SJ_ID,String GYS_ID, String START_TIME, String END_TIME, String STATE, int PAY_DONE, int page, int count, String ORDER_ID,String OUT_ORDER_ID, String INBOUND_STATUS_BEGIN, String INBOUND_STATUS_END) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = "";
        if (GYS_ID.length()>0 && !GYS_ID.equals("0") && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            filter += " AND GYS_ID='"+GYS_ID+"' ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("0") && !SJ_ID.equals("9") && !SJ_ID.equals("999"))
            filter += " AND SJ_ID='"+SJ_ID+"' ";
        if (START_TIME.length()>0)
            filter += " AND CREATE_TIME >= '"+START_TIME+"' ";
        if (END_TIME.length()>0)
            filter += " AND CREATE_TIME <= '"+END_TIME+"' ";
        if (INBOUND_STATUS_BEGIN.length()>0)
            filter += " AND STATUS >= " + INBOUND_STATUS_BEGIN;
        if (INBOUND_STATUS_END.length()>0)
            filter += " AND STATUS <= " + INBOUND_STATUS_END;
        if (PAY_DONE != 9)
            filter += " AND PAY_DONE='"+PAY_DONE+"' ";
        if (ORDER_ID.length()>0)
            filter += " AND ORDER_ID='" + ORDER_ID +"' ";
        if (OUT_ORDER_ID.length()>0)
            filter += " AND OUT_ORDER_ID='" + OUT_ORDER_ID +"' ";
        if (PRO_TYPE_ID.length()>0 && !PRO_TYPE_ID.equals("999") && !PRO_TYPE_ID.equals("0") && !PRO_TYPE_ID.equals("9"))
            filter += " AND ORDER_ID IN (SELECT ORDER_ID FROM "+orderProductTable+" WHERE PRO_ID IN (SELECT PRO_ID FROM "+productTable+" WHERE PRO_TYPE_ID='"+PRO_TYPE_ID+"')) ";


        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM " + gysOrderTable + "  WHERE DELETE_TIME IS NULL ";
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
        String sql = "SELECT * FROM " + gysOrderTable + " WHERE DELETE_TIME IS NULL ";
        sql+=filter;

        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY CREATE_TIME DESC LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            rec = formatGYSOrder(rec);
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
    public Record formatOrderInbound(Record rec) {
        if (rec.isEmpty())
            return rec;
        String ORDER_ID = rec.getString("ORDER_ID");
        Record order = getSingleOrderBase(ORDER_ID);
        order.copyTo(rec);

        Record GYS = GlobalLogics.getUser().getSingleGysBase(rec.getString("GYS_ID"));
        GYS.copyTo(rec);

        String KW_ID = rec.getString("KW_ID");
        Record rec_kw= GlobalLogics.getBaseLogic().getSingleKwBase(KW_ID) ;
        rec.put("KW_NAME",rec_kw.getString("KW_NAME"));
        Record rec_kw_parent= GlobalLogics.getBaseLogic().getSingleKwBase(rec_kw.getString("FID")) ;
        rec.put("PARENT_KW_NAME",rec_kw_parent.getString("KW_NAME"));
        return rec;
    }
    public Record formatGYSOrder(Record rec) {
        if (rec.isEmpty())
            return rec;
        Record GYS = GlobalLogics.getUser().getSingleGysBase(rec.getString("GYS_ID"));
        GYS.copyTo(rec);
        String PARTNER_NO = rec.getString("PARTNER_NO");
        Record partner = GlobalLogics.getUser().getSinglePartnerByNoBaseOrder(PARTNER_NO);
        partner.copyTo(rec);
        rec.put("ORDER_PRODUCTS", getOrderProducts(rec.getString("ORDER_ID")));
        rec.put("ORDER_INBOUNDS", getOrderInbound(rec.getString("ORDER_ID")));
        if (rec.getString("VERIFY_USER_ID").length() > 0) {
            Record u = GlobalLogics.getUser().getSingleUserSimple(rec.getString("VERIFY_USER_ID"));
            rec.put("VERIFY_USER_NAME", u.getString("DISPLAY_NAME"));
        } else {
            rec.put("VERIFY_USER_NAME", "");
        }
        if (rec.getString("CREATE_USER_ID").length() > 0) {
            Record u = GlobalLogics.getUser().getSingleUserSimple(rec.getString("CREATE_USER_ID"));
            rec.put("CREATE_USER_NAME", u.getString("DISPLAY_NAME"));
        } else {
            rec.put("CREATE_USER_NAME", "");
        }
        RecordSet all_packages = getOrderPackages(rec.getString("ORDER_ID"));
        rec.put("ORDER_PACKAGES", all_packages);
        return rec;
    }

    public Record getSingleOrder(String ORDER_ID) {
        String sql ="SELECT * FROM " + gysOrderTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        if (!rec.isEmpty()) {
            rec = formatGYSOrder(rec);
        }
        return rec;
    }
    public Record getSingleOrderBase(String ORDER_ID) {
        String sql ="SELECT * FROM " + gysOrderTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        return rec;
    }
    public Record getSinglePackage(String PACKAGE_CODE) {
        String sql ="SELECT * FROM " + packageTable + " WHERE PACKAGE_CODE='"+PACKAGE_CODE+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        return rec;
    }
    public Record getSingleOrderForPackage(String ORDER_ID) {
        String sql ="SELECT * FROM " + gysOrderTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        if (!rec.isEmpty()) {
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record partner = GlobalLogics.getUser().getSinglePartnerByNoBase(PARTNER_NO);
            rec.put("PARTNER_INFO",partner);
            String SJ_ID = partner.getString("SJ_ID");
            Record sj = GlobalLogics.getUser().getSingleSjBase(SJ_ID);
            rec.put("SJ_INFO",sj);
            if (rec.getString("VERIFY_USER_ID").length() > 0) {
                Record u = GlobalLogics.getUser().getSingleUserSimple(rec.getString("VERIFY_USER_ID"));
                rec.put("VERIFY_USER_NAME", u.getString("DISPLAY_NAME"));
            } else {
                rec.put("VERIFY_USER_NAME", "");
            }
            if (rec.getString("CREATE_USER_ID").length() > 0) {
                Record u = GlobalLogics.getUser().getSingleUserSimple(rec.getString("CREATE_USER_ID"));
                rec.put("CREATE_USER_NAME", u.getString("DISPLAY_NAME"));
            } else {
                rec.put("CREATE_USER_NAME", "");
            }

            RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
            RecordSet recs_inbound = getOrderInbound(rec.getString("ORDER_ID"));
            for (Record r : recs_inbound){
                Record thisKw = allKw.findEq("KW_ID",r.getString("KW_ID"));
                r.put("KW_NAME",thisKw.getString("KW_NAME"));
                Record fatherKw = allKw.findEq("KW_ID",thisKw.getString("FID"));
                r.put("PARENT_KW_NAME", fatherKw.getString("KW_NAME"));
            }
            rec.put("ORDER_INBOUNDS",recs_inbound);
            RecordSet recs_outbound = getOrderOutbound(rec.getString("ORDER_ID"));
            for (Record r : recs_outbound){
                Record thisKw = allKw.findEq("KW_ID",r.getString("KW_ID"));
                r.put("KW_NAME",thisKw.getString("KW_NAME"));
                Record fatherKw = allKw.findEq("KW_ID",thisKw.getString("FID"));
                r.put("PARENT_KW_NAME", fatherKw.getString("KW_NAME"));
            }
            rec.put("ORDER_OUTBOUNDS",recs_outbound);

            String sql00 ="SELECT pro.PRO_DW,pro.PRO_TYPE_ID,pro.PRO_TYPE,pd.* FROM " + orderProductTable + " pd INNER JOIN "+productTable+" pro ON pro.PRO_ID=pd.PRO_ID WHERE pd.ORDER_ID='"+ORDER_ID+"' and pd.DELETE_TIME IS NULL ORDER BY CREATE_TIME DESC";
            RecordSet recs_products = se.executeRecordSet(sql00, null);
            RecordSet allDw = GlobalLogics.getBaseLogic().getAllDW();
            int all_count = 0;int all_has = 0;
            for (Record product : recs_products){
                Record dw = allDw.findEq("DW_SX",product.getString("PRO_DW"));
                product.put("PRO_DW_NAME",dw.getString("DW"));
                Record PRODUCT_SPEC = GlobalLogics.getBaseLogic().getSingleProSpec(product.getString("PRO_SPEC_ID"));
                PRODUCT_SPEC.copyTo(product);
                //再查,已经装箱了多少了
                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+packageProductTable+" WHERE ORDER_ID='"+ORDER_ID+"' AND SPEC_ID='"+product.getString("PRO_SPEC_ID")+"' ";
                Record h =  se.executeRecord(sql2, null);
                int hasCount = h.isEmpty()?0:(int)h.getInt("PRO_COUNT");
                product.put("HAS_PACKAGE_COUNT",hasCount);
                all_count += product.getInt("PRO_COUNT");
                all_has += hasCount;
            }
            rec.put("ALL_COUNT", all_count);
            rec.put("ALL_HAS", all_has);
            rec.put("ALL_LESS", all_count-all_has);
            rec.put("ORDER_PRODUCTS", recs_products);
            RecordSet all_packages = getOrderPackages(rec.getString("ORDER_ID"));
            rec.put("ORDER_PACKAGES", all_packages);
        }
        return rec;
    }

    public RecordSet getOrderProducts(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT pro.PRO_DW,pro.PRO_TYPE_ID,pro.PRO_TYPE,pd.* FROM " + orderProductTable + " pd INNER JOIN "+productTable+" pro ON pro.PRO_ID=pd.PRO_ID WHERE pd.ORDER_ID='"+ORDER_ID+"' and pd.DELETE_TIME IS NULL ORDER BY CREATE_TIME DESC";
        RecordSet recs_products = se.executeRecordSet(sql00, null);
        RecordSet allDw = GlobalLogics.getBaseLogic().getAllDW();
        for (Record product : recs_products){
            Record dw = allDw.findEq("DW_SX",product.getString("PRO_DW"));
            product.put("PRO_DW_NAME",dw.getString("DW"));
            Record PRODUCT_SPEC = GlobalLogics.getBaseLogic().getSingleProSpec(product.getString("PRO_SPEC_ID"));
            PRODUCT_SPEC.copyTo(product);
        }
        return recs_products;
    }

    public RecordSet getOrderInbound(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT * FROM " + orderInboundTable + " WHERE ORDER_ID='"+ORDER_ID+"' ORDER BY CREATE_TIME DESC";
        RecordSet recs_inbound = se.executeRecordSet(sql00, null);
        return recs_inbound;
    }
    public RecordSet getOrderOutbound(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT * FROM " + orderOutboundTable + " WHERE ORDER_ID='"+ORDER_ID+"' ORDER BY CREATE_TIME DESC";
        RecordSet recs_inbound = se.executeRecordSet(sql00, null);
        return recs_inbound;
    }
    public boolean saveInbound(Context ctx,String INBOUND_ID,String ORDER_ID,String KW_ID, String GYS_ID, String GYS_NAME,String INBOUND_TIME) {
        String sql = "INSERT INTO " + orderInboundTable + " (INBOUND_ID,CREATE_USER_ID,ORDER_ID,KW_ID, GYS_ID,GYS_NAME, CREATE_TIME,INBOUND_TIME) VALUES" +
                " ('"+INBOUND_ID+"','"+ctx.getUser_id()+"','"+ORDER_ID+"','" + KW_ID + "','" + GYS_ID +"','" + GYS_NAME + "','"+ DateUtils.now()+"','"+INBOUND_TIME+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean saveOutbound(Context ctx,String OUTBOUND_ID,String ORDER_ID,String KW_ID, String GYS_ID, String GYS_NAME,String OUTBOUND_TIME) {
        String sql = "INSERT INTO " + orderOutboundTable + " (OUTBOUND_ID,CREATE_USER_ID,ORDER_ID,KW_ID, GYS_ID,GYS_NAME, CREATE_TIME,OUTBOUND_TIME) VALUES" +
                " ('"+OUTBOUND_ID+"','"+ctx.getUser_id()+"','"+ORDER_ID+"','" + KW_ID + "','" + GYS_ID +"','" + GYS_NAME + "','"+ DateUtils.now()+"','"+OUTBOUND_TIME+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean saveGysOrder(Context ctx,String USER_ID,String SJ_ID, String ORDER_ID,String OUT_ORDER_ID, String GYS_ID, String GYS_NAME, String SEND_PRICE, String OTHER_PRICE, String PAY_TYPE, String MEMO, String JH_TIME, String JH_TYPE, String JH_ADDR, String IFKP, String KP_TYPE, String TAX, String FK_YD, int isBack,int status,String PARTNER_NO,String KW_ID,String PROVINCE,String CITY,String AREA,String ADDR,String FULL_ADDR,String CONTACT,String MOBILE) {
        String sql = "INSERT INTO " + gysOrderTable + " (USER_ID,SJ_ID,ORDER_ID, OUT_ORDER_ID,GYS_ID, GYS_NAME,   SEND_PRICE, OTHER_PRICE, PAY_TYPE, MEMO,CREATE_TIME,JH_TIME, JH_TYPE, JH_ADDR, IFKP, KP_TYPE, TAX, FK_YD,CREATE_USER_ID,IS_BACK,STATUS, VERIFY_STATUS,PARTNER_NO, KW_ID,PROVINCE, CITY, AREA, ADDR, FULL_ADDR, CONTACT, MOBILE) VALUES" +
                " ('"+USER_ID+"','"+SJ_ID+"','" + ORDER_ID + "','"+OUT_ORDER_ID+"','" + GYS_ID +"','" + GYS_NAME + "','"+SEND_PRICE+"','"+OTHER_PRICE+"','"+PAY_TYPE+"','"+MEMO+"','"+ DateUtils.now()+"','"+JH_TIME+"','"+JH_TYPE+"','"+JH_ADDR+"','"+IFKP+"','"+KP_TYPE+"','"+TAX+"','"+FK_YD+"','"+ctx.getUser_id()+"','"+isBack+"','"+status+"','0','"+PARTNER_NO+"','"+KW_ID+"','"+PROVINCE+"','"+CITY+"','"+AREA+"','"+ADDR+"','"+FULL_ADDR+"','"+CONTACT+"','"+MOBILE+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean updateGysOrder(Context ctx,String ORDER_ID,String OUT_ORDER_ID, String SEND_PRICE, String OTHER_PRICE, String PAY_TYPE, String MEMO, String JH_TIME, String JH_TYPE, String JH_ADDR, String IFKP, String KP_TYPE, String TAX, String FK_YD,String PARTNER_NO,String PROVINCE,String CITY,String AREA,String ADDR,String FULL_ADDR,String CONTACT,String MOBILE) {
        String sql = "UPDATE " + gysOrderTable + " SET OUT_ORDER_ID='"+OUT_ORDER_ID+"',SEND_PRICE='"+SEND_PRICE+"',OTHER_PRICE='"+OTHER_PRICE+"',PAY_TYPE='"+PAY_TYPE+"',MEMO='"+MEMO+"',JH_TIME='"+JH_TIME+"',JH_TYPE='"+JH_TYPE+"',JH_ADDR='"+JH_ADDR+"',IFKP='"+IFKP+"',KP_TYPE='"+KP_TYPE+"',TAX='"+TAX+"',FK_YD='"+FK_YD+"',PARTNER_NO='"+PARTNER_NO+"',PROVINCE='"+PROVINCE+"',CITY='"+CITY+"',AREA='"+AREA+"',ADDR='"+ADDR+"',FULL_ADDR='"+FULL_ADDR+"',CONTACT='"+CONTACT+"',MOBILE='"+MOBILE+"' WHERE ORDER_ID='"+ORDER_ID+"'";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean deleteGysOrderProducts(String ORDER_ID) {
        String sql = "UPDATE " + orderProductTable + " SET DELETE_TIME='"+DateUtils.now()+"' WHERE ORDER_ID='"+ORDER_ID+"'";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean saveGysOrderPro(Context ctx, String ORDER_ID, String PRO_ID, String PRO_SPEC_ID,String PRO_COUNT, String PRO_PRICE, String TAX_PRICE, String PRO_CODE_NUMBER, String IFSF, String PRO_TYPE, String PRO_TYPE_ID, String PRO_NAME, String totalPrice, String ORDER_ITEM_ID, String WS_PRO_PRICE, String TAX_RATE, String WSTotalPrice) {
        if (Double.valueOf(PRO_COUNT)==0){
            String sql = "DELETE FROM " + orderProductTable + " WHERE ORDER_ID='"+ORDER_ID+"' AND PRO_SPEC_ID='"+PRO_SPEC_ID+"' ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n>0;
        }
        String sql = "INSERT INTO " + orderProductTable + " (ORDER_ID,ORDER_PRODUCT_ID, PRO_ID,PRO_SPEC_ID,PRO_COUNT, PRO_PRICE, TAX_PRICE, PRO_CODE_NUMBER,CREATE_TIME,IFSF,PRO_NAME, PRO_TYPE_ID, PRO_TYPE,TOTAL_PRICE, ORDER_ITEM_ID, TAX_RATE, VAT_EXCLUDED_UNIT_PRICE, VAT_EXCLUDED_PRICE) VALUES" +
                " ('" + ORDER_ID + "','"+String.valueOf(RandomUtils.generateId())+"','" + PRO_ID + "','"+PRO_SPEC_ID+"','"+PRO_COUNT+"','"+PRO_PRICE+"','"+TAX_PRICE+"','"+PRO_CODE_NUMBER+"','"+ DateUtils.now()+"','"+IFSF+"','"+PRO_NAME+"','"+PRO_TYPE_ID+"','"+PRO_TYPE+"','"+totalPrice+"','"+ORDER_ITEM_ID+"','"+TAX_RATE+"','"+WS_PRO_PRICE+"','"+WSTotalPrice+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }


    public boolean saveOrderPackage(String ORDER_ID,String PACKAGE_CODE) {
        String sql = "INSERT INTO " + packageTable + " (ORDER_ID,PACKAGE_CODE) VALUES" +
                " ('" + ORDER_ID + "','"+PACKAGE_CODE+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean saveOrderPackageProduct(String ORDER_ID,String PACKAGE_PRODUCT_ID, String PACKAGE_CODE,String SPEC_ID,String PRO_NAME,int PRO_COUNT) {
        String sql = "INSERT INTO " + packageProductTable + " (ORDER_ID,PACKAGE_PRODUCT_ID,PACKAGE_CODE,SPEC_ID,PRO_NAME,PRO_COUNT) VALUES" +
                " ('" + ORDER_ID + "','"+PACKAGE_PRODUCT_ID+"','"+PACKAGE_CODE+"','"+SPEC_ID+"','"+PRO_NAME+"','"+PRO_COUNT+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        if (n>0){
            RecordSet allPros = se.executeRecordSet("SELECT p.*,spec.PRO_SPEC,spec.PRO_COLOR FROM "+packageProductTable+" p INNER JOIN "+productSpecTable+" spec ON spec.SPEC_ID=p.SPEC_ID WHERE p.PACKAGE_CODE='"+PACKAGE_CODE+"'");
            String s = "";
            for (Record r : allPros){
                s+=r.getString("PRO_NAME")+"["+r.getString("PRO_SPEC")+"]"+"("+r.getInt("PRO_COUNT")+")";
            }
            if (s.length()>0)
                se.executeUpdate("UPDATE "+packageTable+" SET PRO_DETAIL='"+s+"' WHERE PACKAGE_CODE='"+PACKAGE_CODE+"' ");
        }
        return n>0;
    }

    public RecordSet getOrderPackages(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT * FROM " + packageTable + " WHERE ORDER_ID='"+ORDER_ID+"' ORDER BY PACKAGE_CODE ";
        RecordSet recs = se.executeRecordSet(sql00, null);
        for (Record r : recs){
            RecordSet pd =  se.executeRecordSet("SELECT p.*,spec.PRO_SPEC,spec.PRO_COLOR FROM " + packageProductTable + " p INNER JOIN "+productSpecTable+" spec ON spec.SPEC_ID=p.SPEC_ID WHERE PACKAGE_CODE='"+r.getString("PACKAGE_CODE")+"'") ;
            r.put("PACKAGE_PRODUCT",pd);
        }
        return recs;
    }

    public RecordSet getOrderPackagesPrint(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="select PRO_DETAIL,COUNT(*) AS COUNT  from t_sys_order_package where order_id='"+ORDER_ID+"' group by PRO_DETAIL ";
        RecordSet recs = se.executeRecordSet(sql00, null);
        return recs;
    }

    public synchronized String getNowPackageCode(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 = "SELECT PACKAGE_CODE FROM " + packageTable + " WHERE ORDER_ID='" + ORDER_ID + "' ORDER BY PACKAGE_CODE DESC LIMIT 1 ";
        Record rec = se.executeRecord(sql00, null);
        if (rec.isEmpty())
            return ORDER_ID + "_" + "0001";
        String PACKAGE_CODE = rec.getString("PACKAGE_CODE");
        List<String> ls = StringUtils2.splitList(PACKAGE_CODE, "_", true);
        int c = Integer.parseInt(ls.get(3));
        c += 1;
        return ls.get(0)+"_"+ls.get(1)+"_"+ls.get(2)+"_"+ Constants.len1To4(c);
    }

    public RecordSet getGysOrderDailyReport(String SJ_ID,String GYS_ID,String START_TIME,String END_TIME) {
        SQLExecutor se = getSqlExecutor();
        String orders_sql = "SELECT ORDER_ID,PARTNER_NO FROM "+gysOrderTable+" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+" AND JH_TIME>='"+START_TIME+"' AND JH_TIME<='"+END_TIME+"'";
        if (GYS_ID.length()>0 && !GYS_ID.equals("999") && !GYS_ID.equals("9") && !GYS_ID.equals("0"))
            orders_sql += " AND GYS_ID='"+GYS_ID+"' ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            orders_sql += " AND SJ_ID='"+SJ_ID+"' ";
        RecordSet allOrders = se.executeRecordSet(orders_sql);
        String ORDER_IDS = allOrders.joinColumnValues("ORDER_ID", ",");
        if (ORDER_IDS.length() > 0)
            ORDER_IDS = Constants.formatString(ORDER_IDS);
        else
            return new RecordSet();

        String sql00 ="SELECT DISTINCT(PRO_SPEC_ID) AS PRO_SPEC_ID FROM " + orderProductTable + " WHERE ORDER_ID IN ("+ORDER_IDS+") AND DELETE_TIME IS NULL";
        RecordSet recs_spec = se.executeRecordSet(sql00, null);
        RecordSet allSpecPros = GlobalLogics.getBaseLogic().getAllGysProSpec(GYS_ID);
        RecordSet allPartners = GlobalLogics.getUser().getAllUserPartners();
        for (Record rec : recs_spec){
            String PRO_SPEC_ID = rec.getString("PRO_SPEC_ID");
            Record pro = allSpecPros.findEq("SPEC_ID",PRO_SPEC_ID);
            pro.copyTo(rec);
            //有多少个门店用的这个商品
            String sql1 = "SELECT DISTINCT(PARTNER_NO) AS PARTNER_NO FROM "+gysOrderTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND DELETE_TIME IS NULL)";
            RecordSet order_partners = se.executeRecordSet(sql1);
            int all = 0;
            for (Record p : order_partners) {
                String PARTNER_NO = p.getString("PARTNER_NO");
                Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
                p0.copyTo(p);

                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"') AND DELETE_TIME IS NULL";
                Record s = se.executeRecord(sql2);
                int allSum = s.isEmpty()?0:(int)s.getInt("PRO_COUNT");
                p.put("PRO_COUNT_SUM",allSum);
                all +=  allSum;
            }
            rec.put("SPEC_ALL_COUNT",all);
            rec.put("PARTNER_DATA",order_partners);
            rec.put("PARTNER_COUNT",order_partners.size());
        }

        return recs_spec;
    }
    public RecordSet getGysOrderDailyReportDH(String SJ_ID,String GYS_ID,String START_TIME,String END_TIME) {
        SQLExecutor se = getSqlExecutor();
        String orders_sql = "SELECT ORDER_ID,PARTNER_NO FROM "+gysOrderTable+" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+" AND JH_TIME>='"+START_TIME+"' AND JH_TIME<='"+END_TIME+"'";
        if (GYS_ID.length()>0 && !GYS_ID.equals("999") && !GYS_ID.equals("9") && !GYS_ID.equals("0"))
            orders_sql += " AND GYS_ID='"+GYS_ID+"' ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            orders_sql += " AND SJ_ID='"+SJ_ID+"' ";

        RecordSet allOrders = se.executeRecordSet(orders_sql);
        String ORDER_IDS = allOrders.joinColumnValues("ORDER_ID", ",");
        if (ORDER_IDS.length() > 0)
            ORDER_IDS = Constants.formatString(ORDER_IDS);
        else
            return new RecordSet();

        RecordSet allSpecPros = GlobalLogics.getBaseLogic().getAllGysProSpec(GYS_ID);
        RecordSet allPartners = GlobalLogics.getUser().getAllUserPartners();

        String sql00 ="SELECT DISTINCT(PARTNER_NO) AS PARTNER_NO FROM " + gysOrderTable + " WHERE ORDER_ID IN ("+ORDER_IDS+") AND DELETE_TIME IS NULL";
        RecordSet recs_partner = se.executeRecordSet(sql00, null);

        for (Record rec : recs_partner){
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
            p0.copyTo(rec);

            //有多少个门店用的这个商品
            String sql1 = "SELECT DISTINCT(PRO_SPEC_ID) AS PRO_SPEC_ID FROM "+orderProductTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"' AND DELETE_TIME IS NULL)";
            RecordSet recs_spec = se.executeRecordSet(sql1);
            int all = 0;
            for (Record p : recs_spec) {
                String PRO_SPEC_ID = p.getString("PRO_SPEC_ID");
                Record pro = allSpecPros.findEq("SPEC_ID",PRO_SPEC_ID);
                pro.copyTo(p);

                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"') AND DELETE_TIME IS NULL";
                Record s = se.executeRecord(sql2);
                int allSum = s.isEmpty()?0:(int)s.getInt("PRO_COUNT");
                p.put("PRO_COUNT_SUM",allSum);
                all +=  allSum;
            }
            rec.put("PARTNER_ALL_COUNT",all);
            rec.put("SPEC_DATA",recs_spec);
            rec.put("SPEC_COUNT",recs_spec.size());
        }

        return recs_partner;
    }

    public RecordSet getGysOrderDailyReport2(String SJ_ID,String GYS_ID,String START_TIME,String END_TIME) {
        SQLExecutor se = getSqlExecutor();
        String orders_sql = "SELECT ORDER_ID,PARTNER_NO FROM "+gysOrderTable+" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+" AND JH_TIME>='"+START_TIME+"' AND JH_TIME<='"+END_TIME+"'";
        if (GYS_ID.length()>0 && !GYS_ID.equals("999") && !GYS_ID.equals("9") && !GYS_ID.equals("0"))
            orders_sql += " AND GYS_ID='"+GYS_ID+"' ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            orders_sql += " AND SJ_ID='"+SJ_ID+"' ";
        RecordSet allOrders = se.executeRecordSet(orders_sql);
        String ORDER_IDS = allOrders.joinColumnValues("ORDER_ID", ",");
        if (ORDER_IDS.length() > 0)
            ORDER_IDS = Constants.formatString(ORDER_IDS);
        else
            return new RecordSet();

        String sql00 ="SELECT DISTINCT(PRO_SPEC_ID) AS PRO_SPEC_ID FROM " + orderProductTable + " WHERE ORDER_ID IN ("+ORDER_IDS+") AND DELETE_TIME IS NULL";
        RecordSet recs_spec = se.executeRecordSet(sql00, null);
        RecordSet allSpecPros = GlobalLogics.getBaseLogic().getAllGysProSpec(GYS_ID);
        RecordSet allGys = GlobalLogics.getUser().getAllSJGysSel("", SJ_ID);
        for (Record rec : recs_spec){
            String PRO_SPEC_ID = rec.getString("PRO_SPEC_ID");
            Record pro = allSpecPros.findEq("SPEC_ID",PRO_SPEC_ID);
            pro.copyTo(rec);
            //有多少个门店用的这个商品
            String sql1 = "SELECT DISTINCT(GYS_ID) AS GYS_ID FROM "+gysOrderTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND DELETE_TIME IS NULL)";
            RecordSet order_gys = se.executeRecordSet(sql1);
            int all = 0;
            for (Record p : order_gys) {
                String GYS_ID_ = p.getString("GYS_ID");
                Record p0 = allGys.findEq("GYS_ID", GYS_ID_);
                p0.copyTo(p);

                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND GYS_ID='"+GYS_ID_+"') AND DELETE_TIME IS NULL";
                Record s = se.executeRecord(sql2);
                int allSum = s.isEmpty()?0:(int)s.getInt("PRO_COUNT");
                p.put("PRO_COUNT_SUM",allSum);
                all +=  allSum;
            }
            rec.put("GYS_ALL_COUNT",all);
            rec.put("GYS_DATA",order_gys);
            rec.put("GYS_COUNT",order_gys.size());
        }

        return recs_spec;
    }

    public Record getAllInboundPageList(Context ctx, String SJ_ID, String GYS_ID, String START_TIME, String END_TIME,  int page, int count, String ORDER_ID, int STATUS) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = "";
        if (GYS_ID.length()>0 && !GYS_ID.equals("0") && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            filter += " AND GYS_ID='"+GYS_ID+"' ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("0") && !SJ_ID.equals("9") && !SJ_ID.equals("999"))
            filter += " AND GYS_ID IN (SELECT GYS_ID FROM "+gysNewTable+" WHERE SJ_ID='"+SJ_ID+"') ";
        if (ORDER_ID.length()>0)
            filter += " AND ORDER_ID = '"+ORDER_ID+"' ";
        if (START_TIME.length()>0)
            filter += " AND INBOUND_TIME >= '"+START_TIME+"' ";
        if (END_TIME.length()>0)
            filter += " AND INBOUND_TIME <= '"+END_TIME+"' ";
        if (STATUS != 0 && STATUS != 999)
            filter += " AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE STATUS='"+STATUS+"' AND STATUS>='"+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+"' AND DELETE_TIME IS NULL) ";
        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM " + orderInboundTable + "  WHERE DELETE_TIME IS NULL ";
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
        String sql = "SELECT * FROM " + orderInboundTable + " WHERE DELETE_TIME IS NULL ";
        sql+=filter;

        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY INBOUND_TIME DESC LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            rec = formatOrderInbound(rec);
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

    public Record getSingleInbound(String ORDER_ID, String INBOUND_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + orderInboundTable + " WHERE DELETE_TIME IS NULL ";
        sql += " AND ORDER_ID='" + ORDER_ID + "'  AND INBOUND_ID='" + INBOUND_ID + "'";
        Record rec = se.executeRecord(sql);
        rec = formatOrderInbound(rec);
        if (!rec.isEmpty()){
            RecordSet all_packages = getOrderPackages(rec.getString("ORDER_ID"));
            int inbound_count = 0;
            for (Record p : all_packages) {
                if (p.getString("IN_KW_TIME").length() > 0) {
                    inbound_count+=1;
                }
            }
            rec.put("PACKAGES_INBOUND_COUNT", inbound_count);
            rec.put("ORDER_PACKAGES", all_packages);

        }
        return rec;
    }
    public Record getSingleOutbound(String ORDER_ID, String OUTBOUND_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + orderOutboundTable + " WHERE DELETE_TIME IS NULL ";
        sql += " AND ORDER_ID='" + ORDER_ID + "'  AND OUTBOUND_ID='" + OUTBOUND_ID + "'";
        Record rec = se.executeRecord(sql);
        rec = formatOrderInbound(rec);
        if (!rec.isEmpty()){
            RecordSet all_packages = getOrderPackages(rec.getString("ORDER_ID"));
            int outbound_count = 0;
            for (Record p : all_packages) {
                if (p.getString("OUT_KW_TIME").length() > 0) {
                    outbound_count+=1;
                }
            }
            rec.put("PACKAGES_OUTBOUND_COUNT", outbound_count);
            rec.put("ORDER_PACKAGES", all_packages);

        }
        return rec;
    }

    public boolean confirmInbound(Context ctx,String PACKAGE_CODE) {
        SQLExecutor se = getSqlExecutor();
        Record single_package = getSinglePackage(PACKAGE_CODE);
        String ORDER_ID = single_package.getString("ORDER_ID");
        String nowTime = DateUtils.now();
        String sql = "UPDATE "+packageTable+" SET IN_KW_TIME='"+nowTime+"',IN_KW_USER_ID='"+ctx.getUser_id()+"' WHERE PACKAGE_CODE='"+PACKAGE_CODE+"' ";
        long n = se.executeUpdate(sql);
        if (n>0){
            String sql2 = "SELECT * FROM " + packageTable + " WHERE IN_KW_TIME='' AND ORDER_ID='"+ORDER_ID+"' ";
            RecordSet notInboundPackage = se.executeRecordSet(sql2);
            if (notInboundPackage.size()>0){
                String sql3 = "UPDATE "+gysOrderTable+" SET STATUS='"+OrderConstants.ORDER_STATUS_INBOUNT_PART+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
                se.executeUpdate(sql3);
            }else{
                String sql3 = "UPDATE "+gysOrderTable+" SET STATUS='"+OrderConstants.ORDER_STATUS_INBOUNT_FINISHED+"',FINISH_INREPOR_TIME='"+nowTime+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
                se.executeUpdate(sql3);
            }
        }
        return true;
    }

    public Record getAllOutboundPageList(Context ctx, String SJ_ID, String GYS_ID, String START_TIME, String END_TIME,  int page, int count, String ORDER_ID, int STATUS) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = "";
        if (GYS_ID.length()>0 && !GYS_ID.equals("0") && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            filter += " AND GYS_ID='"+GYS_ID+"' ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("0") && !SJ_ID.equals("9") && !SJ_ID.equals("999"))
            filter += " AND GYS_ID IN (SELECT GYS_ID FROM "+gysNewTable+" WHERE SJ_ID='"+SJ_ID+"') ";
        if (ORDER_ID.length()>0)
            filter += " AND ORDER_ID = '"+ORDER_ID+"' ";
        if (START_TIME.length()>0)
            filter += " AND OUTBOUND_TIME >= '"+START_TIME+"' ";
        if (END_TIME.length()>0)
            filter += " AND OUTBOUND_TIME <= '"+END_TIME+"' ";
        if (STATUS != 0 && STATUS != 999)
            filter += " AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE STATUS='"+STATUS+"' AND STATUS>='"+OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE+"' AND DELETE_TIME IS NULL) ";

        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM " + orderOutboundTable + "  WHERE DELETE_TIME IS NULL ";
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
        String sql = "SELECT * FROM " + orderOutboundTable + " WHERE DELETE_TIME IS NULL ";
        sql+=filter;

        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY OUTBOUND_TIME DESC LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            rec = formatOrderInbound(rec);
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

    public boolean confirmOutbound(Context ctx,String PACKAGE_CODE) {
        SQLExecutor se = getSqlExecutor();
        Record single_package = getSinglePackage(PACKAGE_CODE);
        String ORDER_ID = single_package.getString("ORDER_ID");
        String nowTime = DateUtils.now();
        String sql = "UPDATE "+packageTable+" SET OUT_KW_TIME='"+nowTime+"',OUT_KW_USER_ID='"+ctx.getUser_id()+"' WHERE PACKAGE_CODE='"+PACKAGE_CODE+"' ";
        long n = se.executeUpdate(sql);
        if (n>0){
            String sql2 = "SELECT * FROM " + packageTable + " WHERE OUT_KW_TIME='' AND IN_KW_TIME!='' AND ORDER_ID='"+ORDER_ID+"' ";
            RecordSet notInboundPackage = se.executeRecordSet(sql2);
            if (notInboundPackage.size()>0){
                String sql3 = "UPDATE "+gysOrderTable+" SET STATUS='"+OrderConstants.ORDER_STATUS_OUTBOUNT_PART+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
                se.executeUpdate(sql3);
            }else{
                String sql3 = "UPDATE "+gysOrderTable+" SET STATUS='"+OrderConstants.ORDER_STATUS_OUTBOUNT_FINISHED+"',FINISH_OUTREPOR_TIME='"+nowTime+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
                se.executeUpdate(sql3);
            }
        }
        return true;
    }

    public RecordSet getNowRepoPackage(String SJ_ID,String GYS_ID,String F_KW_ID,String KW_ID) {
        String sql ="SELECT p.*,g.KW_ID FROM " + packageTable + " p INNER JOIN "+gysOrderTable+" g ON g.ORDER_ID=p.ORDER_ID WHERE p.IN_KW_TIME!='' AND p.OUT_KW_TIME='' AND g.DELETE_TIME IS NULL ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            sql +=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE SJ_ID='"+SJ_ID+"' AND DELETE_TIME IS NULL)";
        if (GYS_ID.length()>0 && !GYS_ID.equals("999") && !GYS_ID.equals("9") && !GYS_ID.equals("0"))
            sql +=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE GYS_ID='"+GYS_ID+"' AND DELETE_TIME IS NULL)";
        if (KW_ID.length()>0 && !KW_ID.equals("999") && !KW_ID.equals("9") && !KW_ID.equals("0"))
            sql +=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE KW_ID='"+KW_ID+"' AND DELETE_TIME IS NULL)";
        if (F_KW_ID.length()>0 && !F_KW_ID.equals("999") && !F_KW_ID.equals("9") && !F_KW_ID.equals("0"))
            sql +=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE KW_ID IN (SELECT KW_ID FROM "+kwTable+" WHERE FID='"+F_KW_ID+"') AND DELETE_TIME IS NULL)";

        SQLExecutor se = getSqlExecutor();
        RecordSet recs = se.executeRecordSet(sql, null);
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        for (Record r : recs){
            RecordSet pd =  se.executeRecordSet("SELECT * FROM " + packageProductTable + " WHERE PACKAGE_CODE='"+r.getString("PACKAGE_CODE")+"'") ;
            r.put("PACKAGE_PRODUCT",pd);

            Record thisKw = allKw.findEq("KW_ID",r.getString("KW_ID"));
            r.put("KW_NAME",thisKw.getString("KW_NAME"));
            Record fatherKw = allKw.findEq("KW_ID",thisKw.getString("FID"));
            r.put("PARENT_KW_NAME", fatherKw.getString("KW_NAME"));

        }
        return recs;
    }

    public RecordSet getGysOrderDailyGoods(String SJ_ID,String GYS_ID,String F_KW_ID,String KW_ID) {
        SQLExecutor se = getSqlExecutor();
        String orders_sql = "SELECT ORDER_ID,PARTNER_NO FROM "+gysOrderTable+" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_PART+" AND STATUS<"+OrderConstants.ORDER_STATUS_OUTBOUNT_PART+" ";

        String orderFilter = "";
        if (SJ_ID.length()>0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            orderFilter +=" AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE SJ_ID='"+SJ_ID+"' AND DELETE_TIME IS NULL)";
        if (GYS_ID.length()>0 && !GYS_ID.equals("999") && !GYS_ID.equals("9") && !GYS_ID.equals("0"))
            orderFilter +=" AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE GYS_ID='"+GYS_ID+"' AND DELETE_TIME IS NULL)";
        if (KW_ID.length()>0 && !KW_ID.equals("999") && !KW_ID.equals("9") && !KW_ID.equals("0"))
            orderFilter +=" AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE KW_ID='"+KW_ID+"' AND DELETE_TIME IS NULL)";
        if (F_KW_ID.length()>0 && !F_KW_ID.equals("999") && !F_KW_ID.equals("9") && !F_KW_ID.equals("0"))
            orderFilter +=" AND ORDER_ID IN (SELECT ORDER_ID FROM "+gysOrderTable+" WHERE KW_ID IN (SELECT KW_ID FROM "+kwTable+" WHERE FID='"+F_KW_ID+"') AND DELETE_TIME IS NULL)";


        RecordSet allOrders = se.executeRecordSet(orders_sql+orderFilter);
        String ORDER_IDS = allOrders.joinColumnValues("ORDER_ID", ",");
        if (ORDER_IDS.length() > 0)
            ORDER_IDS = Constants.formatString(ORDER_IDS);
        else
            return new RecordSet();



        String sql00 ="SELECT DISTINCT(SPEC_ID) AS SPEC_ID FROM " + packageProductTable + " WHERE 1=1 ";
        sql00 += " AND PACKAGE_CODE IN (SELECT PACKAGE_CODE FROM "+packageTable+" WHERE IN_KW_TIME!='' AND OUT_KW_TIME='' )";
        sql00+=" AND ORDER_ID IN ("+ORDER_IDS+")";


        RecordSet recs_spec = se.executeRecordSet(sql00, null);
        RecordSet allSpecPros = GlobalLogics.getBaseLogic().getAllGysProSpec(GYS_ID);
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        for (Record rec : recs_spec){
            String SPEC_ID = rec.getString("SPEC_ID");
            Record pro = allSpecPros.findEq("SPEC_ID",SPEC_ID);
            pro.copyTo(rec);

            String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+packageProductTable+" WHERE SPEC_ID='"+SPEC_ID+"'  AND ORDER_ID IN ("+ORDER_IDS+") AND PACKAGE_CODE IN (SELECT PACKAGE_CODE FROM "+packageTable+" WHERE IN_KW_TIME!='' AND OUT_KW_TIME='' ) ";
            Record s = se.executeRecord(sql2);
            int allSum = s.isEmpty()?0:(int)s.getInt("PRO_COUNT");

            rec.put("ALL_COUNT",allSum);
            //还要看,这些货,存在那些库存的
            String sql3 = "SELECT KW_ID FROM "+gysOrderTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+packageTable+" WHERE IN_KW_TIME!='' AND OUT_KW_TIME='' ) ";
            sql3 += " AND ORDER_ID IN (SELECT ORDER_ID FROM "+packageProductTable+" WHERE SPEC_ID='"+SPEC_ID+"')";
            sql3 += "  GROUP BY KW_ID";

            RecordSet recs_kw = se.executeRecordSet(sql3);
            for (Record r : recs_kw){
                Record thisKw = allKw.findEq("KW_ID",r.getString("KW_ID"));
                r.put("KW_NAME",thisKw.getString("KW_NAME"));
                Record fatherKw = allKw.findEq("KW_ID", thisKw.getString("FID"));
                r.put("PARENT_KW_NAME", fatherKw.getString("KW_NAME"));

            }
            rec.put("ALL_KWS",recs_kw);
        }


        return recs_spec;
    }

    public Record getSingleOrderPrint(String ORDER_ID) {
        String sql ="SELECT * FROM " + gysOrderTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        if (!rec.isEmpty()) {
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record partner = GlobalLogics.getUser().getSinglePartnerByNoBase(PARTNER_NO);
            rec.put("PARTNER_INFO",partner);
            RecordSet all_packages = getOrderPackagesPrint(rec.getString("ORDER_ID"));
            int allBoxCount = 0;
            for (Record r : all_packages){
                allBoxCount += r.getInt("COUNT");
            }
            rec.put("ORDER_PACKAGES", all_packages);
            rec.put("ORDER_PACKAGES_COUNT", allBoxCount);
        }
        return rec;
    }
}
