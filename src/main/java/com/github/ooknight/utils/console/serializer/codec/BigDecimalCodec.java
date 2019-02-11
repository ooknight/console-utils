package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BigDecimalCodec implements ObjectSerializer {

    public final static BigDecimalCodec instance = new BigDecimalCodec();
    final static BigDecimal LOW = BigDecimal.valueOf(-9007199254740991L);
    final static BigDecimal HIGH = BigDecimal.valueOf(9007199254740991L);

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WRITE_NULL_NUMBER_AS_ZERO);
        } else {
            BigDecimal val = (BigDecimal) object;
            int scale = val.scale();
            String outText;
            if (out.isEnabled(SerializerFeature.WRITE_BIG_DECIMAL_AS_PLAIN) && scale >= -100 && scale < 100) {
                outText = val.toPlainString();
            } else {
                outText = val.toString();
            }
            if (scale == 0) {
                if (outText.length() >= 16
                        && SerializerFeature.isEnabled(features, out.features, SerializerFeature.BROWSER_COMPATIBLE)
                        && (val.compareTo(LOW) < 0
                        || val.compareTo(HIGH) > 0)) {
                    out.writeString(outText);
                    return;
                }
            }
            out.write(outText);
            if (out.isEnabled(SerializerFeature.WRITE_CLASS_NAME) && fieldType != BigDecimal.class && val.scale() == 0) {
                out.write('.');
            }
        }
    }
}
