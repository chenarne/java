package com.fwms.basedevss.base.sql;


import java.sql.Statement;

public interface SQLStatementsHandler {
    void handle(Statement[] stmts) throws java.sql.SQLException;
}
