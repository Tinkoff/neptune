package ru.tinkoff.qa.neptune.core.api.properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import ru.tinkoff.qa.neptune.core.api.properties.object.suppliers.ObjectSupplier1;
import ru.tinkoff.qa.neptune.core.api.properties.object.suppliers.ObjectSupplier2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import static java.time.Duration.ofSeconds;
import static java.util.Map.entry;
import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.*;
import static ru.tinkoff.qa.neptune.core.api.properties.TestBooleanPropertySupplier.TEST_BOOLEAN_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestBytePropertySupplier.TEST_BYTE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestDoublePropertySupplier.TEST_DOUBLE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestDurationSupplier.TestChronoUnitSupplier.TEST_CHRONO_UNIT_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestDurationSupplier.TestTimeValueSupplier.TEST_TIME_VALUE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestEnum.*;
import static ru.tinkoff.qa.neptune.core.api.properties.TestEnumItemPropertySuppler.TEST_ENUM_ITEM_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestEnumItemsPropertySuppler.TEST_ENUM_ITEMS_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestFloatPropertySupplier.TEST_FLOAT_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestIntegerPropertySupplier.TEST_INTEGER_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestLongPropertySupplier.TEST_LONG_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestObjectSupplier.TEST_OBJECT_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestObjectsSupplier.TEST_OBJECTS_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestSrortPropertySupplier.TEST_SHORT_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.TestURLValuePropertySupplier.TEST_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.object.suppliers.ObjectSupplier1.O1;
import static ru.tinkoff.qa.neptune.core.api.properties.object.suppliers.ObjectSupplier2.O2;

public class MixedGlobalLocalPropertyReadingTest extends BasePropertyReadingTest {

    private static final Map<String, String> PROPERTY_SET_1 = Map
            .ofEntries(entry(TEST_BOOLEAN_PROPERTY, "false"),
                    entry(TEST_BYTE_PROPERTY, "1"),
                    entry(TEST_DOUBLE_PROPERTY, "2"),
                    entry(TEST_CHRONO_UNIT_PROPERTY,  "SECONDS"),
                    entry(TEST_TIME_VALUE_PROPERTY, "3"),
                    entry(TEST_ENUM_ITEM_PROPERTY, "ITEM3"),
                    entry(TEST_ENUM_ITEMS_PROPERTY, "ITEM1,ITEM2,ITEM4"),
                    entry(TEST_FLOAT_PROPERTY, "4"),
                    entry(TEST_INTEGER_PROPERTY, "5"),
                    entry(TEST_LONG_PROPERTY, "6"),
                    entry(TEST_OBJECTS_PROPERTY, ObjectSupplier1.class.getName() + "," + ObjectSupplier2.class.getName()),
                    entry(TEST_OBJECT_PROPERTY, ObjectSupplier1.class.getName()),
                    entry(TEST_SHORT_PROPERTY, "7"),
                    entry(TEST_URL_PROPERTY, "https://www.google.com"));

    private static final Map<String, String> PROPERTY_SET_2 = Map
            .ofEntries(entry(TEST_FLOAT_PROPERTY, "5"),
                    entry(TEST_INTEGER_PROPERTY, "6"),
                    entry(TEST_LONG_PROPERTY, "7"),
                    entry(TEST_OBJECTS_PROPERTY, ObjectSupplier1.class.getName() + "," + ObjectSupplier1.class.getName()),
                    entry(TEST_OBJECT_PROPERTY, ObjectSupplier2.class.getName()),
                    entry(TEST_SHORT_PROPERTY, EMPTY),
                    entry(TEST_URL_PROPERTY, "https://www.programcreek.com"));

    public MixedGlobalLocalPropertyReadingTest() throws Exception {
        super(is(false),
                is(Byte.valueOf("1")),
                is(2D),
                is(ofSeconds(3)),
                contains(ITEM1, ITEM2, ITEM4),
                is(ITEM3),
                is(5F),
                is(6),
                is(7L),
                contains(O1, O1),
                is(O2),
                nullValue(),
                is(new URL("https://www.programcreek.com")));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Properties prop = new Properties();
        try (OutputStream output = new FileOutputStream(PROPERTIES)) {
            // set the properties value
            PROPERTY_SET_2.forEach(prop::setProperty);
            // save properties to project root folder
            prop.store(output, null);
        }

        var f = new File(new File(GLOBAL_PROPERTIES).getAbsoluteFile()
                .getParentFile()
                .getParentFile(),
                GLOBAL_PROPERTIES);

        try (OutputStream output2 = new FileOutputStream(f)) {
            // set the properties value
            PROPERTY_SET_1.forEach(prop::setProperty);
            // save properties to project root folder
            prop.store(output2, null);
        }

        refreshProperties();
    }

    @AfterClass
    public void tearDown() throws Throwable {
        PROPERTY_SET_1.keySet().forEach(s -> System.getProperties().remove(s));
        PROPERTY_SET_2.keySet().forEach(s -> System.getProperties().remove(s));
        GeneralPropertyInitializer.arePropertiesRead = false;
        File toDelete = getFile(PROPERTIES);
        if (toDelete.exists()) {
            forceDelete(toDelete);
        }

        toDelete = new File(new File(GLOBAL_PROPERTIES).getAbsoluteFile()
                .getParentFile()
                .getParentFile(),
                GLOBAL_PROPERTIES);
        if (toDelete.exists()) {
            forceDelete(toDelete);
        }
    }
}
