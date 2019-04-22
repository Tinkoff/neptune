package ru.tinkoff.qa.neptune.data.base.api;

import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

class OrmObject {
    private boolean equalsByFields(Object obj) {
        Class<?> clazz = this.getClass();
        while (!clazz.equals(Object.class)) {
            var fields = stream(clazz.getDeclaredFields()).filter(field -> !isStatic(field.getModifiers()))
                    .collect(Collectors.toList());
            for (var f: fields) {
                f.setAccessible(true);
                try {
                    var v1 = f.get(this);
                    var v2 = f.get(obj);

                    if (Objects.equals(v1, v2)) {
                        continue;
                    }
                    return false;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        var toCheck = this;
        return ofNullable(obj)
                .map(o -> {
                    if (toCheck == o) {
                        return true;
                    }
                    return toCheck.getClass().equals(obj.getClass()) &&
                            toCheck.equalsByFields(obj);
                })
                .orElse(false);
    }
}
