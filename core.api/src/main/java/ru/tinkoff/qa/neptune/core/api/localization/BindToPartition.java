package ru.tinkoff.qa.neptune.core.api.localization;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation marks customer's subclasses of {@link BundleFillerExtension} to bind them to specific
 * {@link LocalizationBundlePartition}.
 */
@Retention(RUNTIME)
@Target({TYPE})
@Repeatable(BindToPartitions.class)
public @interface BindToPartition {
    /**
     * @return name of {@link LocalizationBundlePartition}
     * @see LocalizationBundlePartition#getName()
     */
    String value();
}
