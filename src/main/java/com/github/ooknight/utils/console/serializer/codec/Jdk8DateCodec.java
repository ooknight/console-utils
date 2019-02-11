package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.serializer.BeanContext;
import com.github.ooknight.utils.console.serializer.ContextObjectSerializer;
import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class Jdk8DateCodec implements ObjectSerializer, ContextObjectSerializer {

    public static final Jdk8DateCodec instance = new Jdk8DateCodec();
    private static final String formatter_iso8601_pattern = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String formatter_iso8601_pattern_23 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String formatter_iso8601_pattern_29 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    private static final DateTimeFormatter formatter_iso8601 = DateTimeFormatter.ofPattern(formatter_iso8601_pattern);

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else {
            if (fieldType == null) {
                fieldType = object.getClass();
            }
            if (fieldType == LocalDateTime.class) {
                final int mask = Feature.USE_ISO8601_DATE_FORMAT.mask();
                LocalDateTime dateTime = (LocalDateTime) object;
                String format = serializer.getDateFormatPattern();
                if (format == null) {
                    if ((features & mask) != 0 || serializer.isEnabled(Feature.USE_ISO8601_DATE_FORMAT)) {
                        format = formatter_iso8601_pattern;
                    } else {
                        int nano = dateTime.getNano();
                        if (nano == 0) {
                            format = formatter_iso8601_pattern;
                        } else if (nano % 1000000 == 0) {
                            format = formatter_iso8601_pattern_23;
                        } else {
                            format = formatter_iso8601_pattern_29;
                        }
                    }
                }
                if (format != null) {
                    write(out, dateTime, format);
                } else if (out.isEnabled(Feature.WRITE_DATE_USE_DATE_FORMAT)) {
                    //使用固定格式转化时间
                    write(out, dateTime, Inspector.DEFFAULT_DATE_FORMAT);
                } else {
                    out.writeLong(dateTime.atZone(Inspector.DEFAULT_TIME_ZONE.toZoneId()).toInstant().toEpochMilli());
                }
            } else {
                out.writeString(object.toString());
            }
        }
    }

    @Override
    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        SerializeWriter out = serializer.out;
        String format = context.getFormat();
        write(out, (TemporalAccessor) object, format);
    }

    private void write(SerializeWriter out, TemporalAccessor object, String format) {
        DateTimeFormatter formatter;
        if ("unixtime".equals(format) && object instanceof ChronoZonedDateTime) {
            long seconds = ((ChronoZonedDateTime) object).toEpochSecond();
            out.writeInt((int) seconds);
            return;
        }
        if (format == formatter_iso8601_pattern) {
            formatter = formatter_iso8601;
        } else {
            formatter = DateTimeFormatter.ofPattern(format);
        }
        String text = formatter.format(object);
        out.writeString(text);
    }
}
