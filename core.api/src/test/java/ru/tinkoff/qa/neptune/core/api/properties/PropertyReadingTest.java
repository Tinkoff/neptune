package ru.tinkoff.qa.neptune.core.api.properties;

import org.hamcrest.Matcher;
import org.testng.annotations.*;
import ru.tinkoff.qa.neptune.core.api.properties.object.suppliers.ObjectSupplier1;
import ru.tinkoff.qa.neptune.core.api.properties.object.suppliers.ObjectSupplier2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import static java.time.Duration.*;
import static java.util.Map.entry;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.getFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.GENERAL_PROPERTIES;
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

@Test
public class PropertyReadingTest {

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
            .ofEntries(entry(TEST_BOOLEAN_PROPERTY, "true"),
                    entry(TEST_BYTE_PROPERTY, "2"),
                    entry(TEST_DOUBLE_PROPERTY, "3"),
                    entry(TEST_CHRONO_UNIT_PROPERTY,  "HOURS"),
                    entry(TEST_TIME_VALUE_PROPERTY, "4"),
                    entry(TEST_ENUM_ITEM_PROPERTY, "ITEM4"),
                    entry(TEST_ENUM_ITEMS_PROPERTY, "ITEM1,ITEM2,ITEM3"),
                    entry(TEST_FLOAT_PROPERTY, "5"),
                    entry(TEST_INTEGER_PROPERTY, "6"),
                    entry(TEST_LONG_PROPERTY, "7"),
                    entry(TEST_OBJECTS_PROPERTY, ObjectSupplier1.class.getName() + "," + ObjectSupplier1.class.getName()),
                    entry(TEST_OBJECT_PROPERTY, ObjectSupplier2.class.getName()),
                    entry(TEST_SHORT_PROPERTY, "8"),
                    entry(TEST_URL_PROPERTY, "https://www.programcreek.com"));

    private final int iterationNumber;
    private final Matcher<? super Boolean> booleanMatcher;
    private final Matcher<? super Byte> byteMatcher;
    private final Matcher<? super Double> doubleMatcher;
    private final Matcher<? super Duration> durationMatcher;
    private final Matcher<? super Iterable<TestEnum>> testEnumItemsMatchers;
    private final Matcher<? super TestEnum> testEnumItemMatchers;
    private final Matcher<? super Float> floatMatcher;
    private final Matcher<? super Integer> intMatcher;
    private final Matcher<? super Long> longMatcher;
    private final Matcher<? super Iterable<Object>> testObjectsMatchers;
    private final Matcher<? super Object> testObjectMatchers;
    private final Matcher<? super Short> shortMatcher;
    private final Matcher<? super URL> urlMatcher;

    @Factory(dataProvider = "dataProvider")
    public PropertyReadingTest(int iterationNumber,
                               Matcher<? super Boolean> booleanMatcher,
                               Matcher<? super Byte> byteMatcher,
                               Matcher<? super Double> doubleMatcher,
                               Matcher<? super Duration> durationMatcher,
                               Matcher<? super Iterable<TestEnum>> testEnumItemsMatchers,
                               Matcher<? super TestEnum> testEnumItemMatchers,
                               Matcher<? super Float> floatMatcher,
                               Matcher<? super Integer> intMatcher,
                               Matcher<? super Long> longMatcher,
                               Matcher<? super Iterable<Object>> testObjectsMatchers,
                               Matcher<? super Object> testObjectMatchers,
                               Matcher<? super Short> shortMatcher,
                               Matcher<? super URL> urlMatcher) {
        this.iterationNumber = iterationNumber;
        this.booleanMatcher = booleanMatcher;
        this.byteMatcher = byteMatcher;
        this.doubleMatcher = doubleMatcher;
        this.durationMatcher = durationMatcher;
        this.testEnumItemsMatchers = testEnumItemsMatchers;
        this.testEnumItemMatchers = testEnumItemMatchers;
        this.floatMatcher = floatMatcher;
        this.intMatcher = intMatcher;
        this.longMatcher = longMatcher;
        this.testObjectsMatchers = testObjectsMatchers;
        this.testObjectMatchers = testObjectMatchers;
        this.shortMatcher = shortMatcher;
        this.urlMatcher = urlMatcher;
    }

