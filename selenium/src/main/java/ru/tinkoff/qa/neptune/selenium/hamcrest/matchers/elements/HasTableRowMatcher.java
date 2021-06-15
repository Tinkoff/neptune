package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.AllMatchersParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableRow;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.TableSize;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.Duration.ofMillis;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.turnReportingOff;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.tableRows;

public abstract class HasTableRowMatcher extends NeptuneFeatureMatcher<Table> {

    @DescriptionFragment(value = "rowMatcher", makeReadableBy = AllMatchersParameterValueGetter.class)
    final Matcher<? super TableRow>[] rowMatchers;

    @SafeVarargs
    private HasTableRowMatcher(Matcher<? super TableRow>... rowMatchers) {
        super(true);
        checkNotNull(rowMatchers);
        checkArgument(rowMatchers.length > 0, "At least one matcher should be defined");
        this.rowMatchers = rowMatchers;
    }

    /**
     * Creates an instance of {@link HasTableRowMatcher} that checks the row of the {@link Table}.
     * The row is specified by {@code expectedRowNum}. It is expected that the row meets every criteria specified
     * by {@code rowMatchers}.
     *
     * @param expectedRowNum is the number of the row to be checked. It should be 0 or greater.
     * @param rowMatchers    criteria to check the table row
     * @return created object of {@link HasTableRowMatcher}
     */
    @SafeVarargs
    public static Matcher<Table> hasARow(int expectedRowNum, Matcher<? super TableRow>... rowMatchers) {
        return new HasTableRowMatcher.HasRowNumber(expectedRowNum, rowMatchers);
    }

    /**
     * Creates an instance of {@link HasTableRowMatcher} that checks the row of the {@link Table}.
     * The row is specified by {@code expectedRowNum}. It is expected that the row meets criteria specified
     * by {@code rowMatcher}.
     *
     * @param expectedRowNum is the number of the row to be checked. It should be 0 or greater.
     * @param rowMatcher     criteria to check the table row
     * @return created object of {@link HasTableRowMatcher}
     */
    @SuppressWarnings("unchecked")
    public static Matcher<Table> hasARow(int expectedRowNum, Matcher<? super TableRow> rowMatcher) {
        return hasARow(expectedRowNum, new Matcher[]{rowMatcher});
    }

    /**
     * Creates an instance of {@link HasTableRowMatcher} that checks the {@link Table}. It is expected that
     * the table has a row that meets every criteria specified by {@code rowMatchers}.
     *
     * @param rowMatchers criteria to check the table row
     * @return created object of {@link HasTableRowMatcher}
     */
    @SafeVarargs
    public static Matcher<Table> hasARow(Matcher<? super TableRow>... rowMatchers) {
        return new HasTableRowMatcher.HasARow(rowMatchers);
    }

    /**
     * Creates an instance of {@link HasTableRowMatcher} that checks the {@link Table}. It is expected that
     * the table has a row that meets criteria specified by  {@code rowMatcher}.
     *
     * @param rowMatcher criteria to check the table row
     * @return created object of {@link HasTableRowMatcher}
     */
    @SuppressWarnings("unchecked")
    public static Matcher<Table> hasARow(Matcher<? super TableRow> rowMatcher) {
        return hasARow(new Matcher[]{rowMatcher});
    }


    @Description("row[{rowNumber}]: {rowMatcher}")
    private static final class HasRowNumber extends HasTableRowMatcher {

        @DescriptionFragment("rowNumber")
        private final int rowNumber;

        @SafeVarargs
        private HasRowNumber(int rowNumber, Matcher<? super TableRow>... rowMatchers) {
            super(rowMatchers);
            checkArgument(rowNumber >= 0,
                    "Number of a row must be 0 or positive integer value");
            this.rowNumber = rowNumber;
        }

        @Override
        protected boolean featureMatches(Table toMatch) {
            var tableRows = turnReportingOff(tableRows().timeOut(ofMillis(0))).get().apply(toMatch);
            var size = tableRows.size();

            if (size < rowNumber + 1) {
                appendMismatchDescription(new PropertyValueMismatch(new TableSize(), size, greaterThanOrEqualTo(rowNumber + 1)));
                return false;
            }

            var row = tableRows.get(rowNumber);
            var rowMatcher = all(rowMatchers);
            if (!rowMatcher.matches(row)) {
                appendMismatchDescription(rowMatcher, row);
                return false;
            }

            return true;
        }
    }

    @Description("has a row: {rowMatcher}")
    private static final class HasARow extends HasTableRowMatcher {

        @SafeVarargs
        private HasARow(Matcher<? super TableRow>... rowMatchers) {
            super(rowMatchers);
        }

        @Override
        protected boolean featureMatches(Table toMatch) {
            var tableRows = tableRows().timeOut(ofMillis(0)).get().apply(toMatch);
            var rowMatcher = iterableHasItem(rowMatchers);

            if (!rowMatcher.matches(tableRows)) {
                appendMismatchDescription(rowMatcher, tableRows);
                return false;
            }

            return true;
        }
    }
}
