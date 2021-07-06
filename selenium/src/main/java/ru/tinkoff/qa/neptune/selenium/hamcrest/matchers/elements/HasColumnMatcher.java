package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.Column;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.AnyThingMatcher.anything;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.KEY_MATCHER_MASK;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.mapped.MappedDiagnosticFeatureMatcher.VALUE_MATCHER_MASK;

@Description("has a column with header <{" + KEY_MATCHER_MASK + "}> {" + VALUE_MATCHER_MASK + "}")
public class HasColumnMatcher extends MappedDiagnosticFeatureMatcher<Table, String, List<String>> {

    private HasColumnMatcher(Matcher<? super String> headerMatcher, Matcher<List<String>> columnMatcher) {
        super(true, headerMatcher, columnMatcher);
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header as it is defined by {@code column}. Also it is expected that a column
     * has values as it is defied by {@code expectedValues} with the same order.
     *
     * @param column         is header mane of the expected column
     * @param expectedValues values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static Matcher<Table> hasAColumn(String column, String... expectedValues) {
        checkArgument(!isBlank(column), "Name of the expected column should not be blank");
        checkNotNull(expectedValues);
        checkArgument(expectedValues.length > 0, "There should be defined at least one expected value in the column");
        return hasAColumn(equalTo(column), iterableInOrder(expectedValues));
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header that meets the criteria defined by {@code columnMatcher}.
     * Also it is expected that a column has values as it is defied by {@code expectedValues} with the same order.
     *
     * @param columnMatcher  is the criteria to check and find the expected column
     * @param expectedValues values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static Matcher<Table> hasAColumn(Matcher<? super String> columnMatcher, String... expectedValues) {
        checkNotNull(expectedValues);
        checkArgument(expectedValues.length > 0, "There should be defined at least one expected value in the column");
        return hasAColumn(columnMatcher, iterableInOrder(expectedValues));
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header as it is defined by {@code column}. Also it is expected that a column
     * has values that meet the criteria defined by {@code valuesMatcher}.
     *
     * @param column        is header mane of the expected column
     * @param valuesMatcher is the criteria to check values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static Matcher<Table> hasAColumn(String column, Matcher<List<String>> valuesMatcher) {
        checkArgument(!isBlank(column), "Name of the expected column should not be blank");
        return hasAColumn(equalTo(column), valuesMatcher);
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header that meets the criteria defined by {@code columnMatcher}.
     * Also it is expected that a column has values that meet the criteria defined by {@code valuesMatcher}.
     *
     * @param columnMatcher is the criteria to check and find the expected column
     * @param valuesMatcher is the criteria to check values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static Matcher<Table> hasAColumn(Matcher<? super String> columnMatcher, Matcher<List<String>> valuesMatcher) {
        return new HasColumnMatcher(columnMatcher, valuesMatcher);
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header that meets the criteria defined by {@code columnMatcher}.
     *
     * @param columnMatcher is the criteria to check and find the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static Matcher<Table> hasAColumn(Matcher<? super String> columnMatcher) {
        return hasAColumn(columnMatcher, anything());
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header as it is defined by {@code column}.
     *
     * @param column is header mane of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static Matcher<Table> hasAColumn(String column) {
        return hasAColumn(equalTo(column));
    }


    @Override
    protected Map<String, List<String>> getMap(Table table) {
        return table.getValue();
    }

    @Override
    protected String getDescriptionOnKeyAbsence() {
        return new Column().toString();
    }

    @Override
    protected String getDescriptionOnValueMismatch(String s) {
        return new Column() + "[" + s + "]";
    }
}
