package ru.tinkoff.qa.neptune.selenium.test.steps.tests.presence;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.Presence.presenceOf;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.presence.ElementPresence.presenceOfAnElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.presence.ElementPresence.presenceOfElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.textFields;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.flag;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.tableRow;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static java.lang.String.format;
import static java.util.List.of;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unchecked")
public class PresenceTest extends BaseWebDriverTest {

    private static final NoSuchElementException IGNORED_EXCEPTION = new NoSuchElementException("Ignored");
    private static final IllegalArgumentException IGNORED_EXCEPTION2 = new IllegalArgumentException("Ignored");
    private static final List<Class<? extends Throwable>> IGNORED_EXCEPTIONS = of(NoSuchElementException.class,
            IllegalArgumentException.class);

    private static final IndexOutOfBoundsException EXPECTED_EXCEPTION_TO_BE_THROWN =
            new IndexOutOfBoundsException("Expected exception to be thrown");

    private static final Function<SeleniumSteps, Object> RETURNS_OBJECT = toGet("Object",
            seleniumSteps -> new Object());

    private static final Function<SeleniumSteps, Object> RETURNS_NULL = toGet("Null value",
            seleniumSteps -> null);

    private static final Function<SeleniumSteps, Object> RETURNS_OBJECT_ARRAY = toGet("Object array",
            seleniumSteps -> new Object[] {1, "String"});

    private static final Function<SeleniumSteps, Object> RETURNS_EMPTY_ARRAY = toGet("Empty array",
            seleniumSteps -> new String[] {});

    private static final Function<SeleniumSteps, Object> RETURNS_OBJECT_ITERABLE = toGet("Object iterable",
            seleniumSteps -> of(1, "String"));

    private static final Function<SeleniumSteps, Object> RETURNS_EMPTY_ITERABLE = toGet("Object iterable",
            seleniumSteps -> of());

    private static final Function<SeleniumSteps, Object> RETURNS_TRUE = toGet("True value",
            seleniumSteps -> true);

    private static final Function<SeleniumSteps, Object> RETURNS_FALSE = toGet("False value",
            seleniumSteps -> false);

    private static final Function<SeleniumSteps, Object> PRODUCES_IGNORED_EXCEPTIONS = toGet("Ignored exception",
            seleniumSteps -> {
        List<RuntimeException> exceptions = of(IGNORED_EXCEPTION, IGNORED_EXCEPTION2);
        throw exceptions.get(new Random().nextInt(exceptions.size()));
    });

    private static final Function<SeleniumSteps, Object> PRODUCES_EXPECTED_EXCEPTIONS = toGet("Expected exception",
            seleniumSteps -> { throw EXPECTED_EXCEPTION_TO_BE_THROWN;});

    @Test
    public void testOfFunctionWhichReturnsValue() {
        assertThat(seleniumSteps.get(presenceOf(RETURNS_OBJECT)), is(true));
    }

    @Test
    public void testOfFunctionWhichReturnsNull() {
        assertThat(seleniumSteps.get(presenceOf(RETURNS_NULL)), is(false));
    }

