package com.fwms.webservice;

import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.log.Logger;
import com.fwms.common.GlobalLogics;
import com.fwms.webservice.entity.*;
import org.springframework.stereotype.Service;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@Service
@WebService
public class OrderServiceImpl implements OrderServiceLogic {
    private static final Logger L = Logger.getLogger(OrderServiceImpl.class);
    //===============面单打印程序==================
    @Override
    public List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getInboundMdList(String userId,String PRINTED,String SJ_ID,String PARTNER_NO,String INBOUND_TIME){
        Record gys = GlobalLogics.getUser().getGysByUser(userId);
        String GYS_ID = gys.getString("GYS_ID");
        Context ctx = new Context();
        ctx.setUser_id(userId);
        RecordSet data = GlobalLogics.getOrderLogic().getAllCanPrintMd(ctx, GYS_ID, Integer.parseInt(PRINTED), SJ_ID, PARTNER_NO, INBOUND_TIME);

        List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> ls = new ArrayList<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE>();

        for (Record rec : data){
            WMS_WEBSERVICE_RESULT_ORDER_PACKAGE o = new WMS_WEBSERVICE_RESULT_ORDER_PACKAGE();
            o.setORDER_ID(rec.getString("ORDER_ID"));
            o.setCONTACT(rec.getString("CONTACT"));
            o.setFULL_ADDR(rec.getString("FULL_ADDR"));
            o.setGYS_ID(rec.getString("GYS_ID"));
            o.setGYS_NAME(rec.getString("GYS_NAME"));
            o.setINBOUND_TIME(rec.getString("INBOUND_TIME"));
            o.setJH_TIME(rec.getString("JH_TIME"));
            o.setKW_ID(rec.getString("KW_ID"));
            o.setKW_NAME(rec.getString("KW_NAME"));
            o.setMOBILE(rec.getString("MOBILE"));
            o.setOUT_ORDER_ID(rec.getString("OUT_ORDER_ID"));
            o.setPARENT_KW_NAME(rec.getString("PARENT_KW_NAME"));
            o.setPARTNER_NAME(rec.getString("PARTNER_NAME"));
            o.setSJ_NAME(rec.getString("SJ_NAME"));
            o.setPARTNER_NO(rec.getString("PARTNER_NO"));
            o.setPACKAGE_CODE(rec.getString("PACKAGE_CODE"));
            o.setPRO_DETAIL(rec.getString("PRO_DETAIL"));
            ls.add(o);
        }


        return ls;
    }

    @Override
    public List<WMS_WEBSERVICE_RESULT_SJ> getAllGysSj(String userId){
        Record gys = GlobalLogics.getUser().getGysByUser(userId);
        String GYS_ID = gys.getString("GYS_ID");
        Context ctx = new Context();
        ctx.setUser_id(userId);
        RecordSet data = GlobalLogics.getUser().getAllGysSj(GYS_ID);

        List<WMS_WEBSERVICE_RESULT_SJ> ls = new ArrayList<WMS_WEBSERVICE_RESULT_SJ>();

        for (Record rec : data){
            WMS_WEBSERVICE_RESULT_SJ o = new WMS_WEBSERVICE_RESULT_SJ();
            o.setSJ_ID(rec.getString("SJ_ID"));
            o.setSJ_NAME(rec.getString("SJ_NAME"));
            o.setSJ_NAME_SX(rec.getString("SJ_NAME_SX"));
            ls.add(o);
        }
        return ls;
    }

    @Override
    public List<WMS_WEBSERVICE_RESULT_PARTNER> getSjPartner(String SJ_ID){
        RecordSet data = GlobalLogics.getUser().getSjPartnerBase(SJ_ID);
        List<WMS_WEBSERVICE_RESULT_PARTNER> ls = new ArrayList<WMS_WEBSERVICE_RESULT_PARTNER>();

        for (Record rec : data){
            WMS_WEBSERVICE_RESULT_PARTNER o = new WMS_WEBSERVICE_RESULT_PARTNER();
            o.setPARTNER_NO(rec.getString("PARTNER_NO"));
            o.setPARTNER_NAME(rec.getString("PARTNER_NAME"));
            ls.add(o);
        }
        return ls;
    }

    //更新面单打印记录
    @Override
    public WMS_WEBSERVICE_RESULT_BOOLEAN updatePackagePrint(String package_code,String userId){
        WMS_WEBSERVICE_RESULT_BOOLEAN o = new WMS_WEBSERVICE_RESULT_BOOLEAN();
        if (package_code.length()<=0) {
            o.setResult(false);
            return o;
        }
        Context ctx = new Context();
        ctx.setUser_id(userId);
        boolean b = GlobalLogics.getOrderLogic().webService_printOrderPackage(ctx, package_code.replace("'", "").replace("&", "").replace(",", "").replace(" ", ""));
        o.setResult(b);
        return o;
    }

    //===============面单打印程序==================











    //===============PDA程序==================

