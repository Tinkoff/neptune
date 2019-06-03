package ru.tinkoff.qa.neptune.core.api.properties.object.suppliers;

import java.util.function.Supplier;

public class ObjectSupplier2 implements Supplier<Object> {

    public static final Object O2 = new Object();
    
    @Override
    public Object get() {
        return O2;
    }
}
