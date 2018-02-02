package com.github.toy.constructor.selenium;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.github.toy.constructor.selenium.PropertySupplier.TimeUnitProperty.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.PropertySupplier.TimeUnitProperty.WAITING_TIME_UNIT;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public interface PropertySupplier<T> extends Supplier<T> {

    enum TimeUnitProperty implements PropertySupplier<TimeUnit> {

        /**
         * Reads property {@code "waiting.for.elements.time.unit"}.
         * This property is needed to define time of the waiting for some web elements
         * are present and suit some criteria. Returns read value
         * or {@link TimeUnit#SECONDS} when nothing is defined
         */
        ELEMENT_WAITING_TIME_UNIT("waiting.for.elements.time.unit"),

        /**
         * Reads property {@code "waiting.selenium.time.unit"}.
         * This property is needed to define time of the waiting for some
         * conditions related to {@code  org.openqa.selenium.*} objects.
         * Returns read value or {@link TimeUnit#SECONDS} when nothing is defined
         */
        WAITING_TIME_UNIT("waiting.selenium.time.unit");

        private final String propertyName;

        TimeUnitProperty(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public TimeUnit get() {
            return returnOptional(this.toString())
                    .map(s -> stream(TimeUnit.values())
                            .filter(timeUnit -> s.trim().equalsIgnoreCase(timeUnit.name()))
                            .findFirst()
                            .orElseThrow(
                                    () -> new IllegalArgumentException(format("Property: %s. Unidentified " +
                                            "time unit %s. Please take a look at " +
                                            "elements of %s", this.toString(), s,
                                            TimeUnit.class.getName())))).orElse(SECONDS);
        }

        @Override
        public String toString() {
            return propertyName;
        }
    }

    enum TimeValueProperty implements PropertySupplier<Long> {
        /**
         * Reads property {@code "waiting.for.elements.time"}.
         * This property is needed to define time of the waiting for some web elements
         * are present and suit some criteria.
         * Returns read value or something that is equivalent to 1 minute see
         * {{@link TimeUnitProperty#ELEMENT_WAITING_TIME_UNIT}} and {@link TimeUnit#convert(long, TimeUnit)}
         */
        ELEMENT_WAITING_TIME_VALUE("waiting.for.elements.time") {
            public Long get() {
                return returnOptional(this.toString())
                        .map(Long::parseLong)
                        .orElseGet(() -> {TimeUnit defaultTimeUnit = ELEMENT_WAITING_TIME_UNIT.get();
                            return defaultTimeUnit.convert(1, MINUTES);
                        });
            }
        },

        /**
         * Reads property {@code "waiting.selenium.time"}.
         * This property is needed to define time of the waiting for some
         * conditions related to {@code  org.openqa.selenium.*} objects.
         * Returns read value or something that is equivalent to 1 minute see
         * {{@link TimeUnitProperty#WAITING_TIME_UNIT}} and {@link TimeUnit#convert(long, TimeUnit)}
         */
        WAITING_TIME_VALUE("waiting.selenium.time") {
            public Long get() {
                return returnOptional(this.toString())
                        .map(Long::parseLong)
                        .orElseGet(() -> {TimeUnit defaultTimeUnit = WAITING_TIME_UNIT.get();
                            return defaultTimeUnit.convert(1, MINUTES);
                        });
            }
        };

        private final String propertyName;

        TimeValueProperty(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public abstract Long get();

        @Override
        public String toString() {
            return propertyName;
        }
    }

    private static Optional<String> returnOptional(String propertyValue) {
        return ofNullable(System.getProperty(propertyValue));
    }
}
