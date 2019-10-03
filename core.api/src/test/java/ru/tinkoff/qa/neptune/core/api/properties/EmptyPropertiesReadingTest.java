package ru.tinkoff.qa.neptune.core.api.properties;

import static java.time.Duration.ofMinutes;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class EmptyPropertiesReadingTest extends BasePropertyReadingTest {

    public EmptyPropertiesReadingTest() {
        super(is(false),
                nullValue(),
                nullValue(),
                is(ofMinutes(1)),
                nullValue(),
                nullValue(),
                nullValue(),
                nullValue(),
                nullValue(),
                nullValue(),
                nullValue(),
                nullValue(),
                nullValue());
    }
}
