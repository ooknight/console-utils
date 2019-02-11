package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.JSONException;
import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;

public class AwtCodec implements ObjectSerializer {

    public static final AwtCodec instance = new AwtCodec();

    public static boolean support(Class<?> clazz) {
        return clazz == Point.class || clazz == Rectangle.class || clazz == Font.class || clazz == Color.class;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        char sep = '{';
        if (object instanceof Point) {
            Point font = (Point) object;
            sep = writeClassName(out, Point.class, sep);
            out.writeFieldValue(sep, "x", font.x);
            out.writeFieldValue(',', "y", font.y);
        } else if (object instanceof Font) {
            Font font = (Font) object;
            sep = writeClassName(out, Font.class, sep);
            out.writeFieldValue(sep, "name", font.getName());
            out.writeFieldValue(',', "style", font.getStyle());
            out.writeFieldValue(',', "size", font.getSize());
        } else if (object instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) object;
            sep = writeClassName(out, Rectangle.class, sep);
            out.writeFieldValue(sep, "x", rectangle.x);
            out.writeFieldValue(',', "y", rectangle.y);
            out.writeFieldValue(',', "width", rectangle.width);
            out.writeFieldValue(',', "height", rectangle.height);
        } else if (object instanceof Color) {
            Color color = (Color) object;
            sep = writeClassName(out, Color.class, sep);
            out.writeFieldValue(sep, "r", color.getRed());
            out.writeFieldValue(',', "g", color.getGreen());
            out.writeFieldValue(',', "b", color.getBlue());
            if (color.getAlpha() > 0) {
                out.writeFieldValue(',', "alpha", color.getAlpha());
            }
        } else {
            throw new JSONException("not support awt class : " + object.getClass().getName());
        }
        out.write('}');
    }

    private char writeClassName(SerializeWriter out, Class<?> clazz, char sep) {
        if (out.isEnabled(Feature.WRITE_CLASS_NAME)) {
            out.write('{');
            out.writeFieldName(Inspector.DEFAULT_TYPE_KEY);
            out.writeString(clazz.getName());
            sep = ',';
        }
        return sep;
    }
}
