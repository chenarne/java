package com.fwms.basedevss.base.util.json;


import org.codehaus.jackson.JsonGenerator;

public interface Jsonable {
    void generateJson(JsonGenerator jg, String[] cols);
}
