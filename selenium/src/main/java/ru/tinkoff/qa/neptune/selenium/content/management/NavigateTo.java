package ru.tinkoff.qa.neptune.selenium.content.management;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserUrlCreator.createBrowserUrl;

/**
 * Defines a browser URL to navigate to
 */
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface NavigateTo {

    /**
     * @return URL to navigate to. String value may contain substrings between braces. This is for variables.
     * @see BrowserUrlVariable
     */
    String to();

    /**
     * Util class that reads metadata of some method/class and returns value of navigation URL
     * for a method/default navigation URL for a class
     */
    class NavigateToReader {

        /**
         * Reads metadata of a method/class and URL variables of the given object and returns value of navigation URL
         * for the method/default navigation URL for the class
         *
         * @param o                is an object whose field values are used as values of URL-variables.
         * @param annotatedElement is a method to get navigation URL for. Also it may be a class
         *                         to get default navigation URL for.
         * @return string value of navigation URL when {@code annotatedElement} is annotated by {@link NavigateTo}.
         * Otherwise it returns {@code null}
         * @see BrowserUrlVariable
         */
        static String getBrowserPageToNavigate(Object o, AnnotatedElement annotatedElement) {
            checkNotNull(o);
            checkNotNull(annotatedElement);

            var a = annotatedElement.getAnnotation(NavigateTo.class);
            if (a == null) {
                return null;
            }

            return createBrowserUrl(o, a.to());
        }
    }
}
