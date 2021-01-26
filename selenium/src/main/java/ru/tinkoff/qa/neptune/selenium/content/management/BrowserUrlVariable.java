package ru.tinkoff.qa.neptune.selenium.content.management;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Retention(RUNTIME)
@Target({FIELD, METHOD})
@Repeatable(BrowserUrlVariables.class)
public @interface BrowserUrlVariable {

    /**
     * @return name of a URL-variable. When value is empty then name of an annotated field is used.
     */
    String name() default EMPTY;

    /**
     * Should be value url-encoded or not for queries of URLs.
     */
    boolean toEncodeForQueries() default true;

    /**
     * @return name of a method whose non-null returned value be should used as a value of the URL-variable
     * to navigate in browser. It has sense when type of a marked field differs from primitive/wrappers/string.
     * This method should not have any parameter and it should not be static
     */
    String method() default EMPTY;
}
