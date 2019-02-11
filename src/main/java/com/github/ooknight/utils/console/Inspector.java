package com.github.ooknight.utils.console;

import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.serializer.Feature;

import java.util.Locale;
import java.util.TimeZone;

public final class Inspector {

    public static final String VERSION = "1.0.0";
    public static TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();
    public static Locale DEFAULT_LOCALE = Locale.getDefault();
    public static String DEFAULT_TYPE_KEY = "@type";
    public static String DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static int DEFAULT_GENERATE_FEATURE = 0;

    static {
        //int features = 0;
        //features |= Feature.QUOTE_FIELD_NAMES.mask();
        //features |= Feature.SKIP_TRANSIENT_FIELD.mask();
        //features |= Feature.WRITE_ENUM_USING_NAME.mask();
        //features |= Feature.SORT_FIELD.mask();
        //DEFAULT_GENERATE_FEATURE = features;
    }

    public static String string(Object object) {
        return string(object, DEFAULT_GENERATE_FEATURE);
    }

    public static String string(Object object, Feature... features) {
        return string(object, DEFAULT_GENERATE_FEATURE, features);
    }

    public static String string(Object object, int defaultFeatures, Feature... features) {
        try (SerializeWriter out = new SerializeWriter(defaultFeatures, features)) {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.write(object);
            return out.toString();
        }
    }

    /*
    public static String string(Object object, String dateFormat, Feature... features) {
        return string(object, SerializeConfig.globalInstance, null, dateFormat, DEFAULT_GENERATE_FEATURE, features);
    }


    public static String string(Object object, SerializeFilter filter, Feature... features) {
        return string(object, SerializeConfig.globalInstance, new SerializeFilter[]{filter}, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static String string(Object object, SerializeFilter[] filters, Feature... features) {
        return string(object, SerializeConfig.globalInstance, filters, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static String string(Object object, SerializeConfig config, Feature... features) {
        return string(object, config, (SerializeFilter) null, features);
    }

    public static String string(Object object, SerializeConfig config, SerializeFilter filter, Feature... features) {
        return string(object, config, new SerializeFilter[]{filter}, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static String string(Object object, SerializeConfig config, SerializeFilter[] filters, Feature... features) {
        return string(object, config, filters, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static String string(Object object, SerializeConfig config, SerializeFilter[] filters, String dateFormat, int defaultFeatures, Feature... features) {
        SerializeWriter out = new SerializeWriter(null, defaultFeatures, features);
        try {
            JSONSerializer serializer = new JSONSerializer(out, config);
            if (dateFormat != null && dateFormat.length() != 0) {
                serializer.setDateFormat(dateFormat);
                serializer.config(Feature.WRITE_DATE_USE_DATE_FORMAT, true);
            }
            if (filters != null) {
                for (SerializeFilter filter : filters) {
                    serializer.addFilter(filter);
                }
            }
            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }

    public static String string(Object object, boolean prettyFormat) {
        if (!prettyFormat) {
            return string(object);
        }
        return string(object, Feature.PRETTY_FORMAT);
    }
    */
}
