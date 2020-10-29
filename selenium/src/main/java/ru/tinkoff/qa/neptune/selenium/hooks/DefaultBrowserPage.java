package ru.tinkoff.qa.neptune.selenium.hooks;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.hooks.BrowserUrlVariableReader.pageToNavigate;
import static ru.tinkoff.qa.neptune.selenium.hooks.DefaultNavigationStrategies.ON_EVERY_TEST;

/**
 * Defines default navigation strategy
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface DefaultBrowserPage {

    /**
     * @return default root URL (schema, host and port when it is necessary) to navigate to.
     * String value may contain substrings between braces. This is for variables.
     * @see BrowserUrlVariable
     */
    String rootUrlAt() default EMPTY;

    /**
     * @return a path of a default URL to navigate to.  String value may contain substrings between braces.
     * This is for variables.
     * @see BrowserUrlVariable
     */
    String pathAt() default EMPTY;

    /**
     * @return a query of a default URL to navigate to.  String value may contain substrings between braces.
     * This is for variables.
     * @see BrowserUrlVariable
     */
    String queryAt() default EMPTY;

    /**
     * @return when navigation to the page should be performed
     */
    DefaultNavigationStrategies when() default ON_EVERY_TEST;

    /**
     * Util class that reads metadata of some class and returns value of default navigation URL
     * for a method
     */
    class DefaultBrowserPageReader {

        /**
         * Reads metadata of a class of the given object and returns value of default navigation URL
         * for the method
         *
         * @param o            is an object whose class and field values are used to get default navigation URL
         * @param method       is a method to get default navigation URL for
         * @param isTestMethod is it test method or not
         * @return string value of default navigation URL
         * <p>when</p>
         * <ul>
         *     <li>Class is annotated by {@link DefaultBrowserPage}</li>
         *     <li>The given method is a test-method and {@link #when()} is {@link DefaultNavigationStrategies#ON_EVERY_TEST}</li>
         *     <li>...or {@link #when()} is {@link DefaultNavigationStrategies#ON_EVERY_METHOD}</li>
         *     <li>The method os not annotated by {@link PreventNavigationToDefaultURL}</li>
         *     <li>The method is not annotated by {@link ForceNavigation}</li>
         * </ul>
         * Otherwise it returns {@code null}
         */
        public static String getDefaultBrowserPageToNavigate(Object o, Method method, boolean isTestMethod) {
            checkNotNull(o);
            checkNotNull(method);

            if (method.getAnnotation(ForceNavigation.class) != null || method.getAnnotation(PreventNavigationToDefaultURL.class) != null) {
                return null;
            }


            var cls = o instanceof Class ? (Class<?>) o : o.getClass();
            var a = cls.getAnnotation(DefaultBrowserPage.class);

            while (!cls.equals(Object.class) && (a == null)) {
                cls = cls.getSuperclass();
                a = cls.getAnnotation(DefaultBrowserPage.class);
            }

            if (a == null) {
                return null;
            }

            if (isBlank(a.rootUrlAt()) && isBlank(a.pathAt()) && isBlank(a.queryAt())) {
                throw new IllegalArgumentException(format("Any of DefaultBrowserPage.rootUrlAt, DefaultBrowserPage.pathAt or " +
                                "DefaultBrowserPage.queryAt should be defined. " +
                                "Please improve %s",
                        method));
            }

            if (!isTestMethod && a.when().equals(ON_EVERY_TEST)) {
                return null;
            }

            return pageToNavigate(o, a.rootUrlAt(), a.pathAt(), a.queryAt());
        }
    }
}
