package com.fwms.basedevss.base.data;


import com.fwms.basedevss.ServerException;
import com.fwms.basedevss.base.BaseErrors;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Schemas {
    public static void standardize(Schema schema, Map<String, Object> rec) {
        Validate.notNull(schema);
        if (rec == null)
            return;

        for (String k : rec.keySet()) {
            DataType type = schema.getType(k);
            if (type != null) {
                Object v = Values.to(rec.get(k), type);
                rec.put(k, v);
            }
        }
    }

    public static void standardize(Schema schema, ObjectNode rec) {
        Validate.notNull(schema);
        if (rec == null)
            return;

        Iterator<String> fieldNamesIter = rec.getFieldNames();
        while (fieldNamesIter.hasNext()) {
            String fieldName = fieldNamesIter.next();
            DataType type = schema.getType(fieldName);
            if (type != null) {
                Object v = Values.to(rec.get(fieldName), type);
                if (v == null || v instanceof Null || v instanceof Privacy) {
                    rec.put(fieldName, (JsonNode)null);
                } else if (v instanceof Boolean) {
                    rec.put(fieldName, (Boolean)v);
                } else if (v instanceof Long) {
                    rec.put(fieldName, (Long)v);
                } else if (v instanceof Double) {
                    rec.put(fieldName, (Double)v);
                } else if (v instanceof String) {
                    rec.put(fieldName, (String)v);
                } else if (v instanceof JsonNode) {
                    rec.put(fieldName, (JsonNode)v);
                } else {
                    throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "json node (record) standardize error");
                }
            }
        }
    }

    public static void standardize(Schema schema, Collection<Record> recs) {
        for (Record rec : recs)
            standardize(schema, rec);
    }


    public static void standardize(Schema schema, ArrayNode recs) {
        for (int i = 0; i < recs.size(); i++) {
            JsonNode jn = recs.get(i);
            if (!(jn instanceof ObjectNode))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "json node (array) standardize error");

            standardize(schema, (ObjectNode)jn);
        }
    }

    public static void standardize(Schema schema, JsonNode jn) {
        if (jn.isObject()) {
            standardize(schema, (ObjectNode)jn);
        } else if (jn.isArray()) {
            standardize(schema, (ArrayNode)jn);
        }
    }

    public static void checkRecordIncludeColumns(Record rec, String... cols) {
        for (String col : cols) {
            if (!rec.has(col))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "Must include column '%s'", col);
        }
    }

    public static void checkRecordIncludeColumns(ObjectNode rec, String... cols) {
        for (String col : cols) {
            if (!rec.has(col))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "Must include column '%s'", col);
        }
    }

    public static void checkRecordExcludeColumns(Record rec, String... cols) {
         for (String col : cols) {
            if (rec.has(col))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "Can't include column '%s'", col);
        }
    }

    public static void checkRecordExcludeColumns(ObjectNode rec, String... cols) {
        for (String col : cols) {
            if (rec.has(col))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "Can't include column '%s'", col);
        }
    }

    public static void checkRecordColumnsIn(Record rec, String... cols) {
        for (String col : rec.getColumns()) {
            if (!ArrayUtils.contains(cols, col))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "Unknown column '%s'", col);
        }
    }

    public static void checkRecordColumnsIn(ObjectNode rec, String... cols) {
        Iterator<String> fieldNamesIter = rec.getFieldNames();
        while (fieldNamesIter.hasNext()) {
            String fieldName = fieldNamesIter.next();
            if (!ArrayUtils.contains(cols, fieldName))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "Unknown column '%s'", fieldName);
        }
    }

    public static void checkSchemaIncludeColumns(Schema schema, String... cols) {
        for (String col : cols) {
            if (!schema.has(col))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "Unknown column '%s'", col);
        }
    }

    public static void checkSchemaIncludeGroupColumns(Schema schema, String group, String... cols) {
        for (String col : cols) {
            if (!schema.hasColumnInGroup(col, group))
                throw new ServerException(BaseErrors.PLATFORM_RECORD_SCHEMA_ERROR, "Unknown column '%s'", col);
        }
    }
}
