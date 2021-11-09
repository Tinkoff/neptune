package ru.tinkoff.qa.neptune.spring.mock.mvc;

public class GenericDto<T, R> {

    private String stringValue;

    private T arrayValue1;

    private R arrayValue2;

    public String getStringValue() {
        return stringValue;
    }

    public GenericDto<T, R> setStringValue(String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    public T getArrayValue1() {
        return arrayValue1;
    }

    public GenericDto<T, R> setArrayValue1(T arrayValue1) {
        this.arrayValue1 = arrayValue1;
        return this;
    }

    public R getArrayValue2() {
        return arrayValue2;
    }

    public GenericDto<T, R> setArrayValue2(R arrayValue2) {
        this.arrayValue2 = arrayValue2;
        return this;
    }
}
