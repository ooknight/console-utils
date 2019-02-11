package com.github.ooknight.utils.console.serializer;

public enum SerializerFeature {
    /**
     *
     */
    QUOTE_FIELD_NAMES,
    /**
     *
     */
    USE_SINGLE_QUOTES,
    /**
     *
     */
    WRITE_MAP_NULL_VALUE,
    /**
     * 用枚举toString()值输出
     */
    WRITE_ENUM_USING_TO_STRING,
    /**
     * 用枚举name()输出
     */
    WRITE_ENUM_USING_NAME,
    /**
     *
     */
    USE_ISO8601_DATE_FORMAT,
    /**
     *
     */
    WRITE_NULL_LIST_AS_EMPTY,
    /**
     *
     */
    WRITE_NULL_STRING_AS_EMPTY,
    /**
     *
     */
    WRITE_NULL_NUMBER_AS_ZERO,
    /**
     *
     */
    WRITE_NULL_BOOLEAN_AS_FALSE,
    /**
     *
     */
    SKIP_TRANSIENT_FIELD,
    /**
     *
     */
    SORT_FIELD,
    /**
     * @deprecated
     */
    @Deprecated
    WRITE_TAB_AS_SPECIAL,
    /**
     *
     */
    PRETTY_FORMAT,
    /**
     *
     */
    WRITE_CLASS_NAME,
    /**
     *
     */
    DISABLE_CIRCULAR_REFERENCE_DETECT, // 32768
    /**
     *
     */
    WRITE_SLASH_AS_SPECIAL,
    /**
     *
     */
    BROWSER_COMPATIBLE,
    /**
     *
     */
    WRITE_DATE_USE_DATE_FORMAT,
    /**
     *
     */
    NOT_WRITE_ROOT_CLASS_NAME,
    /**
     * @deprecated
     */
    @Deprecated
    DISABLE_CHECK_SPECIAL_CHAR,
    /**
     *
     */
    BEAN_TO_ARRAY,
    /**
     *
     */
    WRITE_NON_STRING_KEY_AS_STRING,
    /**
     *
     */
    NOT_WRITE_DEFAULT_VALUE,
    /**
     *
     */
    BROWSER_SECURE,
    /**
     *
     */
    IGNORE_NON_FIELD_GETTER,
    /**
     *
     */
    WRITE_NON_STRING_VALUE_AS_STRING,
    /**
     *
     */
    IGNORE_ERROR_GETTER,
    /**
     *
     */
    WRITE_BIG_DECIMAL_AS_PLAIN,
    /**
     *
     */
    MAP_SORT_FIELD;
    //
    public final static SerializerFeature[] EMPTY = new SerializerFeature[0];
    public static final int WRITE_MAP_NULL_FEATURES
            = WRITE_MAP_NULL_VALUE.mask()
            | WRITE_NULL_BOOLEAN_AS_FALSE.mask()
            | WRITE_NULL_LIST_AS_EMPTY.mask()
            | WRITE_NULL_NUMBER_AS_ZERO.mask()
            | WRITE_NULL_STRING_AS_EMPTY.mask();
    public final int mask;

    SerializerFeature() {
        mask = (1 << ordinal());
    }

    public static boolean isEnabled(int features, SerializerFeature feature) {
        return (features & feature.mask) != 0;
    }

    public static boolean isEnabled(int features, int fieaturesB, SerializerFeature feature) {
        int mask = feature.mask;
        return (features & mask) != 0 || (fieaturesB & mask) != 0;
    }

    public static int config(int features, SerializerFeature feature, boolean state) {
        if (state) {
            features |= feature.mask;
        } else {
            features &= ~feature.mask;
        }
        return features;
    }

    public static int of(SerializerFeature[] features) {
        if (features == null) {
            return 0;
        }
        int value = 0;
        for (SerializerFeature feature : features) {
            value |= feature.mask;
        }
        return value;
    }

    public final int mask() {
        return mask;
    }
}
