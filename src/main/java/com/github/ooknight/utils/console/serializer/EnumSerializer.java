package com.github.ooknight.utils.console.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class EnumSerializer implements ObjectSerializer {

    public static final EnumSerializer instance = new EnumSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        out.writeEnum((Enum<?>) object);
    }
}
