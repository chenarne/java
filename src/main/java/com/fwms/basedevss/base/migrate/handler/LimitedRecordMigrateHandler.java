package com.fwms.basedevss.base.migrate.handler;


import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.migrate.MigrateStopException;
import com.fwms.basedevss.base.migrate.RecordMigrateHandler;

public abstract class LimitedRecordMigrateHandler implements RecordMigrateHandler {
    private final long limit;
    private long counter = 0;

    public LimitedRecordMigrateHandler(long limit) {
        this.limit = limit;
    }

    protected void increaseCount() {
        counter++;
    }

    protected void increaseCount(long n) {
        counter += n;
    }

    protected abstract void handle0(Record in, Record[] out) throws MigrateStopException;

    @Override
    public final void handle(Record in, Record[] out) throws MigrateStopException {
        handle0(in, out);
        if (counter > limit)
            throw new MigrateStopException();
    }
}
