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

    public byte[] makeInboundPackage(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createInboundPackages(TITLE,sheetName,dataList);
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