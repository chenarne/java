package com.fwms.webservice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by acer01 on 2017/6/21/021.
 * 任务
 */
public class WMS_WEBSERVICE_RESULT_JOB implements Serializable {

    /**
     * 来源申请号
     */
    private String sourceCode;
    /**
     * 任务创建者
     */
    private String createUserName;
    /**
     * 任务创建时间
     */
    private String createTime;
    /**
     * 任务创建者写的备注
     */
    private String remark;
    /**
     * 任务项
     */
    private List<WMS_WEBSERVICE_RESULT_JOB_ITEM> itemList;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }


    public List<WMS_WEBSERVICE_RESULT_JOB_ITEM> getItemList() {
        return itemList;
    }

    public void setItemList(List<WMS_WEBSERVICE_RESULT_JOB_ITEM> itemList) {
        this.itemList = itemList;
    }
}
