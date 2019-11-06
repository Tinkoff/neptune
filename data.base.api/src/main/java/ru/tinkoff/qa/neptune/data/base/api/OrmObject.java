package ru.tinkoff.qa.neptune.data.base.api;

import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.contains;

class OrmObject {
    private boolean equalsByFields(Object obj, String... fieldsToBeExcluded) {
        Class<?> clazz = this.getClass();
        while (!clazz.equals(Object.class)) {
            var fields = stream(clazz.getDeclaredFields()).filter(field -> !isStatic(field.getModifiers()))
                    .collect(Collectors.toList());
            for (var f: fields) {

                if (contains(fieldsToBeExcluded, f.getName())) {
                    continue;
                }

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

    protected boolean equals(Object obj, String... fieldsToBeExcluded) {
        var toCheck = this;
        return ofNullable(obj)
                .map(o -> {
                    if (toCheck == o) {
                        return true;
                    }
                    return toCheck.getClass().equals(obj.getClass()) &&
                            toCheck.equalsByFields(obj, fieldsToBeExcluded);
                })
                .orElse(false);
    }

    @Override
    public boolean equals(Object obj) {
        return equals(obj, new String[]{});
    }
}
