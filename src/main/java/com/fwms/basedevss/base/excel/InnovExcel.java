package com.fwms.basedevss.base.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InnovExcel {
    private JExcelUtils jExcelUtils = new JExcelUtils();

    /**
     * dataList style : List<List<String>>
     * @param dataList
     */

    public byte[] genInnovSalaryMonthReportUserAll(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createExcelBufferSalaryMonthReportUserAll(TITLE,sheetName,dataList);
    }
    public byte[] createExcelBuffer(String sheetName, List dataTitles, List datas) {
        return jExcelUtils.createExcelBuffer(sheetName, dataTitles, datas);
    }

    public byte[] genInnovSoldHistoryTH(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.creategenInnovSoldHistoryTH(TITLE, sheetName, dataList);
    }
    public byte[] genInnovSoldHistoryTHNew(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.creategenInnovSoldHistoryTHNew(TITLE, sheetName, dataList);
    }
    public byte[] genMakeFiles(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createFiles(TITLE, sheetName, dataList);
    }
    public byte[] genInnovSoldHistoryTHNewOnlyOrder(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.creategenInnovSoldHistoryTHNewOrderOnly(TITLE, sheetName, dataList);
    }
    public byte[] genInnovOrderHistory(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.creategenInnovOrderHistory(TITLE, sheetName, dataList);
    }
    public byte[] genInnovOrderWlyc(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.creategenInnovWlyc(TITLE, sheetName, dataList);
    }
    public byte[] genInnovSendDeliverMail(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.creategenInnovSendDeliverMail(TITLE, sheetName, dataList);
    }
    public byte[] genInnovExtension(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createInnovExtension(TITLE, sheetName, dataList);
    }
    public byte[] genInnovJxc(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createInnovJxc(TITLE, sheetName, dataList);
    }
    public byte[] genInnovPerformance(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.creategenInnovPerformance(TITLE, sheetName, dataList);
    }

    public byte[] genInnovPackageRecordsBetweenDate(List<List<String>> dataList,long num){
        return jExcelUtils.creategenInnovPackageRecordsExcel(dataList,num);
    };
    public byte[] genInnovTakeEstimateRecords(List<List<String>> dataList){
        return jExcelUtils.creategenInnovTakeEstimateRecordsExcel(dataList);
    };
    public byte[] genInnovChannelTakeRecords(List<List<String>> dataList){
        return jExcelUtils.creategenInnovChannelTakeRecordsExcel(dataList);
    }
    private volatile static InnovExcel innnvovExcel;

    public static InnovExcel getNewInstance() {
        if (innnvovExcel == null) {
            synchronized (InnovExcel.class) {
                if (innnvovExcel == null) {
                    innnvovExcel = new InnovExcel();
                    }
                }
            }
        return innnvovExcel;
    }

}