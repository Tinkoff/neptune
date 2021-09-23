package ru.tinkoff.qa.neptune.spring.mock.mvc;

import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static ru.tinkoff.qa.neptune.spring.mock.mvc.MockMvcContext.getContext;

public final class SpringMockMvcExecutionHook implements ExecutionHook {

    @Override
    public void executeMethodHook(Method method, Object on, boolean isTest) {
        var currentClazz = on instanceof Class ? (Class<?>) on : on.getClass();

        Field mockMvcField = null;
        while (!currentClazz.equals(Object.class) && isNull(mockMvcField)) {
            mockMvcField = stream(currentClazz.getDeclaredFields())
                    .filter(field -> MockMvc.class.isAssignableFrom(field.getType()))
                    .findFirst()
                    .orElse(null);
            currentClazz = currentClazz.getSuperclass();
        }

        if (isNull(mockMvcField)) {
            getContext().setDefault(null);
        } else {
            MockMvc mockMvc = null;
            try {
                if (isStatic(mockMvcField.getModifiers())) {
                    mockMvc = (MockMvc) mockMvcField.get(currentClazz);
                } else {
                    mockMvc = (MockMvc) mockMvcField.get(on);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            getContext().setDefault(mockMvc);
        }
    }
}
