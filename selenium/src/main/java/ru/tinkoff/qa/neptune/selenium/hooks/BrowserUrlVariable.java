package ru.tinkoff.qa.neptune.selenium.hooks;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Marks fields whose values should be used as values of URL-variables.
 * <p>Requirements:</p>
 * <ul>
 *     <li>Used values should not be null.</li>
 *     <li>Field values should be calculated BEFORE target browser navigation is performed</li>
 * </ul>
 * <p>Recommendations:</p>
 * <ul>
 *     <li>It is recommended to annotate fields of primitive/wrapper/String types</li>
 *     <li>In cases when in is necessary to annotate fields of other types make sure that objets of these types
 *     have readable string representations which may be used as parts of page urls. Otherwise it is possible
 *     to uses values of fields of these objects.</li>
 *
 * @see #field()
 * </ul>
 */
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(BrowserUrlVariables.class)
public @interface BrowserUrlVariable {

    /**
     * @return name of a URL-variable. When value is empty then name of an annotated field is used.
     */
    String name() default EMPTY;

    /**
     * Should be value url-encoded or not. This parameter is ignored by root URLs.
     */
    boolean toEncode() default false;

    /**
     * @return name of a field whose non-null value should used as a value of the URL-variable
     * to navigate in browser. It has sense when type of a marked field differs from primitive/wrappers/string
     */
    String field() default EMPTY;
}
