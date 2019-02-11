package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;

public class BigIntegerCodec implements ObjectSerializer {

    public final static BigIntegerCodec instance = new BigIntegerCodec();
    private final static BigInteger LOW = BigInteger.valueOf(-9007199254740991L);
    private final static BigInteger HIGH = BigInteger.valueOf(9007199254740991L);

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WRITE_NULL_NUMBER_AS_ZERO);
            return;
        }
        BigInteger val = (BigInteger) object;
        String str = val.toString();
        if (str.length() >= 16
                && SerializerFeature.isEnabled(features, out.features, SerializerFeature.BROWSER_COMPATIBLE)
                && (val.compareTo(LOW) < 0
                || val.compareTo(HIGH) > 0)) {
            out.writeString(str);
            return;
        }
        out.write(str);
    }
}
