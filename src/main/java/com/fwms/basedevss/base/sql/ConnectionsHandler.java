package com.fwms.basedevss.base.sql;


import java.sql.Connection;

public interface ConnectionsHandler<T> {
    T handle(Connection[] conns);
}
