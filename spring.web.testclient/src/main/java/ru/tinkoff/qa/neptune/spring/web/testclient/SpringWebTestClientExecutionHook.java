package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.test.web.reactive.server.WebTestClient;
import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.getContext;

public final class SpringWebTestClientExecutionHook implements ExecutionHook {

    @Override
    public void executeMethodHook(Method method, Object on, boolean isTest) {
        var currentClazz = on instanceof Class ? (Class<?>) on : on.getClass();

        Field webClientField = null;
        while (!currentClazz.equals(Object.class) && isNull(webClientField)) {
            webClientField = stream(currentClazz.getDeclaredFields())
                    .filter(field -> WebTestClient.class.isAssignableFrom(field.getType()))
                    .findFirst()
                    .orElse(null);
            currentClazz = currentClazz.getSuperclass();
        }

        if (isNull(webClientField)) {
            getContext().setDefault(null);
        } else {
            WebTestClient webClient = null;
            webClientField.setAccessible(true);
            try {
                if (isStatic(webClientField.getModifiers())) {
                    webClient = (WebTestClient) webClientField.get(currentClazz);
                } else if (!(on instanceof Class)) {
                    webClient = (WebTestClient) webClientField.get(on);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            getContext().setDefault(webClient);
        }
    }
}
