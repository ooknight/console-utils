package com.github.ooknight.utils.console.serializer;

import java.io.IOException;

public interface ContextObjectSerializer extends ObjectSerializer {
    void write(JSONSerializer serializer, //
               Object object, //
               BeanContext context) throws IOException;
}
