package com.bff.example.core.configuration;

import io.quarkus.jsonb.JsonbConfigCustomizer;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyOrderStrategy;
import java.util.Locale;

@Singleton
public class JsonbConfiguration implements JsonbConfigCustomizer {

    @Override
    public void customize(JsonbConfig config) {
        config
            .withDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault())
            .withPropertyOrderStrategy(PropertyOrderStrategy.ANY)
            .withNullValues(false);
    }

}
