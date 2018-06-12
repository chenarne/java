package com.fwms.basedevss.base.sql;


import java.sql.Statement;

public interface SQLStatementHandler {
    void handle(Statement stmt) throws java.sql.SQLException;
}
