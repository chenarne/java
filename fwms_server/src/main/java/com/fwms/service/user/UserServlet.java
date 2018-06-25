package com.fwms.service.user;

import com.fwms.basedevss.ServerException;
import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.context.Context;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.log.Logger;
import com.fwms.basedevss.base.sfs.StaticFileStorage;
import com.fwms.basedevss.base.sfs.local.LocalSFS;
import com.fwms.basedevss.base.util.ClassUtils2;
import com.fwms.basedevss.base.util.DateUtils;
import com.fwms.basedevss.base.util.RandomUtils;
import com.fwms.basedevss.base.web.QueryParams;
import com.fwms.basedevss.base.web.webmethod.NoResponse;
import com.fwms.basedevss.base.web.webmethod.WebMethod;
import com.fwms.basedevss.base.web.webmethod.WebMethodServlet;
import com.fwms.common.*;
import com.fwms.repertory.orders.OrderConstants;
import com.fwms.webservice.ServicePublish;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class UserServlet extends WebMethodServlet {
    private static final Logger L = Logger.getLogger(UserServlet.class);
    private StaticFileStorage imgStorage;
    private String imgPattern;

    private StaticFileStorage userPhotoImgStorage;
    private String userPhotoImgPattern;

    @Override
    public void init() throws ServletException {
        Configuration conf = GlobalConfig.get();
        super.init();
        ServicePublish.publish();
        imgStorage = (StaticFileStorage) ClassUtils2.newInstance(conf.getString("service.servlet.imgStorage", ""));
        imgStorage.init();
        imgPattern = conf.getString("service.imgUrlPattern", "http://" + conf.getString("server.host", "localhost") + ":" + GlobalConfig.get().getString("server.web.port", "80") + "/fwms/userPhotoImg/%s");
    }


    //用户修改头像
    @WebMethod("user/update_img_new")
    public Record t_user_updateImgNew(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        String USER_ID = ctx.getUser_id();
        int UPTYPE = (int)qp.getInt("UPTYPE",1);
        Record ug = new Record();
        String EXPNAME = qp.getString("EXPNAME", "");

        FileItem file_item = qp.getFile("USER_IMG");
//        L.debug(null,"USER UPLOAD IMG 0 UPTYPE=:"+UPTYPE);
        if (UPTYPE==1){
            if (!EXPNAME.toUpperCase().equals("JPG") && !EXPNAME.toUpperCase().equals("JPEG") && !EXPNAME.toUpperCase().equals("PNG"))
                return new Record();
            String contentType = file_item.getContentType();
            if (!contentType.contains("image"))
                return new Record();
        }

        int x1 = (int)qp.checkGetInt("x1");
        int y1 = (int)qp.checkGetInt("y1");
        int x2 = (int)qp.checkGetInt("x2");
        int y2 = (int)qp.checkGetInt("y2");
//        L.debug(null,"USER UPLOAD IMG 1 XY=:"+x1+y1+x2+y2);
        String imageName = Long.toString(RandomUtils.generateId());
        String sfn = imageName + "_S.jpg";

        boolean b = false;
        if (UPTYPE==1){
//            L.debug(null,"USER UPLOAD IMG 2 file_item=:"+file_item.getSize());
            if (file_item != null && file_item.getSize() > 0) {
                b = cut(EXPNAME,file_item.getInputStream(), sfn,x1, y1, x2, y2, userPhotoImgStorage);
            } else {
                return new Record();
            }
        }else if (UPTYPE==2){
            //这里是原图上找区间
//            L.debug(null,"USER UPLOAD IMG 2 file_item=:"+file_item.getSize());
            Record user = GlobalLogics.getUser().getSingleUserSimple(USER_ID);
            String user_img = user.getString("IMG_S");
            String fileName = user_img.substring(user_img.lastIndexOf("/") + 1, user_img.length());
            EXPNAME = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            File file = new File(((LocalSFS) userPhotoImgStorage).directory+fileName);
            InputStream is = new FileInputStream(file);
            b = cut(EXPNAME,is, sfn,x1, y1, x2, y2, userPhotoImgStorage);
        }
        if (b){
            ug.put("IMG_S",sfn);
            ug.put("IMG_M",sfn);
            ug.put("IMG_L",sfn);
            ug.put("IMG_T",sfn);
        }

        if (!ug.isEmpty())
            GlobalLogics.getUser().updateUserImg(null, USER_ID, ug.toString(false, false));
        return GlobalLogics.getUser().getSingleUserSimple(USER_ID);
    }
    public String getPostfix(String inputFilePath) {
        return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
    }
    public Iterator<ImageReader> getImageReadersByFormatName(String postFix) {
        if (postFix.toUpperCase().equals("PNG"))
            return ImageIO.getImageReadersByFormatName("png");
        return ImageIO.getImageReadersByFormatName("jpg");
    }
    public boolean cut(String EXPNAME,InputStream inputStream,String fileName,int x1,int y1,int x2,int y2,StaticFileStorage imgStorage) throws IOException {
        boolean b = false;
        ImageInputStream iis = null;
        try {
            String postFix = getPostfix(EXPNAME);
            Iterator<ImageReader> it = getImageReadersByFormatName(postFix);
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(inputStream);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x1, y1, (x2 - x1), (y2 - y1));
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);

            ImageIO.write(bi, postFix, new File(((LocalSFS) imgStorage).directory + fileName));
            b = true;
            return b;
        } catch (Exception e) {
            return b;
        } finally {
            if (iis != null)
                iis.close();
        }
    }

    //用户修改密码
    @WebMethod("user/update_password")
    public String t_user_updatePwd(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_ID = ctx.getUser_id();

        String OLD_PASSWORD = qp.checkGetString("OLD_PASSWORD");
        String pwd = GlobalLogics.getUser().getSingleUserPassword(ctx, USER_ID);
        if (!pwd.equals(OLD_PASSWORD)) {
            throw new ServerException(ErrorCodes.AUTH_OLD_PASSWORD_ERROR, "old password error");
        }

        String PASSWORD = qp.checkGetString("PASSWORD");
        boolean b = GlobalLogics.getUser().updateUserPassword(ctx, USER_ID, PASSWORD);
        if (b){
            return PASSWORD;
        }else {
            return "";
        }
    }


    //用户登录
    @WebMethod("user/admin_login")
    public Record admin_login(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, false, true);
        String t = qp.getString("LOGIN_TYPE", "");

        //过期时间，设置为3天后
        TimeUtils tg = new TimeUtils();
        String EXPIRE_TIME = tg.getOtherDay(DateUtils.nowMillis(), 3);

        int LOGIN_TYPE = 0;
        if (!t.equals(""))
            LOGIN_TYPE = (int) qp.getInt("LOGIN_TYPE", 1);

        Record outTicket = new Record();
        if (LOGIN_TYPE == 1) {   //用本系统的用户名和密码登录
            String USER_NAME = qp.checkGetString("USER_NAME");
            String PASSWORD = qp.checkGetString("PASSWORD");

            Record u = GlobalLogics.getUser().getUserByUserName(USER_NAME);
            if (u.getInt("USER_TYPE")==Constants.USER_TYPE_SJ){
                Record sj = GlobalLogics.getUser().getSjByUser(u.getString("USER_ID"));
                if (sj.isEmpty()){
                    u.put("SJ_ID","");
                    u.put("SJ_NAME","");
                    u.put("SJ_NAME_SX","");
                }else{
                    u.put("SJ_ID",sj.getString("SJ_ID"));
                    u.put("SJ_NAME",sj.getString("SJ_NAME"));
                    u.put("SJ_NAME_SX",sj.getString("SJ_NAME_SX"));
                }
            }
            if (u.getInt("USER_TYPE")==Constants.USER_TYPE_GYS){
                Record gys = GlobalLogics.getUser().getGysByUser(u.getString("USER_ID"));
                if (gys.isEmpty()){
                    u.put("GYS_ID","");
                    u.put("GYS_NAME","");
                    u.put("GYS_NAME_SX","");
                }else{
                    u.put("GYS_ID",gys.getString("GYS_ID"));
                    u.put("GYS_NAME",gys.getString("GYS_NAME"));
                    u.put("GYS_NAME_SX",gys.getString("GYS_NAME_SX"));
                }
            }
            String p = u.getString("USER_PASSWORD");
            if (!p.equals(PASSWORD)) {
                throw new ServerException(ErrorCodes.AUTH_LOGIN_ERROR, "username or password error");
            }
            int VERIFY = (int)u.getInt("VERIFY");
            if (VERIFY==0)
                throw new ServerException(ErrorCodes.AUTH_LOGIN_ERROR_NOT_VERIFY, "username or password error");

            String now_ticket = Constants.genTicket(USER_NAME);
            boolean b = GlobalLogics.getUser().saveUser_ticket(null, u.getString("USER_ID"), now_ticket, EXPIRE_TIME, 1, "LC",u.getString("USER_ID"));
            if (b) {
                outTicket.put("TICKET", now_ticket);
                outTicket.put("USER_NAME", USER_NAME);
                outTicket.put("USER_ID", u.getString("USER_ID"));
                outTicket.put("DISPLAY_NAME", u.getString("DISPLAY_NAME"));
                outTicket.put("DISPLAY_NAME_SX", u.getString("DISPLAY_NAME_SX"));
                outTicket.put("USER_TYPE", u.getInt("USER_TYPE"));
                outTicket.put("SJ_ID", u.getString("SJ_ID"));
                outTicket.put("SJ_NAME", u.getString("SJ_NAME"));
                outTicket.put("SJ_NAME_SX", u.getString("SJ_NAME_SX"));
                outTicket.put("GYS_ID", u.getString("GYS_ID"));
                outTicket.put("GYS_NAME", u.getString("GYS_NAME"));
                outTicket.put("GYS_NAME_SX", u.getString("GYS_NAME_SX"));

                if (u.getString("USER_IMG").length() > 10) {
                    Record ug = Record.fromJson(u.getString("USER_IMG"));
                    outTicket.put("IMG_S", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_S")));
                    outTicket.put("IMG_M", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_M")));
                    outTicket.put("IMG_L", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_L")));
                    outTicket.put("IMG_T", String.format(GlobalConfig.get().getString("service.userPhotoImgPattern", "/userPhotoImg/%s"), ug.getString("IMG_T")));
                    outTicket.put("IMG_S1", ug.getString("IMG_S"));

                } else {
                    if (u.getString("OTHER_IMG").length() > 0){
                        outTicket.put("IMG_S", u.getString("OTHER_IMG"));
                        outTicket.put("IMG_M", u.getString("OTHER_IMG"));
                        outTicket.put("IMG_L", u.getString("OTHER_IMG"));
                        outTicket.put("IMG_T", u.getString("OTHER_IMG"));
                    }else {
                        outTicket.put("IMG_S", "");
                        outTicket.put("IMG_M", "");
                        outTicket.put("IMG_L", "");
                        outTicket.put("IMG_T", "");
                    }
                }
            }
        }

        return outTicket;
    }


    @WebMethod("user/user_get_by_type")
    public RecordSet user_get_by_type(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        int USER_TYPE = (int)qp.checkGetInt("USER_TYPE");
        String DISPLAY_NAME = qp.getString("DISPLAY_NAME", "");
        RecordSet recs = GlobalLogics.getUser().getAllUserByUserType(USER_TYPE, DISPLAY_NAME);
        return recs;
    }

    @WebMethod("user/user_get_all_by_type")
    public RecordSet user_get_all_by_type(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        int USER_TYPE = (int)qp.checkGetInt("USER_TYPE");
        String DISPLAY_NAME = qp.getString("DISPLAY_NAME","");
        RecordSet recs = GlobalLogics.getUser().getAllUserAndDeleteByUserType(USER_TYPE, DISPLAY_NAME);
        return recs;
    }



    @WebMethod("user/user_get_by_type_area")
    public RecordSet user_get_by_type_area(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        int USER_TYPE = (int)qp.checkGetInt("USER_TYPE");
        String DEPARTMENT_ID = qp.getString("DEPARTMENT_ID", "1");
        RecordSet recs = GlobalLogics.getUser().getAllUserByUserTypeArea(USER_TYPE, DEPARTMENT_ID);
        return recs;
    }




    //获取用户密码
    @WebMethod("user/get_single_base_pass")
    public Record User_get_single_base_pass(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_ID = qp.checkGetString("USER_ID");
        return GlobalLogics.getUser().getSingleUserBaseSingleAndPwd(USER_ID);
    }



    private static void createStaticFils(String fileAddr,String fileName,String content){
        File file = new File(fileAddr + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, false);
            out.write(content.getBytes("utf-8"));
            out.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(out);
        }
    }


    //管理员重置用户密码为123456
    @WebMethod("user/admin_create_update_pass")
    public boolean admin_create_update_pass(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_NAME = qp.checkGetString("USER_NAME");
        boolean b = GlobalLogics.getUser().resetPassword(USER_NAME);
        return b;
    }


    //删除用户
    @WebMethod("user/user_delete")
    public boolean User_delete(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_ID = qp.checkGetString("USER_ID");
        return GlobalLogics.getUser().deleteUser(ctx, USER_ID);
    }

    //用户登出
    @WebMethod("user/logout")
    public boolean User_logout(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        return GlobalLogics.getUser().deleteUser_ticket(ctx, ctx.getTicket());
    }

    //获取用户基本信息
    @WebMethod("user/get_single_base")
    public Record User_get_single_base(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_ID = qp.checkGetString("USER_ID");
        return GlobalLogics.getUser().getSingleUserSimple(USER_ID);
    }

    //获取用户详细信息
    @WebMethod("user/get_single_strong")
    public Record User_get_single_strong(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_ID = qp.checkGetString("USER_ID");
        return GlobalLogics.getUser().getSingleUserStrong(ctx, USER_ID);
    }

    //管理员获取用户
    @WebMethod("user/admin_user_get_page_list")
    public Record admin_user_get_page_list(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SEARCH_STR = qp.getString("SEARCH_STR", "");
        int USER_TYPE = (int)qp.getInt("USER_TYPE",2);
        int VERIFY =  (int)qp.getInt("VERIFY",9);
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
        Record rec = GlobalLogics.getUser().getAllUserPageList(ctx, SEARCH_STR, USER_TYPE, VERIFY, PAGE, COUNT);
        return rec;
    }

    @WebMethod("user/admin_user_get_all_sel")
    public RecordSet admin_user_get_all_sel(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        int USER_TYPE = (int)qp.getInt("USER_TYPE",2);
        RecordSet recS = GlobalLogics.getUser().getAllUserSel(USER_TYPE);
        return recS;
    }

    //管理员获取用户
    @WebMethod("user/get_single_edit")
    public Record get_single_edit(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_ID = qp.checkGetString("USER_ID");
        Record rec = GlobalLogics.getUser().getUserById(USER_ID);
        rec.removeColumns("USER_PASSWORD");
        return rec;
    }

    @WebMethod("")
    public NoResponse redirect8080(HttpServletResponse resp) throws IOException {
        resp.sendRedirect("http://localhost/");
        return null;
    }
    @WebMethod("user/lock_user")
    public boolean lock_user(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String USER_ID = qp.checkGetString("USER_ID");
        return GlobalLogics.getUser().lock_user(USER_ID);
    }


    @WebMethod("user/partner_admin_create")
    public Record partner_admin_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record out_rec = new Record();
        String SJ_ID = qp.checkGetString("SJ_ID");
        String PARTNER_NO = String.valueOf(RandomUtils.generateId());

        String PROVINCE = qp.checkGetString("PROVINCE");
        String CITY = qp.checkGetString("CITY");
        String AREA = qp.checkGetString("AREA");
        String ADDR = qp.checkGetString("ADDR");
        String PARTNER_NAME = qp.checkGetString("PARTNER_NAME");
        String TRADE = qp.checkGetString("TRADE");
        String TYPE = qp.checkGetString("TYPE");
        String MOBILE = qp.checkGetString("MOBILE");
        String CONTACT = qp.checkGetString("CONTACT");

        PARTNER_NAME = Constants.replaceErrStr(PARTNER_NAME);
        ADDR = Constants.replaceErrStr(ADDR);
        CONTACT = Constants.replaceErrStr(CONTACT);
        MOBILE = Constants.replaceErrStr(MOBILE);
        TRADE = Constants.replaceErrStr(TRADE);

        boolean b = GlobalLogics.getUser().adminSavePartner( ctx,  SJ_ID,  PARTNER_NO,  PARTNER_NAME,  TRADE,  TYPE,  MOBILE,  CONTACT,  PROVINCE,  CITY,  AREA,  ADDR);
        if (!b){
            out_rec.put("status",0);
            out_rec.put("message","创建店铺失败!");
            return out_rec;
        }  else{

            out_rec.put("status",1);
            out_rec.put("message","创建店铺成功");
            return out_rec;
        }
    }

    @WebMethod("user/partner_admin_update")
    public Record partner_admin_update(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record out_rec = new Record();
        String PARTNER_NO = qp.checkGetString("PARTNER_NO");

        String PROVINCE = qp.checkGetString("PROVINCE");
        String CITY = qp.checkGetString("CITY");
        String AREA = qp.checkGetString("AREA");
        String ADDR = qp.checkGetString("ADDR");
        String PARTNER_NAME = qp.checkGetString("PARTNER_NAME");
        String TRADE = qp.checkGetString("TRADE");
        String TYPE = qp.checkGetString("TYPE");
        String MOBILE = qp.checkGetString("MOBILE");
        String CONTACT = qp.checkGetString("CONTACT");

        PARTNER_NAME = Constants.replaceErrStr(PARTNER_NAME);
        ADDR = Constants.replaceErrStr(ADDR);
        CONTACT = Constants.replaceErrStr(CONTACT);
        MOBILE = Constants.replaceErrStr(MOBILE);
        TRADE = Constants.replaceErrStr(TRADE);

        boolean b = GlobalLogics.getUser().adminUpdatePartner(PARTNER_NO, PARTNER_NAME, TRADE, TYPE, MOBILE, CONTACT, PROVINCE, CITY, AREA, ADDR);
        if (!b){
            out_rec.put("status",0);
            out_rec.put("message","修改店铺失败!");
            return out_rec;
        }  else{

            out_rec.put("status",1);
            out_rec.put("message","修改店铺成功");
            return out_rec;
        }
    }


    @WebMethod("user/partner_get_single")
    public Record partner_get_single(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String PARTNER_NO = qp.checkGetString("PARTNER_NO");
        Record b = GlobalLogics.getUser().getSinglePartnerByNo(PARTNER_NO);
        return b;
    }

    @WebMethod("user/partner_delete")
    public boolean partner_delete(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String PARTNER_NO = qp.checkGetString("PARTNER_NO");
        boolean b = GlobalLogics.getUser().adminDeletePartner(PARTNER_NO);
        return b;
    }


    @WebMethod("user/partner_get_all")
    public RecordSet partner_get_all(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.checkGetString("SJ_ID");
        RecordSet recs = GlobalLogics.getUser().getUserPartnerByUser(SJ_ID);
        return recs;
    }
    @WebMethod("user/sj_partner_get_all")
    public RecordSet sj_partner_get_all(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.checkGetString("SJ_ID");
        RecordSet recs = GlobalLogics.getUser().getUserPartnerByUser(SJ_ID);
        return recs;
    }
    @WebMethod("user/sj_get_all")
    public RecordSet sj_get_all(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        RecordSet recs = GlobalLogics.getUser().getAllSj();
        return recs;
    }

    @WebMethod("user/sj_get_all_by_gys")
    public RecordSet sj_get_all_by_gys(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        RecordSet recs = GlobalLogics.getUser().getAllGysSj(GYS_ID);
        return recs;
    }

    @WebMethod("user/gys_delete")
    public boolean gys_delete(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        return GlobalLogics.getUser().deleteGys(GYS_ID);
    }

    @WebMethod("user/gys_get_all")
    public RecordSet gys_get_all(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SEARCH_STR = qp.getString("SEARCH_STR", "");
        String SJ_ID = qp.checkGetString("SJ_ID");
        return GlobalLogics.getUser().getAllSJGys(SEARCH_STR, SJ_ID);
    }

    @WebMethod("user/sj_gys_get_all")
    public RecordSet sj_gys_get_all(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SEARCH_STR = qp.getString("SEARCH_STR", "");
        Record SJ = GlobalLogics.getUser().getSjByUser(ctx.getUser_id());
        if (SJ.isEmpty())
            return new RecordSet();
        String SJ_ID = SJ.checkGetString("SJ_ID");
        return GlobalLogics.getUser().getAllSJGys(SEARCH_STR, SJ_ID);
    }

    @WebMethod("user/get_sj_by_user")
    public Record get_sj_by_user(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record SJ = GlobalLogics.getUser().getSjByUser(ctx.getUser_id());
        if (SJ.isEmpty())
            return new Record();
        return SJ;
    }

    @WebMethod("user/sj_gys_create")
    public Record sj_gys_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, false);

        Record out_rec = new Record();
        String SJ_ID = qp.checkGetString("SJ_ID");

        String GYS_NAME = qp.checkGetString("GYS_NAME");
        String GYS_NAME_SX = qp.checkGetString("GYS_NAME_SX");
        String ADDR = qp.getString("ADDR", "");
        String CONTACT_USER = qp.getString("CONTACT_USER","");
        String PHONE = qp.getString("PHONE","");
        String KEYWORD="";
        String SUPPORT_TYPE = qp.getString("SUPPORT_TYPE","商品");
        String MEMO = qp.getString("MEMO","");
        String USER_NAME = qp.checkGetString("USER_NAME");
        USER_NAME = Constants.replaceErrStr(USER_NAME);
        //先检查用户名是否存在
        Record u = GlobalLogics.getUser().getUserByUserName(USER_NAME);
        if (!u.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","用户名已经存在了，需要更换");
            return out_rec;
        }

        GYS_NAME = Constants.replaceErrStr(GYS_NAME);
        ADDR = Constants.replaceErrStr(ADDR);
        GYS_NAME_SX = Constants.replaceErrStr(GYS_NAME_SX);
        ADDR = Constants.replaceErrStr(ADDR);
        CONTACT_USER = Constants.replaceErrStr(CONTACT_USER);
        PHONE = Constants.replaceErrStr(PHONE);


        String DISPLAY_NAME = qp.checkGetString("DISPLAY_NAME");
        String DISPLAY_NAME_SX = qp.checkGetString("DISPLAY_NAME_SX");

        DISPLAY_NAME = Constants.replaceErrStr(DISPLAY_NAME);
        DISPLAY_NAME_SX = Constants.replaceErrStr(DISPLAY_NAME_SX);

        String USER_PASSWORD = qp.checkGetString("USER_PASSWORD");
        int USER_TYPE = Constants.USER_TYPE_GYS;
        String USER_ID = String.valueOf(RandomUtils.generateId());
        String user_id = GlobalLogics.getUser().saveUser(ctx, USER_ID, USER_NAME, DISPLAY_NAME, DISPLAY_NAME_SX,USER_PASSWORD, USER_TYPE);
        if (!USER_ID.equals(user_id)){
            out_rec.put("status",0);
            out_rec.put("message","创建登录用户失败");
            return out_rec;
        } else{
            //String GYS_ID, String USER_ID, String SJ_ID, String GYS_NAME, String GYS_NAME_SX, int GYS_TYPE, String ADDR, String CONTACT_USER, String PHONE, String KEYWORD, String SUPPORT_TYPE, String MEMO
            String GYS_ID = String.valueOf(RandomUtils.generateId());
            String back_gys_id= GlobalLogics.getUser().SaveGys(ctx,GYS_ID,USER_ID,SJ_ID,GYS_NAME,GYS_NAME_SX,1,ADDR,CONTACT_USER,PHONE,KEYWORD,SUPPORT_TYPE,MEMO);
            if (!GYS_ID.equals(back_gys_id)){
                GlobalLogics.getUser().deleteUser(USER_ID);
                out_rec.put("status",0);
                out_rec.put("message","创建供应商失败!");
                out_rec.put("user_id",user_id);
                return out_rec;
            }  else{

                out_rec.put("status",1);
                out_rec.put("message","创建成功");
                out_rec.put("user_id", "");
                return out_rec;
            }
        }
    }

    @WebMethod("user/sj_gys_edit")
    public Record sj_gys_edit(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record out_rec = new Record();
        String GYS_ID = qp.checkGetString("GYS_ID");
        Record gys = GlobalLogics.getUser().getSingleGysBase(GYS_ID);
        String USER_ID = gys.getString("USER_ID");

        String GYS_NAME = qp.checkGetString("GYS_NAME");
        String GYS_NAME_SX = qp.checkGetString("GYS_NAME_SX");
        String ADDR = qp.getString("ADDR", "");
        String CONTACT_USER = qp.getString("CONTACT_USER", "");
        String PHONE = qp.getString("PHONE", "");
        String KEYWORD="";
        String SUPPORT_TYPE = qp.getString("SUPPORT_TYPE", "商品");
        String MEMO = qp.getString("MEMO", "");

        String DISPLAY_NAME = qp.checkGetString("DISPLAY_NAME");
        String DISPLAY_NAME_SX = qp.checkGetString("DISPLAY_NAME_SX");

        GYS_NAME = Constants.replaceErrStr(GYS_NAME);
        ADDR = Constants.replaceErrStr(ADDR);
        GYS_NAME_SX = Constants.replaceErrStr(GYS_NAME_SX);
        ADDR = Constants.replaceErrStr(ADDR);
        CONTACT_USER = Constants.replaceErrStr(CONTACT_USER);
        PHONE = Constants.replaceErrStr(PHONE);
        DISPLAY_NAME = Constants.replaceErrStr(DISPLAY_NAME);
        DISPLAY_NAME_SX = Constants.replaceErrStr(DISPLAY_NAME_SX);

        boolean b = GlobalLogics.getUser().updateGys(GYS_ID, USER_ID, GYS_NAME, GYS_NAME_SX, ADDR, CONTACT_USER, PHONE, KEYWORD, SUPPORT_TYPE, MEMO, DISPLAY_NAME, DISPLAY_NAME_SX);
        if (!b){
            out_rec.put("status",0);
            out_rec.put("message","修改供应商失败!");
            return out_rec;
        }  else{

            out_rec.put("status",1);
            out_rec.put("message","修改供应商成功");
            return out_rec;
        }
    }

    @WebMethod("user/gys_get_single")
    public Record gys_get_single(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        return GlobalLogics.getUser().getSingleGys(GYS_ID);
    }
    @WebMethod("user/sj_delete")
    public boolean sj_delete(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.checkGetString("SJ_ID");
        return GlobalLogics.getUser().deleteSj(SJ_ID);
    }


    //管理员获取全部商家
    @WebMethod("user/admin_sj_get_page_list")
    public Record admin_sj_get_page_list(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SEARCH_STR = qp.getString("SEARCH_STR", "");
        int SJ_TYPE = (int)qp.getInt("USER_TYPE",9);
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
        Record rec = GlobalLogics.getUser().getAllSjPageList(ctx, SEARCH_STR, SJ_TYPE, PAGE, COUNT);
        return rec;
    }



    @WebMethod("user/sj_create")
    public Record sj_create(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);

        Record out_rec = new Record();
        String USER_NAME = qp.checkGetString("USER_NAME");
        //先检查用户名是否存在
        Record u = GlobalLogics.getUser().getUserByUserName(USER_NAME);
        if (!u.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","用户名已经存在了，需要更换");
            return out_rec;
        }
        String DISPLAY_NAME = qp.checkGetString("DISPLAY_NAME");
        String DISPLAY_NAME_SX = qp.checkGetString("DISPLAY_NAME_SX");
        String USER_PASSWORD = qp.checkGetString("USER_PASSWORD");

        String SJ_NAME = qp.checkGetString("SJ_NAME");
        String SJ_NAME_SX = qp.checkGetString("SJ_NAME_SX");
        int SJ_TYPE=1;
        String ADDR = qp.getString("ADDR", "");
        String CONTACT = qp.getString("CONTACT", "");
        String MOBILE = qp.getString("MOBILE", "");
        String EMAIL = qp.getString("EMAIL", USER_NAME);
        String MEMO = qp.getString("MEMO", "");

        SJ_NAME = Constants.replaceErrStr(SJ_NAME);
        ADDR = Constants.replaceErrStr(ADDR);
        SJ_NAME_SX = Constants.replaceErrStr(SJ_NAME_SX);
        ADDR = Constants.replaceErrStr(ADDR);
        CONTACT = Constants.replaceErrStr(CONTACT);
        MOBILE = Constants.replaceErrStr(MOBILE);
        DISPLAY_NAME = Constants.replaceErrStr(DISPLAY_NAME);
        DISPLAY_NAME_SX = Constants.replaceErrStr(DISPLAY_NAME_SX);


        int USER_TYPE = Constants.USER_TYPE_SJ;
        String USER_ID = String.valueOf(RandomUtils.generateId());
        String user_id = GlobalLogics.getUser().saveUser(ctx, USER_ID, USER_NAME, DISPLAY_NAME, DISPLAY_NAME_SX, USER_PASSWORD, USER_TYPE);
        if (!USER_ID.equals(user_id)){
            out_rec.put("status",0);
            out_rec.put("message","创建登录用户失败");
            return out_rec;
        } else{
            String SJ_ID = String.valueOf(RandomUtils.generateId());
            String back_gys_id= GlobalLogics.getUser().SaveSj(ctx, SJ_ID, USER_ID,SJ_NAME, SJ_NAME_SX, SJ_TYPE,
             MOBILE, CONTACT, EMAIL, MEMO, ADDR);
            if (!SJ_ID.equals(back_gys_id)){
                GlobalLogics.getUser().deleteUser(USER_ID);
                out_rec.put("status",0);
                out_rec.put("message","创建商家失败!");
                out_rec.put("user_id",user_id);
                return out_rec;
            }  else{

                out_rec.put("status",1);
                out_rec.put("message","创建成功");
                out_rec.put("user_id", "");
                return out_rec;
            }
        }
    }

    @WebMethod("user/sj_update")
    public Record sj_update(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);

        Record out_rec = new Record();
        String SJ_ID = qp.checkGetString("SJ_ID");
        Record old_sj = GlobalLogics.getUser().getSingleSj(SJ_ID);

        String SJ_NAME = qp.checkGetString("SJ_NAME");
        String SJ_NAME_SX = qp.checkGetString("SJ_NAME_SX");
        int SJ_TYPE=1;
        String ADDR = qp.getString("ADDR", "");
        String CONTACT = qp.getString("CONTACT", "");
        String MOBILE = qp.getString("MOBILE", "");
        String EMAIL = qp.getString("EMAIL", "");
        String MEMO = qp.getString("MEMO", "");

        String DISPLAY_NAME = qp.checkGetString("DISPLAY_NAME");
        String DISPLAY_NAME_SX = qp.checkGetString("DISPLAY_NAME_SX");

        SJ_NAME = Constants.replaceErrStr(SJ_NAME);
        ADDR = Constants.replaceErrStr(ADDR);
        SJ_NAME_SX = Constants.replaceErrStr(SJ_NAME_SX);
        ADDR = Constants.replaceErrStr(ADDR);
        CONTACT = Constants.replaceErrStr(CONTACT);
        MOBILE = Constants.replaceErrStr(MOBILE);
        DISPLAY_NAME = Constants.replaceErrStr(DISPLAY_NAME);
        DISPLAY_NAME_SX = Constants.replaceErrStr(DISPLAY_NAME_SX);

        boolean b = GlobalLogics.getUser().updateSj(ctx, SJ_ID, old_sj.getString("USER_ID"), SJ_NAME, SJ_NAME_SX, SJ_TYPE,
                MOBILE, CONTACT, EMAIL, MEMO, ADDR, DISPLAY_NAME, DISPLAY_NAME_SX);

        if (!b){
            out_rec.put("status",0);
            out_rec.put("message","修改商家信息失败");
            return out_rec;
        } else{
            out_rec.put("status",1);
            out_rec.put("message","修改商家信息成功");
            out_rec.put("user_id", "");
            return out_rec;
        }
    }

    @WebMethod("user/sj_get_single")
    public Record sj_get_single(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String SJ_ID = qp.checkGetString("SJ_ID");
        Record rec =  GlobalLogics.getUser().getSingleSj(SJ_ID);
        return rec;
    }

    @WebMethod("user/save_partner_kw")
    public Record save_partner_kw(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        Record out_rec = new Record();
        String PARTNER_NO = qp.checkGetString("PARTNER_NO");
        String KW_ID = qp.checkGetString("KW_ID");
         boolean b =  GlobalLogics.getUser().savePartnerKw( ctx,  PARTNER_NO,  KW_ID);
        if (!b){
            out_rec.put("status",0);
            out_rec.put("message","保存货位失败!");
            return out_rec;
        }  else{

            out_rec.put("status",1);
            out_rec.put("message","保存货位成功");
            out_rec.put("user_id", "");
            return out_rec;
        }
    }

    @WebMethod("user/get_all_gys_sj")
    public RecordSet get_all_gys_sj(HttpServletRequest req, QueryParams qp) throws IOException {
        Context ctx = PortalContext.getContext(req, qp, true, true);
        String GYS_ID = qp.checkGetString("GYS_ID");
        return GlobalLogics.getUser().getAllGysSj(GYS_ID);
    }

    @WebMethod("user/sj_partner_import")
    public Record sj_partner_import(HttpServletRequest req, QueryParams qp) throws UnsupportedEncodingException {
        Context ctx = PortalContext.getContext(req, qp, true, false);
        FileItem file_item = qp.getFile("Filedata");
        String SJ_ID = qp.getString("SJ_ID", "3204993994922383998");

        Record return_rec = new Record();

        if (file_item != null && file_item.getSize() > 0) {
            return_rec = importSjPartner(ctx, file_item, SJ_ID, return_rec);
        }
        return return_rec;
    }

    private Record importSjPartner(Context ctx,FileItem file_item,String SJ_ID,Record return_rec) {
        org.apache.poi.ss.usermodel.Workbook wb = null;
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

        org.apache.poi.ss.usermodel.Sheet sheet = OrderConstants.getSheetByNum(wb, 0);
        int lastRowNum = sheet.getLastRowNum();
        RecordSet data_out = new RecordSet();
        int ALL_ERR_COUNT = 0;
        String err_str = "";

        if (sheet != null) {
            for (int j = 0; j <= lastRowNum; j++) {
                Row row = sheet.getRow(j);
                if (row.getCell(0).getStringCellValue().equals("")
                        || row.getCell(1).getStringCellValue().equals("")
                        || row.getCell(2).getStringCellValue().equals("")
                        || row.getCell(0).getStringCellValue().contains("门店")
                        || row.getCell(1).getStringCellValue().contains("联系")
                        ){
                    continue;
                }


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

                // 0,门店名称
                // 1,联系人
                // 2,联系电话
                // 3,省
                // 4,市
                // 5,区
                // 6,地址	   ---------
                String PARTNER_NO =  RandomUtils.generateStrId();
                String PARTNER_NAME =  ls_cells.get(0).trim();
                String TRADE = "餐饮";
                String TYPE = "实体店铺";
                String MOBILE =  ls_cells.get(2).trim();
                String CONTACT =  ls_cells.get(1).trim();

                String PROVINCE_NAME =  ls_cells.get(3).trim();
                String CITY_NAME =  ls_cells.get(4).trim();
                String AREA_NAME =  ls_cells.get(5).trim();
                if (AREA_NAME.toString().trim().length()<=0){
                    err_str +=PARTNER_NAME+"-区域名称不正确,为空,后续请单独导入";
                    ALL_ERR_COUNT +=1;
                    continue;
                }

                String AREA_ID = "";
                String CITY_ID = "";
                String PROVINCE_ID = "";
                Record area = GlobalLogics.getBaseLogic().getAreaByName(AREA_NAME).getFirstRecord();
                if (area.isEmpty()){
                    err_str +=PARTNER_NAME+"-区域名称不正确,不存在,后续请单独导入";
                    ALL_ERR_COUNT +=1;
                    continue;
                } else{
                    AREA_ID = area.getString("AREA_ID");
                    CITY_ID = area.getString("CITY_ID");
                    Record city = GlobalLogics.getBaseLogic().getCityById(CITY_ID);
                    PROVINCE_ID = city.getString("PROVINCE_ID");
                }

                String ADDR =  ls_cells.get(6).trim();
                boolean b = GlobalLogics.getUser().adminSavePartner(ctx, SJ_ID, PARTNER_NO, PARTNER_NAME, TRADE, TYPE, MOBILE, CONTACT, PROVINCE_ID, CITY_ID, AREA_ID, ADDR);
            }

        }

        if (ALL_ERR_COUNT==0){
            return_rec.put("IMPORTS_STATUS", "1");
            return_rec.put("ERROR_STR", "");
        } else {
            return_rec.put("IMPORTS_STATUS", "0");
            return_rec.put("ERROR_STR", err_str);
        }
        return return_rec;
    }
}

