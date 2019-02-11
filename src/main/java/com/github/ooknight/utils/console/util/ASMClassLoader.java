package com.github.ooknight.utils.console.util;

import com.github.ooknight.utils.console.Inspector;
import com.github.ooknight.utils.console.JSONException;
import com.github.ooknight.utils.console.JSONPathException;
import com.github.ooknight.utils.console.JSONWriter;
import com.github.ooknight.utils.console.TypeReference;
import com.github.ooknight.utils.console.serializer.AfterFilter;
import com.github.ooknight.utils.console.serializer.BeanContext;
import com.github.ooknight.utils.console.serializer.BeforeFilter;
import com.github.ooknight.utils.console.serializer.ContextObjectSerializer;
import com.github.ooknight.utils.console.serializer.ContextValueFilter;
import com.github.ooknight.utils.console.serializer.JSONSerializer;
import com.github.ooknight.utils.console.serializer.JavaBeanSerializer;
import com.github.ooknight.utils.console.serializer.LabelFilter;
import com.github.ooknight.utils.console.serializer.Labels;
import com.github.ooknight.utils.console.serializer.NameFilter;
import com.github.ooknight.utils.console.serializer.ObjectSerializer;
import com.github.ooknight.utils.console.serializer.PropertyFilter;
import com.github.ooknight.utils.console.serializer.PropertyPreFilter;
import com.github.ooknight.utils.console.serializer.SerialContext;
import com.github.ooknight.utils.console.serializer.SerializeBeanInfo;
import com.github.ooknight.utils.console.serializer.SerializeConfig;
import com.github.ooknight.utils.console.serializer.SerializeFilter;
import com.github.ooknight.utils.console.serializer.SerializeFilterable;
import com.github.ooknight.utils.console.serializer.SerializeWriter;
import com.github.ooknight.utils.console.serializer.SerializerFeature;
import com.github.ooknight.utils.console.serializer.ValueFilter;

import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

public class ASMClassLoader extends ClassLoader {

    private static java.security.ProtectionDomain DOMAIN;
    private static Map<String, Class<?>> classMapping = new HashMap<>();

    static {
        DOMAIN = (java.security.ProtectionDomain) java.security.AccessController.doPrivileged((PrivilegedAction<Object>) ASMClassLoader.class::getProtectionDomain);
        Class<?>[] jsonClasses = new Class<?>[]{
                Inspector.class,
                JSONException.class,
                JSONPathException.class,
                JSONWriter.class,
                TypeReference.class,
                FieldInfo.class,
                TypeUtils.class,
                IOUtils.class,
                IdentityHashMap.class,
                ParameterizedTypeImpl.class,
                JavaBeanInfo.class,
                ObjectSerializer.class,
                JavaBeanSerializer.class,
                SerializeFilterable.class,
                SerializeBeanInfo.class,
                JSONSerializer.class,
                SerializeWriter.class,
                SerializeFilter.class,
                Labels.class,
                LabelFilter.class,
                ContextValueFilter.class,
                AfterFilter.class,
                BeforeFilter.class,
                NameFilter.class,
                PropertyFilter.class,
                PropertyPreFilter.class,
                ValueFilter.class,
                SerializerFeature.class,
                ContextObjectSerializer.class,
                SerialContext.class,
                SerializeConfig.class,
                BeanContext.class,
        };
        for (Class<?> clazz : jsonClasses) {
            classMapping.put(clazz.getName(), clazz);
        }
    }

    public ASMClassLoader() {
        super(getParentClassLoader());
    }

    public ASMClassLoader(ClassLoader parent) {
        super(parent);
    }

    private static ClassLoader getParentClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            try {
                contextClassLoader.loadClass(Inspector.class.getName());
                return contextClassLoader;
            } catch (ClassNotFoundException e) {
                // skip
            }
        }
        return Inspector.class.getClassLoader();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> mappingClass = classMapping.get(name);
        if (mappingClass != null) {
            return mappingClass;
        }
        return super.loadClass(name, resolve);
    }

    public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
        return defineClass(name, b, off, len, DOMAIN);
    }

    public boolean isExternalClass(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        if (classLoader == null) {
            return false;
        }
        ClassLoader current = this;
        while (current != null) {
            if (current == classLoader) {
                return false;
            }
            current = current.getParent();
        }
        return true;
    }
}
