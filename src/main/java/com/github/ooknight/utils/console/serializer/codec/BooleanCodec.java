package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class BooleanCodec implements ObjectSerializer {

    public static final BooleanCodec instance = new BooleanCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        Boolean value = (Boolean) object;
        if (value == null) {
            out.writeNull(Feature.WRITE_NULL_BOOLEAN_AS_FALSE);
            return;
        }
        if (value) {
            out.write("true");
        } else {
            out.write("false");
        }
    }
}
