package com.github.toy.constructor.core.api.cleaning;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public interface Stoppable {
    void shutDown();

}
