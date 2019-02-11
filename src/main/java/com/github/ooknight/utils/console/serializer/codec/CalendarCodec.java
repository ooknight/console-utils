package com.github.ooknight.utils.console.serializer.codec;

import com.github.ooknight.utils.console.serializer.BeanContext;
import com.github.ooknight.utils.console.serializer.ContextObjectSerializer;
import com.github.ooknight.utils.console.serializer.Feature;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.util.IOUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarCodec implements ObjectSerializer, ContextObjectSerializer {

    public static final CalendarCodec instance = new CalendarCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        SerializeWriter out = serializer.out;
        String format = context.getFormat();
        Calendar calendar = (Calendar) object;
        if (format.equals("unixtime")) {
            long seconds = calendar.getTimeInMillis() / 1000L;
            out.writeInt((int) seconds);
            return;
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        String text = dateFormat.format(calendar.getTime());
        out.writeString(text);
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        Calendar calendar;
        if (object instanceof XMLGregorianCalendar) {
            calendar = ((XMLGregorianCalendar) object).toGregorianCalendar();
        } else {
            calendar = (Calendar) object;
        }
        if (out.isEnabled(Feature.USE_ISO8601_DATE_FORMAT)) {
            final char quote = out.isEnabled(Feature.USE_SINGLE_QUOTES) ? '\'' : '\"';
            out.append(quote);
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
                out.append("Z");
            } else if (timeZone > 0) {
                out.append("+").append(String.format("%02d", timeZone)).append(":00");
            } else {
                out.append("-").append(String.format("%02d", -timeZone)).append(":00");
            }
            out.append(quote);
        } else {
            Date date = calendar.getTime();
            serializer.write(date);
        }
    }
}
