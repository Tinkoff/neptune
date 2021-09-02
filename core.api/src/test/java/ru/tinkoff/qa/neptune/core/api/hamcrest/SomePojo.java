package ru.tinkoff.qa.neptune.core.api.hamcrest;

public class SomePojo {

    private Object a;

    private Object b;

    public Object getA() {
        return a;
    }

    public SomePojo setA(Object a) {
        this.a = a;
        return this;
    }

    private Object getB() {
        return b;
    }

    public SomePojo setB(Object b) {
        this.b = b;
        return this;
    }
}
