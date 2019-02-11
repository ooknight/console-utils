package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class LongCodec implements ObjectSerializer {

    public static final LongCodec instance = new LongCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(Feature.WRITE_NULL_NUMBER_AS_ZERO);
        } else {
            long value = (Long) object;
            out.writeLong(value);
            if (out.isEnabled(Feature.WRITE_CLASS_NAME) //
                    && value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE //
                    && fieldType != Long.class
                    && fieldType != long.class) {
                out.write('L');
            }
        }
    }
}
