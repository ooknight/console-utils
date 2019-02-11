package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.lang.reflect.Type;

public class GuavaCodec implements ObjectSerializer {

    public static final GuavaCodec instance = new GuavaCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object instanceof Multimap) {
            Multimap multimap = (Multimap) object;
            serializer.write(multimap.asMap());
        }
    }
}
