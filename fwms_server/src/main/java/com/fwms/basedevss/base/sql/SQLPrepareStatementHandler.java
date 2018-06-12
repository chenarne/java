package com.fwms.basedevss.base.sql;

import java.sql.PreparedStatement;

/**
 * Created by liqun on 2017/5/23.
 */
public interface SQLPrepareStatementHandler {
    void handle(PreparedStatement stmt) throws java.sql.SQLException;
}