    @BeforeMethod
    public void setUpBeforeClass() {
        switch (iterationNumber) {
            case  (2):
                PROPERTY_SET_1.forEach(System::setProperty);
                break;
            case (3):
                try {
                    Properties prop = new Properties();
                    try (OutputStream output = new FileOutputStream(GENERAL_PROPERTIES)) {
                        // set the properties value
                        PROPERTY_SET_2.forEach(prop::setProperty);
                        // save properties to project root folder
                        prop.store(output, null);
                    }
                }
                catch (Throwable t) {
                    throw new RuntimeException(t);
                }
                break;
            case (4):
                PROPERTY_SET_1.forEach(System::setProperty);
                try {
                    Properties prop = new Properties();
                    try (OutputStream output = new FileOutputStream(GENERAL_PROPERTIES)) {
                        // set the properties value
                        PROPERTY_SET_2.forEach(prop::setProperty);
                        // save properties to project root folder
                        prop.store(output, null);
                    }
                }
                catch (Throwable t) {
                    throw new RuntimeException(t);
                }
                break;
            default:
                break;
        }
    }

    @Test
    public void booleanTest() {
        assertThat(new TestBooleanPropertySupplier().get(), booleanMatcher);
    }

    @Test
    public void byteTest() {
        assertThat(new TestBytePropertySupplier().get(), byteMatcher);
    }

    @Test
    public void doubleTest() {
        assertThat(new TestDoublePropertySupplier().get(), doubleMatcher);
    }

    @Test
    public void durationTest() {
        assertThat(new TestDurationSupplier(new TestDurationSupplier.TestChronoUnitSupplier(),
                new TestDurationSupplier.TestTimeValueSupplier()).get(), durationMatcher);
    }

    @Test
    public void enumItemsTest() {
        assertThat(new TestEnumItemsPropertySuppler().get(), testEnumItemsMatchers);
    }

    @Test
    public void enumItemTest() {
        assertThat(new TestEnumItemPropertySuppler().get(), testEnumItemMatchers);
    }

    @Test
    public void floatTest() {
        assertThat(new TestFloatPropertySupplier().get(), floatMatcher);
    }

    @Test
    public void intTest() {
        assertThat(new TestIntegerPropertySupplier().get(), intMatcher);
    }

    @Test
    public void longTest() {
        assertThat(new TestLongPropertySupplier().get(), longMatcher);
    }

    @Test
    public void objectsTest() {
        assertThat(ofNullable(new TestObjectsSupplier().get())
                .map(suppliers -> suppliers.stream().map(Supplier::get).collect(toList())).orElse(null), testObjectsMatchers);
    }

    @Test
    public void objectTest() {
        assertThat(ofNullable(new TestObjectSupplier().get())
                .map(Supplier::get).orElse(null), testObjectMatchers);
    }

    @Test
    public void shortTest() {
        assertThat(new TestSrortPropertySupplier().get(), shortMatcher);
    }

    @Test
    public void urlTest() {
        assertThat(new TestURLValuePropertySupplier().get(), urlMatcher);
    }

    @DataProvider
    public static Object[][] dataProvider() throws Throwable {
        return new Object[][]{
                {1,
                        is(false),
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
                        nullValue()
                },

                {2,
                        is(false),
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
                        is(new URL("https://www.google.com"))
                },

                {3,
                        is(true),
                        is(Byte.valueOf("2")),
                        is(3D),
                        is(ofHours(4)),
                        contains(ITEM1,ITEM2,ITEM3),
                        is(ITEM4),
                        is(5F),
                        is(6),
                        is(7L),
                        contains(O1, O1),
                        is(O2),
                        is(Short.valueOf("8")),
                        is(new URL("https://www.programcreek.com"))
                },

                {4,
                        is(false),
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
                        is(new URL("https://www.google.com"))
                },
        };
    }

    @AfterMethod
    public void tearDown() throws Throwable {
        PROPERTY_SET_1.keySet().forEach(s -> System.getProperties().remove(s));
        PROPERTY_SET_2.keySet().forEach(s -> System.getProperties().remove(s));
        GeneralPropertyInitializer.arePropertiesRead = false;
        File toDelete = getFile(GENERAL_PROPERTIES);
        if (toDelete.exists()) {
            forceDelete(toDelete);
        }
    }
}
