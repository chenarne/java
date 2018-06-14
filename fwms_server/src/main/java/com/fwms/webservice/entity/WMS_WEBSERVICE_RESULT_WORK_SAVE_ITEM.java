package com.fwms.webservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by acer01 on 2017/6/21/021.
 * 工作项
 */
public class WMS_WEBSERVICE_RESULT_WORK_SAVE_ITEM implements Serializable {

    /**
     * 申请号
     */
    private String applyId;
    /**
     * 计划申请数量
     */
    private BigDecimal quantity;
    /**
     * 生产日期
     */
    private String productionDate;
    /**
     * 存货码
     */
    private String itemCode;
    /**
     * 货架号
     */
    private String rackCode;
    /**
     * 条形码
     */
    private String barCode;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getRackCode() {
        return rackCode;
    }

    public void setRackCode(String rackCode) {
        this.rackCode = rackCode;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }


    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

}
