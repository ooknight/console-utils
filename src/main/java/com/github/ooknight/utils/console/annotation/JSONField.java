package com.github.ooknight.utils.console.annotation;

import com.github.ooknight.utils.console.serializer.Feature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface JSONField {

    int ordinal() default 0;

    String name() default "";

    String format() default "";

    boolean serialize() default true;

    Feature[] serialzeFeatures() default {};

    String label() default "";

    boolean jsonDirect() default false;

    /**
     * Serializer class to use for serializing associated value.
     */
    Class<?> serializeUsing() default Void.class;

    /**
     * @return the alternative names of the field when it is deserialized
     */
    String[] alternateNames() default {};

    boolean unwrapped() default false;
}
