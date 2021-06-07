package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.AllMatchersParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.Column;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.NoSuchColumnMismatch;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.entryKey;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntryValue;

@Description("has a column with header <{headerMatcher}> {columnMatcher}")
public class HasColumnMatcher extends NeptuneFeatureMatcher<Table> {

    @DescriptionFragment(value = "columnMatcher", makeReadableBy = AllMatchersParameterValueGetter.class)
    final Matcher<?>[] columnMatchers;
    @DescriptionFragment("headerMatcher")
    private final Matcher<? super String> headerMatcher;
    private final Matcher<Iterable<String>> columnMatcher;

    private HasColumnMatcher(Matcher<? super String> headerMatcher, Matcher<Iterable<String>> columnMatcher) {
        super(true);
        checkArgument(nonNull(headerMatcher), "Criteria to match table header should be defined");
        this.columnMatchers = ofNullable(columnMatcher).map(i -> new Matcher[]{i}).orElseGet(() -> new Matcher[]{});
        this.headerMatcher = headerMatcher;
        this.columnMatcher = columnMatcher;
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
    public static Matcher<Table> hasAColumn(String column, Matcher<Iterable<String>> valuesMatcher) {
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
    public static Matcher<Table> hasAColumn(Matcher<? super String> columnMatcher, Matcher<Iterable<String>> valuesMatcher) {
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
        return hasAColumn(columnMatcher, (Matcher<Iterable<String>>) null);
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
    protected boolean featureMatches(Table toMatch) {
        var tableValue = toMatch.getValue();

        var entryMatcher = entryKey(headerMatcher);
        var foundColumns = tableValue
                .entrySet()
                .stream()
                .filter(entryMatcher::matches)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (foundColumns.size() == 0) {
            appendMismatchDescription(new NoSuchColumnMismatch(headerMatcher));
            return false;
        }

        if (!mapHasEntryValue(columnMatcher).matches(foundColumns)) {

            foundColumns.entrySet().forEach(e -> {
                if (!columnMatcher.matches(e)) {
                    appendMismatchDescription(new PropertyValueMismatch(new Column()
                            + "[" + e.getKey() + "]",
                            e.getValue(),
                            columnMatcher));
                }
            });
            return false;
        }

        return true;
    }
}
