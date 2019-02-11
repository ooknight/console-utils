package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.util.IOUtils;
import com.github.ooknight.utils.console.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCodec implements ObjectSerializer {

    public static final DateCodec instance = new DateCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        Date date;
        if (object instanceof Date) {
            date = (Date) object;
        } else {
            date = TypeUtils.castToDate(object);
        }
        if (out.isEnabled(Feature.WRITE_DATE_USE_DATE_FORMAT)) {
            DateFormat format = serializer.getDateFormat();
            if (format == null) {
                format = new SimpleDateFormat(Inspector.DEFFAULT_DATE_FORMAT, serializer.locale);
                format.setTimeZone(serializer.timeZone);
            }
            String text = format.format(date);
            out.writeString(text);
            return;
        }
        if (out.isEnabled(Feature.WRITE_CLASS_NAME)) {
            if (object.getClass() != fieldType) {
                if (object.getClass() == java.util.Date.class) {
                    out.write("new Date(");
                    out.writeLong(((Date) object).getTime());
                    out.write(')');
                } else {
                    out.write('{');
                    out.writeFieldName(Inspector.DEFAULT_TYPE_KEY);
                    serializer.write(object.getClass().getName());
                    out.writeFieldValue(',', "val", ((Date) object).getTime());
                    out.write('}');
                }
                return;
            }
        }
        long time = date.getTime();
        if (out.isEnabled(Feature.USE_ISO8601_DATE_FORMAT)) {
            char quote = out.isEnabled(Feature.USE_SINGLE_QUOTES) ? '\'' : '\"';
            out.write(quote);
            Calendar calendar = Calendar.getInstance(serializer.timeZone, serializer.locale);
            calendar.setTimeInMillis(time);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int millis = calendar.get(Calendar.MILLISECOND);
            char[] buf;
            if (millis != 0) {
                buf = "0000-00-00T00:00:00.000".toCharArray();
                IOUtils.getChars(millis, 23, buf);
                IOUtils.getChars(second, 19, buf);
                IOUtils.getChars(minute, 16, buf);
                IOUtils.getChars(hour, 13, buf);
                IOUtils.getChars(day, 10, buf);
                IOUtils.getChars(month, 7, buf);
                IOUtils.getChars(year, 4, buf);
            } else {
                if (second == 0 && minute == 0 && hour == 0) {
                    buf = "0000-00-00".toCharArray();
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                } else {
                    buf = "0000-00-00T00:00:00".toCharArray();
                    IOUtils.getChars(second, 19, buf);
                    IOUtils.getChars(minute, 16, buf);
                    IOUtils.getChars(hour, 13, buf);
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                }
            }
            out.write(buf);
            int timeZone = calendar.getTimeZone().getOffset(calendar.getTimeInMillis()) / (3600 * 1000);
            if (timeZone == 0) {
                out.write('Z');
            } else {
                if (timeZone > 9) {
                    out.write('+');
                    out.writeInt(timeZone);
                } else if (timeZone > 0) {
                    out.write('+');
                    out.write('0');
                    out.writeInt(timeZone);
                } else if (timeZone < -9) {
                    out.write('-');
                    out.writeInt(timeZone);
                } else if (timeZone < 0) {
                    out.write('-');
                    out.write('0');
                    out.writeInt(-timeZone);
                }
                out.append(":00");
            }
            out.write(quote);
        } else {
            out.writeLong(time);
        }
    }
}
