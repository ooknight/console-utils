package com.github.ooknight.utils.console.serializer;

public interface ContextValueFilter extends SerializeFilter {

    Object process(BeanContext context, Object object, String name, Object value);
}
