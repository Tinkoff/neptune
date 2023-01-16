package ru.tinkoff.qa.neptune.kafka.captors;

import com.google.gson.GsonBuilder;

interface KafkaValueCaptor {

    default StringBuilder getData(Object caught) {
        String value;
        if (caught instanceof String) {
            value = (String) caught;
        } else {
            value = new GsonBuilder().setPrettyPrinting().create().toJson(caught);
        }
        return new StringBuilder(value);
    }
}
