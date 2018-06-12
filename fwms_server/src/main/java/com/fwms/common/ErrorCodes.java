package com.fwms.common;
import com.fwms.basedevss.Errors;

public class ErrorCodes extends Errors {
    //SYSTEM ERROR
    public static final int SYSTEM_ERROR_CODE = 10000;
    public static final int SYSTEM_ERROR = SYSTEM_ERROR_CODE + 1;// 10001 //System error //系统错误
    public static final int SYSTEM_DB_ERROR = SYSTEM_ERROR_CODE + 2;//10002//DB error//数据库操作错误
    public static final int SYSTEM_IP_LIMIT = SYSTEM_ERROR_CODE + 4;//10004 //IP limit //IP限制不能请求该资源
    public static final int SYSTEM_PERMISSION_DENIED = SYSTEM_ERROR_CODE + 5;//10005 //Permission denied, need a high level app id//该资源需要appkey拥有授权
    public static final int SYSTEM_MISSING_PARAMETER = SYSTEM_ERROR_CODE + 6;//10006 //Source paramter (app id) is missing //缺少app id
    public static final int SYSTEM_TOO_MANY_TASKS = SYSTEM_ERROR_CODE + 9;//10009 //Too many pending tasks, system is busy //任务过多，系统繁忙
    public static final int SYSTEM_JOB_EXPIRED = SYSTEM_ERROR_CODE + 10;//10010 //Job expired //任务超时
    public static final int SYSTEM_MISS_REQUIRED_PARAMETER = SYSTEM_ERROR_CODE + 12;//10016 //Miss required parameter (%s) //缺失必选参数 (%s)
    public static final int SYSTEM_PARAMETER_TYPE_ERROR = SYSTEM_ERROR_CODE + 17;//10017 //Parameter (%s)'s value invalid, expect (%s) , but get (%s) , see doc for more info //参数值非法，需为 (%s)，实际为 (%s)
    public static final int SYSTEM_REQUEST_BODY_OVER_LIMIT = SYSTEM_ERROR_CODE + 18;//10018 //Request body length over limit //请求长度超过限制
    public static final int SYSTEM_REQUEST_API_NOT_FOUND = SYSTEM_ERROR_CODE + 20;//10020 //Request api not found //接口不存在
    public static final int SYSTEM_HTTP_METHOD_NOT_SUPPORT = SYSTEM_ERROR_CODE + 21;//10021 //HTTP method is not supported for this request //请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式

    public static final int SYSTEM_HTTP_REQUEST_DELAY = SYSTEM_ERROR_CODE + 22;//10022 //请求超时，客户端的时间跟服务器时间相差10分钟以上



    public static final int SERVICE_ERROR_CODE = 20000;

    //AUTH ERROR   20100
    public static final int AUTH_SERVICE_ERROR_CODE = SERVICE_ERROR_CODE + 100;
    public static final int AUTH_FAILED = AUTH_SERVICE_ERROR_CODE + 1;//20101//Auth faild //认证失败
    public static final int AUTH_LOGIN_ERROR = AUTH_SERVICE_ERROR_CODE + 2;//20102//Username or password error //用户名或密码不正确
    public static final int AUTH_OUT_OF_LIMIT = AUTH_SERVICE_ERROR_CODE + 3;//20103//Username and pwd auth out of rate limit //用户名密码认证超过请求限制
    public static final int AUTH_SIGNATURE_ERROR = AUTH_SERVICE_ERROR_CODE + 4;//20104//Signature invalid //签名值不合法
    public static final int AUTH_SIGNATURE_METHOD_ERROR = AUTH_SERVICE_ERROR_CODE + 5;//20105//Signature invalid //签名方式错误
    public static final int AUTH_TICKET_EXPIRED = AUTH_SERVICE_ERROR_CODE + 6;//20106//Token expired //Token已经过期
    public static final int AUTH_TICKET_INVALID = AUTH_SERVICE_ERROR_CODE + 7;//20107//Ticket不合法

