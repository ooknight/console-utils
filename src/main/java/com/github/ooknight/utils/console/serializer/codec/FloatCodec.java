package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FloatCodec implements ObjectSerializer {

    public static final FloatCodec instance = new FloatCodec();
    private NumberFormat decimalFormat;

    public FloatCodec() {
    }

    public FloatCodec(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    public FloatCodec(String decimalFormat) {
        this(new DecimalFormat(decimalFormat));
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(Feature.WRITE_NULL_NUMBER_AS_ZERO);
            return;
        }
        float floatValue = (Float) object;
        if (decimalFormat != null) {
            String floatText = decimalFormat.format(floatValue);
            out.write(floatText);
        } else {
            out.writeFloat(floatValue, true);
        }
    }
}
