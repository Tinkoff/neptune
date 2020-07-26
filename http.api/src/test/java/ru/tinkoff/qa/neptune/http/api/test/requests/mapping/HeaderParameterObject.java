package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.MethodParameter;

@MethodParameter
public class HeaderParameterObject {
    private String someString;

    private Number someNum;

    private Boolean someBool;

    private Object nullable;

    private Object[] someArray;

    public String getSomeString() {
        return someString;
    }

    public HeaderParameterObject setSomeString(String someString) {
        this.someString = someString;
        return this;
    }

    public Number getSomeNum() {
        return someNum;
    }

    public HeaderParameterObject setSomeNum(Number someNum) {
        this.someNum = someNum;
        return this;
    }

    public Boolean getSomeBool() {
        return someBool;
    }

    public HeaderParameterObject setSomeBool(Boolean someBool) {
        this.someBool = someBool;
        return this;
    }

    public Object getNullable() {
        return nullable;
    }

    public HeaderParameterObject setNullable(Object nullable) {
        this.nullable = nullable;
        return this;
    }

    public Object[] getSomeArray() {
        return someArray;
    }

    public HeaderParameterObject setSomeArray(Object[] someArray) {
        this.someArray = someArray;
        return this;
    }
}