    public static final int AUTH_USERNAME_EXISTS = AUTH_SERVICE_ERROR_CODE + 9;//20109//用户名已经存在
    public static final int AUTH_USERNAME_NOT_EXISTS = AUTH_SERVICE_ERROR_CODE + 10;//20110//重置密码的邮箱不存在
    public static final int AUTH_OLD_PASSWORD_ERROR = AUTH_SERVICE_ERROR_CODE + 11;//20111//用户原密码错误
    public static final int AUTH_NEED_TICKET = AUTH_SERVICE_ERROR_CODE + 12;//20112//本API需要ticket
    public static final int AUTH_HAS_BIND_OTHER_ALREADY = AUTH_SERVICE_ERROR_CODE + 13;//20113//已经跟其他账号绑定了
    public static final int AUTH_HAS_NOT_BIND_OTHER = AUTH_SERVICE_ERROR_CODE + 14;//20114//已经跟其他账号绑定了
    public static final int AUTH_UPDATE_IMG_MUST_UPLOAD = AUTH_SERVICE_ERROR_CODE + 15;//20115//m必须上传文件才能更改头像
    public static final int AUTH_LOGIN_EXPIRE_ERROR = AUTH_SERVICE_ERROR_CODE + 16;//20116//Username or password error //用户名或密码不正确
    public static final int AUTH_LOGIN_USER_DELETE_ERROR = AUTH_SERVICE_ERROR_CODE + 17;//20117//账户已被停用
    public static final int USER_ID_NOT_EXISTS = AUTH_SERVICE_ERROR_CODE + 18;//20118//用户ID不存在
    public static final int AUTH_AGENCYUSER_NOT_NULL = AUTH_SERVICE_ERROR_CODE + 100;//20200//管理用户下有用户，不能删除

    public static final int AUTH_LOGIN_USER_ERROR_DEVICE = AUTH_SERVICE_ERROR_CODE + 19;//20119//已经在其他地方登录了

    public static final int AUTH_REGIST_ERROR_MOBILE = AUTH_SERVICE_ERROR_CODE + 20;//20120//手机号码格式不正确
    public static final int AUTH_REGIST_ERROR_EMAIL = AUTH_SERVICE_ERROR_CODE + 21;//20121//EMAIL格式不正确

    public static final int AUTH_REGIST_ERROR_USER = AUTH_SERVICE_ERROR_CODE + 22;//20122//注册用户名不正确
    public static final int AUTH_REGIST_VERIFY_CODE_EXPIRE = AUTH_SERVICE_ERROR_CODE + 23;//20123//验证码超时
    public static final int AUTH_LOGIN_ERROR_NOT_VERIFY = AUTH_SERVICE_ERROR_CODE + 24;//20124//未验证

    public static final int CARDS_SERVICE_ERROR_CODE = SERVICE_ERROR_CODE + 200;
    public static final int CARDS_EXCEL_INPUT_FILE_ERROR = CARDS_SERVICE_ERROR_CODE + 1;//20201//excel文件错误
    public static final int CARDS_STATE_DATA_DATE_ERROR = CARDS_SERVICE_ERROR_CODE + 2;//20202//卡状态日期格式不对

    public static final int ORDER_ERROR_CODE = SERVICE_ERROR_CODE + 300;

    public static final int USER_CARDS_ERROR_CODE = SERVICE_ERROR_CODE + 400;
    public static final int SOLD_CHANNEL_ERROR = USER_CARDS_ERROR_CODE + 1;//20401//销售渠道值错误






    // MODULE ERROR有关业务相关的ERROR CODE会陆续补充
    //用户注册 20200
    public static final int MODULE_SERVICE_ERROR_CODE_USER = SERVICE_ERROR_CODE + 200;
    public static final int REGIST_EMAIL_HAS_DETECTED = MODULE_SERVICE_ERROR_CODE_USER + 1;//20201//注册邮箱已经存在

    //订单 20300
    public static final int ORDER_SERVICE_ERROR_CODE = SERVICE_ERROR_CODE + 300;
    public static final int ORDER_NEED_ATTACHMENTS = ORDER_SERVICE_ERROR_CODE + 1;//20301//订单必须上传附件
    public static final int ORDER_UPLOAD_ATTACHMENTS_ERROR = ORDER_SERVICE_ERROR_CODE + 2;//20302//订单上传附件失败
    public static final int ORDER_MONEY_FORMAT_ERROR = ORDER_SERVICE_ERROR_CODE + 3;//20303//订单金额格式错误


    //续费 20400
    public static final int RENEW_MONEY_ERROR_CODE = SERVICE_ERROR_CODE + 400;
    public static final int RENEW_MONEY_HASNOT_BEFORE = RENEW_MONEY_ERROR_CODE + 1;//20401//不存在之前的缴费记录


    public static final int URL_ERROR = SERVICE_ERROR_CODE + 8000;//地址错误


