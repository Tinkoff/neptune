package ru.tinkoff.qa.neptune.selenium.hooks;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks methods which should not use browser navigation hook. It have sense when
 * a class which declares annotated method is marked by {@link DefaultBrowserPage}</li>
 * <p>It may be useful</p>
 * <ul>
 *     <li>for chains of dependent test methods performed on some browser page to prevent navigation back
 *     to the starting page</li>
 *     <li>when some class contains test methods which validate not only front-end functionality (API, databases, etc)
 *     and it is not necessary to go back to default URL every time</li>
 * </ul>
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface PreventGettingBackToDefaultPage {
}
