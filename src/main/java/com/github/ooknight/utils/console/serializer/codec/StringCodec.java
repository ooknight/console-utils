package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;

public class StringCodec implements ObjectSerializer {

    public static StringCodec instance = new StringCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        write(serializer, (String) object);
    }

    public void write(JSONSerializer serializer, String value) {
        SerializeWriter out = serializer.out;
        if (value == null) {
            out.writeNull(SerializerFeature.WRITE_NULL_STRING_AS_EMPTY);
            return;
        }
        out.writeString(value);
    }
}
