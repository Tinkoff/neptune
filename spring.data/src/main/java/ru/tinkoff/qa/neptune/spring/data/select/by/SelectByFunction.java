package ru.tinkoff.qa.neptune.spring.data.select.by;

import org.springframework.data.repository.Repository;

import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static org.springframework.util.ClassUtils.getAllInterfaces;

abstract class SelectByFunction<R, ID, T extends Repository<R, ID>, RESULT> implements Function<T, RESULT> {

    private final Class<?>[] supported;

    SelectByFunction(Class<?>... supported) {
        this.supported = supported;
    }

    final RuntimeException unsupportedRepository(T repo) {
        if (isNull(repo)) {
            return new IllegalArgumentException("Repository instance should not be null");
        }

        var interfaces = getAllInterfaces(repo.getClass());
        return new UnsupportedOperationException("Only following Repository interfaces are supported now: \r\n"
                + stream(supported).map(Class::getName).collect(joining("\r\n"))
                + "\r\n"
                + "Class " + repo.getClass().getName() + " implements/extends: "
                + stream(interfaces)
                .map(Class::getName)
                .collect(joining("\r\n")));
    }
}
