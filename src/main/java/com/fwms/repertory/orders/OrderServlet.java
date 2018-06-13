package com.fwms.repertory.orders;

import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.util.RandomUtils;
import com.fwms.basedevss.base.util.StringUtils2;
import com.fwms.basedevss.base.web.QueryParams;
import com.fwms.basedevss.base.web.webmethod.WebMethod;
import com.fwms.basedevss.base.web.webmethod.WebMethodServlet;
import com.fwms.common.Constants;
import com.fwms.common.GlobalLogics;
import com.fwms.common.PortalContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class OrderServlet extends WebMethodServlet {
    @Override
    public void init() throws ServletException {
        Configuration conf = GlobalConfig.get();
        super.init();
    }

    @WebMethod("order/gys_order_get_all_page_list")
    public Record gys_order_get_all_page_list(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String GYS_ID = qp.checkGetString("GYS_ID");
        String WL_TYPE = qp.getString("WL_TYPE", "");
        int PAY_DONE = (int)qp.getInt("PAY_DONE", 9);
        String START_TIME = qp.getString("START_TIME", "");
        String END_TIME = qp.getString("END_TIME", "");
        String STATE = qp.getString("STATE", "0,1,2,9");
        String ORDER_ID = qp.getString("ORDER_ID", "");
        String OUT_ORDER_ID = qp.getString("OUT_ORDER_ID", "");
        String INBOUND_STATUS_BEGIN = qp.getString("INBOUND_STATUS_BEGIN", "");
        String INBOUND_STATUS_END = qp.getString("INBOUND_STATUS_END", "");

        try {
            if (!START_TIME.equals("")) {
                START_TIME = START_TIME + " 00:00:00";
            }
            if (!END_TIME.equals("")) {
                END_TIME = END_TIME + " 23:59:59";
            }
        } catch (Exception e) {
        }
        String p = qp.getString("PAGE", "");
        int PAGE = 0;
        if (!p.equals(""))
            PAGE = (int) qp.getInt("PAGE", 0);

        String c = qp.getString("COUNT", "");
        int COUNT = 0;
        if (!c.equals("")) {
            COUNT = (int) qp.getInt("COUNT", 20);
        } else {
            COUNT = 20;
        }
        Record data = GlobalLogics.getOrderLogic().getAllGysOrderPageList(ctx, WL_TYPE,"999", GYS_ID, START_TIME, END_TIME, STATE, PAY_DONE, PAGE, COUNT, ORDER_ID,OUT_ORDER_ID, INBOUND_STATUS_BEGIN, INBOUND_STATUS_END);

        return data;
    }

    @WebMethod("order/order_get_all_page_list")
    public Record order_get_all_page_list(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.getString("SJ_ID", "");
        String GYS_ID = qp.getString("GYS_ID", "");
        String WL_TYPE = qp.getString("WL_TYPE", "");
        int PAY_DONE = (int)qp.getInt("PAY_DONE", 9);
        String START_TIME = qp.getString("START_TIME", "");
        String END_TIME = qp.getString("END_TIME", "");
        String STATE = qp.getString("STATE", "0,1,2,9");
        String ORDER_ID = qp.getString("ORDER_ID", "");
        String OUT_ORDER_ID = qp.getString("OUT_ORDER_ID", "");
        String INBOUND_STATUS_BEGIN = qp.getString("INBOUND_STATUS_BEGIN", "");
        String INBOUND_STATUS_END = qp.getString("INBOUND_STATUS_END", "");

        try {
            if (!START_TIME.equals("")) {
                START_TIME = START_TIME + " 00:00:00";
            }
            if (!END_TIME.equals("")) {
                END_TIME = END_TIME + " 23:59:59";
            }
        } catch (Exception e) {
        }
        String p = qp.getString("PAGE", "");
        int PAGE = 0;
        if (!p.equals(""))
            PAGE = (int) qp.getInt("PAGE", 0);

        String c = qp.getString("COUNT", "");
        int COUNT = 0;
        if (!c.equals("")) {
            COUNT = (int) qp.getInt("COUNT", 20);
        } else {
            COUNT = 20;
        }
        Record data = GlobalLogics.getOrderLogic().getAllGysOrderPageList(ctx,  WL_TYPE, SJ_ID,GYS_ID, START_TIME, END_TIME, STATE, PAY_DONE, PAGE, COUNT, ORDER_ID,OUT_ORDER_ID, INBOUND_STATUS_BEGIN, INBOUND_STATUS_END);

        return data;
    }

    @WebMethod("order/order_delete")
    public Record order_delete(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record rec = new Record();

        String ORDER_ID = qp.checkGetString("ORDER_ID");

//        List<StockApplyBO> list = applyService.getBySourceCode(ORDER_ID);
//        if (list.size()>0) {
//            return BackResult.error("该采购订单已经生成过通知单，请先删除通知单！");
//        }
        boolean b = GlobalLogics.getOrderLogic().deleteOrder(ctx, ORDER_ID);
        if (b){
            rec.put("STATUS",1);
            rec.put("MESSAGE","删除成功!");
        }
        return rec;
    }

    @WebMethod("order/update_state")
    public boolean update_state(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID");
        int STATE = (int) qp.getInt("STATE", 1);
        boolean b = GlobalLogics.getOrderLogic().updateOrderState(ORDER_ID, STATE);
        return b;
    }

    @WebMethod("order/get_single")
    public Record get_single(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID");
        Record rec = GlobalLogics.getOrderLogic().getSingleOrder(ORDER_ID);
        return rec;
    }

    @WebMethod("order/get_single_package")
    public Record get_single_package(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID");
        Record rec = GlobalLogics.getOrderLogic().getSingleOrderForPackage(ORDER_ID);
        return rec;
    }
    @WebMethod("order/get_single_order_print")
    public Record get_single_order_print(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID");
        Record rec = GlobalLogics.getOrderLogic().getSingleOrderPrint(ORDER_ID);
        return rec;
    }
    @WebMethod("order/verify_order")
    public boolean verify_order(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID");
        boolean b = GlobalLogics.getOrderLogic().updateOrderVerify(ORDER_ID, ctx.getUser_id());
        return b;
    }

    @WebMethod("order/package_delete")
    public boolean package_delete(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String PACKAGE_CODE = qp.checkGetString("PACKAGE_CODE");
        boolean b = GlobalLogics.getOrderLogic().deletePackageCode(PACKAGE_CODE);
        return b;
    }

    @WebMethod("order/print_order_inbound")
    public void print_order_inbound(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID");
        GlobalLogics.getOrderLogic().printOrderInbound(ORDER_ID);
    }

    @WebMethod("order/order_create")
    public boolean createOrder(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String ORDER_ID = Constants.newCgCode(); //用新方式生成采购单ID，废用采购订单外部编号
        String GYS_ID = qp.checkGetString("GYS_ID");
        String PARTNER_NO = qp.checkGetString("PARTNER_NO");
        String SJ_ID = qp.getString("SJ_ID", "");
        if (SJ_ID.length() <= 0)
            SJ_ID = GlobalLogics.getUser().getSinglePartnerByNo(PARTNER_NO).getString("SJ_ID");

        Record partner_single = GlobalLogics.getUser().getSinglePartnerByNo(PARTNER_NO);

        Record partnerKw = GlobalLogics.getUser().getPartnerKw(PARTNER_NO);
        String KW_ID = "";
        if (!partnerKw.isEmpty())
            KW_ID = partnerKw.getString("KW_ID");
        String OUT_ORDER_ID = qp.getString("OUT_ORDER_ID", "");

        Record gysRecord = GlobalLogics.getUser().getSingleGysBase(GYS_ID);
        String GYS_NAME = gysRecord.getString("GYS_NAME");
          String USER_ID = gysRecord.getString("USER_ID");
        String MEMO = qp.getString("MEMO", "");

        String JH_TIME = qp.getString("JH_TIME", "");
        String JH_TYPE = qp.getString("JH_TYPE", "");
        String JH_ADDR = qp.getString("JH_ADDR", "");

        String PRO_VALUES = qp.checkGetString("PRO_VALUES");
        List<String> ls_p = StringUtils2.splitList(PRO_VALUES, ",", true);

        boolean b = GlobalLogics.getOrderLogic().saveGysOrder(ctx, USER_ID, SJ_ID, ORDER_ID, OUT_ORDER_ID, GYS_ID, GYS_NAME, "0", "0", "1", MEMO, JH_TIME, JH_TYPE, JH_ADDR, "0", "0", "0", "0", 0, OrderConstants.ORDER_STATUS_DEFAULT, PARTNER_NO, KW_ID,
                partner_single.getString("PROVINCE"),partner_single.getString("CITY"),partner_single.getString("AREA"),partner_single.getString("ADDR"),partner_single.getString("PROVINCE_NAME")+partner_single.getString("CITY_NAME")+partner_single.getString("AREA_NAME")+partner_single.getString("ADDR"),partner_single.getString("CONTACT"),partner_single.getString("MOBILE"));
        if (b) {
            for (String pro_str : ls_p) {
                List<String> lp = StringUtils2.splitList(pro_str, "@", true);
                if (lp.size() < 2)
                    break;
                BigDecimal PRO_COUNT = new BigDecimal(lp.get(1));

                Record pro_spec = GlobalLogics.getBaseLogic().getSingleProSpec(lp.get(0));
                String pro_id = pro_spec.getString("PRO_ID");
                Record pro = GlobalLogics.getBaseLogic().getSinglePro(pro_id);

                String PRO_TYPE = pro.getString("PRO_TYPE");
                String PRO_TYPE_ID = pro.getString("PRO_TYPE_ID");
                String PRO_NAME = pro_spec.getString("PRO_NAME");


                String PRO_CODE_NUMBER = pro_spec.getString("PRO_CODE");
                String ORDER_ITEM_ID = RandomUtils.generateStrId();

                BigDecimal single_PRICE0 =  new BigDecimal(pro_spec.getString("PRO_PRICE"));

                boolean c = GlobalLogics.getOrderLogic().saveGysOrderPro(ctx, ORDER_ID,pro_id, lp.get(0),
                        PRO_COUNT+"" , pro_spec.getString("PRO_PRICE")+"", "0",
                        PRO_CODE_NUMBER,"0",PRO_TYPE, PRO_TYPE_ID,PRO_NAME,PRO_COUNT.multiply(single_PRICE0)+"" , ORDER_ITEM_ID, 0+"", 0+"", PRO_COUNT.multiply(new BigDecimal(0) )+"");
            }
        }
        return true;
    }
    @WebMethod("order/order_update")
    public boolean order_update(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID"); //用新方式生成采购单ID，废用采购订单外部编号
        String PARTNER_NO = qp.checkGetString("PARTNER_NO");
        Record partner_single = GlobalLogics.getUser().getSinglePartnerByNo(PARTNER_NO);

        String OUT_ORDER_ID = qp.getString("OUT_ORDER_ID", "");
        String MEMO = qp.getString("MEMO", "");

        String JH_TIME = qp.getString("JH_TIME", "");
        String JH_TYPE = qp.getString("JH_TYPE", "");
        String JH_ADDR = qp.getString("JH_ADDR", "");

        String PRO_VALUES = qp.checkGetString("PRO_VALUES");
        List<String> ls_p = StringUtils2.splitList(PRO_VALUES, ",", true);

        boolean b = GlobalLogics.getOrderLogic().updateGysOrder(ctx, ORDER_ID, OUT_ORDER_ID, "0", "0", "1", MEMO, JH_TIME, JH_TYPE, JH_ADDR, "0", "0", "0", "0", PARTNER_NO, partner_single.getString("PROVINCE"), partner_single.getString("CITY"), partner_single.getString("AREA"), partner_single.getString("ADDR"), partner_single.getString("PROVINCE_NAME") + partner_single.getString("CITY_NAME") + partner_single.getString("AREA_NAME") + partner_single.getString("ADDR"), partner_single.getString("CONTACT"), partner_single.getString("MOBILE"));
        if (b) {
            b = GlobalLogics.getOrderLogic().deleteGysOrderProducts(ORDER_ID);
            if (b){
                for (String pro_str : ls_p) {
                    List<String> lp = StringUtils2.splitList(pro_str, "@", true);
                    if (lp.size() < 2)
                        break;
                    BigDecimal PRO_COUNT = new BigDecimal(lp.get(1));

                    Record pro_spec = GlobalLogics.getBaseLogic().getSingleProSpec(lp.get(0));
                    String pro_id = pro_spec.getString("PRO_ID");
                    Record pro = GlobalLogics.getBaseLogic().getSinglePro(pro_id);

                    String PRO_TYPE = pro.getString("PRO_TYPE");
                    String PRO_TYPE_ID = pro.getString("PRO_TYPE_ID");
                    String PRO_NAME = pro_spec.getString("PRO_NAME");


                    String PRO_CODE_NUMBER = pro_spec.getString("PRO_CODE");
                    String ORDER_ITEM_ID = RandomUtils.generateStrId();

                    BigDecimal single_PRICE0 =  new BigDecimal(pro_spec.getString("PRO_PRICE"));


                    boolean c = GlobalLogics.getOrderLogic().saveGysOrderPro(ctx, ORDER_ID,pro_id, lp.get(0),
                            PRO_COUNT+"" , pro_spec.getString("PRO_PRICE")+"", "0",
                            PRO_CODE_NUMBER,"0",PRO_TYPE, PRO_TYPE_ID,PRO_NAME,PRO_COUNT.multiply(single_PRICE0)+"" , ORDER_ITEM_ID, 0+"", 0+"", PRO_COUNT.multiply(new BigDecimal(0) )+"");
                }
            }

        }
        return true;
    }


    @WebMethod("order/create_inbound_notify")
    public Record create_inbound_notify(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record out_rec = new Record();
        String ORDER_ID = qp.checkGetString("ORDER_ID");

        Record order = GlobalLogics.getOrderLogic().getSingleOrderBase(ORDER_ID) ;
        String INBOUND_TIME = qp.getString("INBOUND_TIME", order.getString("JH_TIME"));
        String INBOUND_ID = Constants.newInboundCode();
        boolean b = GlobalLogics.getOrderLogic().saveInbound(ctx, INBOUND_ID, ORDER_ID, order.getString("KW_ID"), order.getString("GYS_ID"), GlobalLogics.getUser().getSingleGysBase(order.getString("GYS_ID")).getString("GYS_NAME_SX"), INBOUND_TIME);
        if (!b){
            out_rec.put("status",0);
            out_rec.put("message","入库通知单产生失败");
            return out_rec;
        }else{
            //更新订单状态
            GlobalLogics.getOrderLogic().updateOrderStatusInbound(ctx, ORDER_ID, OrderConstants.ORDER_STATUS_INBOUNT_CREATE, INBOUND_TIME);
            out_rec.put("status",1);
            out_rec.put("message","入库通知单产生成功");
            return out_rec;
        }
    }
    @WebMethod("order/create_outbound_notify")
    public Record create_outbound_notify(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record out_rec = new Record();
        String ORDER_ID = qp.checkGetString("ORDER_ID");

        Record order = GlobalLogics.getOrderLogic().getSingleOrderBase(ORDER_ID) ;
        String OUTBOUND_TIME = qp.getString("OUTBOUND_TIME", order.getString("JH_TIME"));
        String OUTBOUND_ID = Constants.newOutboundCode();
        boolean b = GlobalLogics.getOrderLogic().saveOutbound(ctx, OUTBOUND_ID, ORDER_ID, order.getString("KW_ID"), order.getString("GYS_ID"), GlobalLogics.getUser().getSingleGysBase(order.getString("GYS_ID")).getString("GYS_NAME_SX"), OUTBOUND_TIME);
        if (!b){
            out_rec.put("status",0);
            out_rec.put("message","出库通知单产生失败");
            return out_rec;
        }else{
            //更新订单状态
            GlobalLogics.getOrderLogic().updateOrderStatusOutbound(ctx, ORDER_ID, OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE, OUTBOUND_TIME);
            out_rec.put("status",1);
            out_rec.put("message","出库通知单产生成功");
            return out_rec;
        }
    }
    @WebMethod("order/create_package")
    public Record create_package(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        Record out_rec = new Record();

        String ORDER_ID = qp.checkGetString("ORDER_ID"); //用新方式生成采购单ID，废用采购订单外部编号
        String PRO_VALUES = qp.checkGetString("PRO_VALUES");
        int PACKAGE_COUNT = (int)qp.checkGetInt("PACKAGE_COUNT");
        //如果是1个箱子,那么所有的商品都放在一个箱子内
        //如果是多个箱子,就按照箱子数量,来除,每个箱子的内容
        List<String> ls_p = StringUtils2.splitList(PRO_VALUES, ",", true);
        if (PACKAGE_COUNT == 1) {
            String newPackageCode = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
            boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode);
            if (b) {
                for (String pro_str : ls_p) {
                    List<String> lp = StringUtils2.splitList(pro_str, "@", true);
                    if (lp.size() < 2)
                        break;
                    Record pro_spec = GlobalLogics.getBaseLogic().getSingleProSpec(lp.get(0));
                    boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                            newPackageCode, lp.get(0), pro_spec.getString("PRO_NAME"), Integer.parseInt(lp.get(1)));
                    if (!c) {
                        b = false;
                        break;
                    }
                }
                if (!b){
                    GlobalLogics.getOrderLogic().deletePackageCode(newPackageCode);
                    out_rec.put("status",0);
                    out_rec.put("message","保存装箱产品数据失败");
                    return out_rec;
                }else{
                    out_rec.put("status",1);
                    out_rec.put("message","保存装箱产品数据成功");
                    return out_rec;
                }
            }else{
                out_rec.put("status",0);
                out_rec.put("message","保存装箱数据失败");
                return out_rec;
            }
        } else {
            if (ls_p.size() > 1) {
                out_rec.put("status",0);
                out_rec.put("message","多个箱子的,里面只能装一种货品");
                return out_rec;
            }
            List<String> lp = StringUtils2.splitList(ls_p.get(0), "@", true);
            int allCount = Integer.parseInt(lp.get(1));
            if (PACKAGE_COUNT>allCount) {
                out_rec.put("status",0);
                out_rec.put("message","箱子数量比货品数量还多,无法装箱");
                return out_rec;
            }

            Record pro_spec = GlobalLogics.getBaseLogic().getSingleProSpec(lp.get(0));
            if (allCount % PACKAGE_COUNT==0){   //整除了,可以按照整除来装
                int singleProCount = (int)(allCount / PACKAGE_COUNT);
                for (int i=1;i<=PACKAGE_COUNT;i++){
                    String newPackageCode = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
                    boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode);
                    if (b){
                        boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                                newPackageCode, lp.get(0), pro_spec.getString("PRO_NAME"), singleProCount);
                    }
                }
            } else{   //无法整除
                double intSingleProCount = Math.ceil(Double.parseDouble(String.valueOf(allCount)) / Double.parseDouble(String.valueOf(PACKAGE_COUNT)));
                //前面几个箱子,按照最大的来装,
                for (int i=1;i<PACKAGE_COUNT;i++){
                    String newPackageCode = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
                    boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode);
                    if (b){
                        boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                                newPackageCode, lp.get(0), pro_spec.getString("PRO_NAME"), (int)intSingleProCount);
                    }
                }
                //最后一个箱子,按照总数相减的来装
                int lessCount = allCount- (PACKAGE_COUNT-1)*(int)intSingleProCount;
                String newPackageCode1 = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
                boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode1);
                if (b){
                    boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                            newPackageCode1, lp.get(0), pro_spec.getString("PRO_NAME"), lessCount);
                }
            }
        }
        out_rec.put("status",1);
        out_rec.put("message","装箱完毕");
        return out_rec;
    }


    @WebMethod("order/report_gys_order_daily")
    public RecordSet report_gys_order_daily(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.getString("GYS_ID","999");
        String SJ_ID = qp.getString("SJ_ID", "999");
        String START_TIME = qp.checkGetString("START_TIME");
        String END_TIME = qp.checkGetString("END_TIME");
        RecordSet recs  = GlobalLogics.getOrderLogic().getGysOrderDailyReport(SJ_ID, GYS_ID, START_TIME, END_TIME);
        return recs;
    }

    @WebMethod("order/report_gys_order_daily1")
    public RecordSet report_gys_order_daily1(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.getString("GYS_ID", "999");
        String SJ_ID = qp.getString("SJ_ID", "999");
        String START_TIME = qp.checkGetString("START_TIME");
        String END_TIME = qp.checkGetString("END_TIME");
        RecordSet recs  = GlobalLogics.getOrderLogic().getGysOrderDailyReportDH(SJ_ID, GYS_ID, START_TIME, END_TIME);
        return recs;
    }

    @WebMethod("order/report_gys_order_daily2")
    public RecordSet report_gys_order_daily2(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.getString("GYS_ID","999");
        String SJ_ID = qp.getString("SJ_ID", "999");
        String START_TIME = qp.checkGetString("START_TIME");
        String END_TIME = qp.checkGetString("END_TIME");
        RecordSet recs  = GlobalLogics.getOrderLogic().getGysOrderDailyReport2(SJ_ID, GYS_ID, START_TIME, END_TIME);
        return recs;
    }

    @WebMethod("order/sj_order_get_all_page_list")
    public Record sj_order_get_all_page_list(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String SJ_ID = qp.checkGetString("SJ_ID");
        String GYS_ID = qp.getString("GYS_ID", "999");
        String WL_TYPE = qp.getString("WL_TYPE", "");
        int PAY_DONE = (int)qp.getInt("PAY_DONE", 9);
        String START_TIME = qp.getString("START_TIME", "");
        String END_TIME = qp.getString("END_TIME", "");
        String STATE = qp.getString("STATE", "0,1,2,9");
        String ORDER_ID = qp.getString("ORDER_ID", "");
        String OUT_ORDER_ID = qp.getString("OUT_ORDER_ID", "");
        String INBOUND_STATUS_BEGIN = qp.getString("INBOUND_STATUS_BEGIN", "");
        String INBOUND_STATUS_END = qp.getString("INBOUND_STATUS_END", "");

        try {
            if (!START_TIME.equals("")) {
                START_TIME = START_TIME + " 00:00:00";
            }
            if (!END_TIME.equals("")) {
                END_TIME = END_TIME + " 23:59:59";
            }
        } catch (Exception e) {
        }
        String p = qp.getString("PAGE", "");
        int PAGE = 0;
        if (!p.equals(""))
            PAGE = (int) qp.getInt("PAGE", 0);

        String c = qp.getString("COUNT", "");
        int COUNT = 0;
        if (!c.equals("")) {
            COUNT = (int) qp.getInt("COUNT", 20);
        } else {
            COUNT = 20;
        }
        Record data = GlobalLogics.getOrderLogic().getAllGysOrderPageList(ctx, WL_TYPE, SJ_ID,GYS_ID, START_TIME, END_TIME, STATE, PAY_DONE, PAGE, COUNT, ORDER_ID,OUT_ORDER_ID, INBOUND_STATUS_BEGIN, INBOUND_STATUS_END);

        return data;
    }

    @WebMethod("order/inbound_get_all_page_list")
    public Record inbound_get_all_page_list(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String GYS_ID = qp.getString("GYS_ID", "999");
        String SJ_ID = qp.getString("SJ_ID", "999");
        String START_TIME = qp.getString("START_TIME", "");
        String END_TIME = qp.getString("END_TIME", "");
        String ORDER_ID = qp.getString("ORDER_ID", "");
        int STATUS = (int)qp.getInt("STATUS", 999);

        String p = qp.getString("PAGE", "");
        int PAGE = 0;
        if (!p.equals(""))
            PAGE = (int) qp.getInt("PAGE", 0);

        String c = qp.getString("COUNT", "");
        int COUNT = 0;
        if (!c.equals("")) {
            COUNT = (int) qp.getInt("COUNT", 20);
        } else {
            COUNT = 20;
        }
        Record data = GlobalLogics.getOrderLogic().getAllInboundPageList(ctx, SJ_ID, GYS_ID, START_TIME, END_TIME, PAGE, COUNT, ORDER_ID, STATUS);

        return data;
    }

    @WebMethod("order/outbound_get_all_page_list")
    public Record outbound_get_all_page_list(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String GYS_ID = qp.getString("GYS_ID", "999");
        String SJ_ID = qp.getString("SJ_ID", "999");
        String START_TIME = qp.getString("START_TIME", "");
        String END_TIME = qp.getString("END_TIME", "");
        String ORDER_ID = qp.getString("ORDER_ID", "");
        int STATUS = (int)qp.getInt("STATUS", 999);

        String p = qp.getString("PAGE", "");
        int PAGE = 0;
        if (!p.equals(""))
            PAGE = (int) qp.getInt("PAGE", 0);

        String c = qp.getString("COUNT", "");
        int COUNT = 0;
        if (!c.equals("")) {
            COUNT = (int) qp.getInt("COUNT", 20);
        } else {
            COUNT = 20;
        }
        Record data = GlobalLogics.getOrderLogic().getAllOutboundPageList(ctx, SJ_ID, GYS_ID, START_TIME, END_TIME, PAGE, COUNT, ORDER_ID, STATUS);

        return data;
    }

    @WebMethod("order/get_single_inbound")
    public Record get_single_inbound(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String ORDER_ID = qp.getString("ORDER_ID", "");
        String INBOUND_ID = qp.getString("INBOUND_ID", "");
        Record rec  = GlobalLogics.getOrderLogic().getSingleInbound(ORDER_ID, INBOUND_ID);
        return rec;
    }

    @WebMethod("order/create_inbound")
    public boolean create_inbound(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String PACKAGE_CODES = qp.checkGetString("PACKAGE_CODES");
        List<String> ls = StringUtils2.splitList(PACKAGE_CODES, ",", true);
        for (String l : ls) {
            //1,更新
            GlobalLogics.getOrderLogic().confirmInbound(ctx, l);
        }
        return true;
    }

    @WebMethod("order/get_single_outbound")
    public Record get_single_outbound(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String ORDER_ID = qp.getString("ORDER_ID", "");
        String OUTBOUND_ID = qp.getString("OUTBOUND_ID", "");
        Record rec  = GlobalLogics.getOrderLogic().getSingleOutbound(ORDER_ID, OUTBOUND_ID);
        return rec;
    }

    @WebMethod("order/create_outbound")
    public boolean create_outbound(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String PACKAGE_CODES = qp.checkGetString("PACKAGE_CODES");
        List<String> ls = StringUtils2.splitList(PACKAGE_CODES, ",", true);
        for (String l : ls) {
            //1,更新
            GlobalLogics.getOrderLogic().confirmOutbound(ctx, l);
        }
        return true;
    }

    @WebMethod("order/get_now_repo_daily")
    public RecordSet get_now_repo_daily(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.getString("SJ_ID", "999");
        String GYS_ID = qp.getString("GYS_ID", "999");
        String F_KW_ID = qp.getString("F_KW_ID", "999");
        String KW_ID = qp.getString("KW_ID", "999");
        RecordSet recs  = GlobalLogics.getOrderLogic().getNowRepoPackage(SJ_ID, GYS_ID, F_KW_ID, KW_ID);
        return recs;
    }

    @WebMethod("order/get_now_repo_goods")
    public RecordSet get_now_repo_goods(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.getString("SJ_ID", "999");
        String GYS_ID = qp.getString("GYS_ID", "999");
        String F_KW_ID = qp.getString("F_KW_ID", "999");
        String KW_ID = qp.getString("KW_ID", "999");
        RecordSet recs  = GlobalLogics.getOrderLogic().getGysOrderDailyGoods(SJ_ID, GYS_ID, F_KW_ID, KW_ID);
        return recs;
    }

    //获取该供应商全部需打印面单的记录
    @WebMethod("order/inbound_md_get_all_page_list")
    public RecordSet inbound_md_get_all_page_list(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String GYS_ID = qp.getString("GYS_ID", "999");
        String SJ_ID = qp.getString("SJ_ID", "999");
        String PARTNER_NO = qp.getString("PARTNER_NO", "999");
        int PRINTED = (int)qp.getInt("PRINTED",999);
        RecordSet data = GlobalLogics.getOrderLogic().getAllCanPrintMd(ctx, GYS_ID,PRINTED,SJ_ID,PARTNER_NO);

        return data;
    }
}

