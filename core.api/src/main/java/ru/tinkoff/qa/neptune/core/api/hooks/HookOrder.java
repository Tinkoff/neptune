package ru.tinkoff.qa.neptune.core.api.hooks;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is designed to help to manage order of invocation of the {@link ExecutionHook#executeMethodHook(Method, Object, boolean)}
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface HookOrder {

    /**
     * @return priority of a hook execution. This is a positive value which is greater than 0.
     * As a value is greater as priority is lower
     */
    byte priority();
}
