package ru.tinkoff.qa.neptune.core.api.properties;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.PROPERTIES;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.refreshProperties;
import static ru.tinkoff.qa.neptune.core.api.properties.PropertyIntEnumWithDefaultValue.INT_VALUE;
import static ru.tinkoff.qa.neptune.core.api.properties.PropertyStringEnumWithDefaultValue.STR_VALUE;

public class PropertyWithDefaultValueTest {

    @DataProvider
    public static Object[][] data1() {
        return new Object[][]{
                {new PropertyIntWithDefaultValue(), 2},
                {INT_VALUE, 2},
                {new PropertyStringWithDefaultValue(), "Some String"},
                {STR_VALUE, "Some String 2"},
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return new Object[][]{
                {new PropertyIntWithDefaultValue(), 3},
                {INT_VALUE, 3},
                {new PropertyStringWithDefaultValue(), "ABC"},
                {STR_VALUE, "ABC 2"},
        };
    }

    @DataProvider
    public static Object[][] data3() {
        return new Object[][]{
                {new PropertyIntWithDefaultValue()},
                {INT_VALUE},
                {new PropertyStringWithDefaultValue()},
                {STR_VALUE},
        };
    }

    @Test(dataProvider = "data1")
    public void test1(PropertySupplier<?, ?> s, Object o) {
        assertThat("Default value of the property", s.get(), is(o));
    }

    @Test(dataProvider = "data2")
    public <T> void test2(PropertySupplier<?, T> s, T o) {
        s.accept(o);
        try {
            assertThat("Value of the property", s.get(), is(o));
        } finally {
            s.accept(null);
        }
    }

    @Test(dataProvider = "data3")
    public void test3(PropertySupplier<?, ?> s) throws IOException {
        var m = new HashMap<String, String>() {
            {
                put(s.getName(), EMPTY);
            }
        };
        Properties prop = new Properties();

        try {
            try (OutputStream output = new FileOutputStream(PROPERTIES)) {
                // set the properties value
                m.forEach(prop::setProperty);
                // save properties to project root folder
                prop.store(output, null);
            }
            refreshProperties();
            assertThat("Value of the property", s.get(), nullValue());
        } finally {
            File toDelete = getFile(PROPERTIES);
            if (toDelete.exists()) {
                forceDelete(toDelete);
            }
        }
    }
}
