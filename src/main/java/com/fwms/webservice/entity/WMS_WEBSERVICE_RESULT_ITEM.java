package com.fwms.webservice.entity;

import java.io.Serializable;

/**
 * Created by acer01 on 2017/6/21/021.
 * 存货
 */
public class WMS_WEBSERVICE_RESULT_ITEM implements Serializable {

    /**
     * 计量单位ID
     */
    private Integer unitId;
    /**
     * 存货号
     */
    private String code;
    /**
     * 存货名称
     */
    private String name;
    /**
     * 存货类型名
     */
    private String itemTypeName;
    /**
     * 规格
     */
    private String spec;
    /**
     * 计量单位名称
     */
    private String unitName;

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
