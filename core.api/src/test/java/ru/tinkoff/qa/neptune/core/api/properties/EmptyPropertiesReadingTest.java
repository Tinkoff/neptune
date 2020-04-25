package ru.tinkoff.qa.neptune.core.api.properties;

import org.testng.annotations.AfterClass;

import java.io.File;

import static java.time.Duration.ofMinutes;
import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.getFile;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.PROPERTIES;

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

    @AfterClass
    public void tearDown() {
        GeneralPropertyInitializer.arePropertiesRead = false;
    }
}
