package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

public class ReferenceCodec implements ObjectSerializer {

    public static final ReferenceCodec instance = new ReferenceCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Object item;
        if (object instanceof AtomicReference) {
            AtomicReference val = (AtomicReference) object;
            item = val.get();
        } else {
            item = ((Reference) object).get();
        }
        serializer.write(item);
    }
}
