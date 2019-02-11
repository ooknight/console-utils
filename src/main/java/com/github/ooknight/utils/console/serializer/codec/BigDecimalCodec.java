package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BigDecimalCodec implements ObjectSerializer {

    public static final BigDecimalCodec instance = new BigDecimalCodec();
    private static final BigDecimal LOW = BigDecimal.valueOf(-9007199254740991L);
    private static final BigDecimal HIGH = BigDecimal.valueOf(9007199254740991L);

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(Feature.WRITE_NULL_NUMBER_AS_ZERO);
        } else {
            BigDecimal val = (BigDecimal) object;
            int scale = val.scale();
            String outText;
            if (out.isEnabled(Feature.WRITE_BIG_DECIMAL_AS_PLAIN) && scale >= -100 && scale < 100) {
                outText = val.toPlainString();
            } else {
                outText = val.toString();
            }
            if (scale == 0) {
                if (outText.length() >= 16
                        && Feature.isEnabled(features, out.features, Feature.BROWSER_COMPATIBLE)
                        && (val.compareTo(LOW) < 0
                        || val.compareTo(HIGH) > 0)) {
                    out.writeString(outText);
                    return;
                }
            }
            out.write(outText);
            if (out.isEnabled(Feature.WRITE_CLASS_NAME) && fieldType != BigDecimal.class && val.scale() == 0) {
                out.write('.');
            }
        }
    }
}
