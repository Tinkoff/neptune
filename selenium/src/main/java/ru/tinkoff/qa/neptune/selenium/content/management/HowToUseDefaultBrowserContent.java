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
public @interface HowToUseDefaultBrowserContent {
    /**
     * @return an element that describes how often default content management strategy
     * should be used
     */
    BrowserContentUsage howOften() default ONCE;

    /**
     * @return to add navigation parameters to strategy which is assigned to a method.
     * Navigation parameters are added when a test or fixture method is annotated by any/both {@link SwitchToWindow}
     * and/or {@link SwitchToFrame}, but is not annotated by {@link Navigate}
     */
    boolean addNavigationParams() default false;

    /**
     * @return to add window parameters to strategy which is assigned to a method.
     * Window parameters are added when a test or fixture method is annotated by any/both {@link Navigate}
     * and/or {@link SwitchToFrame}, but is not annotated by {@link SwitchToWindow}
     */
    boolean addWindowParams() default false;

    /**
     * @return to add frame parameters to strategy which is assigned to a method.
     * Frame parameters are added when a test or fixture method is annotated by any/both {@link Navigate}
     * and/or {@link SwitchToWindow}, but is not annotated by {@link SwitchToFrame}
     */
    boolean addFrameParams() default false;
}
