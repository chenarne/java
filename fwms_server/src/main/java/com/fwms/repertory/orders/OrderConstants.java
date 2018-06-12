package com.fwms.repertory.orders;

/**
 * Created by chenarne on 2018/6/5.
 */
public class OrderConstants {
    public static final int
            INVOICE_CHECK_WAIT = 10,
            INVOICE_CHECK_PASS = 15,
            INVOICE_TRANSPORT_CHECK_WAIT = 10,
            INVOICE_TRANSPORT_CHECK_PASS = 15,

    REF_NA = 10,
            REF_ING = 15,
            REF_OVER = 20,

    //采购-订单

    PURCHASE_ORDER_STATUS_NEW = 1,
            PURCHASE_ORDER_STATUS_ING = 11,
            PURCHASE_ORDER_STATUS_FINISH = 61,
            PURCHASE_ORDER_STATUS_STOP = 71,

    SETTLE_STATUS_NEW = 5,
            SETTLE_STATUS_STEP_ONE = 10,
            SETTLE_STATUS_STEP_TWO = 15;

    public static final int
            //无来源
            STOCK_SOURCE_NA = 0,
    //生产-接出
    STOCK_SOURCE_OUTOF = 11,
    //生产-生产计划
    STOCK_SOURCE_PRODUCTION = 15,
    //生产-领料
    STOCK_SOURCE_PICK_MATERIAL = 51,
    //生产-报废
    STOCK_SOURCE_SCRAP = 101,
    //采购-订单
    STOCK_SOURCE_PURCHASE_ORDER = 21,
    //库存-转库
    STOCK_SOURCE_TRANSFER = 4,
    //库存-调拨
    STOCK_SOURCE_DISPATCH = 5,
    //库存-通知单
    STOCK_SOURCE_APPLY = 301,
    //库存-移位
    STOCK_SOURCE_MOVE = 3,
    //销售-订单
    STOCK_SOURCE_SOLD = 13,


    //申请类型 - 无
    APPLY_TYPE_NA= 0,
    //申请类型 - 采购
    APPLY_TYPE_PURCHASE= 1,
    //申请类型 - 材料出库
    APPLY_TYPE_PICK_OUTBOUND = 2,
    //申请类型 - 成品
    APPLY_TYPE_PRODUCTION_FINISH = 3,
    //申请类型 - 半成品
    APPLY_TYPE_PRODUCTION_HALF= 4,
    //申请类型 - 报废出库
    APPLY_TYPE_SCRAP_OUTBOUND =5,
    //申请类型 - 材料还库
    APPLY_TYPE_PICK_BACK_OUTBOUND = 6,
    //申请类型 - 移位出库
    APPLY_TYPE_MOVE_OUTBOUND =7,
    //申请类型 - 移位入库

    APPLY_TYPE_MOVE_INBOUND =8,
    //申请类型 - 采购退货
//    APPLY_TYPE_PURCHASE_BACK = 9,
    //申请类型 - 转库入库
    APPLY_TYPE_TRANSFER_INBOUND =10,
    //申请类型 - 转库出库
    APPLY_TYPE_TRANSFER_OUTBOUND =11,
    //申请类型 - 调拨入库
    APPLY_TYPE_DISPATCH_INBOUND =12,
    //申请类型 - 调拨出库
    APPLY_TYPE_DISPATCH_OUTBOUND =13,
    //申请类型 - 销售出库
    APPLY_TYPE_SOLD_OUTBOUND =14,


    DEPARTMENT_SUPPLY = 2,

    IN_OUT_TYPE_PURCHASE = 1,
            IN_OUT_TYPE_PURCHASE_BACK = 2,
            IN_OUT_TYPE_PICK_MATERIAL = 3,
            IN_OUT_TYPE_PICK_MATERIAL_BACK = 4,

    //盘点状态 - 等待审核
    REVIEW_STATUS_WAIT_PASS = 5,
    //盘点状态 - 通过审核
    REVIEW_STATUS_PASS = 10,


    //业务类型 - 销售
    BUSINESS_TYPE_SOLD = 1,
    //业务类型 - 调拨
    BUSINESS_TYPE_DISPATCH = 2,


    //期初 (采购单,生产)
    INIT_INBOUND = 1,
    //不是期初 (采购单,生产)
    INIT_INBOUND_NOT = 0,


    STOCK_APPLY_NEW = 1,
            STOCK_APPLY_ING = 2,
            STOCK_APPLY_OVER = 3,

    TRANSFER_IS_INBOUND = 1,
            TRANSFER_IS_NOT_INBOUND = 0,
            TRANSFER_IS_OUTBOUND = 1,
            TRANSFER_IS_NOT_OUTBOUND = 0,

    //    TRANSFER_STATUS_WAIT_PASS = 5,
//    TRANSFER_STATUS_PASS = 7,
//    TRANSFER_STATUS_WAIT = 9,
    TRANSFER_STATUS_WAIT_OUT = 15,
            TRANSFER_STATUS_WAIT_IN = 20,
            TRANSFER_STATUS_OVER = 30,


    DISPATCH_STATUS_WAIT_PASS = 5,
            DISPATCH_STATUS_PASS = 15,
            DISPATCH_STATUS_OUTBOUND_ING = 18,
            DISPATCH_STATUS_SEND = 25,
            DISPATCH_STATUS_INBOUND_ING = 30,
            DISPATCH_STATUS_OVER = 35,

    DISPATCH_IS_OUT = 1,
            DISPATCH_IS_NOT_OUT = 0,

    DISPATCH_IS_SEND = 1,
            DISPATCH_IS_NOT_SEND = 0,

    DISPATCH_IS_IN = 1,
            DISPATCH_IS_NOT_IN = 0,

    //调拨类型 分仓铺货
    DISPATCH_TYPE_DIST = 1,
    //调拨类型 业务调拨
    DISPATCH_TYPE_BUSINESS = 2,
    //调拨类型 生产调拨
    DISPATCH_TYPE_PRODUCTION = 3,

    //是出入库退还
    BOUND_IS_BACK = 1,
    //不是出入库退还
    BOUND_IS_NOT_BACK = 0,

    //包装计划 - 铺货
    PACKAGE_PLAN_TYPE_PH = 3,
    //包装计划 - TOB
    PACKAGE_PLAN_TYPE_TOB = 2;


    public static int ORDER_STATUS_DEFAULT =1, //订单初始状态
            ORDER_STATUS_CONFIRMED =5, //订单已审核
            ORDER_STATUS_INBOUNT_CREATE =8, //订单创单入库通知单
            ORDER_STATUS_INBOUNT_PART =10, //订单部分入仓库
            ORDER_STATUS_INBOUNT_FINISHED =40, //订单全部入仓库
            ORDER_STATUS_OUTBOUNT_CREATE =45, //订单创单出库通知单
            ORDER_STATUS_OUTBOUNT_PART =50, //订单部分出仓库
            ORDER_STATUS_OUTBOUNT_FINISHED =70, //订单全部出仓库
            ORDER_STATUS_FINISHED =100; //订单完成

}
