package ru.tinkoff.qa.neptune.allure;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a package, test-class or test-method to be excluded from the allure report.
 * When a package is marked by this annotation then all classes from this package
 * are considered excluded from the report.
 * Then a class is marked by this annotation then all test methods (including inherited) belong
 * to the class are considered excluded. Nested test-classes are excluded too.
 * <p></p>
 * The handling of the annotation is implemented in dependent modules
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD, PACKAGE})
public @interface ExcludeFromAllureReport {
}
