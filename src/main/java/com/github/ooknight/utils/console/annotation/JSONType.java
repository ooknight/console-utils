package com.github.ooknight.utils.console.annotation;

import com.github.ooknight.utils.console.PropertyNamingStrategy;
import com.github.ooknight.utils.console.serializer.SerializeFilter;
import com.github.ooknight.utils.console.serializer.SerializerFeature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JSONType {

    boolean asm() default true;

    String[] orders() default {};

    String[] includes() default {};

    String[] ignores() default {};

    SerializerFeature[] serialzeFeatures() default {};

    boolean alphabetic() default true;

    Class<?> mappingTo() default Void.class;

    Class<?> builder() default Void.class;

    String typeName() default "";

    String typeKey() default "";

    Class<?>[] seeAlso() default {};

    Class<?> serializer() default Void.class;

    boolean serializeEnumAsJavaBean() default false;

    PropertyNamingStrategy naming() default PropertyNamingStrategy.CamelCase;

    Class<? extends SerializeFilter>[] serialzeFilters() default {};
}
