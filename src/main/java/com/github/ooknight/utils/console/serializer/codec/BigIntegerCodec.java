package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;

public class BigIntegerCodec implements ObjectSerializer {

    public static final BigIntegerCodec instance = new BigIntegerCodec();
    private static final BigInteger LOW = BigInteger.valueOf(-9007199254740991L);
    private static final BigInteger HIGH = BigInteger.valueOf(9007199254740991L);

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(Feature.WRITE_NULL_NUMBER_AS_ZERO);
            return;
        }
        BigInteger val = (BigInteger) object;
        String str = val.toString();
        if (str.length() >= 16
                && Feature.isEnabled(features, out.features, Feature.BROWSER_COMPATIBLE)
                && (val.compareTo(LOW) < 0
                || val.compareTo(HIGH) > 0)) {
            out.writeString(str);
            return;
        }
        out.write(str);
    }
}
