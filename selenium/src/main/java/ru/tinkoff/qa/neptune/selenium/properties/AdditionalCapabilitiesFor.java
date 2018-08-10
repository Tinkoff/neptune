package ru.tinkoff.qa.neptune.selenium.properties;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is supposed to mark subclasses of {@link CapabilitySupplier}
 */
@Retention(RUNTIME) @Target({TYPE})
public @interface AdditionalCapabilitiesFor {
    /**
     * Which type of supported capabilities should be amended with capabilities supplied by instance
     * of the marked class
     * @return type of capabilities to be amended
     */
    CapabilityTypes[] type();

    /**
     * Name of the supplier which is given by customizer.
     * @return Name of the supplier.
     */
    String supplierName();
}
