package com.fwms.common;

import com.fwms.basedevss.ServerException;
import com.fwms.basedevss.base.conf.Configuration;
import com.fwms.basedevss.base.conf.GlobalConfig;
import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.data.RecordSet;
import com.fwms.basedevss.base.sfs.SFSUtils;
import com.fwms.basedevss.base.sfs.StaticFileStorage;
import com.fwms.basedevss.base.sfs.local.LocalSFS;
import com.fwms.basedevss.base.util.DateUtils;
import com.fwms.basedevss.base.util.Encoders;
import com.fwms.basedevss.base.util.StringUtils2;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Constants {

    //用户注册类型    1为自己注册 2为第三方用户认证后注册
    public static final int USER_REGIST_TYPE_FWMS = 1;
    public static final int USER_REGIST_TYPE_OTHER = 2;
    public static final int USER_REGIST_TYPE_ADMIN = 9;



    //用户类型    1为个人用户 2为各种管理员  9为超级管理员  0为未登录
    //11为投资者
    public static final int USER_TYPE_GYS = 1;
    public static final int USER_TYPE_SJ = 2;
    public static final int USER_TYPE_KG = 3;
    public static final int USER_TYPE_SUPER_ADMIN = 9;


    //访问设备类型，1为手机，2为平板，3为TV  ,9为后台管理
    public static final int VISIT_DEVICE_TYPE_MOBILE = 1;
    public static final int VISIT_DEVICE_TYPE_HD = 2;
    public static final int VISIT_DEVICE_TYPE_TV = 3;
    public static final int VISIT_DEVICE_TYPE_WEB = 9;

    //支付类型
    public static final int PAY_TYPE_ZFB = 1;
    public static final int PAY_TYPE_YL = 2;
    public static final int PAY_TYPE_WEIXIN = 3;
    //购买类型
    public static final int BUY_TYPE_MONEY = 1;
    public static final int BUY_TYPE_SCORE = 2;
    public static final int BUY_TYPE_CARD = 3;
    public static final int BUY_TYPE_MONEY_CARD = 4;

    //订单管家
    public static final int ORDER_PLAN=10;//订单管家基本配置
    public static final int ORDER_PLAN_MESSAGE=11;//订单管家回复设置
    public static final int ORDER_PLAN_ORDER_MESSAGE=12;//订单管家订单下单回复设置
    //交易类型1收款2退款
    public static final int COLLECT_TYPE_RECEIVE = 1;
    public static final int COLLECT_TYPE_BACKOUT = 2;

    public static final String SHORTURL_MATCH = "z";

    public static final String OTHER_DL_QQ = "QQDL";
    public static final String OTHER_DL_SINA = "SINADL";
    public static final String OTHER_DL_WEIXIN = "WEIXINDL";

    public static final String BUDGET_CHANGE_ADD_ONLY = "追加";
    public static final String BUDGET_CHANGE_REDUCE_ONLY = "减少";
    public static final String BUDGET_CHANGE_ONLY = "转移";

    public static final String BUDGET_ID_CG = "3043922052855098181";
    public static final String EXPENSE_ID_CG = "3044074860231560855";

    public static final int COST_DATA_TYPE_APPLY = 1;
    public static final int COST_DATA_TYPE_CG = 2;
    public static final int COST_DATA_TYPE_BL = 5;

    public static final String AREA_HUADONG = "山东省,江苏省,安徽省,浙江省,福建省,上海,上海市";
    public static final String AREA_HUANAN = "广东省,广西省,海南省";
    public static final String AREA_HUAZHONG = "湖北省,湖南省,河南省,江西省";
    public static final String AREA_HUABEI = "北京,北京市,天津,天津市,河北省,山西省,内蒙古自治区";
    public static final String AREA_XIBEI = "宁夏回族自治区,新疆维吾尔自治区,青海省,陕西省,甘肃省";
    public static final String AREA_XINAN = "四川省,云南省,贵州省,西藏自治区,重庆,重庆市";
    public static final String AREA_DONGBEI = "辽宁省,吉林省,黑龙江省";
    public static final String AREA_GANGAOTAI = "台湾,香港,澳门";


    //===============以上是对象的分类====================


    //验证手机号码
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isEmailAddress(String address) {
        Pattern p = Pattern.compile("\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}");
        Matcher m = p.matcher(address);
        return m.matches();
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    public boolean isAddrEmail(String addr) {
        boolean isEmail = false;
        String emailAddressPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";     //手机"1[0-9]{10}"
        Pattern pattern = Pattern.compile(emailAddressPattern, Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(addr);
        while (match.find()) {
            isEmail = true;
        }
        return isEmail;
    }
    public static String dateString2longYearMonth(long d) {
        String date = new SimpleDateFormat("yyyy-MM").format(new Date(d));
        return date;
    }

    public static String dateString2longYearMonthSimple(long d) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date(d));
        return date;
    }

    public static String dateLongToString(long d) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(d));
        return date;
    }

    public static String dateLongToStringHour(long d) {
        String date = new SimpleDateFormat("HH").format(new Date(d));
        return date;
    }

    public static String dateLongToStringShort(long d) {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(d));
        return date;
    }

    public static String dateLongToStringShortShort(long d) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date(d));
        return date;
    }

    public static String dateLongToStringShortShort2(long d) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(d));
        return date;
    }

    public static long dateString2long(String in_time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        long f = 0;
        try {
            date = format.parse(in_time);
            f = Long.parseLong(String.valueOf(date.getTime()));
        } catch (ParseException e) {
        }

        return f;
    }

    public static String getNowYear() {
        String date = new SimpleDateFormat("yyyy").format(new Date(DateUtils.nowMillis()));
        return date;
    }
    public static String getNowMonth() {
        String date = new SimpleDateFormat("MM").format(new Date(DateUtils.nowMillis()));
        return date;
    }
    public static String getNowDay() {
        String date = new SimpleDateFormat("dd").format(new Date(DateUtils.nowMillis()));
        return date;
    }

    public static boolean uploadFile(FileItem file_item, String real_fileName, StaticFileStorage fileStorage) {
        String file_name = file_item.getName();
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_name == null || file_name.trim().length() == 0) {
            legal_file = false;
        }
        if (file_size <= 0) {
            legal_file = false;
        }
        if (legal_file) {
            // bugsImgStorage wanghanxiao OSS done

            SFSUtils.saveUpload(file_item, fileStorage, real_fileName);
            return true;
        } else {
            return false;
        }
    }

    public static boolean uploadFileSavedLocal(FileItem file_item, String real_fileName, StaticFileStorage fileStorage) {
        String file_name = file_item.getName();
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_name == null || file_name.trim().length() == 0) {
            legal_file = false;
        }
        if (file_size <= 0) {
            legal_file = false;
        }
        if (legal_file) {
            SFSUtils.saveUpload(file_item, fileStorage, real_fileName);
            return true;
        } else {
            return false;
        }
    }

    //图片两个压缩方法一
    public static boolean uploadImageFileAndScare(FileItem file_item, String real_fileName, StaticFileStorage photoStorage, int width_0) {
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_size <= 0) {
            legal_file = false;
        }
        if (file_size > 10 * 1024 * 1024) {
            System.out.println("too big file upload. max size is 10M, current file size is [" + file_size + "]");
            legal_file = false;
        }
        if (legal_file) {
            int width, height, bWidth = 0, bHeight = 0;

            ImageIO.setUseCache(false);
            BufferedImage image = null;
            try {
                image = ImageIO.read(file_item.getInputStream());
                width = image.getWidth();
                height = image.getHeight();

                bWidth = width_0;
                bHeight = (int) width_0 * height / width;

                String format = real_fileName.substring(real_fileName.lastIndexOf(".") + 1, real_fileName.length());
                if (!format.toUpperCase().equals("JPG") || !format.toUpperCase().equals("JPEG") || !format.toUpperCase().equals("PNG")
                        || !format.toUpperCase().equals("GIF") || !format.toUpperCase().equals("BMP") || !format.toUpperCase().equals("TIFF")
                        || !format.toUpperCase().equals("PSD") || !format.toUpperCase().equals("SVG"))
                    format = "jpg";
                SFSUtils.saveScaledUploadImage(file_item, photoStorage, real_fileName, Integer.toString(bWidth), Integer.toString(bHeight), format);
            } catch (IOException e) {
            }
            return true;
        } else {
            return false;
        }
    }
    public static boolean isImage(String CT) {
        List<String> allowType = Arrays.asList("image/bmp", "image/png", "image/gif", "image/jpg", "image/jpeg", "image/pjpeg");
        return allowType.contains(CT);

    }
    //图片两个压缩方法二
    public static boolean uploadImageFileThumbnail(String oldFileName, FileItem file_item, String real_fileName, StaticFileStorage photoStorage, int width_0) {
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_size <= 0) {
            legal_file = false;
        }
        if (file_size > 10 * 1024 * 1024) {   //大于10MB失败
            legal_file = false;
        }
        if (legal_file) {
            int width, height, bWidth = 0, bHeight = 0;

            ImageIO.setUseCache(false);
            BufferedImage image = null;
            try {
                image = ImageIO.read(file_item.getInputStream());
                width = image.getWidth();
                height = image.getHeight();

                bWidth = width_0;
                bHeight = (int) width_0 * height / width;

                String path = ((LocalSFS) photoStorage).directory;
                // bugsImgStorage wanghanxiao OSS
//                uploadFileOSS(file_item, AliyunOSSDir.USER_PHOTO_IMG_STORAGE, real_fileName);

                Thumbnails.of(new File(path + oldFileName))
                        .size(bWidth, bHeight)
                        .toFile(new File(path + real_fileName));

            } catch (IOException e) {
            } finally {
                image = null;
            }
            return true;
        } else {
            return false;
        }
    }


    public static String genTicket(String loginName) {
        return Encoders.toBase64(loginName + "_" + DateUtils.nowMillis() + "_" + new Random().nextInt(10000));
    }


    public static String generateHash(String sid, String username, String password) {
        String sourceHash = sid + username + password;
        String hash = "";
        try {
            byte[] baseBytes = Base64.encodeBase64(DigestUtils.md5(sourceHash.getBytes("UTF-8")));
            hash = new String(baseBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return hash;
    }

    public static String strLen1To2(String inStr) {
        if (inStr.length() == 1)
            inStr = "0" + inStr;
        return inStr;
    }

    public static boolean checkHash(String expectHash, String sid, String username, String password) {
        String actualHash = generateHash(sid, username, password);
        return (StringUtils.isNotEmpty(expectHash)) && (expectHash.equalsIgnoreCase(actualHash));
    }

    public static String matchBadStr(String instr, String matchStr) {
        String[] myStr = StringUtils2.splitArray(matchStr, " ", true);
        for (int i = 0; i < myStr.length; i++) {
            instr = instr.replace(myStr[i], "**");
        }
        return instr;
    }

    public static String one2TwoStr(String instr) {
        if (instr.length() == 1)
            instr = "0" + instr;
        return instr;
    }

    public static String getPercent(int x, int total) {
        String result = "";//接受百分比的值
        if (total == 0 || x == 0)
            return "0.00%";
        result = String.valueOf((double) (Math.round(x * 100 / total))) + "%";
        return result;
    }

    public  static String getTwoXs(float in){
        if (in==0)
            return "0.00";
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        String dd = df.format(in);
        return dd;
    }

    public static String getOrignalImaUrl(String old_url, String width0) {
        if (old_url.indexOf("Storage") < 0)
            return old_url;

        String[] arrayName = StringUtils2.splitArray(old_url, "/", true);


        String lastName = arrayName[arrayName.length - 1];

        String preName = old_url.replace(lastName, "");
        String[] arrayNameLast = StringUtils2.splitArray(lastName, ".", true);

        String newName = preName + arrayNameLast[0] + "_" + width0 + "." + arrayNameLast[1];
        return newName;
    }

    //判断字符串是否含中文
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    //判断一个字符串出现的次数
    public static int countInString(String text, String sub) {
        int count = 0, start = 0;
        while ((start = text.indexOf(sub, start)) >= 0) {
            start += sub.length();
            count++;
        }
        return count;
    }

    public static final String allChar = "0123456789abcdefghjklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";

    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }


    //日期格式化
    public static String formatStandardDate(String dd) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String dd = "2015-9-3   13:8:9";
        Date ddd = format.parse(dd);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ddd);
        return date;
    }

    public static String replaceErrStr(String cv){
        if (cv.equals(""))
            return cv;
        cv = cv.replaceAll("'","");
        cv = cv.replaceAll("\\+","");
        cv = cv.replaceAll("&","");
        cv = cv.replaceAll("=","");
        return cv;
    }

    //标准化EXCEL导入时的各种时间格式
    //2015-02-27 12:33
    //2015-03-17 14:02:30
    //4/10/15 9:42
    public static String formatExcelStandardDateNum(String dd) {
        //20150511214234
        String date1="";
        String date2="";
        String date3="";
        String date4="00";
        String date5="00";
        String date6="00";
        if (dd.length() != 14)
            return DateUtils.now();

        date1 = dd.substring(0, 4);
        date2 = dd.substring(4, 6);
        date3 = dd.substring(6, 8);

        date4 = dd.substring(8, 10);
        date5 = dd.substring(10, 12);
        date6 = dd.substring(12, 14);
        return date1+"-"+date2+"-"+date3 + " " + date4+":"+date5+":"+date6;
    }

    public static String formatExcelStandardDate(String dd) {
        dd = dd.replace("AM","").replace("PM","").replace("   "," ");
        dd = dd.replace("  "," ").replace("  "," ");

        String date1="";
        String date2="";
        String date3="";
        String date4="00";
        String date5="00";
        String date6="00";
        if (dd.contains("/")) {
            String D0[] = dd.split(" ");
            String d01[] = D0[0].split("/");
            if (d01.length==3){
                date1 =YearLen2To4(d01[2]);
                date2 =DayLen1To2(d01[0]);
                date3 =DayLen1To2(d01[1]);
            }
            String d02[] = D0[1].split(":");
            if (d02.length==2){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
            }
            if (d02.length==3){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
                date6 =DayLen1To2(d02[2]);
            }
        } else if (dd.contains("-")){
            String D0[] = dd.split(" ");
            String d01[] = D0[0].split("-");
            if (d01.length==3){
                date1 =YearLen2To4(d01[0]);
                date2 =DayLen1To2(d01[1]);
                date3 =DayLen1To2(d01[2]);
            }
            String d02[] = D0[1].split(":");
            if (d02.length==2){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
            }
            if (d02.length==3){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
                date6 =DayLen1To2(d02[2]);
            }
        }
        return date1+"-"+date2+"-"+date3 + " " + date4+":"+date5+":"+date6;
    }

    public static String formatExcelStandardDateForKd(String dd) {
        String date1="";
        String date2="";
        String date3="";
        if (dd.contains("/")) {
            String d01[] = dd.split("/");
            if (d01.length==3){
                date1 =YearLen2To4(d01[0]);
                date2 =DayLen1To2(d01[1]);
                date3 =DayLen1To2(d01[2]);
            }
        } else if (dd.contains("-")){
            String d01[] = dd.split("-");
            if (d01.length==3){
                date1 =YearLen2To4(d01[0]);
                date2 =DayLen1To2(d01[1]);
                date3 =DayLen1To2(d01[2]);
            }
        }
        return date1+"-"+date2+"-"+date3;
    }

    public static String formatExcelStandardDateForTemp(String dd) {
        //      10/1/15 12:26
        dd = dd.replace("AM","").replace("PM","").replace("   "," ");
        dd = dd.replace("  "," ").replace("  "," ");

        String date1="";
        String date2="";
        String date3="";
        String date4="00";
        String date5="00";
        String date6="00";
        if (dd.contains("/")) {
            String D0[] = dd.split(" ");
            String d01[] = D0[0].split("/");
            if (d01.length==3){
                date1 =YearLen2To4(d01[2]);
                date2 =DayLen1To2(d01[0]);
                date3 =DayLen1To2(d01[1]);
            }
            String d02[] = D0[1].split(":");
            if (d02.length==2){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
            }
            if (d02.length==3){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
                date6 =DayLen1To2(d02[2]);
            }
        } else if (dd.contains("-")){
            String D0[] = dd.split(" ");
            String d01[] = D0[0].split("-");
            if (d01.length==3){
                date1 =YearLen2To4(d01[0]);
                date2 =DayLen1To2(d01[1]);
                date3 =DayLen1To2(d01[2]);
            }
            String d02[] = D0[1].split(":");
            if (d02.length==2){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
            }
            if (d02.length==3){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
                date6 =DayLen1To2(d02[2]);
            }
        }
        return date1+"-"+date2+"-"+date3 + " " + date4+":"+date5+":"+date6;
    }

    public static String YearLen2To4(String instr) {
        if (instr.length() == 2) {
            instr = "20" + instr;
        }
        return instr;
    }

    public static String DayLen1To2(String instr) {
        if (instr.length() == 1) {
            instr = "0" + instr;
        }
        return instr;
    }

    public static void addZip(String oldFileName, String newFileName,String static_file_addr){
        zipFile( static_file_addr + oldFileName,  static_file_addr + newFileName);
    }
    public static void zipFile(String srcFilePath, String zipFilePath) {
        if(srcFilePath == null){
            throw new RuntimeException("需要压缩的文件的完整路径 不能为空!");
        }
        if(zipFilePath == null){
            throw new RuntimeException("压缩生成的文件的路径 不能为空!");
        }

        ZipOutputStream zout = null;
        FileInputStream fin = null;

        try{
            File txtFile = new File(srcFilePath);
            fin = new FileInputStream(txtFile);
        }catch (FileNotFoundException e) {
            throw new RuntimeException("压缩失败!找不到文件" + srcFilePath);
        }finally {
            try {
                fin.close();
            } catch (Exception e) {

            }
        }
        try {
            zout = new ZipOutputStream(new FileOutputStream(new File(zipFilePath)));

            File srcFile = new File(srcFilePath);
            if (srcFile.exists())
                srcFile.delete();
            fin = new FileInputStream(srcFile);

            byte[] bb = new byte[4096];
            int i = 0;
            zout.putNextEntry(new ZipEntry(srcFile.getName()));
            while ((i = fin.read(bb)) != -1) {
                zout.setLevel(9);
                zout.write(bb, 0, i);
            }
        }  catch (IOException e) {
            throw new RuntimeException("压缩失败!", e);
        } finally {
            try {
                zout.close();
                fin.close();
            } catch (Exception e) {
            }
        }
    }


    public static String bigProName2DetailName(String bigName){
        String DETAIL_NAME="";
        return DETAIL_NAME;
    }


    public static String len1To4(int inNum){
        String outStr = "";
        if (String.valueOf(inNum).length()==1){
            outStr = "000"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==2){
            outStr = "00"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==3){
            outStr = "0"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==4){
            outStr = String.valueOf(inNum);
        }
        return outStr;
    }
    public static String len1To3(int inNum){
        String outStr = "";
        if (String.valueOf(inNum).length()==1){
            outStr = "00"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==2){
            outStr = "0"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==3){
            outStr = String.valueOf(inNum);
        }
//        if (String.valueOf(inNum).length()==4){
//            outStr = String.valueOf(inNum);
//        }
        return outStr;
    }

    public static String[] getBigArea(String bigArea){
        if (bigArea.contains("华东"))
            return AREA_HUADONG.split(",");

        if (bigArea.contains("华南"))
            return AREA_HUANAN.split(",");

        if (bigArea.contains("华中"))
            return AREA_HUAZHONG.split(",");

        if (bigArea.contains("华北"))
            return AREA_HUABEI.split(",");

        if (bigArea.contains("西北"))
            return AREA_XIBEI.split(",");

        if (bigArea.contains("西南"))
            return AREA_XINAN.split(",");

        if (bigArea.contains("东北"))
            return AREA_DONGBEI.split(",");

        if (bigArea.contains("港澳台"))
            return AREA_GANGAOTAI.split(",");

        return new String[]{};
    }


    public static String formatString(String inStr){
        if (inStr.length()<=0)
            return inStr;
        inStr = inStr.replace("'","");
        inStr = "'"+inStr.replace(",","','")+"'";
        return inStr;
    }
    public static String getNowDateNUm(String ORDER_NO){
        //再加6个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        return ORDER_NO+String.valueOf(num);
    }


    public static int orderUpdateType_copy_order = 10;
    public static int orderUpdateType_create_manual = 11;
    public static int orderUpdateType_update_deliver_date = 21;
    public static int orderUpdateType_update_addr = 22;
    public static int orderUpdateType_update_shr = 23;
    public static int orderUpdateType_update_product = 24;
    public static int orderUpdateType_update_pssd = 25;
    public static int orderUpdateType_update_remark = 26;
    public static int orderUpdateType_update_deliver_name = 27;
    public static int orderUpdateType_update_status = 28;
    public static int orderUpdateType_update_main_remark = 29;
    public static int orderUpdateType_export_deliver = 40;
    public static int orderUpdateType_import_deliver = 41;
    public static int orderUpdateType_package_order = 42;
    public static int orderUpdateType_update_pickup_time = 43;

    public static int orderUpdateType_cancel_order = 61;
    public static int orderUpdateType_request_tk = 62;
    public static int orderUpdateType_deal_tk = 63;
    public static int orderUpdateType_allocation_create = 64;

    public static int orderUpdateType_channel_import = 65;
    public static int orderUpdateType_update_product_batch = 66;
    public static int orderUpdateType_add_product_batch = 67;
    public static int orderUpdateType_update_deliver_date_batch = 68;
    public static int orderUpdateType_update_deliver_company_batch = 69;
    public static int orderUpdateType_order_package_retry = 70;
    public static int orderUpdateType_update_occupy_date = 71;
    public static Map<String,String> ipMap=null;
    public static String generateOrderId() {
        String DATE = DateUtils.now().replace("-", "").replace(" ", "").replace(":", "");
        //再加5个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        return DATE+String.valueOf(num);
    }

    /**
     * 采购单ID生成，新策略
     * @author 潘连旺
     * @return
     */
    public static String generateNewOrderId() {
        String DATE = DateUtils.now().replace("-", "").replace(" ", "").replace(":", "");
        //再加5个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        return "CG" + DATE+String.valueOf(num);
    }
    /**
     * 入库单ID生成，新策略
     * @author 潘连旺
     * @return
     */
    public static String generateNewInboundId() {
        String DATE = DateUtils.now().replace("-", "").replace(" ", "").replace(":", "");
        //再加5个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        return "RK" + DATE+String.valueOf(num);
    }

    /**
     * 出库单ID生成，新策略
     * @author 潘连旺
     * @return
     */
    public static String generateNewOutboundId() {
        String DATE = DateUtils.now().replace("-", "").replace(" ", "").replace(":", "");
        //再加5个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        return "CK" + DATE+String.valueOf(num);
    }
    //根据类型获取仓库ID,
    public static String getDcType(String DC_TYPE){
        String kw_ids = "3040500995282352673,3154474960186441723";
        if (DC_TYPE.equals("1")){
            kw_ids = "3040500995282352673,3154474960186441723";
        }
        if (DC_TYPE.equals("2")){
            kw_ids = "3081977233866209944";
        }
        if (DC_TYPE.equals("3")){
            kw_ids = "3095901450406408540";
        }
        if (DC_TYPE.equals("4")){
            kw_ids = "3183277396074720824";
        }
        if (DC_TYPE.equals("8")){//获取4个大仓
            kw_ids = "3040500995282352673,3154474960186441723,3095901450406408540,3081977233866209944,3183277396074720824";
        }
        return kw_ids;
    }
    /**
     * 调拨单ID生成，策略
     * @author 潘连旺
     * @return
     */
    public static String generateNewMoveId() {
        String DATE = DateUtils.now().replace("-", "").replace(" ", "").replace(":", "");
        //再加5个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        return "DB" + DATE+String.valueOf(num);
    }

    public static RecordSet getMonthByDateArea(String START_TIME,String END_TIME ){
        TimeUtils tg = new TimeUtils();
        RecordSet recs_month = new RecordSet();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        try {
            min.setTime(sdf.parse(START_TIME));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(END_TIME));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        }catch (Exception e){}


        Calendar curr = min;
        while (curr.before(max)) {
            recs_month.add(Record.of("MONTH",sdf.format(curr.getTime())));
            curr.add(Calendar.MONTH, 1);
        }
        recs_month.sort("MONTH",false);
        return recs_month;
    }

    public static String filterEmoji(String source) {
        if(source != null)
        {
            Pattern emoji = Pattern.compile(
                    "[\\\\ud83c\\\\udc00-\\\\ud83c\\\\udfff]|[\\\\ud83d\\\\udc00-\\\\ud83d\\\\udfff]|[\\\\u2600-\\\\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())
            {
                source = emojiMatcher.replaceAll("");
                return source ;
            }
            return source;
        }
        return source;
    }

    //unicode替换
    public static String encodingtoStr(String str) {
        if (str.length()<=0)
            return str;
        if(str != null)
        {
            Pattern emoji =  Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

            Matcher emojiMatcher = emoji.matcher(str);
            if ( emojiMatcher.find())
            {
                str = emojiMatcher.replaceAll("");
                return str ;
            }
            return str;
        }
        return str;
    }

    public static String returnProvinceCityAreaColName(int TRANSPORT_TYPE){
        String colName = " STATUS ";
        if (TRANSPORT_TYPE !=1)
            colName = " ORDINARY_STATUS ";
        return colName;
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     * @param  s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }
    public static String getServerIpName(String ip){
        String server="";
        if(ipMap==null){
            ipMap=new HashMap<String, String>();
            ipMap.put("10.163.13.187","U8Server");
            ipMap.put("10.171.98.191","finance");
            ipMap.put("10.51.63.156","156");
            ipMap.put("10.27.35.225","web2");
            ipMap.put("10.24.193.20","userweb2");
            ipMap.put("10.26.47.27","userweb1");
            ipMap.put("10.46.183.120","data");
            ipMap.put("10.24.195.219","web3");
            ipMap.put("10.24.190.243","web1");
            ipMap.put("10.47.212.48","test");
            ipMap.put("10.46.165.71","erp");
        }
        if(!ipMap.containsKey(ip)){
            String[] adArr = ip.split("\\.");
            if(adArr.length>0) {
                server = adArr[adArr.length - 1];
            }
        }else {
            server=ipMap.get(ip);
        }
        return server;
    }
    public static int parserDeptFromUserDeptId(int DEPARTMENT_ID){
        int SOLD_DEPT_ID = 0;
        if (DEPARTMENT_ID==1){
            SOLD_DEPT_ID = 6;
        }else if(DEPARTMENT_ID==2){
            SOLD_DEPT_ID = 5;
        }else if(DEPARTMENT_ID==3){
            SOLD_DEPT_ID = 14;
        }else if(DEPARTMENT_ID==4){
            SOLD_DEPT_ID = 4;
        }else if(DEPARTMENT_ID==5){
            SOLD_DEPT_ID = 8;
        }else if(DEPARTMENT_ID==6){
        }else if(DEPARTMENT_ID==7){
            SOLD_DEPT_ID = 9;
        }else if(DEPARTMENT_ID==8){
            SOLD_DEPT_ID = 10;
        }else if(DEPARTMENT_ID==9){
            SOLD_DEPT_ID = 1;
        }else if(DEPARTMENT_ID==10){
            SOLD_DEPT_ID = 12;
        }else if(DEPARTMENT_ID==11){
            SOLD_DEPT_ID = 7;
        }else if(DEPARTMENT_ID==12){
            SOLD_DEPT_ID = 14;
        }else if(DEPARTMENT_ID==16){
        }else if(DEPARTMENT_ID==17){
        }else if(DEPARTMENT_ID==18){
        }
        return SOLD_DEPT_ID;
    }

    public static float getPackageWeight(int package_size){
        int max = 996;
        int min = 900;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        double b = (double)s * package_size / 1000;
        return (float)b;
    }


    public static String generateRandomId_three() {
        //3个随机数
        Random random = new Random();
        int num = -1;
        num = (int) (random.nextDouble() * (1000 - 100) + 100);
        return String.valueOf(num);
    }

    //采购单生产
    public static String newCgCode() {
        return genTimeSequenceDefault("CG_", "t_sys_order" + "code");
    }
    public static String newInboundCode() {
        return genTimeSequenceDefault("DH_", "t_sys_inbound" + "code");
    }
    public static String newOutboundCode() {
        return genTimeSequenceDefault("JH_", "t_sys_outbound" + "code");
    }
    public static String genTimeSequenceDefault(String headCode,String table){
        return genTimeSequence(headCode, table, "yyyyMMdd");
    }

    public static String genTimeSequence(String headCode,String table,String dateFormat){
        int id = GlobalLogics.getBaseLogic().genNaturalSequence(table);
        return headCode+DateUtils.formatDate(new Date(),dateFormat) +"_"+ Constants.len1To3(id);
    }


}
