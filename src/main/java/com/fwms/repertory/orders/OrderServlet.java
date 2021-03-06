package com.fwms.repertory.orders;

import com.fwms.basedevss.ServerException;
import com.fwms.basedevss.base.BaseErrors;
import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.excel.InnovExcel;
import com.fwms.basedevss.base.sfs.StaticFileStorage;
import com.fwms.basedevss.base.util.ClassUtils2;
import com.fwms.basedevss.base.util.DateUtils;
import com.fwms.basedevss.base.util.RandomUtils;
import com.fwms.basedevss.base.util.StringUtils2;
import com.fwms.basedevss.base.web.QueryParams;
import com.fwms.basedevss.base.web.webmethod.WebMethod;
import com.fwms.basedevss.base.web.webmethod.WebMethodServlet;
import com.fwms.common.Constants;
import com.fwms.common.GlobalLogics;
import com.fwms.common.PortalContext;

import com.fwms.common.TimeUtils;
import com.fwms.webservice.OrderServiceImpl;
import com.fwms.webservice.entity.WMS_WEBSERVICE_RESULT;
import com.microsoft.schemas.office.visio.x2012.main.CellType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.solr.common.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
        String PARTNER_NO = qp.getString("PARTNER_NO", "999");
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
        Record data = GlobalLogics.getOrderLogic().getAllGysOrderPageList(ctx, PARTNER_NO,WL_TYPE,"999", GYS_ID, START_TIME, END_TIME, STATE, PAY_DONE, PAGE, COUNT, ORDER_ID,OUT_ORDER_ID, INBOUND_STATUS_BEGIN, INBOUND_STATUS_END);

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
        String PARTNER_NO = qp.getString("PARTNER_NO", "999");
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
        Record data = GlobalLogics.getOrderLogic().getAllGysOrderPageList(ctx, PARTNER_NO, WL_TYPE, SJ_ID,GYS_ID, START_TIME, END_TIME, STATE, PAY_DONE, PAGE, COUNT, ORDER_ID,OUT_ORDER_ID, INBOUND_STATUS_BEGIN, INBOUND_STATUS_END);

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
        String INBOUND_TIME = qp.getString("INBOUND_TIME", "");
        String JH_TYPE = qp.getString("JH_TYPE", "");
        String JH_ADDR = qp.getString("JH_ADDR", "");

        String PRO_VALUES = qp.checkGetString("PRO_VALUES");
        List<String> ls_p = StringUtils2.splitList(PRO_VALUES, ",", true);

        boolean b = GlobalLogics.getOrderLogic().saveGysOrderImport(ctx, USER_ID, SJ_ID, ORDER_ID, OUT_ORDER_ID, GYS_ID, GYS_NAME, "0", "0", "1", MEMO, JH_TIME, JH_TYPE, JH_ADDR, INBOUND_TIME,JH_TIME,"0", "0", "0", "0", 0, OrderConstants.ORDER_STATUS_DEFAULT, PARTNER_NO, KW_ID,
                partner_single.getString("PROVINCE"), partner_single.getString("CITY"), partner_single.getString("AREA"), partner_single.getString("ADDR"), partner_single.getString("PROVINCE_NAME") + partner_single.getString("CITY_NAME") + partner_single.getString("AREA_NAME") + partner_single.getString("ADDR"), partner_single.getString("CONTACT"), partner_single.getString("MOBILE"));
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
        String INBOUND_TIME = qp.getString("INBOUND_TIME", "");
        String JH_TYPE = qp.getString("JH_TYPE", "");
        String JH_ADDR = qp.getString("JH_ADDR", "");

        String PRO_VALUES = qp.checkGetString("PRO_VALUES");
        List<String> ls_p = StringUtils2.splitList(PRO_VALUES, ",", true);

        GlobalLogics.getOrderLogic().deletePackageAll(ORDER_ID);

        boolean b = GlobalLogics.getOrderLogic().updateGysOrder(ctx, ORDER_ID, OUT_ORDER_ID, "0", "0", "1", MEMO, JH_TIME, INBOUND_TIME, JH_TYPE, JH_ADDR, "0", "0", "0", "0", PARTNER_NO, partner_single.getString("PROVINCE"), partner_single.getString("CITY"), partner_single.getString("AREA"), partner_single.getString("ADDR"), partner_single.getString("PROVINCE_NAME") + partner_single.getString("CITY_NAME") + partner_single.getString("AREA_NAME") + partner_single.getString("ADDR"), partner_single.getString("CONTACT"), partner_single.getString("MOBILE"));
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
        String ORDER_IDS = qp.checkGetString("ORDER_ID");

        List<String> ls = StringUtils2.splitList(ORDER_IDS, ",", true);
        int success_count = 0;
        for (String ORDER_ID : ls){
            Record order = GlobalLogics.getOrderLogic().getSingleOrderBase(ORDER_ID) ;
            String INBOUND_TIME = order.getString("INBOUND_TIME");
            if (INBOUND_TIME.length()<=0){
                INBOUND_TIME = order.getString("JH_TIME");
            }
            String INBOUND_ID = Constants.newInboundCode();
            boolean b = GlobalLogics.getOrderLogic().saveInbound(ctx, INBOUND_ID, ORDER_ID, order.getString("KW_ID"), order.getString("GYS_ID"), GlobalLogics.getUser().getSingleGysBase(order.getString("GYS_ID")).getString("GYS_NAME_SX"), INBOUND_TIME);
            if (b){
                //更新订单状态
                GlobalLogics.getOrderLogic().updateOrderStatusInbound(ctx, ORDER_ID, OrderConstants.ORDER_STATUS_INBOUNT_CREATE);
                success_count +=1;
            }
        }
        if (success_count != ls.size()){
            out_rec.put("status",0);
            out_rec.put("message","入库通知单产生失败");
            return out_rec;
        }else{
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
        String OUTBOUND_TIME = order.getString("OUTBOUND_TIME");
        if (OUTBOUND_TIME.length()<=0){
            OUTBOUND_TIME = order.getString("JH_TIME");
        }
        String OUTBOUND_ID = Constants.newOutboundCode();
        boolean b = GlobalLogics.getOrderLogic().saveOutbound(ctx, OUTBOUND_ID, ORDER_ID, order.getString("KW_ID"), order.getString("GYS_ID"), GlobalLogics.getUser().getSingleGysBase(order.getString("GYS_ID")).getString("GYS_NAME_SX"), OUTBOUND_TIME);
        if (!b){
            out_rec.put("status",0);
            out_rec.put("message","出库通知单产生失败");
            return out_rec;
        }else{
            //更新订单状态
            GlobalLogics.getOrderLogic().updateOrderStatusOutbound(ctx, ORDER_ID, OrderConstants.ORDER_STATUS_OUTBOUNT_CREATE);
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
        out_rec.put("message", "装箱完毕");
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
        String PARTNER_NO = qp.getString("PARTNER_NO", "999");

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
        Record data = GlobalLogics.getOrderLogic().getAllGysOrderPageList(ctx, PARTNER_NO,WL_TYPE, SJ_ID, GYS_ID, START_TIME, END_TIME, STATE, PAY_DONE, PAGE, COUNT, ORDER_ID, OUT_ORDER_ID, INBOUND_STATUS_BEGIN, INBOUND_STATUS_END);

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
        String START_TIME = qp.getString("START_TIME", "");
        String END_TIME = qp.getString("END_TIME", "");
        RecordSet recs  = GlobalLogics.getOrderLogic().getNowRepoPackage(SJ_ID, GYS_ID, F_KW_ID, KW_ID, START_TIME, END_TIME);
        return recs;
    }
    @WebMethod("order/get_order_package_base")
    public RecordSet get_order_package_base(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID");
        RecordSet recs  = GlobalLogics.getOrderLogic().getOrderPackagesBase(ORDER_ID);
        return recs;
    }


    @WebMethod("order/get_now_repo_goods")
    public RecordSet get_now_repo_goods(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.getString("SJ_ID", "999");
        String GYS_ID = qp.getString("GYS_ID", "999");
        String F_KW_ID = qp.getString("F_KW_ID", "999");
        String KW_ID = qp.getString("KW_ID", "999");
        String START_TIME = qp.getString("START_TIME", "");
        String END_TIME = qp.getString("END_TIME", "");
        RecordSet recs  = GlobalLogics.getOrderLogic().getGysOrderDailyGoods(SJ_ID, GYS_ID, F_KW_ID, KW_ID, START_TIME, END_TIME);
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
        String INBOUND_TIME = qp.getString("INBOUND_TIME", "");
        RecordSet data = GlobalLogics.getOrderLogic().getAllCanPrintMd(ctx, GYS_ID, PRINTED, SJ_ID, PARTNER_NO, INBOUND_TIME);

        return data;
    }

    @WebMethod("order/get_single_order_by_package")
    public Record get_single_order_by_package(HttpServletRequest req, QueryParams qp) throws IOException {
        String PACKAGE_CODE = qp.checkGetString("PACKAGE_CODE");
        Record rec = GlobalLogics.getOrderLogic().getSingleOrderByPackageCode(PACKAGE_CODE);
        return rec;
    }

    @WebMethod("order/order_excel_insert")
    public Record order_excel_insert_new(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        FileItem file_item = qp.getFile("Filedata");
        String GYS_ID = qp.checkGetString("GYS_ID");

        Record return_rec = new Record();

        if (file_item != null && file_item.getSize() > 0) {
            return_rec = importOrder(ctx, file_item, GYS_ID,return_rec);
        }
        return return_rec;
    }

    private Record importOrder(Context ctx,FileItem file_item,String GYS_ID,Record return_rec) {
        Workbook wb = null;
        try {
            // 构造Workbook（工作薄）对象
            wb = WorkbookFactory.create(file_item.getInputStream());
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        if (wb == null) {
            return_rec.put("IMPORTS_STATUS", "0");
            return_rec.put("ERROR_TYPE", "1");
            return_rec.put("DATA", new RecordSet());
            return return_rec;
        }

        Sheet sheet = OrderConstants.getSheetByNum(wb, 0);
        int lastRowNum = sheet.getLastRowNum();
        RecordSet data_out = new RecordSet();
        int ALL_ERR_COUNT = 0;
        if (sheet != null) {
            for (int j = 0; j <= lastRowNum; j++) {
                Row row = sheet.getRow(j);
                if (row.getCell(0).getStringCellValue().equals("")
                        || row.getCell(1).getStringCellValue().equals("")
                        || row.getCell(2).getStringCellValue().equals("")

                        || row.getCell(0).getStringCellValue().contains("项目")


                        ){
                    continue;
                }
                Record data_check = new Record();
                String err_str = "";
                int ERR_COUNT = 0;

                int lastCellNum = row.getLastCellNum();
                List<String> ls_cells = new ArrayList<String>();
                for (int k = 0; k <= lastCellNum; k++) {
                    if (row.getCell(k) != null)  {
                       String cv = OrderConstants.getCellValueByCell(row.getCell(k));
                        ls_cells.add(Constants.replaceErrStr(cv));
                    }
                    else  {
                        ls_cells.add(" ");
                    }
                }

                // 0,第二项目号 -----物料编码
                // 1,配送路线
                // 2,售至地址名	 -----门店
                // 3,说明1	   -------商品名称
                // 4,说明行2  -------规格
                // 5,单位
                // 6,数量	   ---------
                // 7,请求日期  ---------入库日期
                // 8,配送日期----此项原EXCEL 表模板缺失
                String IMPORT_ID =  RandomUtils.generateStrId();
                data_check.put("IMPORT_ID",IMPORT_ID);
                String PARTNER_NAME =  ls_cells.get(2).trim();
                String PARTNER_NO =  "";
                Record rec_partner = GlobalLogics.getUser().check_partner_name(PARTNER_NAME);
                if (rec_partner.isEmpty()) {
                    ERR_COUNT +=1;
                    err_str += "门店名称-不存在,";
                }  else{
                    PARTNER_NO = rec_partner.getString("PARTNER_NO");
                }

                data_check.put("PARTNER_NAME",PARTNER_NAME);
                data_check.put("PARTNER_NO",PARTNER_NO);

                String PRO_NAME =  "";
                String PRO_SPEC =  "";
                String SPEC_ID =  "";
                String PRO_CODE =  ls_cells.get(0).toString().trim();
                if (!PRO_CODE.equals("")) {
                     Record proSpec = GlobalLogics.getBaseLogic().getSingleProBaseByProCode(PRO_CODE);
                     if (!proSpec.isEmpty()){
                         PRO_NAME =  proSpec.getString("PRO_NAME");
                         PRO_SPEC =  proSpec.getString("PRO_SPEC");
                         SPEC_ID =  proSpec.getString("SPEC_ID");
                         data_check.put("PRO_NAME", PRO_NAME);
                         data_check.put("PRO_SPEC", PRO_SPEC);
                         data_check.put("SPEC_ID", SPEC_ID);
                     }
                }
                data_check.put("PRO_CODE", PRO_CODE);
                if (PRO_NAME.length() <= 0 ) {
                    PRO_NAME = ls_cells.get(3).toString().trim();
                    PRO_SPEC = ls_cells.get(4).toString().trim();
                    Record rec_pro_spec = GlobalLogics.getUser().check_pro_name_spec(PRO_NAME.trim(), PRO_SPEC.trim());
                    if (rec_pro_spec.isEmpty()) {
                        ERR_COUNT += 1;
                        err_str += "货品名称或者规格-不存在,";
                    } else {
                        SPEC_ID = rec_pro_spec.getString("SPEC_ID");
                    }
                    data_check.put("PRO_NAME", PRO_NAME);
                    data_check.put("PRO_SPEC", PRO_SPEC);
                    data_check.put("SPEC_ID", SPEC_ID);
                }

                String PRO_COUNT =  ls_cells.get(6).toString().trim();
                int c = 0;
                try {
                    c = (int)Double.parseDouble(PRO_COUNT);
                }catch (Exception e){
                    ERR_COUNT +=1;
                    err_str += "货品数量-格式不正确,";
                }
                data_check.put("PRO_COUNT", c);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String INBOUND_TIME = ls_cells.get(7).toString().trim();
                String INBOUND_TIME_ = "";
                try {
//                    INBOUND_TIME = OrderConstants.numericTransDate(row.getCell(7), Double.parseDouble(ls_cells.get(7).toString()));
                    Date ddd = format.parse(INBOUND_TIME);
                    INBOUND_TIME_ = new SimpleDateFormat("yyyy-MM-dd").format(ddd);
                }catch (Exception e){
                    ERR_COUNT +=1;
                    err_str += "入库日期-格式不正确,";
                }
                data_check.put("INBOUND_TIME",INBOUND_TIME_.length()<=0?INBOUND_TIME:INBOUND_TIME_);
                String JH_TIME =  ls_cells.get(8).toString().trim();
                String JH_TIME_ = "";
                try {
//                    JH_TIME = OrderConstants.numericTransDate(row.getCell(8), Double.parseDouble(ls_cells.get(8).toString().trim()));
                    Date ddd = format.parse(JH_TIME);
                    JH_TIME_ = new SimpleDateFormat("yyyy-MM-dd").format(ddd);
                }catch (Exception e){
                    ERR_COUNT +=1;
                    err_str += "交货日期-格式不正确,";
                }

                data_check.put("JH_TIME",JH_TIME_.length()<=0?JH_TIME:JH_TIME_);
                String OUTBOUND_TIME_ = JH_TIME_;
                String OUT_ORDER_ID =  ls_cells.get(9);
                if (OUT_ORDER_ID.length()>0 && OUT_ORDER_ID.contains("E")){
                    ERR_COUNT +=1;
                    err_str += "外部订单号未设置为文本格式,";
                }
                data_check.put("OUT_ORDER_ID",OUT_ORDER_ID);

                //==========整合错误信息===========
                if (err_str.length()>0)
                    err_str = err_str.substring(0,err_str.length()-1);

                data_check.put("ERR_COUNT",ERR_COUNT);
                data_check.put("ERR_STR",err_str);
                //==========整合错误信息===========

                data_out.add(data_check);
                ALL_ERR_COUNT += ERR_COUNT;
            }

        }
         GlobalLogics.getOrderLogic().deleteAllOrderImport(GYS_ID,ctx.getUser_id());
        for (Record r : data_out){
             GlobalLogics.getOrderLogic().saveOrderImport(r.getString("IMPORT_ID"),GYS_ID,ctx.getUser_id(),r.getString("OUT_ORDER_ID")
                     ,r.getString("PARTNER_NAME"),r.getString("PARTNER_NO"),r.getString("SPEC_ID"),r.getString("PRO_NAME"),r.getString("PRO_SPEC"),(int)r.getInt("PRO_COUNT")
                     ,r.getString("INBOUND_TIME"),r.getString("JH_TIME"),r.getString("ERR_STR"),r.getString("PRO_CODE"));
        }
        if (ALL_ERR_COUNT==0){
            return_rec.put("IMPORTS_STATUS", "1");
            return_rec.put("ERROR_TYPE", "0");
        } else {
            return_rec.put("IMPORTS_STATUS", "0");
            return_rec.put("ERROR_TYPE", "2");
        }
        return_rec.put("DATA", data_out);
        return return_rec;
    }

    @WebMethod("order/order_excel_insert_really")
    public Record order_excel_insert_really(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String GYS_ID = qp.checkGetString("GYS_ID");
        Record gys = GlobalLogics.getUser().getSingleGysBase(GYS_ID);

        String IMPORT_IDS = qp.checkGetString("IMPORT_IDS");

        Record out_rec = new Record();

        RecordSet recs_imports = GlobalLogics.getOrderLogic().getAllImportsByIDS(IMPORT_IDS);

        //1,首先看,要下多少个订单,根据 PARTNER_NO 和 INBOUND_TIME来筛选
        List<String> ls_partner = new ArrayList<String>();
        for (Record r : recs_imports){
            if (!ls_partner.contains(r.getString("PARTNER_NO")))
                ls_partner.add(r.getString("PARTNER_NO"));
        }

        for (String partner_no : ls_partner){
            Record partner_single = GlobalLogics.getUser().getSinglePartnerByNo(partner_no);
            String JH_ADDR = partner_single.getString("FULL_ADDR");
            RecordSet partnerOrders = recs_imports.findsEq("PARTNER_NO", partner_no);
            Record partnerKw = GlobalLogics.getUser().getPartnerKw(partner_no);
            List<String> ls_jh_time = new ArrayList<String>();
            for (Record r : partnerOrders){
                if (!ls_jh_time.contains(r.getString("JH_TIME")))
                    ls_jh_time.add(r.getString("JH_TIME"));
            }

            for (String jhTime : ls_jh_time){
                RecordSet partnerJhOrders = partnerOrders.findsEq("JH_TIME", jhTime);
                String ORDER_ID = Constants.newCgCode();
                boolean b = GlobalLogics.getOrderLogic().saveGysOrderImport(ctx, ctx.getUser_id(), gys.getString("SJ_ID"), ORDER_ID,
                        partnerJhOrders.get(0).getString("OUT_ORDER_ID"), GYS_ID, gys.getString("GYS_NAME"), "0", "0", "1", "", jhTime, "供应商送货", JH_ADDR, partnerJhOrders.get(0).getString("INBOUND_TIME"),jhTime, "0", "0", "0", "0", 0, OrderConstants.ORDER_STATUS_DEFAULT, partner_no, partnerKw.getString("KW_ID"),
                        partner_single.getString("PROVINCE"), partner_single.getString("CITY"), partner_single.getString("AREA"), partner_single.getString("ADDR"), partner_single.getString("PROVINCE_NAME") + partner_single.getString("CITY_NAME") + partner_single.getString("AREA_NAME") + partner_single.getString("ADDR"), partner_single.getString("CONTACT"), partner_single.getString("MOBILE"));
                if (b) {
                    for (Record pro_str : partnerJhOrders) {
                        String SPEC_ID = pro_str.getString("SPEC_ID");
                        int PRO_COUNT = (int)pro_str.getInt("PRO_COUNT");

                        Record pro_spec = GlobalLogics.getBaseLogic().getSingleProSpec(SPEC_ID);
                        String pro_id = pro_spec.getString("PRO_ID");
                        Record pro = GlobalLogics.getBaseLogic().getSinglePro(pro_id);

                        String PRO_TYPE = pro.getString("PRO_TYPE");
                        String PRO_TYPE_ID = pro.getString("PRO_TYPE_ID");
                        String PRO_NAME = pro_spec.getString("PRO_NAME");


                        String PRO_CODE_NUMBER = pro_spec.getString("PRO_CODE");
                        String ORDER_ITEM_ID = RandomUtils.generateStrId();

                        boolean c = GlobalLogics.getOrderLogic().saveGysOrderPro(ctx, ORDER_ID,pro_id, SPEC_ID,
                                PRO_COUNT+"" , pro_spec.getString("PRO_PRICE")+"", "0",
                                PRO_CODE_NUMBER,"0",PRO_TYPE, PRO_TYPE_ID,PRO_NAME,"0" , ORDER_ITEM_ID, 0+"", 0+"", "0");
                    }
                    //审核
//                    GlobalLogics.getOrderLogic().updateOrderVerify(ORDER_ID, ctx.getUser_id());
//                    //装箱
                    orderAutoPackageProduct(ORDER_ID,GYS_ID,new Record());
//                    //入库通知
//                    String INBOUND_ID = Constants.newInboundCode();
//                    String INBOUND_TIME = partnerJhOrders.get(0).getString("INBOUND_TIME");
//                    if (INBOUND_TIME.length()<=0)  {
//                        INBOUND_TIME = tg.getOtherDaySimple(jhTime, -1);
//                    }
//                    boolean d = GlobalLogics.getOrderLogic().saveInbound(ctx, INBOUND_ID, ORDER_ID, partnerKw.getString("KW_ID"), GYS_ID, gys.getString("GYS_NAME"), INBOUND_TIME);
//                    if (d){
//                        GlobalLogics.getOrderLogic().updateOrderStatusInbound(ctx, ORDER_ID, OrderConstants.ORDER_STATUS_INBOUNT_CREATE, INBOUND_TIME);
//                    }
                    //删除导入的记录
                    GlobalLogics.getOrderLogic().deleteAllOrderImport(GYS_ID, ctx.getUser_id());
                }
            }
        }

        out_rec.put("STATUS",1);
        out_rec.put("MESSAGE","导入订单成功,并自动完成如下操作:\r\n 1,并且已经审核\r\n 2,产生装箱 \r\n 3, 产生入库通知单");
        return out_rec;
    }


    @WebMethod("order/order_auto_package")
    public Record order_auto_package(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String ORDER_ID = qp.checkGetString("ORDER_ID");
        String GYS_ID = qp.checkGetString("GYS_ID");
        Record out_rec = new Record();
        return orderAutoPackageProduct(ORDER_ID,GYS_ID,out_rec);
    }

    public  Record orderAutoPackageProduct(String ORDER_ID,String GYS_ID,Record out_rec){
        //先把所有的装箱,全部删除
        GlobalLogics.getOrderLogic().deletePackageAll(ORDER_ID);

        RecordSet orderProduct = GlobalLogics.getOrderLogic().getOrderProductsSpec(ORDER_ID);

        RecordSet singleBoxPro = new RecordSet(); //单独成箱
        RecordSet fullBoxPro = new RecordSet();   //非单独成箱

        for (Record p : orderProduct){
            if (p.getInt("SINGLE_BOX")==1){
                singleBoxPro.add(p);
            } else{
                fullBoxPro.add(p);
            }
        }

        for (Record r : singleBoxPro){
            int PRO_COUNT = (int)r.getInt("PRO_COUNT");
            String SPEC_ID = r.getString("SPEC_ID");
            for (int i=1;i<=PRO_COUNT;i++){
                String newPackageCode = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
                boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode);
                if (b) {
                    boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                            newPackageCode, SPEC_ID, r.getString("PRO_NAME"), 1);
                }else{
                    out_rec.put("status",0);
                    out_rec.put("message","自动装箱数据失败,或者不完整,请手动装箱");
                    return out_rec;
                }
            }
        }
        if (fullBoxPro.size()>0){     //非单独成箱的操作
            //1,首先再看,这些商品里面,有哪些是设置了最大数量的

            RecordSet hasSetMaxCount = new RecordSet();   //设置了每箱最大数量
            RecordSet notSetMaxCount = new RecordSet();   //没有设置每箱最大数量
            for (Record rec : fullBoxPro){
                Record m = GlobalLogics.getBaseLogic().existsMaxCountSet(GYS_ID,rec.getString("SPEC_ID"));
                if (m.isEmpty()){
                    notSetMaxCount.add(rec);
                }else{
                    rec.put("MAX_COUNT",m.getInt("COUNT"));
                    hasSetMaxCount.add(rec);
                }
            }

            if (hasSetMaxCount.size()>0){
                 for (Record has : hasSetMaxCount){
                     int MAX_COUNT = (int)has.getInt("MAX_COUNT");
                     int PRO_COUNT = (int)has.getInt("PRO_COUNT");
                     double PACKAGE_COUNT = Math.ceil(Double.parseDouble(String.valueOf(PRO_COUNT)) / Double.parseDouble(String.valueOf(MAX_COUNT)));
                     if (PACKAGE_COUNT<=1){
                         String newPackageCode = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
                         boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode);
                         if (b){
                             boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                                     newPackageCode, has.getString("SPEC_ID"), has.getString("PRO_NAME"), PRO_COUNT);
                         }
                     }else{
                         for (int i=1;i<PACKAGE_COUNT;i++){
                             String newPackageCode = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
                             boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode);
                             if (b){
                                 boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                                         newPackageCode, has.getString("SPEC_ID"), has.getString("PRO_NAME"), MAX_COUNT);
                             }
                         }
                         //最后一个箱子,按照总数相减的来装
                         int lessCount = PRO_COUNT- ((int)PACKAGE_COUNT-1)* MAX_COUNT;
                         String newPackageCode1 = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
                         boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode1);
                         if (b){
                             boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                                     newPackageCode1, has.getString("SPEC_ID"), has.getString("PRO_NAME"), lessCount);
                         }
                     }
                 }
            }
            if (notSetMaxCount.size()>0){
                //获取这个供应商所有的规则
                RecordSet recs_rules= GlobalLogics.getBaseLogic().getAllSpecFullBox(GYS_ID);

                //再找,这些商品,哪些可以合一个箱子的
                for (Record rule : recs_rules){
                    RecordSet ALL_FULL_BOX = RecordSet.fromJson(rule.getString("ALL_FULL_BOX"));
                    RecordSet t =  new RecordSet();
                    for (Record f : notSetMaxCount){
                        Record find = ALL_FULL_BOX.findEq("SPEC_ID",f.getString("SPEC_ID"));
                        if (!find.isEmpty()){
                            t.add(f);
                        }
                    }
                    if (t.size()>0){
                        //t 里面的,全部装一个箱子
                        String newPackageCode = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
                        boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode);
                        if (b) {
                            for (Record t0 : t) {
                                boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                                        newPackageCode, t0.getString("SPEC_ID"), t0.getString("PRO_NAME"), (int)t0.getInt("PRO_COUNT"));
                            }
                        }
                    }
                }
            }

        }

        //最后看,还剩哪些没有自动装箱的,就全部放在一个箱子里面
        //或者提出数来
        RecordSet package_status = GlobalLogics.getOrderLogic().getOrderPackageStatus(ORDER_ID);
        int all_count = 0;
        int all_has = 0;
        RecordSet final_recs = new RecordSet();
        for (Record p : package_status) {
            all_count += p.getInt("PRO_COUNT");
            all_has += p.getInt("HAS_PACKAGE_COUNT");
            if (p.getInt("PRO_COUNT")-p.getInt("HAS_PACKAGE_COUNT")>0) {
                final_recs.add(p);
            }
        }
        if (all_count-all_has<=0){
            out_rec.put("status",1);
            out_rec.put("message","全部自动装箱完毕");
        }else{
            //t 里面的,全部装一个箱子
            String newPackageCode = GlobalLogics.getOrderLogic().getNowPackageCode(ORDER_ID);
            boolean b = GlobalLogics.getOrderLogic().saveOrderPackage(ORDER_ID, newPackageCode);
            if (b) {
                for (Record t0 : final_recs) {
                    boolean c = GlobalLogics.getOrderLogic().saveOrderPackageProduct(ORDER_ID, String.valueOf(RandomUtils.generateId()),
                            newPackageCode, t0.getString("PRO_SPEC_ID"), t0.getString("PRO_NAME"), (int)t0.getInt("PRO_COUNT"));
                }
            }
            out_rec.put("status",1);
            out_rec.put("message","自动装箱完毕,部分货品因未设置合箱规则,也做了合箱,请注意");
        }
        return out_rec;
    }

    @WebMethod("order/get_inbound_print_kw")
    public RecordSet get_inbound_print_kw(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String KW_ID = qp.checkGetString("KW_ID");
        String START_TIME = qp.getString("T1", "");
        String END_TIME = qp.getString("T2", "");
        String GYS_ID = qp.getString("GYS_ID", "999");
        RecordSet data = GlobalLogics.getOrderLogic().getInboundPrintKw(GYS_ID,KW_ID, START_TIME, END_TIME);
        return data;
    }
    @WebMethod("order/get_inbound_print_box")
    public RecordSet get_inbound_print_box(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String KW_ID = qp.checkGetString("KW_ID");
        String START_TIME = qp.getString("T1", "");
        String END_TIME = qp.getString("T2", "");
        String GYS_ID = qp.getString("GYS_ID", "999");
        RecordSet data = GlobalLogics.getOrderLogic().getInboundPrintBox(GYS_ID, KW_ID, START_TIME, END_TIME);
        return data;
    }

    @WebMethod("order/get_outbound_print_kw")
    public RecordSet get_outbound_print_kw(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String KW_ID = qp.checkGetString("KW_ID");
        String START_TIME = qp.getString("T1", "");
        String END_TIME = qp.getString("T2", "");
        String GYS_ID = qp.getString("GYS_ID", "999");
        RecordSet data = GlobalLogics.getOrderLogic().getOutboundPrintKw(GYS_ID, KW_ID, START_TIME, END_TIME);
        return data;
    }
    @WebMethod("order/get_outbound_print_box")
    public RecordSet get_outbound_print_box(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String KW_ID = qp.checkGetString("KW_ID");
        String START_TIME = qp.getString("T1", "");
        String END_TIME = qp.getString("T2", "");
        String GYS_ID = qp.getString("GYS_ID", "999");
        RecordSet data = GlobalLogics.getOrderLogic().getOutboundPrintBox(GYS_ID, KW_ID, START_TIME, END_TIME);
        return data;
    }

    //==============
    @WebMethod("order/expert_excel_inbound_box")
    public String expert_excel_inbound_box(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String START_TIME = qp.checkGetString("START_TIME");
        String END_TIME = qp.checkGetString("END_TIME");
        String GYS_ID = qp.getString("GYS_ID", "999");
        String GYS_NAME = qp.getString("GYS_NAME", "999");
        String KW_ID = qp.getString("KW_ID", "999");
        String KW_NAME = qp.getString("KW_NAME", "999");
        if (GYS_ID.length()<=0 || GYS_ID.equals("9") || GYS_ID.equals("999")){
            GYS_NAME = "全部";
        }
        RecordSet recs = GlobalLogics.getOrderLogic().getInboundPrintBoxForExcel(GYS_ID, KW_ID, KW_NAME, START_TIME, END_TIME);
        if (recs.size() <= 0)
            return "NO DATA";
        return makeExcelInboundPackages(ctx, recs, KW_NAME+ " " + GYS_NAME + " 入库通知单");
    }
    public static String makeExcelInboundPackages(Context ctx, RecordSet recs, String title) throws IOException {
        InnovExcel ie = new InnovExcel();
        List<List<String>> dataList = new ArrayList<List<String>>();
        dataList.add(Arrays.asList(
                "入库所在线路",  "收货店铺", "每箱【箱内货品】", "入库日期"
        ));

        for (Record us : recs) {
            dataList.add(Arrays.asList(
                    us.getString("KW_NAME"), us.getString("PARTNER_NAME"), us.getString("PRO_DETAIL"), us.getString("INBOUND_TIME")
            ));
        }
        String sheetName = DateUtils.now().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
        byte[] buff = ie.makeInboundPackage(title, dataList, sheetName);
        Configuration conf = GlobalConfig.get();
        StaticFileStorage sfs = (StaticFileStorage) ClassUtils2.newInstance(conf.getString("service.export.excel.fileStorage", ""));
        String file = "exp_" + sheetName + ".xls";
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 16);
            org.apache.commons.io.IOUtils.copy(new ByteArrayInputStream(buff), out);
            out.flush();
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(out);
        }
        String url0 = String.format(conf.getString("service.export.excelPattern", "/exportFileStorage/%s"), file);
        ie = null;
        return url0;
    }

    @WebMethod("order/expert_excel_outbound_box")
    public String expert_excel_outbound_box(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String START_TIME = qp.checkGetString("START_TIME");
        String END_TIME = qp.checkGetString("END_TIME");
        String GYS_ID = qp.getString("GYS_ID", "999");
        String GYS_NAME = qp.getString("GYS_NAME", "999");
        String KW_ID = qp.getString("KW_ID", "999");
        String KW_NAME = qp.getString("KW_NAME", "999");
        if (GYS_ID.length()<=0 || GYS_ID.equals("9") || GYS_ID.equals("999")){
            GYS_NAME = "全部";
        }
        RecordSet recs = GlobalLogics.getOrderLogic().getOutboundPrintBoxForExcel(GYS_ID, KW_ID, KW_NAME, START_TIME, END_TIME);
        if (recs.size() <= 0)
            return "NO DATA";
        return makeExcelOutboundPackages(ctx, recs, KW_NAME + " " + GYS_NAME + "  出库拣货单");
    }
    public static String makeExcelOutboundPackages(Context ctx, RecordSet recs, String title) throws IOException {
        InnovExcel ie = new InnovExcel();
        List<List<String>> dataList = new ArrayList<List<String>>();
        dataList.add(Arrays.asList(
                "入库所在线路",  "收货店铺", "每箱【箱内货品】", "出库日期"
        ));

        for (Record us : recs) {
            dataList.add(Arrays.asList(
                    us.getString("KW_NAME"), us.getString("PARTNER_NAME"), us.getString("PRO_DETAIL"), us.getString("OUTBOUND_TIME")
            ));
        }
        String sheetName = DateUtils.now().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
        byte[] buff = ie.makeInboundPackage(title, dataList,sheetName);
        Configuration conf = GlobalConfig.get();
        StaticFileStorage sfs = (StaticFileStorage) ClassUtils2.newInstance(conf.getString("service.export.excel.fileStorage", ""));
        String file = "exp_" + sheetName + ".xls";
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 16);
            org.apache.commons.io.IOUtils.copy(new ByteArrayInputStream(buff), out);
            out.flush();
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(out);
        }
        String url0 = String.format(conf.getString("service.export.excelPattern", "/exportFileStorage/%s"), file);
        ie = null;
        return url0;
    }

    @WebMethod("order/test")
    public void test(HttpServletRequest req, QueryParams qp) throws IOException {
        GlobalLogics.getOrderLogic().webService_getAllInboundKwPackages("2018-07-12", "2018-07-14", "3203959557465792485");
//        GlobalLogics.getOrderLogic().test("JH_20180619_006","CG_20180619_042_0001");
    }
}

