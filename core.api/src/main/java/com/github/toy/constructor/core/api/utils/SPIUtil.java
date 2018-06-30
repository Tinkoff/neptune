package com.github.toy.constructor.core.api.utils;

import java.util.List;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public final class SPIUtil {

    private SPIUtil() {
        super();
    }

    /**
     * Creates an object list for the given type using SPI engines.
     *
     * @param classToLoad is a service class that should be instantiated.
     * @param <T> is type of a list that should be returned.
     * @return list of objects.
     */
    public static <T> List<T> loadSPI(Class<T> classToLoad) {
        return ServiceLoader.load(classToLoad)
                .stream()
                .map(ServiceLoader.Provider::get).collect(toList());
    }
}
