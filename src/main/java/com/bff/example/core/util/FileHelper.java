package com.bff.example.core.util;

import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import com.bff.example.infrastructure.mongo.metadata.SectionEntity;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyOrderStrategy;
import java.io.InputStream;

public class FileHelper {

    private final JsonbConfig config;
    private final Jsonb jsonb;

    public FileHelper() {
        this.config = new JsonbConfig()
            .withPropertyOrderStrategy(PropertyOrderStrategy.ANY)
            .withNullValues(false);
        this.jsonb = JsonbBuilder.create(config);
    }

    public SectionEntity readSectionByResource(String resource){
        InputStream sectionStream = getResourceAsStream(resource);
        return jsonb.fromJson(sectionStream, SectionEntity.class);
    }

    public PageEntity readPageByResource(String resource){
        InputStream pageStream = getResourceAsStream(resource);
        return jsonb.fromJson(pageStream, PageEntity.class);
    }

    private InputStream getResourceAsStream(String resource) {
        var loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(resource);
    }
}
