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

import java.util.ArrayList;
import java.util.List;


public class OrderImpl implements OrderLogic, Initializable {
    private static final Logger L = Logger.getLogger(OrderImpl.class);

    private ConnectionFactory connectionFactory;
    private String db;

    private String orderTable = "t_sys_order";
    private String orderImportTable = "t_sys_order_import";
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
        this.orderTable = null;
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
        String sql = "UPDATE  " + orderTable + " SET DELETE_TIME='"+DateUtils.now()+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateOrderStatusInbound(Context ctx, String ORDER_ID, int STATUS) {
        String sql = "UPDATE  " + orderTable + " SET STATUS='"+STATUS+"',VERIFY_STATUS='1',VERIFY_USER_ID='"+ctx.getUser_id()+"',VERIFY_TIME='"+DateUtils.now()+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateOrderStatusOutbound(Context ctx, String ORDER_ID,int STATUS) {
        String sql = "UPDATE  " + orderTable + " SET STATUS='"+STATUS+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateOrderState(String ORDER_ID,int STATE) {
        String sql ="UPDATE " + orderTable + " SET STATE='"+STATE+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean updateOrderVerify(String ORDER_ID,String USER_ID) {
        String sql ="UPDATE " + orderTable + " SET STATUS='"+OrderConstants.ORDER_STATUS_CONFIRMED+"',VERIFY_STATUS='1',VERIFY_USER_ID='"+USER_ID+"',VERIFY_TIME='"+DateUtils.now()+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
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

    public boolean deletePackageAll(String ORDER_ID) {
        String sql1 = "DELETE FROM " + packageProductTable + "  WHERE ORDER_ID='" + ORDER_ID + "' ";
        String sql2 = "DELETE FROM " + packageTable + "  WHERE ORDER_ID='" + ORDER_ID + "' ";
        List<String> ls = new ArrayList<String>();
        ls.add(sql1);
        ls.add(sql2);

        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(ls);
        return n > 0;
    }

    public boolean printOrderInbound(String ORDER_ID) {
        String sql1 = "UPDATE " + orderTable + " SET PRINT_INBOUND=PRINT_INBOUND+1  WHERE ORDER_ID='" + ORDER_ID + "' ";
        List<String> ls = new ArrayList<String>();
        ls.add(sql1);
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(ls);
        return n > 0;
    }

        //获取所有,分页
    public Record getAllGysOrderPageList(Context ctx,String PARTNER_NO, String PRO_TYPE_ID, String SJ_ID,String GYS_ID, String START_TIME, String END_TIME, String STATE, int PAY_DONE, int page, int count, String ORDER_ID,String OUT_ORDER_ID, String INBOUND_STATUS_BEGIN, String INBOUND_STATUS_END) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = "";
        if (GYS_ID.length()>0 && !GYS_ID.equals("0") && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            filter += " AND GYS_ID='"+GYS_ID+"' ";
        if (PARTNER_NO.length()>0 && !PARTNER_NO.equals("0") && !PARTNER_NO.equals("9") && !PARTNER_NO.equals("999") )
            filter += " AND PARTNER_NO='"+PARTNER_NO+"' ";
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


        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM " + orderTable + "  WHERE DELETE_TIME IS NULL ";
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
        String sql = "SELECT * FROM " + orderTable + " WHERE DELETE_TIME IS NULL ";
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
            rec = formatGYSOrderList(rec);
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
        Record partner = GlobalLogics.getUser().getSinglePartnerByNo(order.getString("PARTNER_NO"));
        rec.put("PARTNER_NAME",partner.getString("PARTNER_NAME"));
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
        rec.put("ORDER_CREATE_TIME",rec.getString("CREATE_TIME"));

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
    public Record formatGYSOrderList(Record rec) {

        if (rec.isEmpty())
            return rec;
        rec.put("ORDER_CREATE_TIME",rec.getString("CREATE_TIME"));

        Record GYS = GlobalLogics.getUser().getSingleGysBase(rec.getString("GYS_ID"));
        GYS.copyTo(rec);
        String PARTNER_NO = rec.getString("PARTNER_NO");
        Record partner = GlobalLogics.getUser().getSinglePartnerByNoBaseOrder(PARTNER_NO);
        partner.copyTo(rec);

        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT PRO_SPEC_ID,PRO_COUNT FROM " + orderProductTable + " WHERE ORDER_ID='"+rec.getString("ORDER_ID")+"' ";
        RecordSet recs_products = se.executeRecordSet(sql00, null);
        int all_count = 0;int all_has = 0;
        for (Record product : recs_products){
            //再查,已经装箱了多少了
            String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+packageProductTable+" WHERE ORDER_ID='"+rec.getString("ORDER_ID")+"' AND SPEC_ID='"+product.getString("PRO_SPEC_ID")+"' ";
            Record h =  se.executeRecord(sql2, null);
            int hasCount = h.isEmpty()?0:(int)h.getInt("PRO_COUNT");
            product.put("HAS_PACKAGE_COUNT",hasCount);
            all_count += product.getInt("PRO_COUNT");
            all_has += hasCount;
        }
        rec.put("ALL_COUNT", all_count);
        rec.put("ALL_HAS", all_has);
        rec.put("ALL_LESS", all_count - all_has);


//        rec.put("ORDER_PRODUCTS", getOrderProducts(rec.getString("ORDER_ID")));
//        rec.put("ORDER_INBOUNDS", getOrderInbound(rec.getString("ORDER_ID")));
//        if (rec.getString("VERIFY_USER_ID").length() > 0) {
//            Record u = GlobalLogics.getUser().getSingleUserSimple(rec.getString("VERIFY_USER_ID"));
//            rec.put("VERIFY_USER_NAME", u.getString("DISPLAY_NAME"));
//        } else {
//            rec.put("VERIFY_USER_NAME", "");
//        }
//        if (rec.getString("CREATE_USER_ID").length() > 0) {
//            Record u = GlobalLogics.getUser().getSingleUserSimple(rec.getString("CREATE_USER_ID"));
//            rec.put("CREATE_USER_NAME", u.getString("DISPLAY_NAME"));
//        } else {
//            rec.put("CREATE_USER_NAME", "");
//        }
//        RecordSet all_packages = getOrderPackages(rec.getString("ORDER_ID"));
//        rec.put("ORDER_PACKAGES", all_packages);
        return rec;
    }

    public Record getSingleOrder(String ORDER_ID) {
        String sql ="SELECT * FROM " + orderTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        if (!rec.isEmpty()) {
            rec = formatGYSOrder(rec);
        }
        return rec;
    }
    public Record getSingleOrderBase(String ORDER_ID) {
        String sql ="SELECT * FROM " + orderTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        return rec;
    }
    public Record getSinglePackage(String PACKAGE_CODE) {
        PACKAGE_CODE = PACKAGE_CODE.replace("'","");
        String sql ="SELECT * FROM " + packageTable + " WHERE PACKAGE_CODE='"+PACKAGE_CODE+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        return rec;
    }
    public Record getSingleOrderForPackage(String ORDER_ID) {
        String sql ="SELECT * FROM " + orderTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
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

            String sql00 ="SELECT pro.PRO_DW,pro.PRO_DW_NAME,pro.PRO_TYPE_ID,pro.PRO_TYPE,pd.* FROM " + orderProductTable + " pd INNER JOIN "+productTable+" pro ON pro.PRO_ID=pd.PRO_ID WHERE pd.ORDER_ID='"+ORDER_ID+"' and pd.DELETE_TIME IS NULL ORDER BY CREATE_TIME DESC";
            RecordSet recs_products = se.executeRecordSet(sql00, null);
//            RecordSet allDw = GlobalLogics.getBaseLogic().getAllDW();
            int all_count = 0;int all_has = 0;
            for (Record product : recs_products){
//                Record dw = allDw.findEq("DW_SX",product.getString("PRO_DW"));
//                product.put("PRO_DW_NAME",dw.getString("DW"));
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
        String sql00 ="SELECT pro.PRO_DW,pro.PRO_DW_NAME,pro.PRO_TYPE_ID,pro.PRO_TYPE,pd.* FROM " + orderProductTable + " pd INNER JOIN "+productTable+" pro ON pro.PRO_ID=pd.PRO_ID WHERE pd.ORDER_ID='"+ORDER_ID+"' and pd.DELETE_TIME IS NULL ORDER BY CREATE_TIME DESC";
        RecordSet recs_products = se.executeRecordSet(sql00, null);
//        RecordSet allDw = GlobalLogics.getBaseLogic().getAllDW();
        for (Record product : recs_products){
//            Record dw = allDw.findEq("DW_SX",product.getString("PRO_DW"));
//            product.put("PRO_DW_NAME",dw.getString("DW"));
            Record PRODUCT_SPEC = GlobalLogics.getBaseLogic().getSingleProSpec(product.getString("PRO_SPEC_ID"));
            PRODUCT_SPEC.copyTo(product);
        }
        return recs_products;
    }

    public RecordSet getOrderProductsSpec(String ORDER_ID) {
        SQLExecutor se = getSqlExecutor();
        String sql00 ="SELECT pro.PRO_SPEC_ID,pro.PRO_COUNT,p.SPEC_ID,p.SINGLE_BOX FROM " + orderProductTable + " pro INNER JOIN "+productSpecTable+" p ON p.SPEC_ID=pro.PRO_SPEC_ID WHERE pro.ORDER_ID='"+ORDER_ID+"' and pro.DELETE_TIME IS NULL ";
        RecordSet recs_products = se.executeRecordSet(sql00, null);
        return recs_products;
    }

    public RecordSet getOrderInbound(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT * FROM " + orderInboundTable + " WHERE ORDER_ID='"+ORDER_ID+"' AND DELETE_TIME IS NULL ORDER BY CREATE_TIME DESC";
        RecordSet recs_inbound = se.executeRecordSet(sql00, null);
        return recs_inbound;
    }
    public RecordSet getOrderOutbound(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT * FROM " + orderOutboundTable + " WHERE ORDER_ID='"+ORDER_ID+"' AND DELETE_TIME IS NULL ORDER BY CREATE_TIME DESC";
        RecordSet recs_inbound = se.executeRecordSet(sql00, null);
        return recs_inbound;
    }
    public boolean saveInbound(Context ctx,String INBOUND_ID,String ORDER_ID,String KW_ID, String GYS_ID, String GYS_NAME,String INBOUND_TIME) {
        RecordSet order_inbounds = getOrderInbound(ORDER_ID);
        if (order_inbounds.size()<=0) {
            String sql = "INSERT INTO " + orderInboundTable + " (INBOUND_ID,CREATE_USER_ID,ORDER_ID,KW_ID, GYS_ID,GYS_NAME, CREATE_TIME,INBOUND_TIME) VALUES" +
                    " ('"+INBOUND_ID+"','"+ctx.getUser_id()+"','"+ORDER_ID+"','" + KW_ID + "','" + GYS_ID +"','" + GYS_NAME + "','"+ DateUtils.now()+"','"+INBOUND_TIME+"') ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n>0;
        }else{
            return false;
        }
    }
    public boolean saveOutbound(Context ctx,String OUTBOUND_ID,String ORDER_ID,String KW_ID, String GYS_ID, String GYS_NAME,String OUTBOUND_TIME) {
        RecordSet order_outbounds = getOrderOutbound(ORDER_ID);
        if (order_outbounds.size()<=0) {
            String sql = "INSERT INTO " + orderOutboundTable + " (OUTBOUND_ID,CREATE_USER_ID,ORDER_ID,KW_ID, GYS_ID,GYS_NAME, CREATE_TIME,OUTBOUND_TIME) VALUES" +
                    " ('"+OUTBOUND_ID+"','"+ctx.getUser_id()+"','"+ORDER_ID+"','" + KW_ID + "','" + GYS_ID +"','" + GYS_NAME + "','"+ DateUtils.now()+"','"+OUTBOUND_TIME+"') ";
            SQLExecutor se = getSqlExecutor();
            long n = se.executeUpdate(sql);
            return n>0;
        }else{
            return false;
        }
    }
    public boolean saveGysOrder(Context ctx,String USER_ID,String SJ_ID, String ORDER_ID,String OUT_ORDER_ID, String GYS_ID, String GYS_NAME, String SEND_PRICE, String OTHER_PRICE, String PAY_TYPE, String MEMO, String JH_TIME, String JH_TYPE, String JH_ADDR, String IFKP, String KP_TYPE, String TAX, String FK_YD, int isBack,int status,String PARTNER_NO,String KW_ID,String PROVINCE,String CITY,String AREA,String ADDR,String FULL_ADDR,String CONTACT,String MOBILE) {
        String sql = "INSERT INTO " + orderTable + " (USER_ID,SJ_ID,ORDER_ID, OUT_ORDER_ID,GYS_ID, GYS_NAME,   SEND_PRICE, OTHER_PRICE, PAY_TYPE, MEMO,CREATE_TIME,JH_TIME, JH_TYPE, JH_ADDR, IFKP, KP_TYPE, TAX, FK_YD,CREATE_USER_ID,IS_BACK,STATUS, VERIFY_STATUS,PARTNER_NO, KW_ID,PROVINCE, CITY, AREA, ADDR, FULL_ADDR, CONTACT, MOBILE) VALUES" +
                " ('"+USER_ID+"','"+SJ_ID+"','" + ORDER_ID + "','"+OUT_ORDER_ID+"','" + GYS_ID +"','" + GYS_NAME + "','"+SEND_PRICE+"','"+OTHER_PRICE+"','"+PAY_TYPE+"','"+MEMO+"','"+ DateUtils.now()+"','"+JH_TIME+"','"+JH_TYPE+"','"+JH_ADDR+"','"+IFKP+"','"+KP_TYPE+"','"+TAX+"','"+FK_YD+"','"+ctx.getUser_id()+"','"+isBack+"','"+status+"','0','"+PARTNER_NO+"','"+KW_ID+"','"+PROVINCE+"','"+CITY+"','"+AREA+"','"+ADDR+"','"+FULL_ADDR+"','"+CONTACT+"','"+MOBILE+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }
    public boolean saveGysOrderImport(Context ctx,String USER_ID,String SJ_ID, String ORDER_ID,String OUT_ORDER_ID, String GYS_ID, String GYS_NAME, String SEND_PRICE, String OTHER_PRICE, String PAY_TYPE, String MEMO, String JH_TIME, String JH_TYPE, String JH_ADDR,String INBOUND_TIME,String OUTBOUND_TIME, String IFKP, String KP_TYPE, String TAX, String FK_YD, int isBack,int status,String PARTNER_NO,String KW_ID,String PROVINCE,String CITY,String AREA,String ADDR,String FULL_ADDR,String CONTACT,String MOBILE) {
        String sql = "INSERT INTO " + orderTable + " (USER_ID,SJ_ID,ORDER_ID, OUT_ORDER_ID,GYS_ID, GYS_NAME,   SEND_PRICE, OTHER_PRICE, PAY_TYPE, MEMO,CREATE_TIME,JH_TIME, JH_TYPE, JH_ADDR, INBOUND_TIME,OUTBOUND_TIME,IFKP, KP_TYPE, TAX, FK_YD,CREATE_USER_ID,IS_BACK,STATUS, VERIFY_STATUS,PARTNER_NO, KW_ID,PROVINCE, CITY, AREA, ADDR, FULL_ADDR, CONTACT, MOBILE) VALUES" +
                " ('"+USER_ID+"','"+SJ_ID+"','" + ORDER_ID + "','"+OUT_ORDER_ID+"','" + GYS_ID +"','" + GYS_NAME + "','"+SEND_PRICE+"','"+OTHER_PRICE+"','"+PAY_TYPE+"','"+MEMO+"','"+ DateUtils.now()+"','"+JH_TIME+"','"+JH_TYPE+"','"+JH_ADDR+"','"+INBOUND_TIME+"','"+OUTBOUND_TIME+"','"+IFKP+"','"+KP_TYPE+"','"+TAX+"','"+FK_YD+"','"+ctx.getUser_id()+"','"+isBack+"','"+status+"','0','"+PARTNER_NO+"','"+KW_ID+"','"+PROVINCE+"','"+CITY+"','"+AREA+"','"+ADDR+"','"+FULL_ADDR+"','"+CONTACT+"','"+MOBILE+"') ";
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean updateGysOrder(Context ctx,String ORDER_ID,String OUT_ORDER_ID, String SEND_PRICE, String OTHER_PRICE, String PAY_TYPE, String MEMO, String JH_TIME,String INBOUND_TIME, String JH_TYPE, String JH_ADDR, String IFKP, String KP_TYPE, String TAX, String FK_YD,String PARTNER_NO,String PROVINCE,String CITY,String AREA,String ADDR,String FULL_ADDR,String CONTACT,String MOBILE) {
        String sql = "UPDATE " + orderTable + " SET OUT_ORDER_ID='"+OUT_ORDER_ID+"',SEND_PRICE='"+SEND_PRICE+"',OTHER_PRICE='"+OTHER_PRICE+"',PAY_TYPE='"+PAY_TYPE+"',MEMO='"+MEMO+"',JH_TIME='"+JH_TIME+"',INBOUND_TIME='"+INBOUND_TIME+"',OUTBOUND_TIME='"+JH_TIME+"',JH_TYPE='"+JH_TYPE+"',JH_ADDR='"+JH_ADDR+"',IFKP='"+IFKP+"',KP_TYPE='"+KP_TYPE+"',TAX='"+TAX+"',FK_YD='"+FK_YD+"',PARTNER_NO='"+PARTNER_NO+"',PROVINCE='"+PROVINCE+"',CITY='"+CITY+"',AREA='"+AREA+"',ADDR='"+ADDR+"',FULL_ADDR='"+FULL_ADDR+"',CONTACT='"+CONTACT+"',MOBILE='"+MOBILE+"' WHERE ORDER_ID='"+ORDER_ID+"'";
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
            RecordSet allPros = se.executeRecordSet("SELECT p.*,spec.PRO_SPEC,spec.PRO_COLOR,spec.PRO_DW_NAME FROM "+packageProductTable+" p INNER JOIN "+productSpecTable+" spec ON spec.SPEC_ID=p.SPEC_ID WHERE p.PACKAGE_CODE='"+PACKAGE_CODE+"'");
            String s = "";
            for (Record r : allPros){
                s+=r.getString("PRO_NAME")+" ["+r.getString("PRO_SPEC")+"] "+"( "+r.getInt("PRO_COUNT")+ " " + r.getString("PRO_DW_NAME") + " )"+",";
            }
            if (s.length()>0)
                s = s.substring(0,s.length()-1);
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
            RecordSet pd =  se.executeRecordSet("SELECT p.*,spec.PRO_SPEC,spec.PRO_COLOR,spec.PRO_DW_NAME FROM " + packageProductTable + " p INNER JOIN "+productSpecTable+" spec ON spec.SPEC_ID=p.SPEC_ID WHERE PACKAGE_CODE='"+r.getString("PACKAGE_CODE")+"'") ;
            r.put("PACKAGE_PRODUCT",pd);
        }
        return recs;
    }

    public RecordSet getOrderPackagesPrint(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="select PRO_DETAIL,COUNT(*) AS COUNT  from "+packageTable+" where ORDER_ID='"+ORDER_ID+"' group by PRO_DETAIL ";
        RecordSet recs = se.executeRecordSet(sql00, null);
        return recs;
    }

    public RecordSet getOrderPackagesBase(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT * FROM "+packageTable+" WHERE ORDER_ID='"+ORDER_ID+"' ";
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
        String orders_sql = "SELECT ORDER_ID,PARTNER_NO FROM "+ orderTable +" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+" AND JH_TIME>='"+START_TIME+"' AND JH_TIME<='"+END_TIME+"'";
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
            String sql1 = "SELECT DISTINCT(PARTNER_NO) AS PARTNER_NO FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND DELETE_TIME IS NULL)";
            RecordSet order_partners = se.executeRecordSet(sql1);
            int all = 0;
            for (Record p : order_partners) {
                String PARTNER_NO = p.getString("PARTNER_NO");
                Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
                p0.copyTo(p);

                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"') AND DELETE_TIME IS NULL";
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
        String orders_sql = "SELECT ORDER_ID,PARTNER_NO FROM "+ orderTable +" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+" AND JH_TIME>='"+START_TIME+"' AND JH_TIME<='"+END_TIME+"'";
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

        String sql00 ="SELECT DISTINCT(PARTNER_NO) AS PARTNER_NO FROM " + orderTable + " WHERE ORDER_ID IN ("+ORDER_IDS+") AND DELETE_TIME IS NULL";
        RecordSet recs_partner = se.executeRecordSet(sql00, null);

        for (Record rec : recs_partner){
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
            p0.copyTo(rec);

            //有多少个门店用的这个商品
            String sql1 = "SELECT DISTINCT(PRO_SPEC_ID) AS PRO_SPEC_ID FROM "+orderProductTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"' AND DELETE_TIME IS NULL)";
            RecordSet recs_spec = se.executeRecordSet(sql1);
            int all = 0;
            for (Record p : recs_spec) {
                String PRO_SPEC_ID = p.getString("PRO_SPEC_ID");
                Record pro = allSpecPros.findEq("SPEC_ID",PRO_SPEC_ID);
                pro.copyTo(p);

                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"') AND DELETE_TIME IS NULL";
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
        String orders_sql = "SELECT ORDER_ID,PARTNER_NO FROM "+ orderTable +" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+" AND JH_TIME>='"+START_TIME+"' AND JH_TIME<='"+END_TIME+"'";
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
            String sql1 = "SELECT DISTINCT(GYS_ID) AS GYS_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND DELETE_TIME IS NULL)";
            RecordSet order_gys = se.executeRecordSet(sql1);
            int all = 0;
            for (Record p : order_gys) {
                String GYS_ID_ = p.getString("GYS_ID");
                Record p0 = allGys.findEq("GYS_ID", GYS_ID_);
                p0.copyTo(p);

                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND GYS_ID='"+GYS_ID_+"') AND DELETE_TIME IS NULL";
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
            filter += " AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE STATUS='"+STATUS+"' AND STATUS>='"+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+"' AND DELETE_TIME IS NULL) ";
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
    public Record getSingleInboundBase(String INBOUND_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + orderInboundTable + " WHERE INBOUND_ID='" + INBOUND_ID + "'";
        Record rec = se.executeRecord(sql);
        return rec;
    }
    public Record getSingleOutboundBase(String OUTBOUND_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT * FROM " + orderOutboundTable + " WHERE OUTBOUND_ID='" + OUTBOUND_ID + "'";
        Record rec = se.executeRecord(sql);
        return rec;
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
        if (single_package.isEmpty())
            return false;
        String ORDER_ID = single_package.getString("ORDER_ID");
        String nowTime = DateUtils.now();
        String sql = "UPDATE "+packageTable+" SET IN_KW_TIME='"+nowTime+"',IN_KW_USER_ID='"+ctx.getUser_id()+"' WHERE PACKAGE_CODE='"+PACKAGE_CODE+"' ";
        long n = se.executeUpdate(sql);
        if (n>0){
            String sql2 = "SELECT * FROM " + packageTable + " WHERE IN_KW_TIME='' AND ORDER_ID='"+ORDER_ID+"' ";
            RecordSet notInboundPackage = se.executeRecordSet(sql2);
            if (notInboundPackage.size()>0){
                String sql3 = "UPDATE "+ orderTable +" SET STATUS='"+OrderConstants.ORDER_STATUS_INBOUNT_PART+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
                se.executeUpdate(sql3);
            }else{
                String sql3 = "UPDATE "+ orderTable +" SET STATUS='"+OrderConstants.ORDER_STATUS_INBOUNT_FINISHED+"',FINISH_INREPOR_TIME='"+nowTime+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
                long n1 = se.executeUpdate(sql3);
                if (n1>0){
                    //此时创建出库通知单
                    Record order = GlobalLogics.getOrderLogic().getSingleOrderBase(ORDER_ID) ;
                    String OUTBOUND_TIME = order.getString("OUTBOUND_TIME");
                    if (OUTBOUND_TIME.length()<=0){
                        OUTBOUND_TIME = order.getString("JH_TIME");
                    }
                    String OUTBOUND_ID = Constants.newOutboundCode();
                    boolean b = GlobalLogics.getOrderLogic().saveOutbound(ctx, OUTBOUND_ID, ORDER_ID, order.getString("KW_ID"), order.getString("GYS_ID"), GlobalLogics.getUser().getSingleGysBase(order.getString("GYS_ID")).getString("GYS_NAME_SX"), OUTBOUND_TIME);
                    if (b){
                        //更新订单状态
                        GlobalLogics.getOrderLogic().updateOrderStatusOutbound(ctx, ORDER_ID, OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE);
                    }
                }
            }
            return true;
        } else{
            return false;
        }
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
            filter += " AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE STATUS='"+STATUS+"' AND STATUS>='"+OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE+"' AND DELETE_TIME IS NULL) ";

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
        if (single_package.isEmpty())
            return false;
        String ORDER_ID = single_package.getString("ORDER_ID");
        String nowTime = DateUtils.now();
        String sql = "UPDATE "+packageTable+" SET OUT_KW_TIME='"+nowTime+"',OUT_KW_USER_ID='"+ctx.getUser_id()+"' WHERE PACKAGE_CODE='"+PACKAGE_CODE+"' ";
        long n = se.executeUpdate(sql);
        if (n>0){
            String sql2 = "SELECT * FROM " + packageTable + " WHERE OUT_KW_TIME='' AND IN_KW_TIME!='' AND ORDER_ID='"+ORDER_ID+"' ";
            RecordSet notInboundPackage = se.executeRecordSet(sql2);
            if (notInboundPackage.size()>0){
                String sql3 = "UPDATE "+ orderTable +" SET STATUS='"+OrderConstants.ORDER_STATUS_OUTBOUNT_PART+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
                se.executeUpdate(sql3);
            }else{
                String sql3 = "UPDATE "+ orderTable +" SET STATUS='"+OrderConstants.ORDER_STATUS_OUTBOUNT_FINISHED+"',FINISH_OUTREPOR_TIME='"+nowTime+"' WHERE ORDER_ID='"+ORDER_ID+"' ";
                se.executeUpdate(sql3);
            }
            return true;
        } else{
            return false;
        }
    }

    public RecordSet getNowRepoPackage(String SJ_ID,String GYS_ID,String F_KW_ID,String KW_ID,String START_TIME,String END_TIME) {
        String sql ="SELECT p.*,g.KW_ID FROM " + packageTable + " p INNER JOIN "+ orderTable +" g ON g.ORDER_ID=p.ORDER_ID WHERE p.IN_KW_TIME!='' AND p.OUT_KW_TIME='' AND g.DELETE_TIME IS NULL ";
        if (SJ_ID.length()>0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            sql +=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE SJ_ID='"+SJ_ID+"' AND DELETE_TIME IS NULL)";
        if (GYS_ID.length()>0 && !GYS_ID.equals("999") && !GYS_ID.equals("9") && !GYS_ID.equals("0"))
            sql +=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE GYS_ID='"+GYS_ID+"' AND DELETE_TIME IS NULL)";
        if (KW_ID.length()>0 && !KW_ID.equals("999") && !KW_ID.equals("9") && !KW_ID.equals("0"))
            sql +=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE KW_ID='"+KW_ID+"' AND DELETE_TIME IS NULL)";
        if (F_KW_ID.length()>0 && !F_KW_ID.equals("999") && !F_KW_ID.equals("9") && !F_KW_ID.equals("0"))
            sql +=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE KW_ID IN (SELECT KW_ID FROM "+kwTable+" WHERE FID='"+F_KW_ID+"') AND DELETE_TIME IS NULL)";
        if (START_TIME.length()>0)
            sql +=" AND INBOUND_TIME>='"+START_TIME+"' ";
        if (END_TIME.length()>0)
            sql +=" AND INBOUND_TIME<='"+END_TIME+"' ";
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

    public RecordSet getGysOrderDailyGoods(String SJ_ID,String GYS_ID,String F_KW_ID,String KW_ID,String START_TIME,String END_TIME) {
        SQLExecutor se = getSqlExecutor();
        String orders_sql = "SELECT ORDER_ID,PARTNER_NO FROM "+ orderTable +" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_PART+" AND STATUS<"+OrderConstants.ORDER_STATUS_OUTBOUNT_PART+" ";

        String orderFilter = "";
        if (SJ_ID.length()>0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            orderFilter +=" AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE SJ_ID='"+SJ_ID+"' AND DELETE_TIME IS NULL)";
        if (GYS_ID.length()>0 && !GYS_ID.equals("999") && !GYS_ID.equals("9") && !GYS_ID.equals("0"))
            orderFilter +=" AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE GYS_ID='"+GYS_ID+"' AND DELETE_TIME IS NULL)";
        if (KW_ID.length()>0 && !KW_ID.equals("999") && !KW_ID.equals("9") && !KW_ID.equals("0"))
            orderFilter +=" AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE KW_ID='"+KW_ID+"' AND DELETE_TIME IS NULL)";
        if (F_KW_ID.length()>0 && !F_KW_ID.equals("999") && !F_KW_ID.equals("9") && !F_KW_ID.equals("0"))
            orderFilter +=" AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE KW_ID IN (SELECT KW_ID FROM "+kwTable+" WHERE FID='"+F_KW_ID+"') AND DELETE_TIME IS NULL)";
        if (START_TIME.length()>0)
            orderFilter +=" AND INBOUND_TIME>='"+START_TIME+"' ";
        if (END_TIME.length()>0)
            orderFilter +=" AND INBOUND_TIME<='"+END_TIME+"' ";
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
            String sql3 = "SELECT KW_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+packageTable+" WHERE IN_KW_TIME!='' AND OUT_KW_TIME='' ) ";
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
        String sql ="SELECT * FROM " + orderTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
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

    public RecordSet getAllCanPrintMd(Context ctx, String GYS_ID,int isPrinted,String SJ_ID,String PARTNER_NO,String INBOUND_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = "";
        filter += " AND o.ORDER_ID IN (SELECT ORDER_ID FROM " + orderTable + " WHERE GYS_ID='" + GYS_ID + "' ";
        if (SJ_ID.length() > 0 && !SJ_ID.equals("999") && !SJ_ID.equals("9") && !SJ_ID.equals("0"))
            filter += " AND SJ_ID='" + SJ_ID + "' ";
        if (INBOUND_TIME.length() > 0 && !INBOUND_TIME.equals("999") && !INBOUND_TIME.equals("9") && !INBOUND_TIME.equals("0"))
            filter += " AND INBOUND_TIME='" + INBOUND_TIME + "' ";
        if (PARTNER_NO.length() > 0 && !PARTNER_NO.equals("999") && !PARTNER_NO.equals("9") && !PARTNER_NO.equals("0"))
            filter += " AND PARTNER_NO='" + PARTNER_NO + "' ";
        filter += " AND DELETE_TIME IS NULL AND STATUS>='" + OrderConstants.ORDER_STATUS_INBOUNT_CREATE + "') ";
        if (isPrinted != 9 && isPrinted != 999) {
            if (isPrinted==1){
                filter += " AND p.PRINT>0";
            }
            if (isPrinted==0){
                filter += " AND p.PRINT=0";
            }
        }

        String sql = "SELECT o.*,p.PACKAGE_CODE,p.PRINT,p.PRO_DETAIL,p.IN_KW_TIME,p.OUT_KW_TIME FROM " + packageTable + " p INNER JOIN "+ orderTable +" o ON o.ORDER_ID=p.ORDER_ID WHERE 1=1 ";
        sql+=filter;

        sql += " ORDER BY p.PACKAGE_CODE ";
        RecordSet recs = se.executeRecordSet(sql, null);
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        RecordSet allGys = GlobalLogics.getUser().getAllGys();
        RecordSet allSj = GlobalLogics.getUser().getAllSj();
        RecordSet allPartner = GlobalLogics.getUser().getAllPartners();
        for (Record rec : recs) {
//            RecordSet pd =  se.executeRecordSet("SELECT p.*,spec.PRO_SPEC,spec.PRO_COLOR FROM " + packageProductTable + " p INNER JOIN " + productSpecTable + " spec ON spec.SPEC_ID=p.SPEC_ID WHERE PACKAGE_CODE='" + rec.getString("PACKAGE_CODE") + "'") ;
//            rec.put("PACKAGE_PRODUCT", pd);

            String KW_ID = rec.getString("KW_ID");
            Record rec_kw= allKw.findEq("KW_ID",KW_ID)     ;//GlobalLogics.getBaseLogic().getSingleKwBase(KW_ID) ;
            rec.put("KW_NAME", rec_kw.getString("KW_NAME"));
            Record rec_kw_parent= allKw.findEq("KW_ID", rec_kw.getString("FID"));//GlobalLogics.getBaseLogic().getSingleKwBase(rec_kw.getString("FID")) ;
            rec.put("PARENT_KW_NAME", rec_kw_parent.getString("KW_NAME"));

            Record GYS = allGys.findEq("GYS_ID", rec.getString("GYS_ID"));//GlobalLogics.getUser().getSingleGysBase(rec.getString("GYS_ID"));
            rec.put("GYS_NAME",GYS.getString("GYS_NAME"));
            String PARTNER_NO_ = rec.getString("PARTNER_NO");
            Record partner = allPartner.findEq("PARTNER_NO",PARTNER_NO_);//GlobalLogics.getUser().getSinglePartnerByNo(PARTNER_NO_);
            rec.put("PARTNER_NAME",partner.getString("PARTNER_NAME"));
            String SJ_ID_ =  partner.getString("SJ_ID");
            Record sj = allSj.findEq("SJ_ID", SJ_ID_);//GlobalLogics.getUser().getSingleSjBase(SJ_ID_);
            rec.put("SJ_NAME",sj.getString("SJ_NAME"));
        }
        return recs;
    }

    public Record getSingleOrderByPackageCode(String PACKAGE_CODE) {
        String orderColumns = "o.ORDER_ID,o.OUT_ORDER_ID,o.GYS_ID,o.JH_TIME,o.KW_ID,o.PARTNER_NO,o.FULL_ADDR,o.CONTACT,o.MOBILE,o.INBOUND_TIME";
        String sql ="SELECT p.PRO_DETAIL,"+orderColumns+" FROM "+packageTable+" p INNER JOIN  " + orderTable + " o ON o.ORDER_ID=p.ORDER_ID WHERE p.PACKAGE_CODE='"+PACKAGE_CODE+"' ";
        SQLExecutor se = getSqlExecutor();
        Record rec = se.executeRecord(sql);
        if (!rec.isEmpty()) {
            String KW_ID = rec.getString("KW_ID");
            Record rec_kw= GlobalLogics.getBaseLogic().getSingleKwBase(KW_ID) ;
            rec.put("KW_NAME",rec_kw.getString("KW_NAME"));
            Record rec_kw_parent= GlobalLogics.getBaseLogic().getSingleKwBase(rec_kw.getString("FID")) ;
            rec.put("PARENT_KW_NAME", rec_kw_parent.getString("KW_NAME"));
            Record GYS = GlobalLogics.getUser().getSingleGysBase(rec.getString("GYS_ID"));
            rec.put("GYS_NAME",GYS.getString("GYS_NAME"));
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record partner = GlobalLogics.getUser().getSinglePartnerByNo(PARTNER_NO);
            rec.put("PARTNER_NAME",partner.getString("PARTNER_NAME"));
            String SJ_ID =  partner.getString("SJ_ID");
            Record sj = GlobalLogics.getUser().getSingleSjBase(SJ_ID);
            rec.put("SJ_NAME",sj.getString("SJ_NAME"));
        }
        return rec;
    }



    ///===========webservice 用=============
    public RecordSet webService_getAllInbound(String KW_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = " AND ORDER_ID IN ";
        filter += " ( ";
        filter += " SELECT ORDER_ID FROM "+ orderTable +" WHERE STATUS>='"+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+"' AND STATUS<='"+OrderConstants.ORDER_STATUS_INBOUNT_PART+"' AND DELETE_TIME IS NULL  ";
        if (KW_ID.length() > 0 && !KW_ID.equals("999") && !KW_ID.equals("9") && !KW_ID.equals("0"))
            filter += " AND KW_ID IN (SELECT KW_ID FROM " + kwTable + " WHERE FID='" + KW_ID + "') ";
        filter += " )  ";
        String sql = "SELECT * FROM " + orderInboundTable + " WHERE DELETE_TIME IS NULL ";
        sql+=filter;
        sql += " ORDER BY INBOUND_TIME DESC ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            rec = formatOrderInbound(rec);
        }
        return recs;
    }

    //获取全部已经产生入库通知单的,这个时间段的 kw
    //不管是哪个供应商的货,只管收
    public RecordSet webService_getAllInboundKws(String START_TIME,String END_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        String sql = "SELECT DISTINCT(KW_ID) AS KW_ID FROM " + orderInboundTable + " WHERE DELETE_TIME IS NULL ";
        if (START_TIME.length() >= 0)
            sql += " AND INBOUND_TIME>='" + START_TIME + "' ";
        if (END_TIME.length() >= 0)
            sql += " AND INBOUND_TIME<='" + END_TIME + "' ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            String KW_ID = rec.getString("KW_ID");
            Record k = allKw.findEq("KW_ID", KW_ID);
            rec.put("KW_NAME",k.getString("KW_NAME"));

            String sql2 = "SELECT PACKAGE_CODE,IN_KW_TIME,OUT_KW_TIME FROM "+packageTable+" WHERE ORDER_ID IN (SELECT ORDER_ID FROM "+orderTable+" WHERE DELETE_TIME IS NULL AND STATUS>='"+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+"' AND STATUS<'"+OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE+"' AND KW_ID='"+KW_ID+"') ";
            sql2 +=" AND ORDER_ID IN (";
            sql2 +=" SELECT ORDER_ID FROM "+orderInboundTable+" WHERE DELETE_TIME IS NULL AND KW_ID ='"+KW_ID+"' ";
            if (START_TIME.length() >= 0)
                sql2 += " AND INBOUND_TIME>='" + START_TIME + "' ";
            if (END_TIME.length() >= 0)
                sql2 += " AND INBOUND_TIME<='" + END_TIME + "' ";
            sql2 +=" )";
            RecordSet all_packages = se.executeRecordSet(sql2);
            int HAS = 0;
            for (Record r : all_packages) {
                if (r.getString("IN_KW_TIME").length() > 0)
                    HAS += 1;
            }
            rec.put("ALL_PACKAGE_COUNT", all_packages.size());
            rec.put("HAS_PACKAGE_COUNT", HAS);
            rec.put("LESS_PACKAGE_COUNT", all_packages.size() - HAS);
        }

        for (int i=recs.size()-1;i>=0;i--){
            if (recs.get(i).getInt("ALL_PACKAGE_COUNT")>0 && recs.get(i).getInt("LESS_PACKAGE_COUNT")==0)
                recs.remove(i);
        }
        return recs;
    }

    //获取全部已经产生入库通知单的,这个时间段的 kw
    //不管是哪个供应商的货,只管收 ,
    //所有的箱子
    public RecordSet webService_getAllInboundKwPackages(String START_TIME,String END_TIME,String KW_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql2 = "SELECT p.*,o.PARTNER_NO FROM "+packageTable+" p INNER JOIN "+orderTable+" o ON o.ORDER_ID=p.ORDER_ID WHERE o.DELETE_TIME IS NULL AND o.STATUS>='"+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+"' AND o.STATUS<'"+OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE+"' AND o.KW_ID='"+KW_ID+"' ";
        sql2 +=" AND p.ORDER_ID IN (";
        sql2 +=" SELECT ORDER_ID FROM "+orderInboundTable+" WHERE DELETE_TIME IS NULL AND KW_ID ='"+KW_ID+"' ";
        if (START_TIME.length() >= 0)
            sql2 += " AND INBOUND_TIME>='" + START_TIME + "' ";
        if (END_TIME.length() >= 0)
            sql2 += " AND INBOUND_TIME<='" + END_TIME + "' ";
        sql2 +=" )";
        sql2 += " AND p.IN_KW_TIME='' ";
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        RecordSet recs = se.executeRecordSet(sql2, null);
        for (Record rec : recs) {
            Record rec_kw = allKw.findEq("KW_ID", KW_ID);
            rec.put("KW_NAME", rec_kw.getString("KW_NAME"));
            String FID = rec_kw.getString("FID");
            Record rec_kw_parent = allKw.findEq("KW_ID", FID);
            rec.put("PARENT_KW_NAME", rec_kw_parent.getString("KW_NAME"));
            Record p = GlobalLogics.getUser().getSinglePartnerByNoBase(rec.getString("PARTNER_NO"));
            rec.put("PARTNER_NAME", p.getString("PARTNER_NAME"));
        }
        return recs;
    }

    public RecordSet webService_getAllOutboundKws(String START_TIME,String END_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        String sql = "SELECT DISTINCT(KW_ID) AS KW_ID FROM " + orderOutboundTable + " WHERE DELETE_TIME IS NULL ";
        if (START_TIME.length() >= 0)
            sql += " AND OUTBOUND_TIME>='" + START_TIME + "' ";
        if (END_TIME.length() >= 0)
            sql += " AND OUTBOUND_TIME<='" + END_TIME + "' ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            String KW_ID = rec.getString("KW_ID");
            Record k = allKw.findEq("KW_ID", KW_ID);
            rec.put("KW_NAME",k.getString("KW_NAME"));

            String sql2 = "SELECT PACKAGE_CODE,IN_KW_TIME,OUT_KW_TIME FROM "+packageTable+" WHERE ORDER_ID IN (SELECT ORDER_ID FROM "+orderTable+" WHERE DELETE_TIME IS NULL AND STATUS>='"+OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE+"' AND STATUS<='"+OrderConstants.ORDER_STATUS_FINISHED+"' AND KW_ID='"+KW_ID+"') ";
            sql2 +=" AND ORDER_ID IN (";
            sql2 +=" SELECT ORDER_ID FROM "+orderOutboundTable+" WHERE DELETE_TIME IS NULL AND KW_ID ='"+KW_ID+"' ";
            if (START_TIME.length() >= 0)
                sql2 += " AND OUTBOUND_TIME>='" + START_TIME + "' ";
            if (END_TIME.length() >= 0)
                sql2 += " AND OUTBOUND_TIME<='" + END_TIME + "' ";
            sql2 +=" )";
            RecordSet all_packages = se.executeRecordSet(sql2);
            int HAS = 0;
            for (Record r : all_packages) {
                if (r.getString("OUT_KW_TIME").length() > 0)
                    HAS += 1;
            }
            rec.put("ALL_PACKAGE_COUNT", all_packages.size());
            rec.put("HAS_PACKAGE_COUNT", HAS);
            rec.put("LESS_PACKAGE_COUNT", all_packages.size() - HAS);
        }

        for (int i=recs.size()-1;i>=0;i--){
            if (recs.get(i).getInt("ALL_PACKAGE_COUNT")>0 && recs.get(i).getInt("LESS_PACKAGE_COUNT")==0)
                recs.remove(i);
        }
        return recs;
    }

    //获取全部已经产生入库通知单的,这个时间段的 kw
    //不管是哪个供应商的货,只管收 ,
    //所有的箱子
    public RecordSet webService_getAllOutboundKwPackages(String START_TIME,String END_TIME,String KW_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql2 = "SELECT p.*,o.PARTNER_NO FROM "+packageTable+" p INNER JOIN "+orderTable+" o ON o.ORDER_ID=p.ORDER_ID WHERE o.DELETE_TIME IS NULL AND o.STATUS>='"+OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE+"' AND o.STATUS<='"+OrderConstants.ORDER_STATUS_OUTBOUNT_FINISHED+"' AND o.KW_ID='"+KW_ID+"' ";
        sql2 +=" AND p.ORDER_ID IN (";
        sql2 +=" SELECT ORDER_ID FROM "+orderOutboundTable+" WHERE DELETE_TIME IS NULL AND KW_ID ='"+KW_ID+"' ";
        if (START_TIME.length() >= 0)
            sql2 += " AND OUTBOUND_TIME>='" + START_TIME + "' ";
        if (END_TIME.length() >= 0)
            sql2 += " AND OUTBOUND_TIME<='" + END_TIME + "' ";
        sql2 +=" )";
        sql2 += " AND p.OUT_KW_TIME='' ";
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        RecordSet recs = se.executeRecordSet(sql2, null);
        for (Record rec : recs) {
            Record rec_kw = allKw.findEq("KW_ID", KW_ID);
            rec.put("KW_NAME", rec_kw.getString("KW_NAME"));
            String FID = rec_kw.getString("FID");
            Record rec_kw_parent = allKw.findEq("KW_ID", FID);
            rec.put("PARENT_KW_NAME", rec_kw_parent.getString("KW_NAME"));
            Record p = GlobalLogics.getUser().getSinglePartnerByNoBase(rec.getString("PARTNER_NO"));
            rec.put("PARTNER_NAME", p.getString("PARTNER_NAME"));
        }
        return recs;
    }

    ///===========webservice 用=============
    public RecordSet webService_getAllOutbound(String KW_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = " AND ORDER_ID IN ";
        filter += " ( ";
        filter += " SELECT ORDER_ID FROM "+ orderTable +" WHERE STATUS>='"+OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE+"' AND STATUS<='"+OrderConstants.ORDER_STATUS_OUTBOUNT_PART+"' AND DELETE_TIME IS NULL  ";
        if (KW_ID.length() > 0 && !KW_ID.equals("999") && !KW_ID.equals("9") && !KW_ID.equals("0"))
            filter += " AND KW_ID IN (SELECT KW_ID FROM " + kwTable + " WHERE FID='" + KW_ID + "') ";
        filter += " )  ";
        String sql = "SELECT * FROM " + orderOutboundTable + " WHERE DELETE_TIME IS NULL ";
        sql+=filter;
        sql += " ORDER BY OUTBOUND_TIME DESC ";
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            rec = formatOrderInbound(rec);
        }
        return recs;
    }

    public boolean webService_printOrderPackage(Context ctx, String PACKAGE_CODE) {
        String sql1 = "UPDATE " + packageTable + " SET PRINT=PRINT+1  WHERE PACKAGE_CODE='" + PACKAGE_CODE + "' ";
        List<String> ls = new ArrayList<String>();
        ls.add(sql1);
        SQLExecutor se = getSqlExecutor();
        long n = se.executeUpdate(ls);
        return n > 0;
    }



    public RecordSet webService_getInboundPackage(String INBOUND_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT o.*,p.PACKAGE_CODE,p.PRINT,p.PRO_DETAIL,p.IN_KW_TIME,p.OUT_KW_TIME FROM " + packageTable + " p INNER JOIN "+ orderTable +" o ON o.ORDER_ID=p.ORDER_ID WHERE 1=1 ";
        sql+=" AND p.IN_KW_TIME='' AND p.OUT_KW_TIME='' ";
        sql+=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+orderInboundTable+" WHERE INBOUND_ID='"+INBOUND_ID+"') ";


        sql += " ORDER BY p.PACKAGE_CODE ";
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            String KW_ID = rec.getString("KW_ID");
            Record rec_kw = allKw.findEq("KW_ID", KW_ID);
            rec.put("KW_NAME", rec_kw.getString("KW_NAME"));
            String FID = rec_kw.getString("FID");
            Record rec_kw_parent = allKw.findEq("KW_ID", FID);
            rec.put("PARENT_KW_NAME", rec_kw_parent.getString("KW_NAME"));
//            Record GYS = GlobalLogics.getUser().getSingleGysBase(rec.getString("GYS_ID"));
//            rec.put("GYS_NAME",GYS.getString("GYS_NAME"));
//            String PARTNER_NO_ = rec.getString("PARTNER_NO");
//            Record partner = GlobalLogics.getUser().getSinglePartnerByNo(PARTNER_NO_);
//            rec.put("PARTNER_NAME",partner.getString("PARTNER_NAME"));
//            String SJ_ID_ =  partner.getString("SJ_ID");
//            Record sj = GlobalLogics.getUser().getSingleSjBase(SJ_ID_);
//            rec.put("SJ_NAME",sj.getString("SJ_NAME"));
        }
        return recs;
    }

    public RecordSet webService_getOutboundPackage(String OUTBOUND_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql = "SELECT o.*,p.PACKAGE_CODE,p.PRINT,p.PRO_DETAIL,p.IN_KW_TIME,p.OUT_KW_TIME FROM " + packageTable + " p INNER JOIN "+ orderTable +" o ON o.ORDER_ID=p.ORDER_ID WHERE 1=1 ";
        sql+=" AND p.IN_KW_TIME!='' AND p.OUT_KW_TIME='' ";
        sql+=" AND p.ORDER_ID IN (SELECT ORDER_ID FROM "+orderOutboundTable+" WHERE OUTBOUND_ID='"+OUTBOUND_ID+"') ";

        sql += " ORDER BY p.PACKAGE_CODE ";
        RecordSet allKw = GlobalLogics.getBaseLogic().getAllKW();
        RecordSet recs = se.executeRecordSet(sql, null);
        for (Record rec : recs) {
            String KW_ID = rec.getString("KW_ID");
            Record rec_kw = allKw.findEq("KW_ID", KW_ID);
            rec.put("KW_NAME", rec_kw.getString("KW_NAME"));
            String FID = rec_kw.getString("FID");
            Record rec_kw_parent = allKw.findEq("KW_ID", FID);
            rec.put("PARENT_KW_NAME", rec_kw_parent.getString("KW_NAME"));
//            Record GYS = GlobalLogics.getUser().getSingleGysBase(rec.getString("GYS_ID"));
//            rec.put("GYS_NAME",GYS.getString("GYS_NAME"));
//            String PARTNER_NO_ = rec.getString("PARTNER_NO");
//            Record partner = GlobalLogics.getUser().getSinglePartnerByNo(PARTNER_NO_);
//            rec.put("PARTNER_NAME",partner.getString("PARTNER_NAME"));
//            String SJ_ID_ =  partner.getString("SJ_ID");
//            Record sj = GlobalLogics.getUser().getSingleSjBase(SJ_ID_);
//            rec.put("SJ_NAME",sj.getString("SJ_NAME"));
        }
        return recs;
    }


    public RecordSet getOrderPackageStatus(String ORDER_ID) {
        SQLExecutor se = read_getSqlExecutor();
        String sql00 ="SELECT * FROM " + orderProductTable + " WHERE ORDER_ID='"+ORDER_ID+"' ";
        RecordSet recs0 = se.executeRecordSet(sql00, null);

        for (Record r0 : recs0){
            //已经装了多少
            String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+packageProductTable+" WHERE ORDER_ID='"+ORDER_ID+"' AND SPEC_ID='"+r0.getString("PRO_SPEC_ID")+"' ";
            Record h =  se.executeRecord(sql2, null);
            int hasCount = h.isEmpty()?0:(int)h.getInt("PRO_COUNT");
            r0.put("HAS_PACKAGE_COUNT",hasCount);
        }
        return recs0;
    }



    public RecordSet getInboundPrintKw(String GYS_ID,String KW_ID, String START_TIME, String END_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = "";

        String sql0 = "SELECT ORDER_ID FROM "+ orderTable +" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_INBOUNT_CREATE+" ";
        filter += " AND KW_ID='"+KW_ID+"' ";
        if (GYS_ID.length() > 0 && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            filter += " AND GYS_ID='" + GYS_ID + "' ";
        if (START_TIME.length()>0)
            filter += " AND INBOUND_TIME >= '"+START_TIME+"' ";
        if (END_TIME.length()>0)
            filter += " AND INBOUND_TIME <= '"+END_TIME+"' ";
        filter += " AND DELETE_TIME IS NULL ";
        sql0+=filter;


        RecordSet allOrders = se.executeRecordSet(sql0);
        String ORDER_IDS = allOrders.joinColumnValues("ORDER_ID", ",");
        if (ORDER_IDS.length() > 0)
            ORDER_IDS = Constants.formatString(ORDER_IDS);
        else
            return new RecordSet();

        RecordSet allSpecPros = GlobalLogics.getBaseLogic().getAllGysProSpec("");
        RecordSet allPartners = GlobalLogics.getUser().getAllUserPartners();

        String sql00 ="SELECT PARTNER_NO,KW_ID,INBOUND_TIME FROM " + orderTable + " WHERE ORDER_ID IN ("+ORDER_IDS+") AND DELETE_TIME IS NULL GROUP BY PARTNER_NO";
        RecordSet recs_partner = se.executeRecordSet(sql00, null);

        Record kw = GlobalLogics.getBaseLogic().getSingleKw(KW_ID);

        for (Record rec : recs_partner){
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
            p0.copyTo(rec);

            rec.put("KW_NAME",kw.getString("KW_NAME"));
            //有多少个门店用的这个商品
            String sql1 = "SELECT DISTINCT(PRO_SPEC_ID) AS PRO_SPEC_ID FROM "+orderProductTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"' AND DELETE_TIME IS NULL)";
            RecordSet recs_spec = se.executeRecordSet(sql1);
            int all = 0;
            for (Record p : recs_spec) {
                String PRO_SPEC_ID = p.getString("PRO_SPEC_ID");
                Record pro = allSpecPros.findEq("SPEC_ID",PRO_SPEC_ID);
                pro.copyTo(p);

                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"') AND DELETE_TIME IS NULL";
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

    public RecordSet getInboundPrintBox(String GYS_ID,String KW_ID,String START_TIME, String END_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        RecordSet allPartners = GlobalLogics.getUser().getAllUserPartners();

        String sql00 = "SELECT DISTINCT(PARTNER_NO) AS PARTNER_NO FROM " + orderTable + " WHERE STATUS>=" + OrderConstants.ORDER_STATUS_INBOUNT_CREATE + " AND INBOUND_TIME <= '" + END_TIME + "' AND INBOUND_TIME >= '" + START_TIME + "' AND KW_ID='" + KW_ID + "' AND DELETE_TIME IS NULL";
        if (GYS_ID.length() > 0 && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            sql00 += " AND GYS_ID='" + GYS_ID + "' ";

        RecordSet recs_partner = se.executeRecordSet(sql00, null);

        Record kw = GlobalLogics.getBaseLogic().getSingleKw(KW_ID);


        for (Record rec : recs_partner){
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
            p0.copyTo(rec);
            rec.put("KW_NAME", kw.getString("KW_NAME"));


            String sql01 = "SELECT p.*,o.KW_ID,o.INBOUND_TIME,o.OUTBOUND_TIME,o.PARTNER_NO FROM " + packageTable + " p INNER JOIN " + orderTable + " o ON o.ORDER_ID=p.ORDER_ID WHERE 1=1 ";
            sql01 += " AND o.PARTNER_NO='" + PARTNER_NO + "' AND o.STATUS>=" + OrderConstants.ORDER_STATUS_INBOUNT_CREATE + " AND o.INBOUND_TIME <= '" + END_TIME + "' AND o.INBOUND_TIME >= '" + START_TIME + "' AND o.KW_ID='" + KW_ID + "' AND o.DELETE_TIME IS NULL ";
            if (GYS_ID.length() > 0 && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
                sql01 += " AND o.GYS_ID='" + GYS_ID + "' ";

            RecordSet allPackage = se.executeRecordSet(sql01, null);

            rec.put("PACKAGES_DATA",allPackage);
            rec.put("PACKAGES_COUNT",allPackage.size());
        }
        return recs_partner;
    }

    public RecordSet getInboundPrintBoxForExcel(String GYS_ID,String KW_ID,String KW_NAME,String START_TIME, String END_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        RecordSet allPartners = GlobalLogics.getUser().getAllUserPartners();

        String sql00 = "SELECT p.*,o.KW_ID,o.INBOUND_TIME,o.OUTBOUND_TIME,o.PARTNER_NO FROM " + packageTable + " p INNER JOIN "+orderTable+" o ON o.ORDER_ID = p.ORDER_ID WHERE o.STATUS>=" + OrderConstants.ORDER_STATUS_INBOUNT_CREATE + " AND o.INBOUND_TIME <= '" + END_TIME + "' AND o.INBOUND_TIME >= '" + START_TIME + "' AND o.KW_ID='" + KW_ID + "' AND o.DELETE_TIME IS NULL";
        if (GYS_ID.length() > 0 && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            sql00 += " AND o.GYS_ID='" + GYS_ID + "' ";
        sql00 +=" ORDER BY o.PARTNER_NO";
        RecordSet recs_partner = se.executeRecordSet(sql00, null);
        for (Record rec : recs_partner){
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
            rec.put("PARTNER_NAME", p0.getString("PARTNER_NAME"));
            rec.put("KW_NAME", KW_NAME);
        }
        return recs_partner;
    }

    public RecordSet getOutboundPrintBoxForExcel(String GYS_ID,String KW_ID,String KW_NAME,String START_TIME, String END_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        RecordSet allPartners = GlobalLogics.getUser().getAllUserPartners();

        String sql00 = "SELECT p.*,o.KW_ID,o.INBOUND_TIME,o.OUTBOUND_TIME,o.PARTNER_NO FROM " + packageTable + " p INNER JOIN "+orderTable+" o ON o.ORDER_ID = p.ORDER_ID WHERE o.STATUS>=" + OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE + " AND o.OUTBOUND_TIME <= '" + END_TIME + "' AND o.OUTBOUND_TIME >= '" + START_TIME + "' AND o.KW_ID='" + KW_ID + "' AND o.DELETE_TIME IS NULL";
        if (GYS_ID.length() > 0 && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            sql00 += " AND o.GYS_ID='" + GYS_ID + "' ";
        sql00 +=" ORDER BY o.PARTNER_NO";
        RecordSet recs_partner = se.executeRecordSet(sql00, null);
        for (Record rec : recs_partner){
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
            rec.put("PARTNER_NAME", p0.getString("PARTNER_NAME"));
            rec.put("KW_NAME", KW_NAME);
        }
        return recs_partner;
    }

    public RecordSet getOutboundPrintKw(String GYS_ID,String KW_ID, String START_TIME, String END_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        String filter = "";

        String sql0 = "SELECT ORDER_ID FROM "+ orderTable +" WHERE DELETE_TIME IS NULL AND STATUS>="+OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE+" ";
        filter += " AND KW_ID='"+KW_ID+"' ";
        if (GYS_ID.length() > 0 && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            filter += " AND GYS_ID='" + GYS_ID + "' ";
        if (START_TIME.length()>0)
            filter += " AND OUTBOUND_TIME >= '"+START_TIME+"' ";
        if (END_TIME.length()>0)
            filter += " AND OUTBOUND_TIME <= '"+END_TIME+"' ";
        filter += " AND DELETE_TIME IS NULL ";
        sql0+=filter;


        RecordSet allOrders = se.executeRecordSet(sql0);
        String ORDER_IDS = allOrders.joinColumnValues("ORDER_ID", ",");
        if (ORDER_IDS.length() > 0)
            ORDER_IDS = Constants.formatString(ORDER_IDS);
        else
            return new RecordSet();

        RecordSet allSpecPros = GlobalLogics.getBaseLogic().getAllGysProSpec("");
        RecordSet allPartners = GlobalLogics.getUser().getAllUserPartners();

        String sql00 ="SELECT PARTNER_NO,KW_ID,OUTBOUND_TIME FROM " + orderTable + " WHERE ORDER_ID IN ("+ORDER_IDS+") AND DELETE_TIME IS NULL GROUP BY PARTNER_NO";
        RecordSet recs_partner = se.executeRecordSet(sql00, null);

        Record kw = GlobalLogics.getBaseLogic().getSingleKw(KW_ID);

        for (Record rec : recs_partner){
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
            p0.copyTo(rec);

            rec.put("KW_NAME",kw.getString("KW_NAME"));
            //有多少个门店用的这个商品
            String sql1 = "SELECT DISTINCT(PRO_SPEC_ID) AS PRO_SPEC_ID FROM "+orderProductTable+" WHERE ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"' AND DELETE_TIME IS NULL)";
            RecordSet recs_spec = se.executeRecordSet(sql1);
            int all = 0;
            for (Record p : recs_spec) {
                String PRO_SPEC_ID = p.getString("PRO_SPEC_ID");
                Record pro = allSpecPros.findEq("SPEC_ID",PRO_SPEC_ID);
                pro.copyTo(p);

                String sql2 = "SELECT SUM(PRO_COUNT) AS PRO_COUNT FROM "+orderProductTable+" WHERE PRO_SPEC_ID='"+PRO_SPEC_ID+"' AND ORDER_ID IN ("+ORDER_IDS+") AND ORDER_ID IN (SELECT ORDER_ID FROM "+ orderTable +" WHERE ORDER_ID IN ("+ORDER_IDS+") AND PARTNER_NO='"+PARTNER_NO+"') AND DELETE_TIME IS NULL";
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

    public RecordSet getOutboundPrintBox(String GYS_ID,String KW_ID,String START_TIME, String END_TIME) {
        SQLExecutor se = read_getSqlExecutor();
        RecordSet allPartners = GlobalLogics.getUser().getAllUserPartners();

        String sql00 = "SELECT DISTINCT(PARTNER_NO) AS PARTNER_NO FROM " + orderTable + " WHERE STATUS>=" + OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE + " AND OUTBOUND_TIME <= '" + END_TIME + "' AND OUTBOUND_TIME >= '" + START_TIME + "' AND KW_ID='" + KW_ID + "' AND DELETE_TIME IS NULL";
        if (GYS_ID.length() > 0 && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
            sql00 += " AND GYS_ID='" + GYS_ID + "' ";

        RecordSet recs_partner = se.executeRecordSet(sql00, null);

        Record kw = GlobalLogics.getBaseLogic().getSingleKw(KW_ID);


        for (Record rec : recs_partner){
            String PARTNER_NO = rec.getString("PARTNER_NO");
            Record p0 = allPartners.findEq("PARTNER_NO", PARTNER_NO);
            p0.copyTo(rec);
            rec.put("KW_NAME", kw.getString("KW_NAME"));


            String sql01 = "SELECT p.*,o.KW_ID,o.INBOUND_TIME,o.OUTBOUND_TIME,o.PARTNER_NO FROM " + packageTable + " p INNER JOIN " + orderTable + " o ON o.ORDER_ID=p.ORDER_ID WHERE 1=1 ";
            sql01 += " AND o.PARTNER_NO='" + PARTNER_NO + "' AND o.STATUS>=" + OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE + " AND o.OUTBOUND_TIME <= '" + END_TIME + "' AND o.OUTBOUND_TIME >= '" + START_TIME + "' AND o.KW_ID='" + KW_ID + "' AND o.DELETE_TIME IS NULL ";
            if (GYS_ID.length() > 0 && !GYS_ID.equals("9") && !GYS_ID.equals("999"))
                sql01 += " AND o.GYS_ID='" + GYS_ID + "' ";

            RecordSet allPackage = se.executeRecordSet(sql01, null);

            rec.put("PACKAGES_DATA",allPackage);
            rec.put("PACKAGES_COUNT",allPackage.size());
        }
        return recs_partner;
    }


    public boolean deleteAllOrderImport(String GYS_ID,String USER_ID){
        SQLExecutor se = getSqlExecutor();
        String sql = "DELETE FROM "+orderImportTable+" WHERE GYS_ID='"+GYS_ID+"' AND USER_ID='"+USER_ID+"' ";
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public boolean saveOrderImport(String IMPORT_ID,String GYS_ID,String USER_ID,String OUT_ORDER_ID,String PARTNER_NAME,String PARTNER_NO,String SPEC_ID,String PRO_NAME,String PRO_SPEC,int PRO_COUNT,String INBOUND_TIME,String JH_TIME,String ERR_STR,String PRO_CODE){
        SQLExecutor se = getSqlExecutor();
        String sql = "INSERT INTO "+orderImportTable+" (IMPORT_ID,GYS_ID, USER_ID, OUT_ORDER_ID, PARTNER_NAME,PARTNER_NO, SPEC_ID, PRO_NAME, PRO_SPEC, PRO_COUNT, INBOUND_TIME, JH_TIME, ERR_STR,CREATE_TIME,PRO_CODE) ";
        sql+=" VALUES ('"+IMPORT_ID+"','"+GYS_ID+"','"+USER_ID+"','"+OUT_ORDER_ID+"','"+PARTNER_NAME+"','"+PARTNER_NO+"','"+SPEC_ID+"','"+PRO_NAME+"','"+PRO_SPEC+"','"+PRO_COUNT+"','"+INBOUND_TIME+"','"+JH_TIME+"','"+ERR_STR+"','"+DateUtils.now()+"','"+PRO_CODE+"')";
        long n = se.executeUpdate(sql);
        return n>0;
    }

    public RecordSet getAllImportsByIDS(String IMPORT_IDS) {
        SQLExecutor se = getSqlExecutor();
        String sql = "SELECT * FROM " + orderImportTable + " WHERE IMPORT_ID IN (" + Constants.formatString(IMPORT_IDS) + ")";
        RecordSet recs = se.executeRecordSet(sql);
        return recs;
    }

    public void test(String outbound_id,String package_code){
        String t = "";
        Record package_single  = GlobalLogics.getOrderLogic().getSinglePackage(package_code);
        String order_id = package_single.getString("ORDER_ID");
        Record order = GlobalLogics.getOrderLogic().getSingleOrderBase(order_id);
        Record outb = GlobalLogics.getOrderLogic().getSingleOutboundBase(outbound_id);
        if (!order.getString("KW_ID").equals(outb.getString("KW_ID"))) {
            t = "123";
        }
    }

}

