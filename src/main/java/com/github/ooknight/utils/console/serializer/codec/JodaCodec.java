package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.serializer.BeanContext;
import com.github.ooknight.utils.console.serializer.ContextObjectSerializer;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.serializer.SerializerFeature;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.lang.reflect.Type;

public class JodaCodec implements ObjectSerializer, ContextObjectSerializer {

    public final static JodaCodec instance = new JodaCodec();
    private final static String formatter_iso8601_pattern = "yyyy-MM-dd'T'HH:mm:ss";
    private final static String formatter_iso8601_pattern_23 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private final static String formatter_iso8601_pattern_29 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    private final static DateTimeFormatter formatter_iso8601 = DateTimeFormat.forPattern(formatter_iso8601_pattern);

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else {
            if (fieldType == null) {
                fieldType = object.getClass();
            }
            if (fieldType == LocalDateTime.class) {
                final int mask = SerializerFeature.USE_ISO8601_DATE_FORMAT.mask();
                LocalDateTime dateTime = (LocalDateTime) object;
                String format = serializer.getDateFormatPattern();
                if (format == null) {
                    if ((features & mask) != 0 || serializer.isEnabled(SerializerFeature.USE_ISO8601_DATE_FORMAT)) {
                        format = formatter_iso8601_pattern;
                    } else {
                        int millis = dateTime.getMillisOfSecond();
                        if (millis == 0) {
                            format = formatter_iso8601_pattern_23;
                        } else {
                            format = formatter_iso8601_pattern_29;
                        }
                    }
                }
                if (format != null) {
                    write(out, dateTime, format);
                } else if (out.isEnabled(SerializerFeature.WRITE_DATE_USE_DATE_FORMAT)) {
                    //使用固定格式转化时间
                    write(out, dateTime, Inspector.DEFFAULT_DATE_FORMAT);
                } else {
                    out.writeLong(dateTime.toDateTime(DateTimeZone.forTimeZone(Inspector.DEFAULT_TIME_ZONE)).toInstant().getMillis());
                }
            } else {
                out.writeString(object.toString());
            }
        }
    }

    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        SerializeWriter out = serializer.out;
        String format = context.getFormat();
        write(out, (ReadablePartial) object, format);
    }

    private void write(SerializeWriter out, ReadablePartial object, String format) {
        DateTimeFormatter formatter;
        if (format == formatter_iso8601_pattern) {
            formatter = formatter_iso8601;
        } else {
            formatter = DateTimeFormat.forPattern(format);
        }
        String text = formatter.print(object);
        out.writeString(text);
    }
}
