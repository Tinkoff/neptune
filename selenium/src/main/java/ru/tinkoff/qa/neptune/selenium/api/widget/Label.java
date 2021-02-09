package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.WebElement;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * It is possible to find some web element by the full text matching. Labels (titles, texts of related elements, visible
 * values of some attributes etc) is alternative way to find some element/group of elements described by {@link Widget}.
 * <p>
 * This annotation should mark methods of a {@link Widget} that return {@code String} and have no parameters. Values which
 * are returned by these methods play role of labels.
 * <p>
 * Also this annotation may mark {@link org.openqa.selenium.WebElement} fields. Text content ({@link WebElement#getText()})
 * of these elements are considered labels.
 */
@Retention(RUNTIME)
@Target({METHOD, FIELD})
public @interface Label {
}
