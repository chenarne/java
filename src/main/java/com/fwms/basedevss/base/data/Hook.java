package com.fwms.basedevss.base.data;


import com.fwms.basedevss.base.context.Context;

public interface Hook {
    void before(Context ctx, RecordSet recs);
    void after(Context ctx, RecordSet recs);
}
