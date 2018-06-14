package com.fwms.webservice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by acer01 on 2017/6/21/021.
 * 工作
 */
public class WMS_WEBSERVICE_RESULT_WORK implements Serializable {

    /**
     * 来源申请号
     */
    private String sourceCode;

    /**
     * 工作状态名称
     * 5 正在工作
     * 10 暂停工作
     * 15 结束工作
     */
    private String workStatusName;

    /**
     * 工作状态
     * 5 正在工作
     * 10 暂停工作
     * 15 结束工作
     */
    private Integer workStatus;

    /**
     * 工作项
     */
    private List<WMS_WEBSERVICE_RESULT_WORK_ITEM> itemList;


    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getWorkStatusName() {
        return workStatusName;
    }

    public void setWorkStatusName(String workStatusName) {
        this.workStatusName = workStatusName;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public List<WMS_WEBSERVICE_RESULT_WORK_ITEM> getItemList() {
        return itemList;
    }

    public void setItemList(List<WMS_WEBSERVICE_RESULT_WORK_ITEM> itemList) {
        this.itemList = itemList;
    }
}