    //获取全部入库通知单
    @Override
    public List<WMS_WEBSERVICE_RESULT_ORDER_INBOUND> getAllPackageInbound(String kwId){

        RecordSet data = GlobalLogics.getOrderLogic().webService_getAllInbound(kwId);

        List<WMS_WEBSERVICE_RESULT_ORDER_INBOUND> ls = new ArrayList<WMS_WEBSERVICE_RESULT_ORDER_INBOUND>();

        for (Record rec : data){
            WMS_WEBSERVICE_RESULT_ORDER_INBOUND o = new WMS_WEBSERVICE_RESULT_ORDER_INBOUND();

            o.setORDER_ID(rec.getString("ORDER_ID"));
            o.setINBOUND_TIME(rec.getString("INBOUND_TIME"));
            o.setKW_ID(rec.getString("KW_ID"));
            o.setKW_NAME(rec.getString("KW_NAME"));
            o.setPARENT_KW_NAME(rec.getString("PARENT_KW_NAME"));
            o.setPARTNER_NAME(rec.getString("PARTNER_NAME"));
            o.setINBOUND_ID(rec.getString("INBOUND_ID"));
            ls.add(o);
        }
        return ls;
    }
    //获取全部出库通知单
    @Override
    public List<WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND> getAllPackageOutbound(String kwId){

        RecordSet data = GlobalLogics.getOrderLogic().webService_getAllOutbound(kwId);

        List<WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND> ls = new ArrayList<WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND>();

        for (Record rec : data){
            WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND o = new WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND();

            o.setORDER_ID(rec.getString("ORDER_ID"));
            o.setOUTBOUND_TIME(rec.getString("OUTBOUND_TIME"));
            o.setKW_ID(rec.getString("KW_ID"));
            o.setKW_NAME(rec.getString("KW_NAME"));
            o.setPARENT_KW_NAME(rec.getString("PARENT_KW_NAME"));
            o.setPARTNER_NAME(rec.getString("PARTNER_NAME"));
            o.setOUTBOUND_ID(rec.getString("OUTBOUND_ID"));
            ls.add(o);
        }
        return ls;
    }
    //更新入库
    @Override
    public WMS_WEBSERVICE_RESULT updatePackageInbound(String inbound_id,String package_code,String userId){
        WMS_WEBSERVICE_RESULT o = new WMS_WEBSERVICE_RESULT();

        Context ctx = new Context();
        ctx.setUser_id(userId);

        //首先判断,这个 package_code ,是不是这个 inboundid 的
        Record package_single  = GlobalLogics.getOrderLogic().getSinglePackage(package_code);
        if (package_single.isEmpty()){
            o.setSTATUS(0);
            o.setMESSAGE("此箱码不存在");
            return o;
        }else{
            String order_id = package_single.getString("ORDER_ID");
            Record order = GlobalLogics.getOrderLogic().getSingleOrderBase(order_id);
            Record inb = GlobalLogics.getOrderLogic().getSingleInboundBase(inbound_id);
            if (!order.getString("KW_ID").equals(inb.getString("KW_ID"))) {
                o.setSTATUS(0);
                o.setMESSAGE("此箱码,不属于这个货位");
                return o;
            }
        }

        boolean b = GlobalLogics.getOrderLogic().confirmInbound(ctx, package_code.replace("'", "").replace("&", "").replace(",", "").replace(" ", ""));
        if (b){
            o.setSTATUS(1);
            o.setMESSAGE("入库成功");
            return o;
        }   else{
            o.setSTATUS(1);
            o.setMESSAGE("入库操作失败,请检查数据");
            return o;
        }
    }
    //更新出库
    @Override
    public WMS_WEBSERVICE_RESULT updatePackageOutbound(String outbound_id,String package_code,String userId){
        WMS_WEBSERVICE_RESULT o = new WMS_WEBSERVICE_RESULT();
        L.debug(null,"outbound_id="+outbound_id + ",package_code="+package_code);
        //首先判断,这个 package_code ,是不是这个  outboundid 的
        Record package_single  = GlobalLogics.getOrderLogic().getSinglePackage(package_code);
        L.debug(null,"package_single="+package_single);
        if (package_single.isEmpty()){
            o.setSTATUS(0);
            o.setMESSAGE("此箱码不存在");
            return o;
        }else{
            String order_id = package_single.getString("ORDER_ID");
            Record order = GlobalLogics.getOrderLogic().getSingleOrderBase(order_id);
            L.debug(null,"order="+order);
            Record outb = GlobalLogics.getOrderLogic().getSingleOutboundBase(outbound_id);
            L.debug(null,"outb="+outb);
            if (!order.getString("KW_ID").equals(outb.getString("KW_ID"))) {
                L.debug(null,"order_kw_id="+order.getString("KW_ID") + ",outb_kw_id="+outb.getString("KW_ID"));
                o.setSTATUS(0);
                o.setMESSAGE("此箱码,不属于这个货位");
                return o;
            }
        }
        Context ctx = new Context();
        ctx.setUser_id(userId);
        boolean b = GlobalLogics.getOrderLogic().confirmOutbound(ctx, package_code.replace("'", "").replace("&", "").replace(",", "").replace(" ", ""));
        if (b){
            o.setSTATUS(1);
            o.setMESSAGE("出库成功");
            return o;
        }   else{
            o.setSTATUS(1);
            o.setMESSAGE("出库操作失败,请检查数据");
            return o;
        }
    }

