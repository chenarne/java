package com.fwms.basedevss.base.migrate.handler;


import com.fwms.basedevss.base.data.Record;
import com.fwms.basedevss.base.migrate.MigrateStopException;
import com.fwms.basedevss.base.migrate.RecordMigrateHandler;

public abstract class CounterMigrateHandler implements RecordMigrateHandler {

    private long counter = 0;

    protected CounterMigrateHandler() {
    }

    protected long counter() {
        return counter;
    }

    protected abstract void handle0(Record in, Record[] out) throws MigrateStopException;

    @Override
    public final void handle(Record in, Record[] out) throws MigrateStopException {
        handle0(in, out);
        counter++;
    }
}
