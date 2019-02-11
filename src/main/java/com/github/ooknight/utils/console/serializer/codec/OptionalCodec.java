package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.JSONException;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class OptionalCodec implements ObjectSerializer {

    public static OptionalCodec instance = new OptionalCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }
        if (object instanceof Optional) {
            Optional<?> optional = (Optional<?>) object;
            Object value = optional.isPresent() ? optional.get() : null;
            serializer.write(value);
            return;
        }
        if (object instanceof OptionalDouble) {
            OptionalDouble optional = (OptionalDouble) object;
            if (optional.isPresent()) {
                double value = optional.getAsDouble();
                serializer.write(value);
            } else {
                serializer.writeNull();
            }
            return;
        }
        if (object instanceof OptionalInt) {
            OptionalInt optional = (OptionalInt) object;
            if (optional.isPresent()) {
                int value = optional.getAsInt();
                serializer.out.writeInt(value);
            } else {
                serializer.writeNull();
            }
            return;
        }
        if (object instanceof OptionalLong) {
            OptionalLong optional = (OptionalLong) object;
            if (optional.isPresent()) {
                long value = optional.getAsLong();
                serializer.out.writeLong(value);
            } else {
                serializer.writeNull();
            }
            return;
        }
        throw new JSONException("not support optional : " + object.getClass());
    }
}
