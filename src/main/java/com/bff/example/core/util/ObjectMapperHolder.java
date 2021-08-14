package com.bff.example.core.util;

import com.bff.example.core.exception.JsonParserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ObjectMapperHolder {

    public static <T> T readValue(String content, Class<T> valueType) {
        try{
            return new ObjectMapper().readValue(content, valueType);
        } catch (Exception exception) {
            throw new JsonParserException(exception);
        }
    }

    public static String writeValueAsString(Object objeto) {
        try{
            return new ObjectMapper().writeValueAsString(objeto);
        } catch (Exception exception) {
            throw new JsonParserException(exception);
        }
    }

}
