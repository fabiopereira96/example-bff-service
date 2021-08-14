package com.bff.example.core.util;

import com.github.mustachejava.DefaultMustacheFactory;
import lombok.experimental.UtilityClass;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.bff.example.core.util.ObjectMapperHolder.readValue;

@UtilityClass
public final class MustacheFactory {

    private static final String FILE_NAME = "object";

    public static Map<String, Object> buildAndReplace(String metadata, Object object) {
        var writer = new StringWriter();
        compileData(writer, metadata, object);
        return readValue(writer.toString(), HashMap.class);
    }

    public static <T> T buildAndReplace(Object objectMetadata, Object object, Class<T> returnType) {
        var metadata = ObjectMapperHolder.writeValueAsString(objectMetadata);
        var writer = new StringWriter();
        compileData(writer, metadata, object);
        return readValue(writer.toString(), returnType);
    }

    private static void compileData(StringWriter writer, String metadata, Object object) {
        var convertedObjectAsString = ObjectMapperHolder.writeValueAsString(object);
        HashMap<String, Object> dynamicValues = readValue(convertedObjectAsString, HashMap.class);

        var mustache = new DefaultMustacheFactory()
            .compile(new StringReader(metadata), FILE_NAME);
        mustache.execute(writer, dynamicValues);
        writer.flush();
    }

}
