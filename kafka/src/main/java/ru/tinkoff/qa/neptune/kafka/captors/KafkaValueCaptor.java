package ru.tinkoff.qa.neptune.kafka.captors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

interface KafkaValueCaptor {

    default StringBuilder getData(Object caught) {
        String value;
        if (caught instanceof String) {
            value = (String) caught;
        } else {
            try {
                value = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(caught);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return new StringBuilder(value);
    }
}
