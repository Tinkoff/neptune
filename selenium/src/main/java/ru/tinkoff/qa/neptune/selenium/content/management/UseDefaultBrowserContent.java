package ru.tinkoff.qa.neptune.selenium.content.management;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.ONCE;

/**
 * This annotation marks a class to define how often default content management strategy
 * should be used. Default content management strategy is defined by {@link SwitchToWindow},
 * {@link Navigate} and {@link SwitchToFrame} annotations on a class.
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface UseDefaultBrowserContent {
    /**
     * @return an element that describes how often default content management strategy
     * should be used
     */
    BrowserContentUsage value() default ONCE;
}
