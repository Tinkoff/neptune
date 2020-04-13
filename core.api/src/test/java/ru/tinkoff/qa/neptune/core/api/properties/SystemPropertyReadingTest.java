package ru.tinkoff.qa.neptune.core.api.properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import ru.tinkoff.qa.neptune.core.api.properties.object.suppliers.ObjectSupplier1;
import ru.tinkoff.qa.neptune.core.api.properties.object.suppliers.ObjectSupplier2;

import java.net.URL;
import java.util.Map;

import static java.time.Duration.ofSeconds;
import static java.util.Map.entry;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
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

public class SystemPropertyReadingTest extends BasePropertyReadingTest {

    private static final Map<String, String> PROPERTY_SET = Map
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

    public SystemPropertyReadingTest() throws Exception {
        super(is(false),
                is(Byte.valueOf("1")),
                is(2D),
                is(ofSeconds(3)),
                contains(ITEM1, ITEM2, ITEM4),
                is(ITEM3),
                is(4F),
                is(5),
                is(6L),
                contains(O1, O2),
                is(O1),
                is(Short.valueOf("7")),
                is(new URL("https://www.google.com")));
    }

    @BeforeClass
    public static void setUp() {
        PROPERTY_SET.forEach(System::setProperty);
    }

    @AfterClass
    public void tearDown() {
        GeneralPropertyInitializer.arePropertiesRead = false;
        PROPERTY_SET.keySet().forEach(s -> System.getProperties().remove(s));
    }
}
