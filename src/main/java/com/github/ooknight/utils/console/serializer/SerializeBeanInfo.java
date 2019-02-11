package com.github.ooknight.utils.console.serializer;

import com.github.ooknight.utils.console.util.FieldInfo;

public class SerializeBeanInfo {

    protected final Class<?> beanType;
    protected final String typeName;
    protected final String typeKey;
    protected final FieldInfo[] fields;
    protected final FieldInfo[] sortedFields;
    protected int features;

    public SerializeBeanInfo(Class<?> beanType, String typeName, String typeKey, int features, FieldInfo[] fields, FieldInfo[] sortedFields) {
        this.beanType = beanType;
        this.typeName = typeName;
        this.typeKey = typeKey;
        this.features = features;
        this.fields = fields;
        this.sortedFields = sortedFields;
    }
}
