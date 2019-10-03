package ru.tinkoff.qa.neptune.core.api.properties;

import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class BasePropertyReadingTest {

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


    BasePropertyReadingTest(Matcher<? super Boolean> booleanMatcher,
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
}
