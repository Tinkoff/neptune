package ru.tinkoff.qa.neptune.rabbit.mq.test;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DraftDto {
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public DraftDto setName(String name) {
        this.name = name;
        return this;
    }
}
