package com.fwms.webservice;

import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
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

    //===============面单打印程序==================
    @Override
    public List<WMS_WEBSERVICE_RESULT_ORDER_PACKAGE> getInboundMdList(String userId,String PRINTED,String SJ_ID,String PARTNER_NO,String INBOUND_TIME

    ){
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
    public List<WMS_WEBSERVICE_RESULT_ORDER_INBOUND> updatePackageInbound(String kwId){

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
    public List<WMS_WEBSERVICE_RESULT_ORDER_OUTBOUND> updatePackageOutbound(String kwId){

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
    public WMS_WEBSERVICE_RESULT_BOOLEAN updatePackageInbound(String package_code,String userId){
        WMS_WEBSERVICE_RESULT_BOOLEAN o = new WMS_WEBSERVICE_RESULT_BOOLEAN();

        Context ctx = new Context();
        ctx.setUser_id(userId);
        boolean b = GlobalLogics.getOrderLogic().confirmInbound(ctx, package_code.replace("'", "").replace("&", "").replace(",", "").replace(" ", ""));
        o.setResult(b);
        return o;
    }
    //更新出库
    @Override
    public WMS_WEBSERVICE_RESULT_BOOLEAN updatePackageOutbound(String package_code,String userId){
        WMS_WEBSERVICE_RESULT_BOOLEAN o = new WMS_WEBSERVICE_RESULT_BOOLEAN();
        if (package_code.length()<=0) {
            o.setResult(false);
            return o;
        }
        Context ctx = new Context();
        ctx.setUser_id(userId);
        boolean b = GlobalLogics.getOrderLogic().confirmOutbound(ctx, package_code.replace("'", "").replace("&", "").replace(",", "").replace(" ", ""));
        o.setResult(b);
        return o;
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