    //获取这个 INBOUND_ID 的所有箱子
    @Override
    public List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getScanInboundList(String INBOUND_ID){
        RecordSet data = GlobalLogics.getOrderLogic().webService_getInboundPackage(INBOUND_ID);

        List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> ls = new ArrayList<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE>();

        for (Record rec : data){
            WMS_WEBSERVICE_RESULT_ORDER_PACKAGE o = new WMS_WEBSERVICE_RESULT_ORDER_PACKAGE();
            o.setORDER_ID(rec.getString("ORDER_ID"));
            o.setCONTACT(rec.getString("CONTACT"));
            o.setFULL_ADDR(rec.getString("FULL_ADDR"));
            o.setGYS_ID(rec.getString("GYS_ID"));
            o.setGYS_NAME(rec.getString("GYS_NAME"));
            o.setINBOUND_TIME(rec.getString("INBOUND_TIME"));
            o.setJH_TIME(rec.getString("JH_TIME"));
            o.setKW_ID(rec.getString("KW_ID"));
            o.setKW_NAME(rec.getString("KW_NAME"));
            o.setMOBILE(rec.getString("MOBILE"));
            o.setOUT_ORDER_ID(rec.getString("OUT_ORDER_ID"));
            o.setPARENT_KW_NAME(rec.getString("PARENT_KW_NAME"));
            o.setPARTNER_NAME(rec.getString("PARTNER_NAME"));
            o.setSJ_NAME(rec.getString("SJ_NAME"));
            o.setPARTNER_NO(rec.getString("PARTNER_NO"));
            o.setPACKAGE_CODE(rec.getString("PACKAGE_CODE"));
            o.setPRO_DETAIL(rec.getString("PRO_DETAIL"));
            ls.add(o);
        }


        return ls;
    }

    //获取这个 OUTBOUND_ID 的所有箱子
    @Override
    public List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getScanOutboundList(String OUTBOUND_ID){
        RecordSet data = GlobalLogics.getOrderLogic().webService_getOutboundPackage(OUTBOUND_ID);

        List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> ls = new ArrayList<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE>();

        for (Record rec : data){
            WMS_WEBSERVICE_RESULT_ORDER_PACKAGE o = new WMS_WEBSERVICE_RESULT_ORDER_PACKAGE();
            o.setORDER_ID(rec.getString("ORDER_ID"));
            o.setCONTACT(rec.getString("CONTACT"));
            o.setFULL_ADDR(rec.getString("FULL_ADDR"));
            o.setGYS_ID(rec.getString("GYS_ID"));
            o.setGYS_NAME(rec.getString("GYS_NAME"));
            o.setINBOUND_TIME(rec.getString("INBOUND_TIME"));
            o.setJH_TIME(rec.getString("JH_TIME"));
            o.setKW_ID(rec.getString("KW_ID"));
            o.setKW_NAME(rec.getString("KW_NAME"));
            o.setMOBILE(rec.getString("MOBILE"));
            o.setOUT_ORDER_ID(rec.getString("OUT_ORDER_ID"));
            o.setPARENT_KW_NAME(rec.getString("PARENT_KW_NAME"));
            o.setPARTNER_NAME(rec.getString("PARTNER_NAME"));
            o.setSJ_NAME(rec.getString("SJ_NAME"));
            o.setPARTNER_NO(rec.getString("PARTNER_NO"));
            o.setPACKAGE_CODE(rec.getString("PACKAGE_CODE"));
            o.setPRO_DETAIL(rec.getString("PRO_DETAIL"));
            ls.add(o);
        }


        return ls;
    }

    //===============PDA程序==================

















    //===============通用的登录程序==================
    @Override
    public WMS_WEBSERVICE_RESULT_USER packageUserLogin(String user_name,String password){
        WMS_WEBSERVICE_RESULT_USER o = new WMS_WEBSERVICE_RESULT_USER();
        o.setUSER_NAME("");
        o.setDISPLAY_NAME("");
        o.setUSER_ID("");
        if (user_name.length()<=0 || password.length()<=0)
            return o;

        Record u = GlobalLogics.getUser().getUserByUserName(user_name);
        String p = u.getString("USER_PASSWORD");
        int VERIFY = (int)u.getInt("VERIFY");
        if (!p.equals(password) || VERIFY==0)
            return o;

        o.setUSER_NAME(u.getString("USER_NAME"));
        o.setDISPLAY_NAME(u.getString("DISPLAY_NAME"));
        o.setUSER_ID(u.getString("USER_ID"));
        return o;
    }
    //===============通用的登录程序==================
}