    @Test
    public void testOfGetSupplierWhichReturnsValue() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(RETURNS_OBJECT))), is(true));
    }

    @Test
    public void testOfGetSupplierWhichReturnsNull() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(RETURNS_NULL))), is(false));
    }

    @Test
    public void testOfFunctionWhichReturnsArray() {
        assertThat(seleniumSteps.get(presenceOf(RETURNS_OBJECT_ARRAY)), is(true));
    }

    @Test
    public void testOfFunctionWhichReturnsEmptyArray() {
        assertThat(seleniumSteps.get(presenceOf(RETURNS_EMPTY_ARRAY)), is(false));
    }

    @Test
    public void testOfGetSupplierWhichReturnsArray() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(RETURNS_OBJECT_ARRAY))), is(true));
    }

    @Test
    public void testOfGetSupplierWhichReturnsEmptyArray() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(RETURNS_EMPTY_ARRAY))), is(false));
    }

    @Test
    public void testOfFunctionWhichReturnsIterable() {
        assertThat(seleniumSteps.get(presenceOf(RETURNS_OBJECT_ITERABLE)), is(true));
    }

    @Test
    public void testOfFunctionWhichReturnsEmptyIterable() {
        assertThat(seleniumSteps.get(presenceOf(RETURNS_EMPTY_ITERABLE)), is(false));
    }

    @Test
    public void testOfGetSupplierWhichReturnsIterable() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(RETURNS_OBJECT_ITERABLE))), is(true));
    }

    @Test
    public void testOfGetSupplierWhichReturnsEmptyIterable() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(RETURNS_EMPTY_ITERABLE))), is(false));
    }

    @Test
    public void testOfFunctionWhichReturnsTrue() {
        assertThat(seleniumSteps.get(presenceOf(RETURNS_TRUE)), is(true));
    }

    @Test
    public void testOfFunctionWhichReturnsFalse() {
        assertThat(seleniumSteps.get(presenceOf(RETURNS_FALSE)), is(false));
    }

    @Test
    public void testOfGetSupplierWhichReturnsTrue() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(RETURNS_TRUE))), is(true));
    }

    @Test
    public void testOfGetSupplierWhichReturnsFalse() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(RETURNS_FALSE))), is(false));
    }

    @Test
    public void testOfFunctionWhichThrowsIgnoredException() {
        assertThat(seleniumSteps.get(presenceOf(PRODUCES_IGNORED_EXCEPTIONS)
                .addIgnored(IGNORED_EXCEPTIONS)), is(false));
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Expected exception to be thrown")
    public void testOfFunctionWhichThrowsExpectedException() {
        assertThat(seleniumSteps.get(presenceOf(PRODUCES_EXPECTED_EXCEPTIONS)), is(false));
    }

    @Test
    public void testOfGetSupplierWhichThrowsIgnoredException() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(PRODUCES_IGNORED_EXCEPTIONS))
                .addIgnored(IGNORED_EXCEPTIONS)), is(false));
    }

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Expected exception to be thrown")
    public void testOfGetSupplierWhichThrowsExpectedException() {
        assertThat(seleniumSteps.get(presenceOf(new TestGetSupplier().set(PRODUCES_EXPECTED_EXCEPTIONS))), is(false));
    }

    @Test
    public void positiveTestOfSearchSupplier() {
        assertThat(seleniumSteps
                        .get(presenceOfAnElement(flag().foundFrom(tableRow(FIVE_SECONDS,
                                condition(format("Contains %s, %s and %s", CELL_TEXT87, CELL_TEXT88, CELL_TEXT89),
                                        tableRow -> tableRow.getValue().containsAll(of(CELL_TEXT87, CELL_TEXT88, CELL_TEXT89))))))),
                is(true));
    }

    @Test
    public void negativeTestOfSearchSupplier() {
        assertThat(seleniumSteps
                        .get(presenceOfAnElement(flag(FIVE_SECONDS).foundFrom(tableRow(FIVE_SECONDS,
                                condition(format("Contains %s, %s and %s", CELL_TEXT49, CELL_TEXT50, CELL_TEXT51),
                                        tableRow -> tableRow.getValue().containsAll(of(CELL_TEXT49, CELL_TEXT50, CELL_TEXT51))))))),
                is(false));
    }

    @Test
    public void positiveTestOfMultiSearchSupplier() {
        assertThat(seleniumSteps
                        .get(presenceOfElements(textFields().foundFrom(tableRow(FIVE_SECONDS,
                                condition(format("Contains %s, %s and %s", CELL_TEXT84, CELL_TEXT85, CELL_TEXT86),
                                        tableRow -> tableRow.getValue().containsAll(of(CELL_TEXT84, CELL_TEXT85, CELL_TEXT86))))))),
                is(true));
    }

    @Test
    public void negativeTestOfMultiSearchSupplier() {
        assertThat(seleniumSteps
                        .get(presenceOfElements(textFields(FIVE_SECONDS).foundFrom(tableRow(FIVE_SECONDS,
                                condition(format("Contains %s, %s and %s", CELL_TEXT22, CELL_TEXT23, CELL_TEXT24),
                                        tableRow -> tableRow.getValue().containsAll(of(CELL_TEXT22, CELL_TEXT23, CELL_TEXT24))))))),
                is(false));
    }

    private static class TestGetSupplier extends GetStepSupplier<SeleniumSteps, Object, TestGetSupplier> {
        public TestGetSupplier set(Function<SeleniumSteps, Object> function) {
            return super.set(function);
        }
    }
}