    //跟订单相关 20700
    public static final int ORDER_ERROR_CODE_BASE = SERVICE_ERROR_CODE + 700;
    public static final int ORDER_BOOK_ID_ERROR = ORDER_ERROR_CODE_BASE + 1;//20701//买的书的ID不存在
    public static final int ORDER_ID_NOT_EXISTS = ORDER_ERROR_CODE_BASE + 2;//20702//订单的ID不存在
    public static final int ORDER_HAS_DELETED = ORDER_ERROR_CODE_BASE + 3;//20703//订单被删除
    public static final int ORDER_HAS_DEALED = ORDER_ERROR_CODE_BASE + 4;//20704//订单已经完成
    public static final int ORDER_IS_NOT_YOURS = ORDER_ERROR_CODE_BASE + 5;//20705//订单不是自己的
    public static final int ORDER_BUY_TYPE_ERROR = ORDER_ERROR_CODE_BASE + 6;//20706//购买方式不正确
    public static final int ORDER_QUAN_ID_ERROR = ORDER_ERROR_CODE_BASE + 7;//20707//必须有券ID号
    public static final int ORDER_QUAN_NOT_YOURS = ORDER_ERROR_CODE_BASE + 8;//20708//该券不是你自己的
    public static final int ORDER_QUAN_HAS_USE_ALREADY = ORDER_ERROR_CODE_BASE + 9;//20709//该券已经用过了
    public static final int ORDER_QUAN_HAS_NOT_EXISTS = ORDER_ERROR_CODE_BASE + 10;//20710//没有这个券
    public static final int ORDER_MONEY_ERROR = ORDER_ERROR_CODE_BASE + 11;//20711//订单价格不正确
    public static final int ORDER_PAY_TYPE_ERROR = ORDER_ERROR_CODE_BASE + 12;//20712//支付方式不正确
    public static final int ORDER_OUT_OF_USE_DAY = ORDER_ERROR_CODE_BASE + 13;//20713//该券已经过期
    public static final int ORDER_GOLDEN_NOT_ENOUGH = ORDER_ERROR_CODE_BASE + 14;//20714//金币数量不够
    public static final int ORDER_QUAN_USED_FOR_OTHER_ORDER = ORDER_ERROR_CODE_BASE + 15;//20715//折扣券已经被别的订单用掉了
    public static final int ORDER_QUAN_CANT_USED_OVER_THREE = ORDER_ERROR_CODE_BASE + 16;//20716//最多只能用三张券买东西
    public static final int ORDER_QUAN_CANT_USED_SAME_TYPE_CARD = ORDER_ERROR_CODE_BASE + 17;//20717//同一种类型的券，一次只能用一张
    public static final int ORDER_MONEY_BUY_TYPE_CANT_FINISH = ORDER_ERROR_CODE_BASE + 20;//20720//这种类型的购买不能调用完成的动作
    public static final int ORDER_BOOK_ID_ERROR_EXISTS = ORDER_ERROR_CODE_BASE + 21;//20721//不能重复购买一本书
    public static final int ORDER_BOOK_CARD_ERROR_ON_THIS_BUY = ORDER_ERROR_CODE_BASE + 22;//20722//这张券本次购买用不上
    public static final int ORDER_BOOK_EXISTS_ON_OTHER_ORDER = ORDER_ERROR_CODE_BASE + 23;//20723//这本书别的订单正在买，或者已经买了
    public static final int ORDER_KC_NOT_ENOUGH = ORDER_ERROR_CODE_BASE + 24;//20724//库存不够
    public static final int ORDER_G_CODE_USED_ALREADY = ORDER_ERROR_CODE_BASE + 25;//20725//G_CODE用过了
    public static final int ORDER_G_CODE_NOT_EXISTS = ORDER_ERROR_CODE_BASE + 26;//20726//G_CODE不存在

    public static final int ORDER_CF_MONEY = ORDER_ERROR_CODE_BASE + 25;//20725//JIESHULE
    public static final int ORDER_CF_END = ORDER_ERROR_CODE_BASE + 26;//20725//JIESHULE
    public static final int ORDER_CF_BACK= ORDER_ERROR_CODE_BASE + 27;//支付没有返回
    public static final int ORDER_CF_N0NE= ORDER_ERROR_CODE_BASE + 28;//没有支付成功


    public static final int ORDER_PAY_SIGN_ERROR = ORDER_ERROR_CODE_BASE + 51;//20751//第三方支付签名失败
    public static final int ORDER_PAY_TX_ERROR = ORDER_ERROR_CODE_BASE + 52;//20752//第三方支付通讯交互失败

}
