package com.github.ooknight.utils.console.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;

public class EnumerationSerializer implements ObjectSerializer {

    public static final EnumerationSerializer instance = new EnumerationSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(Feature.WRITE_NULL_LIST_AS_EMPTY);
            return;
        }
        Type elementType = null;
        if (out.isEnabled(Feature.WRITE_CLASS_NAME)) {
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType param = (ParameterizedType) fieldType;
                elementType = param.getActualTypeArguments()[0];
            }
        }
        Enumeration<?> e = (Enumeration<?>) object;
        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);
        try {
            int i = 0;
            out.append('[');
            while (e.hasMoreElements()) {
                Object item = e.nextElement();
                if (i++ != 0) {
                    out.append(',');
                }
                if (item == null) {
                    out.writeNull();
                    continue;
                }
                ObjectSerializer itemSerializer = serializer.getObjectWriter(item.getClass());
                itemSerializer.write(serializer, item, i - 1, elementType, 0);
            }
            out.append(']');
        } finally {
            serializer.context = context;
        }
    }
}
