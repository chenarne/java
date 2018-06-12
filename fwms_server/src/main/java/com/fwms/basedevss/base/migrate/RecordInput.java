package com.fwms.basedevss.base.migrate;


import com.fwms.basedevss.base.util.Initializable;
import com.fwms.basedevss.base.data.Record;

public interface RecordInput extends Initializable {
    Record input();
}
