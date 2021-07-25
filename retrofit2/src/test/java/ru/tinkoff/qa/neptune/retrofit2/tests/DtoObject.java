package ru.tinkoff.qa.neptune.retrofit2.tests;

import java.util.Map;

public class DtoObject {

    private boolean bool;

    private String color;

    private int number;

    private String string;

    private Map<?, ?> object;

    public boolean getBool() {
        return this.bool;
    }

    public DtoObject setBool(boolean bool) {
        this.bool = bool;
        return this;
    }

    public String getColor() {
        return this.color;
    }

    public DtoObject setColor(String color) {
        this.color = color;
        return this;
    }

    public int getNumber() {
        return this.number;
    }

    public DtoObject setNumber(int number) {
        this.number = number;
        return this;
    }

    public String getString() {
        return this.string;
    }

    public DtoObject setString(String string) {
        this.string = string;
        return this;
    }

    public Map<?, ?> getObject() {
        return object;
    }

    public DtoObject setObject(Map<?, ?> object) {
        this.object = object;
        return this;
    }
}
