package com.github.ooknight.utils.console.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Interface representing a custom serializer for fastjson. You should write a custom serializer, if
 * you are not happy with the default serialization done by fastjson. You will also need to register
 * this serializer through {@link SerializeConfig#put(Type, ObjectSerializer)}.
 *
 * <pre>
 * public static class Result {
 *     public ResultCode code;
 * }
 *
 * public static enum ResultCode {
 *     LOGIN_FAILURE(8), INVALID_ARGUMENT(0), SIGN_ERROR(17);
 *     public final int value;
 *     ResultCode(int value){
 *         this.value = value;
 *     }
 * }
 *
 * public static class ResultCodeSerilaizer implements ObjectSerializer {
 *     public void write(JSONSerializer serializer,
 *                       Object object,
 *                       Object fieldName,
 *                       Type fieldType,
 *                       int features) throws IOException {
 *         serializer.write(((ResultCode) object).value);
 *     }
 * }
 *
 * SerializeConfig.getGlobalInstance().put(ResultCode.class, new ResultCodeSerilaizer());
 *
 * Result result = new Result();
 * result.code = ResultCode.SIGN_ERROR;
 * String json = Inspector.string(result, config); // {"code":17}
 * Assert.assertEquals("{\"code\":17}", json);
 * </pre>
 */
public interface ObjectSerializer {

    /**
     * fastjson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     *
     * @param serializer serializer
     * @param object     src the object that needs to be converted to Json.
     * @param fieldName  parent object field name
     * @param fieldType  parent object field type
     * @param features   parent object field serializer features
     * @throws IOException exception
     */
    void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException;
}
