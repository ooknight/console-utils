package com.github.ooknight.utils.console.annotation;

import com.github.ooknight.utils.console.serializer.SerializerFeature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface JSONField {

    /**
     * config encode/decode ordinal
     *
     * @return
     * @since 1.1.42
     */
    int ordinal() default 0;

    String name() default "";

    String format() default "";

    boolean serialize() default true;

    SerializerFeature[] serialzeFeatures() default {};

    String label() default "";

    /**
     * @since 1.2.12
     */
    boolean jsonDirect() default false;

    /**
     * Serializer class to use for serializing associated value.
     *
     * @since 1.2.16
     */
    Class<?> serializeUsing() default Void.class;

    /**
     * @return the alternative names of the field when it is deserialized
     * @since 1.2.21
     */
    String[] alternateNames() default {};

    /**
     * @since 1.2.31
     */
    boolean unwrapped() default false;
}
