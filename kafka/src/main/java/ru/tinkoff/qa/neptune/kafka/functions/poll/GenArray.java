package ru.tinkoff.qa.neptune.kafka.functions.poll;

import java.lang.reflect.Array;

public class GenArray<E> {
    private E[] a;

    public GenArray(Class<E> c, int s) {
        @SuppressWarnings("unchecked") final E[] a = (E[]) Array.newInstance(c, s);
        this.a = a;
    }

    E get(int i) {
        return a[i];
    }
}
