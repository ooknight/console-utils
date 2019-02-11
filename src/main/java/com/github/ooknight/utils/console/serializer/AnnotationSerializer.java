package com.github.ooknight.utils.console.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class AnnotationSerializer implements ObjectSerializer {

    public static AnnotationSerializer instance = new AnnotationSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        //Class objClass = object.getClass();
        //Class[] interfaces = objClass.getInterfaces();
        //if (interfaces.length == 1 && interfaces[0].isAnnotation()) {
        //    Class annotationClass = interfaces[0];
        //    AnnotationType type = AnnotationType.getInstance(annotationClass);
        //    Map<String, Method> members = type.members();
        //    JSONObject json = new JSONObject(members.size());
        //    Iterator<Map.Entry<String, Method>> iterator = members.entrySet().iterator();
        //    Map.Entry<String, Method> entry;
        //    Object val = null;
        //    while (iterator.hasNext()) {
        //        entry = iterator.next();
        //        try {
        //            val = entry.getValue().invoke(object);
        //        } catch (IllegalAccessException e) {
        //            // skip
        //        } catch (InvocationTargetException e) {
        //            // skip
        //        }
        //        json.put(entry.getKey(), Inspector.toJSON(val));
        //    }
        //    serializer.write(json);
        //    return;
        //}
        throw new RuntimeException("annotation serializer does not support in console utils");
    }
}
