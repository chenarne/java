package com.fwms.basedevss.base.migrate;


import com.fwms.basedevss.base.data.Record;

public interface RecordMigrateHandler {
    void handle(Record in, Record[] out) throws MigrateStopException;
}
