package ru.tinkoff.qa.neptune.rabbit.mq.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.function.Supplier;

public class DefaultMapper implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return new JsonMapper();
    }
}
