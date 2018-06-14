package com.fwms.webservice.entity;

import java.io.Serializable;

/**
 * Created by acer01 on 2017/6/21/021.
 * 申请任务
 */
public class WMS_WEBSERVICE_RESULT_APPLY implements Serializable {

    /**
     * 工作状态名称
     */
    private String statusName;
    /**
     * 出入库类型ID
     */
    private Integer inOutTypeId;
    /**
     * 出入库类型名称
     */
    private String inOutTypeName;
    /**
     * 上游来源单号
     */
    private String sourceCode;
    /**
     * 上游来源单号类型
     */
    private Integer sourceTypeId;
    /**
     * 申请人
     */
    private String createUserName;
    /**
     * 申请时间
     */
    private String createTime;
    /**
     * 备注
     */
    private String remark;

    public Integer getSourceTypeId() {
        return sourceTypeId;
    }

    public void setSourceTypeId(Integer sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getInOutTypeId() {
        return inOutTypeId;
    }

    public void setInOutTypeId(Integer inOutTypeId) {
        this.inOutTypeId = inOutTypeId;
    }

    public String getInOutTypeName() {
        return inOutTypeName;
    }

    public void setInOutTypeName(String inOutTypeName) {
        this.inOutTypeName = inOutTypeName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
