package ru.tinkoff.qa.neptune.testng.integration;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;

class InstantiatedContexts {
    private static final Map<Class<?>, Object> contextMap = new HashMap<>();

    private synchronized static Object find(Class<?> toFind) {
        return contextMap.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(toFind)
                        || toFind.isAssignableFrom(entry.getKey()))
                .findFirst().map(Map.Entry::getValue)
                .orElse(null);
    }

    private synchronized static Object create(Class<?> toInstantiate)  {
        var toBeReturned = getProxied(toInstantiate);
        contextMap.put(toInstantiate, toBeReturned);
        return toBeReturned;
    }

    static Object getInstantiatedContext(Class<?> toInstantiate) {
        return ofNullable(find(toInstantiate))
                .orElseGet(() -> create(toInstantiate));
    }
}
