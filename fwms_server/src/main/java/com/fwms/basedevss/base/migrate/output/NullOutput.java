package com.fwms.basedevss.base.migrate.output;


import com.fwms.basedevss.base.migrate.RecordOutput;
import com.fwms.basedevss.base.data.Record;

public class NullOutput implements RecordOutput {
    @Override
    public void output(Record rec) {
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }
}
