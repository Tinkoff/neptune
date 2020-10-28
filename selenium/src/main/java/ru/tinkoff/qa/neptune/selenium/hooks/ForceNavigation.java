package ru.tinkoff.qa.neptune.selenium.hooks;

import ru.tinkoff.qa.neptune.selenium.properties.URLProperties;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.tinkoff.qa.neptune.selenium.hooks.BrowserUrlVariableReader.pageToNavigate;

/**
 * Defines default navigation strategy
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ForceNavigation {
    /**
     * @return a full page url to navigate to or a relative part of an URL to value defined by
     * {@link URLProperties#BASE_WEB_DRIVER_URL_PROPERTY}. String value may contain substrings between braces.
     * This is for variables.
     * @see BrowserUrlVariable
     */
    String to();

    /**
     * Util class that reads metadata of some method and returns value of navigation URL
     * for a method
     */
    class ForceNavigationReader {

        /**
         * Reads metadata of a method and URL variables of the given object and returns value of navigation URL
         * for the method
         *
         * @param o      is an object whose field values are used to get navigation URL
         * @param method is a method to get navigation URL for
         * @return string value of navigation URL when the method is annotated by {@link ForceNavigation}.
         * Otherwise it returns {@code null}
         */
        public static String getBrowserPageToNavigate(Object o, Method method) {
            checkNotNull(o);
            checkNotNull(method);

            var a = method.getAnnotation(ForceNavigation.class);
            if (a == null) {
                return null;
            }

            return pageToNavigate(o, a.to());
        }
    }
}
