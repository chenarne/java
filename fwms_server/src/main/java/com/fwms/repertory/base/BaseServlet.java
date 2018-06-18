package com.fwms.repertory.base;

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
import com.fwms.common.GlobalLogics;
import com.fwms.common.PortalContext;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BaseServlet extends WebMethodServlet {
    @Override
    public void init() throws ServletException {
        Configuration conf = GlobalConfig.get();
        super.init();
    }

    @WebMethod("base/get_province")
    public RecordSet get_province(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        return GlobalLogics.getBaseLogic().getProvince();
    }

    @WebMethod("base/get_city")
    public RecordSet get_city(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String PROVINCE_ID = qp.getString("PROVINCE_ID", "");
        return GlobalLogics.getBaseLogic().getCity(PROVINCE_ID);
    }
    @WebMethod("base/get_area")
    public RecordSet get_area(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String CITY_ID = qp.getString("CITY_ID", "");
        return GlobalLogics.getBaseLogic().getArea(CITY_ID);
    }


    @WebMethod("base/dw_save")
    public boolean dw_save(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String DW_SX = qp.getString("DW_SX", "");
        String DW = qp.getString("DW", "");
        return GlobalLogics.getBaseLogic().saveDw(DW_SX, DW);
    }

    @WebMethod("base/dw_delete")
    public boolean dw_delete(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String DW_SX = qp.getString("DW_SX", "");
        return GlobalLogics.getBaseLogic().deleteDw(DW_SX);
    }

    @WebMethod("base/dw_all")
    public RecordSet dw_all(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        return GlobalLogics.getBaseLogic().getAllDW();
    }

    @WebMethod("base/sx_get_dw")
    public Record sx_get_dw(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String DW_SX = qp.getString("DW_SX", "");
        return GlobalLogics.getBaseLogic().getDWBYSX(DW_SX);
    }

    @WebMethod("base/kw_get_all_by_level")
    public RecordSet kw_get_all_by_level(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        int KF_FLAG = (int)qp.getInt("KF_FLAG",1);
        int factId=(int)qp.getInt("factId",999);
        return GlobalLogics.getBaseLogic().getAllKwBaseByLevel(KF_FLAG, factId);
    }

    @WebMethod("base/pro_get_all_line")
    public RecordSet pro_get_all_line(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        return GlobalLogics.getBaseLogic().getAllProLine();
    }

    @WebMethod("base/get_all_parent_kw")
    public RecordSet get_all_parent_kw(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        return GlobalLogics.getBaseLogic().getAllParentKw(999, 999);
    }
    @WebMethod("base/get_all_son_kw")
    public RecordSet get_all_son_kw(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String FID = qp.checkGetString("FID");
        return GlobalLogics.getBaseLogic().getAllSonKw(999, 999,FID);
    }

    @WebMethod("base/kw_delete")
    public boolean kw_delete(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String KW_ID = qp.checkGetString("KW_ID");
        boolean b= GlobalLogics.getBaseLogic().deleteKw(KW_ID);
        return b;
    }

    //保存库位
    @WebMethod("base/kw_create")
    public boolean kw_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String KW_ID = String.valueOf(RandomUtils.generateId());
        String KW_NAME = qp.checkGetString("KW_NAME");
        String KW_ADDR = qp.getString("KW_ADDR", "");
        String KW_PEROID = qp.checkGetString("KW_PEROID");
        int KF_FLAG = (int)qp.getInt("KF_FLAG",1);
        int LEVEL = (int)qp.getInt("LEVEL",1);
        String FID = qp.getString("FID","");
        int factId=(int)qp.getInt("factId",1);
        if (LEVEL==1)
            FID = KW_ID;

        String CONSIGNEE_NAME = qp.getString("CONSIGNEE_NAME", "");
        String CONSIGNEE_PHONE = qp.getString("CONSIGNEE_PHONE", "");
        String CONSIGNEE_ADDR = qp.getString("CONSIGNEE_ADDR", "");
        String PROVINCE = qp.getString("PROVINCE", "");
        String CITY = qp.getString("CITY", "");
        String AREA = qp.getString("AREA", "");
        String AREA_ID = qp.getString("AREA_ID","");

        boolean b= GlobalLogics.getBaseLogic().saveKw(factId,KW_ID,KW_NAME,KW_ADDR,KW_PEROID,"",KF_FLAG,LEVEL,FID,CONSIGNEE_NAME,CONSIGNEE_PHONE,CONSIGNEE_ADDR,PROVINCE,CITY,AREA,AREA_ID);
        return b;
    }

    @WebMethod("base/kw_update")
    public boolean kw_update(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String KW_ID = qp.checkGetString("KW_ID");
        String KW_NAME = qp.checkGetString("KW_NAME");
        String KW_ADDR = qp.getString("KW_ADDR", "");
        String KW_PEROID = qp.checkGetString("KW_PEROID");
        String USER_NAMES = qp.getString("USER_ID", "");
        int KF_FLAG = (int)qp.getInt("KF_FLAG",1);
        String CONSIGNEE_NAME = qp.getString("CONSIGNEE_NAME", "");
        String CONSIGNEE_PHONE = qp.getString("CONSIGNEE_PHONE", "");
        String CONSIGNEE_ADDR = qp.getString("CONSIGNEE_ADDR", "");
        String PROVINCE = qp.getString("PROVINCE", "");
        String CITY = qp.getString("CITY", "");
        String AREA = qp.getString("AREA", "");
        String AREA_ID = qp.getString("AREA_ID","");
        int factId =(int)qp.getInt("factId",1);

        boolean b= GlobalLogics.getBaseLogic().updateKw(factId, KW_ID, KW_NAME, KW_ADDR, KW_PEROID, KF_FLAG, CONSIGNEE_NAME, CONSIGNEE_PHONE, CONSIGNEE_ADDR, PROVINCE, CITY, AREA, AREA_ID, "");
        return b;
    }
    @WebMethod("base/kw_get_single")
    public Record kw_get_single(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String KW_ID = qp.checkGetString("KW_ID");
        Record rec= GlobalLogics.getBaseLogic().getSingleKwBase(KW_ID);
        return rec;
    }

    //================================

    @WebMethod("base/pro_create")
    public boolean pro_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        String PRO_ID = String.valueOf(RandomUtils.generateId());

        String PRO_CODE = qp.getString("PRO_CODE", "");
        String PRO_TYPE = qp.getString("PRO_TYPE", "");
        String PRO_TYPE_ID = qp.getString("PRO_TYPE_ID", "");
        String PRO_NAME = qp.getString("PRO_NAME", "");
        String PRO_NAME_SX = qp.getString("PRO_NAME_SX", "");
        String MEMO = qp.getString("MEMO", "");
        String DW = qp.getString("DW", "");
        int TRANSPORT_TYPE = (int)qp.getInt("TRANSPORT_TYPE", 2);
        String DW_NAME = GlobalLogics.getBaseLogic().getDWBYSX(DW).getString("DW");
        boolean b= GlobalLogics.getBaseLogic().saveUserProduct(GYS_ID,PRO_ID, PRO_CODE, PRO_TYPE, PRO_TYPE_ID, PRO_NAME, PRO_NAME_SX, MEMO,TRANSPORT_TYPE,DW,DW_NAME);
        return b;
    }

    @WebMethod("base/pro_update")
    public boolean pro_update(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String PRO_ID = qp.checkGetString("PRO_ID");

        String PRO_CODE = qp.getString("PRO_CODE", "");
        String PRO_TYPE = qp.getString("PRO_TYPE", "");
        String PRO_TYPE_ID = qp.getString("PRO_TYPE_ID", "");
        String PRO_NAME = qp.getString("PRO_NAME", "");
        String PRO_NAME_SX = qp.getString("PRO_NAME_SX", "");
        String MEMO = qp.getString("MEMO", "");
        String DW = qp.getString("DW", "");
        int TRANSPORT_TYPE = (int)qp.getInt("TRANSPORT_TYPE", 2);
        String DW_NAME = GlobalLogics.getBaseLogic().getDWBYSX(DW).getString("DW");
        boolean b= GlobalLogics.getBaseLogic().updateProduct(PRO_ID, PRO_CODE, PRO_TYPE, PRO_TYPE_ID, PRO_NAME, PRO_NAME_SX, MEMO, TRANSPORT_TYPE, DW, DW_NAME);
        return b;
    }

    @WebMethod("base/get_all_gys_pro")
    public RecordSet get_all_gys_pro(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        RecordSet recs= GlobalLogics.getBaseLogic().getAllGysPro(GYS_ID);
        return recs;
    }

    @WebMethod("base/get_all_gys_pro_spec")
    public RecordSet get_all_gys_pro_spec(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        RecordSet recs= GlobalLogics.getBaseLogic().getAllGysProSpec(GYS_ID);
        return recs;
    }

    @WebMethod("base/get_single_user_pro")
    public Record get_single_user_pro(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String PRO_ID = qp.checkGetString("PRO_ID");
        Record rec= GlobalLogics.getBaseLogic().getSinglePro(PRO_ID);
        return rec;
    }

    @WebMethod("base/pro_delete_spec")
    public boolean pro_delete_spec(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SPEC_ID = qp.checkGetString("SPEC_ID");
        boolean b= GlobalLogics.getBaseLogic().deleteProductSpec(SPEC_ID);
        return b;
    }

    @WebMethod("base/pro_delete")
    public boolean pro_delete(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String PRO_ID = qp.checkGetString("PRO_ID");
        boolean b= GlobalLogics.getBaseLogic().deleteProduct(PRO_ID);
        return b;
    }

    @WebMethod("base/pro_spec_create")
    public boolean pro_spec_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String PRO_ID = qp.checkGetString("PRO_ID");
        Record p = GlobalLogics.getBaseLogic().getSingleProBase(PRO_ID);
        String SPEC_ID = String.valueOf(RandomUtils.generateId());

        String PRO_CODE = qp.getString("PRO_CODE", "");
        String PRO_SPEC = qp.getString("PRO_SPEC", "");
        String PRO_COLOR = qp.getString("PRO_COLOR", "");
        String PRO_PRICE = qp.getString("PRO_PRICE", "");
        String PRO_PRICE_1 = qp.getString("PRO_PRICE_1", "");
        String PRO_NAME = qp.getString("PRO_NAME", "");
        String PRO_NAME_SX = qp.getString("PRO_NAME_SX", "");
        int PERIOD = (int)qp.getInt("PERIOD", 1);
        int SINGLE_BOX = (int)qp.getInt("SINGLE_BOX", 0);
        String BAR_CODE = qp.getString("BAR_CODE", "");
        String MEMO = qp.getString("MEMO", "");

        boolean b= GlobalLogics.getBaseLogic().saveProductSpec(PRO_ID, SPEC_ID, PRO_CODE, PRO_SPEC, PRO_COLOR, PRO_PRICE, PRO_PRICE_1, PRO_NAME, PRO_NAME_SX, PERIOD, MEMO, BAR_CODE,p.getString("PRO_DW_NAME"),SINGLE_BOX);
        return b;
    }

    @WebMethod("base/pro_spec_update")
    public Record pro_spec_update(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record out_rec = new Record();

        String SPEC_ID = qp.checkGetString("SPEC_ID");

        String PRO_CODE = qp.getString("PRO_CODE", "");
        String PRO_SPEC = qp.getString("PRO_SPEC", "");
        String PRO_COLOR = qp.getString("PRO_COLOR", "");
        String PRO_PRICE = qp.getString("PRO_PRICE", "");
        String PRO_PRICE_1 = qp.getString("PRO_PRICE_1", "");
        String PRO_NAME = qp.getString("PRO_NAME", "");
        String PRO_NAME_SX = qp.getString("PRO_NAME_SX", "");
        int PERIOD = (int)qp.getInt("PERIOD", 1);
        int SINGLE_BOX = (int)qp.getInt("SINGLE_BOX", 0);
        String BAR_CODE = qp.getString("BAR_CODE", "");
        String MEMO = qp.getString("MEMO", "");

        Record old_spec = GlobalLogics.getBaseLogic().getSingleProSpec(SPEC_ID);
        if (old_spec.getInt("SINGLE_BOX") != SINGLE_BOX){
              if (SINGLE_BOX==1){ //原来是0,不单独成箱的,查一下有没有 SPEC 存在,存在了就不允许改
                  RecordSet exists = GlobalLogics.getBaseLogic().existsSpecFullBoxThisGys(SPEC_ID);
                  if (exists.size()>0){
                      out_rec.put("STATUS",0);
                      out_rec.put("MESSAGE","已经存在合箱的规则了,必须先删除合箱规则中的此货品,才能改成“单独成箱” ");
                      return out_rec;
                  }
              }
        }

        boolean b= GlobalLogics.getBaseLogic().updateProductSpec(SPEC_ID, PRO_CODE, PRO_SPEC, PRO_COLOR, PRO_PRICE, PRO_PRICE_1, PRO_NAME, PRO_NAME_SX, PERIOD, MEMO, BAR_CODE, SINGLE_BOX);
        if (b){
            out_rec.put("STATUS",1);
            out_rec.put("MESSAGE","保存成功");
        }else{
            out_rec.put("STATUS",0);
            out_rec.put("MESSAGE","保存失败 ");
        }
        return out_rec;
    }

    @WebMethod("base/get_single_user_pro_spec")
    public Record get_single_user_pro_spec(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SPEC_ID = qp.checkGetString("SPEC_ID");
        Record rec= GlobalLogics.getBaseLogic().getSingleProSpec(SPEC_ID);
        return rec;
    }

    @WebMethod("base/get_partner_kw_single")
    public Record get_partner_kw_single(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String PARTNER_NO = qp.checkGetString("PARTNER_NO");
        Record rec= GlobalLogics.getBaseLogic().getPartnerKw(PARTNER_NO);
        return rec;
    }

    @WebMethod("base/get_sj_partner_kw")
    public RecordSet get_sj_partner_kw(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.checkGetString("SJ_ID");
        RecordSet recs= GlobalLogics.getBaseLogic().getAllPartnerKw(SJ_ID);
        return recs;
    }

    @WebMethod("base/get_all_full_box")
    public RecordSet get_all_full_box(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        RecordSet recs= GlobalLogics.getBaseLogic().getAllSpecFullBox(GYS_ID);
        return recs;
    }

    @WebMethod("base/full_box_delete")
    public boolean full_box_delete(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String BOX_ID = qp.checkGetString("BOX_ID");
        boolean b= GlobalLogics.getBaseLogic().deleteSpecFullBoxAll(BOX_ID);
        return b;
    }

    @WebMethod("base/get_gys_pro_spec_can_full_box_create")
    public RecordSet get_gys_pro_spec_can_full_box_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        RecordSet recs= GlobalLogics.getBaseLogic().getAllGysProSpecCanFullBox(GYS_ID);
        return recs;
    }
    @WebMethod("base/get_gys_pro_spec_can_full_box_update")
    public RecordSet get_gys_pro_spec_can_full_box_update(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        String BOX_ID = qp.getString("BOX_ID", "");
        RecordSet recs= GlobalLogics.getBaseLogic().getAllGysProSpecCanFullBoxUpdate(GYS_ID, BOX_ID);
        return recs;
    }
    @WebMethod("base/full_box_create")
    public boolean full_box_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        String PRO_VALUES = qp.checkGetString("PRO_VALUES");

        List<String> ls_p = StringUtils2.splitList(PRO_VALUES, ",", true);
        String BOX_ID = RandomUtils.generateStrId();
        for (String SPEC_ID : ls_p) {
            Record pro_spec = GlobalLogics.getBaseLogic().getSingleProSpec(SPEC_ID);
            boolean c = GlobalLogics.getBaseLogic().saveSpecFullBox(BOX_ID,GYS_ID,SPEC_ID,pro_spec.getString("PRO_NAME"),pro_spec.getString("PRO_SPEC"));
        }
        return true;
    }

    @WebMethod("base/check_full_box_create")
    public boolean check_full_box_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        String BOX_ID = qp.checkGetString("BOX_ID");
        String SPEC_ID = qp.checkGetString("SPEC_ID");
        Record pro_spec = GlobalLogics.getBaseLogic().getSingleProSpec(SPEC_ID);
        boolean c = GlobalLogics.getBaseLogic().saveSpecFullBox2(BOX_ID, GYS_ID, SPEC_ID, pro_spec.getString("PRO_NAME"), pro_spec.getString("PRO_SPEC"));
        return true;
    }
}

