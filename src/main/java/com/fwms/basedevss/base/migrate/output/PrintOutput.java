package com.fwms.basedevss.base.migrate.output;

import com.fwms.basedevss.base.migrate.RecordOutput;
import com.fwms.basedevss.base.data.Record;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;


public class PrintOutput implements RecordOutput {
    protected PrintStream stream;

    public PrintOutput() {
        this(System.out);
    }

    public PrintOutput(PrintStream stream) {
        Validate.notNull(stream);
        this.stream = stream;
    }

    @Override
    public void output(Record rec) {
        stream.println(rec.toString(false, false));
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }
}
