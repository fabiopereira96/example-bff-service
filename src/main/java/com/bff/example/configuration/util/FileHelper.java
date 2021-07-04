package com.bff.example.configuration.util;

import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import com.bff.example.infrastructure.mongo.metadata.SectionEntity;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.io.InputStream;

public class FileHelper {

    private final Jsonb jsonb;

    public FileHelper() {
        this.jsonb = JsonbBuilder.create(new JsonbConfig());
    }

    public SectionEntity readSectionByResource(String resource){
        InputStream sectionStream = getResourceAsStream(resource);
        return jsonb.fromJson(sectionStream, SectionEntity.class);
    }

    public PageEntity readPageByResource(String resource){
        InputStream sectionStream = getResourceAsStream(resource);
        return jsonb.fromJson(sectionStream, PageEntity.class);
    }

    private InputStream getResourceAsStream(String resource) {
        var loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(resource);
    }
}
