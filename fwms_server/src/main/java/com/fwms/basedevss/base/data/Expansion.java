package com.fwms.basedevss.base.data;


import com.fwms.basedevss.base.context.Context;

public interface Expansion {
    void expand(Context ctx, RecordSet recs, String[] cols);
}
