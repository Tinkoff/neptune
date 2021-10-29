package ru.tinkoff.qa.neptune.spring.data;


import org.springframework.data.repository.Repository;

import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static org.springframework.util.ClassUtils.getAllInterfaces;

public abstract class SpringDataFunction<INPUT, RESULT> implements Function<INPUT, RESULT> {

    private final Class<?>[] supported;

    protected SpringDataFunction(Class<?>... supported) {
        this.supported = supported;
    }

    protected final <T extends Repository<?, ?>> RuntimeException unsupportedRepository(T repo) {
        if (isNull(repo)) {
            return new IllegalArgumentException("Repository instance should not be null");
        }

        var interfaces = getAllInterfaces(repo);
        return new UnsupportedOperationException("Only following Repository interfaces are supported now: \r\n"
                + stream(supported).map(Class::getName).collect(joining("\r\n"))
                + "\r\n"
                + "Class " + repo.getClass().getName() + " implements/extends: "
                + stream(interfaces)
                .map(Class::getName)
                .collect(joining("\r\n")));
    }
}
