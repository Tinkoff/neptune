package ru.tinkoff.qa.neptune.selenium.content.management;

import ru.tinkoff.qa.neptune.core.api.hooks.ExecutionHook;
import ru.tinkoff.qa.neptune.core.api.hooks.HookOrder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_TEST_METHOD;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.ONCE;

@HookOrder(priority = 10)
public final class ContentManagementHook implements ExecutionHook {

    private static final ThreadLocal<Map<Class<?>, BrowserContentUsage>> DEFAULT_CONTENT_USAGE
            = new ThreadLocal<Map<Class<?>, BrowserContentUsage>>();
    private static final ThreadLocal<Map<Class<?>, ContentManagementCommand>> DEFAULT_CONTENT_MANAGEMENT_COMMAND
            = new ThreadLocal<Map<Class<?>, ContentManagementCommand>>();

    private static <T> T getFromClass(Class<?> clazz, Object on, BiFunction<Class<?>, Object, T> toGet) {
        var cls = clazz;
        T result = null;
        while (isNull(result) && nonNull(cls)) {
            result = toGet.apply(clazz, on);
            cls = cls.getSuperclass();
        }
        return result;
    }

    private static <T> T getFromClass(Class<?> clazz, Function<Class<?>, T> toGet) {
        return getFromClass(clazz, null, (aClass, o) -> toGet.apply(aClass));
    }

    private static ContentManagementCommand getDefaultContentManagementCommand(Class<?> cls, Object on, boolean isTest) {
        if (isNull(DEFAULT_CONTENT_USAGE.get())) {
            DEFAULT_CONTENT_USAGE.set(new HashMap<>());
            DEFAULT_CONTENT_MANAGEMENT_COMMAND.set(new HashMap<>());
        }

        var usageMap = DEFAULT_CONTENT_USAGE.get();
        if (usageMap.containsKey(cls)) {
            return ofNullable(usageMap.get(cls))
                    .map(browserContentUsage -> {
                        var defaultCommand = DEFAULT_CONTENT_MANAGEMENT_COMMAND.get().get(cls);
                        if (isNull(defaultCommand)) {
                            return null;
                        }

                        if (browserContentUsage.equals(ONCE) && defaultCommand.isExecuted()) {
                            return null;
                        }

                        if (!isTest && browserContentUsage.equals(FOR_EVERY_TEST_METHOD)) {
                            return null;
                        }

                        return defaultCommand;
                    })
                    .orElse(null);
        }

        var usage = ofNullable(getFromClass(cls, aClass -> ofNullable(aClass.getAnnotation(UseDefaultBrowserContent.class))
                .map(UseDefaultBrowserContent::value)
                .orElse(null)))
                .orElse(ONCE);

        var getWindow = getFromClass(cls, aClass -> ofNullable(aClass.getAnnotation(SwitchToWindow.class))
                .map(SwitchToWindow.SwitchToWindowReader::getWindow)
                .orElse(null));
    }

    @Override
    public void executeMethodHook(Method method, Object on, boolean isTest) {
    }
}
