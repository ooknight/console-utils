package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.JavaBeanSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerialContext;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.serializer.SerializerFeature;
import com.github.ooknight.utils.console.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class CollectionCodec implements ObjectSerializer {

    public final static CollectionCodec instance = new CollectionCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WRITE_NULL_LIST_AS_EMPTY);
            return;
        }
        Type elementType = null;
        if (out.isEnabled(SerializerFeature.WRITE_CLASS_NAME)
                || SerializerFeature.isEnabled(features, SerializerFeature.WRITE_CLASS_NAME)) {
            elementType = TypeUtils.getCollectionItemType(fieldType);
        }
        Collection<?> collection = (Collection<?>) object;
        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);
        if (out.isEnabled(SerializerFeature.WRITE_CLASS_NAME)) {
            if (HashSet.class == collection.getClass()) {
                out.append("Set");
            } else if (TreeSet.class == collection.getClass()) {
                out.append("TreeSet");
            }
        }
        try {
            int i = 0;
            out.append('[');
            for (Object item : collection) {
                if (i++ != 0) {
                    out.append(',');
                }
                if (item == null) {
                    out.writeNull();
                    continue;
                }
                Class<?> clazz = item.getClass();
                if (clazz == Integer.class) {
                    out.writeInt(((Integer) item).intValue());
                    continue;
                }
                if (clazz == Long.class) {
                    out.writeLong(((Long) item).longValue());
                    if (out.isEnabled(SerializerFeature.WRITE_CLASS_NAME)) {
                        out.write('L');
                    }
                    continue;
                }
                ObjectSerializer itemSerializer = serializer.getObjectWriter(clazz);
                if (SerializerFeature.isEnabled(features, SerializerFeature.WRITE_CLASS_NAME)
                        && itemSerializer instanceof JavaBeanSerializer) {
                    JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) itemSerializer;
                    javaBeanSerializer.writeNoneASM(serializer, item, i - 1, elementType, features);
                } else {
                    itemSerializer.write(serializer, item, i - 1, elementType, features);
                }
            }
            out.append(']');
        } finally {
            serializer.context = context;
        }
    }
}
