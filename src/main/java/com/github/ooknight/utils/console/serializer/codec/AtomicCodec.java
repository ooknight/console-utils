package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicCodec implements ObjectSerializer {

    public final static AtomicCodec instance = new AtomicCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object instanceof AtomicInteger) {
            AtomicInteger val = (AtomicInteger) object;
            out.writeInt(val.get());
            return;
        }
        if (object instanceof AtomicLong) {
            AtomicLong val = (AtomicLong) object;
            out.writeLong(val.get());
            return;
        }
        if (object instanceof AtomicBoolean) {
            AtomicBoolean val = (AtomicBoolean) object;
            out.append(val.get() ? "true" : "false");
            return;
        }
        if (object == null) {
            out.writeNull(SerializerFeature.WRITE_NULL_LIST_AS_EMPTY);
            return;
        }
        if (object instanceof AtomicIntegerArray) {
            AtomicIntegerArray array = (AtomicIntegerArray) object;
            int len = array.length();
            out.write('[');
            for (int i = 0; i < len; ++i) {
                int val = array.get(i);
                if (i != 0) {
                    out.write(',');
                }
                out.writeInt(val);
            }
            out.write(']');
            return;
        }
        AtomicLongArray array = (AtomicLongArray) object;
        int len = array.length();
        out.write('[');
        for (int i = 0; i < len; ++i) {
            long val = array.get(i);
            if (i != 0) {
                out.write(',');
            }
            out.writeLong(val);
        }
        out.write(']');
    }
}
