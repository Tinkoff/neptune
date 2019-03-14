package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import com.google.common.collect.Iterables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

public class HasColumnMatcher<T extends Iterable<?>> extends TypeSafeDiagnosingMatcher<Table> {

    private final Matcher<? super String> headerMatcher;
    private final Matcher<T> columnMatcher;

    private HasColumnMatcher(Matcher<? super String> headerMatcher, Matcher<T> columnMatcher) {
        checkArgument(nonNull(headerMatcher), "Criteria to match table header should be defined");
        checkArgument(nonNull(columnMatcher), "Criteria to match table column should be defined");
        this.headerMatcher = headerMatcher;
        this.columnMatcher = columnMatcher;
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header as it is defined by {@code column}. Also it is expected that a column
     * has values as it is defied by {@code expectedValues} with the same order.
     *
     * @param column is header mane of the expected column
     * @param expectedValues values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static HasColumnMatcher<Iterable<? extends String>> hasAColumn(String column, String... expectedValues) {
        checkArgument(!isBlank(column), "Name of the expected column should not be blank");
        checkNotNull(expectedValues);
        checkArgument(expectedValues.length > 0, "There should be defined at least one expected value in the column");
        return new HasColumnMatcher<>(equalTo(column), contains(expectedValues));
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header as it is defined by {@code column}. Also it is expected that a column
     * has values as it is defied by {@code expectedValues} with the same order.
     *
     * @param column is header mane of the expected column
     * @param expectedValues values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static HasColumnMatcher<Iterable<? extends String>> hasAColumn(String column, Collection<String> expectedValues) {
        checkNotNull(expectedValues);
        return hasAColumn(column, expectedValues.toArray(new String[] {}));
    }


    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header that meets the criteria defined by {@code columnMatcher}.
     * Also it is expected that a column has values as it is defied by {@code expectedValues} with the same order.
     *
     * @param columnMatcher is the criteria to check and find the expected column
     * @param expectedValues values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static HasColumnMatcher<Iterable<? extends String>> hasAColumn(Matcher<? super String> columnMatcher, String... expectedValues) {
        checkNotNull(expectedValues);
        checkArgument(expectedValues.length > 0, "There should be defined at least one expected value in the column");
        return new HasColumnMatcher<>(columnMatcher, contains(expectedValues));
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header that meets the criteria defined by {@code columnMatcher}.
     * Also it is expected that a column has values as it is defied by {@code expectedValues} with the same order.
     *
     * @param columnMatcher is the criteria to check and find the expected column
     * @param expectedValues values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static HasColumnMatcher<Iterable<? extends String>> hasAColumn(Matcher<? super String> columnMatcher, Collection<String> expectedValues) {
        checkNotNull(expectedValues);
        return hasAColumn(columnMatcher, expectedValues.toArray(new String[] {}));
    }

    /**
     * Creates an instance of {@link HasColumnMatcher} that checks columns of the {@link Table}. It is expected
     * that the table has a column with a header as it is defined by {@code column}. Also it is expected that a column
     * has values that meet the criteria defined by {@code valuesMatcher}.
     *
     * @param column is header mane of the expected column
     * @param valuesMatcher is the criteria to check values of the expected column
     * @return created object of {@link HasColumnMatcher}
     */
    public static HasColumnMatcher<? extends Iterable<?>> hasAColumn(String column, Matcher<? extends Iterable<?>> valuesMatcher) {
        checkArgument(!isBlank(column), "Name of the expected column should not be blank");
        return new HasColumnMatcher<>(equalTo(column), valuesMatcher);
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
    public static HasColumnMatcher<? extends Iterable<?>> hasAColumn(Matcher<? super String> columnMatcher, Matcher<? extends Iterable<?>> valuesMatcher) {
        return new HasColumnMatcher<>(columnMatcher, valuesMatcher);
    }

    @Override
    protected boolean matchesSafely(Table item, Description mismatchDescription) {
        boolean result;
        Set<String> suitableHeaders = new HashSet<>();
        var tableValue = item.getValue();

        var headers = tableValue.keySet();
        headers.forEach(s -> {
            if (headerMatcher.matches(s)) {
                suitableHeaders.add(s);
            }
        });

        if (suitableHeaders.size() == 0) {
            mismatchDescription.appendText(format("Table %s has no column header that matches %s. Actual list column headers: %s",
                    item, headerMatcher, Iterables.toString(headers)));
            return false;
        }

        Map<String, Description> columnMismatches = new HashMap<>();
        for (String header: suitableHeaders) {
            var column = tableValue.get(header);
            if (columnMatcher.matches(column)) {
                return true;
            }
            var columnMismatch = new StringDescription();
            columnMismatch.appendText(format("Column %s: \n", header));
            columnMatcher.describeMismatch(column, columnMismatch);
            columnMismatches.put(header, columnMismatch);
        }

        mismatchDescription.appendText(format("Table %s has column headers that meet the criteria '%s'. Headers: %s.\n",
                item, columnMatcher, Iterables.toString(suitableHeaders)));
        mismatchDescription.appendText("But there are mismatches of column values:\n");
        columnMismatches.forEach((key, value) -> mismatchDescription.appendText(format("\n - Header: %s. Mismatch: %s", key, value)));
        return false;
    }

    @Override
    public String toString() {
        return format("Table has a column with a header that '%s' and values that meet '%s'", headerMatcher, columnMatcher);
    }
}
