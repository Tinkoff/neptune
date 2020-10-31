package ru.tinkoff.qa.neptune.selenium.hooks;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.hooks.BrowserUrlCreator.createBrowserUrl;

/**
 * Defines a browser URL to force the navigating to
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ForceNavigation {

    /**
     * @return URL to navigate to. String value may contain substrings between braces. This is for variables.
     * @see BrowserUrlVariable
     */
    String to() default EMPTY;

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

            if (isBlank(a.to())) {
                throw new IllegalArgumentException(format("ForceNavigation.to should be defined. Please improve %s",
                        method));
            }

            return createBrowserUrl(o, a.to());
        }
    }
}
