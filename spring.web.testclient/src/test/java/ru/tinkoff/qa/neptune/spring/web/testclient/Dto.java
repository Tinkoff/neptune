package ru.tinkoff.qa.neptune.spring.web.testclient;

import java.util.List;

public class Dto {

    private String stringValue;

    private List<String> arrayValue1;

    private Integer[] arrayValue2;

    public String getStringValue() {
        return stringValue;
    }

    public Dto setStringValue(String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    public List<String> getArrayValue1() {
        return arrayValue1;
    }

    public Dto setArrayValue1(List<String> arrayValue1) {
        this.arrayValue1 = arrayValue1;
        return this;
    }

    public Integer[] getArrayValue2() {
        return arrayValue2;
    }

    public Dto setArrayValue2(Integer[] arrayValue2) {
        this.arrayValue2 = arrayValue2;
        return this;
    }
}
