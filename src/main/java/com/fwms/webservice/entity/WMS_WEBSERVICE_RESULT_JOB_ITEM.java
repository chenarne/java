package com.fwms.webservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by acer01 on 2017/6/21/021.
 * 任务项
 */
public class WMS_WEBSERVICE_RESULT_JOB_ITEM implements Serializable {

    /**
     * 计划数量
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
     * 存货名称
     */
    private String itemName;


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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
