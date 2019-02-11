package com.github.ooknight.utils.console.util;

import com.github.ooknight.utils.console.annotation.JSONField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class JavaBeanInfo {

    public final Class<?> clazz;
    public final Class<?> builderClass;
    public final Constructor<?> defaultConstructor;
    public final Constructor<?> creatorConstructor;
    public final Method factoryMethod;
    public final Method buildMethod;
    public final int defaultConstructorParameterSize;
    public final FieldInfo[] fields;
    public final FieldInfo[] sortedFields;
    public final String typeName;
    public final String typeKey;
    public String[] orders;
    public Type[] creatorConstructorParameterTypes;
    public String[] creatorConstructorParameters;
    public boolean kotlin;
    public Constructor<?> kotlinDefaultConstructor;

    public JavaBeanInfo(Class<?> clazz, Class<?> builderClass, Constructor<?> defaultConstructor, Constructor<?> creatorConstructor, Method factoryMethod, Method buildMethod, List<FieldInfo> fieldList) {
        this.clazz = clazz;
        this.builderClass = builderClass;
        this.defaultConstructor = defaultConstructor;
        this.creatorConstructor = creatorConstructor;
        this.factoryMethod = factoryMethod;
        this.buildMethod = buildMethod;
        this.typeName = clazz.getName();
        this.typeKey = null;
        this.orders = null;
        fields = new FieldInfo[fieldList.size()];
        fieldList.toArray(fields);
        FieldInfo[] sortedFields = new FieldInfo[fields.length];
        System.arraycopy(fields, 0, sortedFields, 0, fields.length);
        Arrays.sort(sortedFields);
        if (Arrays.equals(fields, sortedFields)) {
            sortedFields = fields;
        }
        this.sortedFields = sortedFields;
        if (defaultConstructor != null) {
            defaultConstructorParameterSize = defaultConstructor.getParameterTypes().length;
        } else if (factoryMethod != null) {
            defaultConstructorParameterSize = factoryMethod.getParameterTypes().length;
        } else {
            defaultConstructorParameterSize = 0;
        }
        if (creatorConstructor != null) {
            this.creatorConstructorParameterTypes = creatorConstructor.getParameterTypes();
            kotlin = TypeUtils.isKotlin(clazz);
            if (kotlin) {
                this.creatorConstructorParameters = TypeUtils.getKoltinConstructorParameters(clazz);
                try {
                    this.kotlinDefaultConstructor = clazz.getConstructor();
                } catch (Throwable ex) {
                    // skip
                }
                Annotation[][] paramAnnotationArrays = creatorConstructor.getParameterAnnotations();
                for (int i = 0; i < creatorConstructorParameters.length && i < paramAnnotationArrays.length; ++i) {
                    Annotation[] paramAnnotations = paramAnnotationArrays[i];
                    JSONField fieldAnnotation = null;
                    for (Annotation paramAnnotation : paramAnnotations) {
                        if (paramAnnotation instanceof JSONField) {
                            fieldAnnotation = (JSONField) paramAnnotation;
                            break;
                        }
                    }
                    if (fieldAnnotation != null) {
                        String fieldAnnotationName = fieldAnnotation.name();
                        if (fieldAnnotationName.length() > 0) {
                            creatorConstructorParameters[i] = fieldAnnotationName;
                        }
                    }
                }
            } else {
                boolean match;
                if (creatorConstructorParameterTypes.length != fields.length) {
                    match = false;
                } else {
                    match = true;
                    for (int i = 0; i < creatorConstructorParameterTypes.length; i++) {
                        if (creatorConstructorParameterTypes[i] != fields[i].fieldClass) {
                            match = false;
                            break;
                        }
                    }
                }
                if (!match) {
                    this.creatorConstructorParameters = ASMUtils.lookupParameterNames(creatorConstructor);
                }
            }
        }
    }
}
