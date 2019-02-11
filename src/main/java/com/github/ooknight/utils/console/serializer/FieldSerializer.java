package com.github.ooknight.utils.console.serializer;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.annotation.JSONField;
import com.github.ooknight.utils.console.serializer.codec.FloatCodec;
import com.github.ooknight.utils.console.util.FieldInfo;
import com.github.ooknight.utils.console.util.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class FieldSerializer implements Comparable<FieldSerializer> {

    public final FieldInfo fieldInfo;
    protected final boolean writeNull;
    private final String double_quoted_fieldPrefix;
    protected int features;
    protected BeanContext fieldContext;
    protected boolean writeEnumUsingToString = false;
    protected boolean writeEnumUsingName = false;
    protected boolean disableCircularReferenceDetect = false;
    protected boolean serializeUsing = false;
    protected boolean persistenceXToMany;
    protected boolean browserCompatible;
    private String single_quoted_fieldPrefix;
    private String un_quoted_fieldPrefix;
    private String format;
    private RuntimeSerializerInfo runtimeInfo;

    public FieldSerializer(Class<?> beanType, FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
        this.fieldContext = new BeanContext(beanType, fieldInfo);
        fieldInfo.setAccessible();
        this.double_quoted_fieldPrefix = '"' + fieldInfo.name + "\"=";
        boolean writeNull = false;
        JSONField annotation = fieldInfo.getAnnotation();
        if (annotation != null) {
            for (Feature feature : annotation.serialzeFeatures()) {
                if ((feature.mask() & Feature.WRITE_MAP_NULL_FEATURES) != 0) {
                    writeNull = true;
                    break;
                }
            }
            format = annotation.format();
            if (format.trim().length() == 0) {
                format = null;
            }
            for (Feature feature : annotation.serialzeFeatures()) {
                if (feature == Feature.WRITE_ENUM_USING_TO_STRING) {
                    writeEnumUsingToString = true;
                } else if (feature == Feature.WRITE_ENUM_USING_NAME) {
                    writeEnumUsingName = true;
                } else if (feature == Feature.DISABLE_CIRCULAR_REFERENCE_DETECT) {
                    disableCircularReferenceDetect = true;
                } else if (feature == Feature.BROWSER_COMPATIBLE) {
                    browserCompatible = true;
                }
            }
            features = Feature.of(annotation.serialzeFeatures());
        }
        this.writeNull = writeNull;
        persistenceXToMany = TypeUtils.isAnnotationPresentOneToMany(fieldInfo.method)
                || TypeUtils.isAnnotationPresentManyToMany(fieldInfo.method);
    }

    public void writePrefix(JSONSerializer serializer) {
        SerializeWriter out = serializer.out;
        if (out.quoteFieldNames) {
            if (out.useSingleQuotes) {
                if (single_quoted_fieldPrefix == null) {
                    single_quoted_fieldPrefix = '\'' + fieldInfo.name + "\'=";
                }
                out.write(single_quoted_fieldPrefix);
            } else {
                out.write(double_quoted_fieldPrefix);
            }
        } else {
            if (un_quoted_fieldPrefix == null) {
                this.un_quoted_fieldPrefix = fieldInfo.name + "=";
            }
            out.write(un_quoted_fieldPrefix);
        }
    }

    public Object getPropertyValueDirect(Object object) throws InvocationTargetException, IllegalAccessException {
        Object fieldValue = fieldInfo.get(object);
        if (persistenceXToMany && !TypeUtils.isHibernateInitialized(fieldValue)) {
            return null;
        }
        return fieldValue;
    }

    public Object getPropertyValue(Object object) throws InvocationTargetException, IllegalAccessException {
        Object propertyValue = fieldInfo.get(object);
        if (format != null && propertyValue != null) {
            if (fieldInfo.fieldClass == Date.class) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Inspector.DEFAULT_LOCALE);
                dateFormat.setTimeZone(Inspector.DEFAULT_TIME_ZONE);
                return dateFormat.format(propertyValue);
            }
        }
        return propertyValue;
    }

    public int compareTo(FieldSerializer o) {
        return this.fieldInfo.compareTo(o.fieldInfo);
    }

    public void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception {
        if (runtimeInfo == null) {
            Class<?> runtimeFieldClass;
            if (propertyValue == null) {
                runtimeFieldClass = this.fieldInfo.fieldClass;
                if (runtimeFieldClass == byte.class) {
                    runtimeFieldClass = Byte.class;
                } else if (runtimeFieldClass == short.class) {
                    runtimeFieldClass = Short.class;
                } else if (runtimeFieldClass == int.class) {
                    runtimeFieldClass = Integer.class;
                } else if (runtimeFieldClass == long.class) {
                    runtimeFieldClass = Long.class;
                } else if (runtimeFieldClass == float.class) {
                    runtimeFieldClass = Float.class;
                } else if (runtimeFieldClass == double.class) {
                    runtimeFieldClass = Double.class;
                } else if (runtimeFieldClass == boolean.class) {
                    runtimeFieldClass = Boolean.class;
                }
            } else {
                runtimeFieldClass = propertyValue.getClass();
            }
            ObjectSerializer fieldSerializer = null;
            JSONField fieldAnnotation = fieldInfo.getAnnotation();
            if (fieldAnnotation != null && fieldAnnotation.serializeUsing() != Void.class) {
                fieldSerializer = (ObjectSerializer) fieldAnnotation.serializeUsing().newInstance();
                serializeUsing = true;
            } else {
                if (format != null) {
                    if (runtimeFieldClass == double.class || runtimeFieldClass == Double.class) {
                        fieldSerializer = new DoubleSerializer(format);
                    } else if (runtimeFieldClass == float.class || runtimeFieldClass == Float.class) {
                        fieldSerializer = new FloatCodec(format);
                    }
                }
                if (fieldSerializer == null) {
                    fieldSerializer = serializer.getObjectWriter(runtimeFieldClass);
                }
            }
            runtimeInfo = new RuntimeSerializerInfo(fieldSerializer, runtimeFieldClass);
        }
        final RuntimeSerializerInfo runtimeInfo = this.runtimeInfo;
        final int fieldFeatures
                = (disableCircularReferenceDetect
                ? (fieldInfo.serialzeFeatures | Feature.DISABLE_CIRCULAR_REFERENCE_DETECT.mask)
                : fieldInfo.serialzeFeatures) | features;
        if (propertyValue == null) {
            SerializeWriter out = serializer.out;
            if (fieldInfo.fieldClass == Object.class
                    && out.isEnabled(Feature.WRITE_MAP_NULL_FEATURES)) {
                out.writeNull();
                return;
            }
            Class<?> runtimeFieldClass = runtimeInfo.runtimeFieldClass;
            if (Number.class.isAssignableFrom(runtimeFieldClass)) {
                out.writeNull(features, Feature.WRITE_NULL_NUMBER_AS_ZERO.mask);
                return;
            } else if (String.class == runtimeFieldClass) {
                out.writeNull(features, Feature.WRITE_NULL_STRING_AS_EMPTY.mask);
                return;
            } else if (Boolean.class == runtimeFieldClass) {
                out.writeNull(features, Feature.WRITE_NULL_BOOLEAN_AS_FALSE.mask);
                return;
            } else if (Collection.class.isAssignableFrom(runtimeFieldClass)) {
                out.writeNull(features, Feature.WRITE_NULL_LIST_AS_EMPTY.mask);
                return;
            }
            ObjectSerializer fieldSerializer = runtimeInfo.fieldSerializer;
            if ((out.isEnabled(Feature.WRITE_MAP_NULL_FEATURES))
                    && fieldSerializer instanceof JavaBeanSerializer) {
                out.writeNull();
                return;
            }
            fieldSerializer.write(serializer, null, fieldInfo.name, fieldInfo.fieldType, fieldFeatures);
            return;
        }
        if (fieldInfo.isEnum) {
            if (writeEnumUsingName) {
                serializer.out.writeString(((Enum<?>) propertyValue).name());
                return;
            }
            if (writeEnumUsingToString) {
                serializer.out.writeString((propertyValue).toString());
                return;
            }
        }
        Class<?> valueClass = propertyValue.getClass();
        ObjectSerializer valueSerializer;
        if (valueClass == runtimeInfo.runtimeFieldClass || serializeUsing) {
            valueSerializer = runtimeInfo.fieldSerializer;
        } else {
            valueSerializer = serializer.getObjectWriter(valueClass);
        }
        if (format != null && !(valueSerializer instanceof DoubleSerializer || valueSerializer instanceof FloatCodec)) {
            if (valueSerializer instanceof ContextObjectSerializer) {
                ((ContextObjectSerializer) valueSerializer).write(serializer, propertyValue, this.fieldContext);
            } else {
                serializer.writeWithFormat(propertyValue, format);
            }
            return;
        }
        if (fieldInfo.unwrapped) {
            if (valueSerializer instanceof JavaBeanSerializer) {
                JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) valueSerializer;
                javaBeanSerializer.write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType, fieldFeatures, true);
                return;
            }
            if (valueSerializer instanceof MapSerializer) {
                MapSerializer mapSerializer = (MapSerializer) valueSerializer;
                mapSerializer.write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType, fieldFeatures, true);
                return;
            }
        }
        if ((features & Feature.WRITE_CLASS_NAME.mask) != 0
                && valueClass != fieldInfo.fieldClass
                && JavaBeanSerializer.class.isInstance(valueSerializer)) {
            ((JavaBeanSerializer) valueSerializer).write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType, fieldFeatures, false);
            return;
        }
        if (browserCompatible && propertyValue != null
                && (fieldInfo.fieldClass == long.class || fieldInfo.fieldClass == Long.class)) {
            long value = (Long) propertyValue;
            if (value > 9007199254740991L || value < -9007199254740991L) {
                serializer.getWriter().writeString(Long.toString(value));
                return;
            }
        }
        valueSerializer.write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType, fieldFeatures);
    }

    static class RuntimeSerializerInfo {

        final ObjectSerializer fieldSerializer;
        final Class<?> runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer fieldSerializer, Class<?> runtimeFieldClass) {
            this.fieldSerializer = fieldSerializer;
            this.runtimeFieldClass = runtimeFieldClass;
        }
    }
}
