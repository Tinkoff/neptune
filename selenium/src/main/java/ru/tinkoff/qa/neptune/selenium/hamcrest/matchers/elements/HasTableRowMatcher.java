package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableRow;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.tableRows;

public class HasTableRowMatcher extends TypeSafeDiagnosingMatcher<Table> {

    private final BaseMatcher<? super TableRow> rowMatcher;
    private final int expectedRowNum;

    private HasTableRowMatcher(BaseMatcher<? super TableRow> rowMatcher, Integer expectedRowNum) {
        checkNotNull(rowMatcher);
        checkArgument(expectedRowNum >= 0,
                "Expected number of a row must be null or positive integer value");
        this.rowMatcher = rowMatcher;
        this.expectedRowNum = expectedRowNum;
    }

    /**
     * Creates an instance of {@link HasTableRowMatcher} that the row of the {@link Table}.
     * The row is specified by {@code expectedRowNum}. It is expected that the row meets the criteria specified
     * by {@code rowMatcher}.
     *
     * @param expectedRowNum is the number of the row to be checked. It should be 0 or greater.
     * @param rowMatcher is the criteria to check the table row
     * @return created object of {@link HasTableRowMatcher}
     */
    public static HasTableRowMatcher hasARow(int expectedRowNum, BaseMatcher<? super TableRow> rowMatcher) {
        return new HasTableRowMatcher(rowMatcher, expectedRowNum);
    }

    @Override
    protected boolean matchesSafely(Table item, Description mismatchDescription) {
        var tableRows = tableRows().get().apply(item);

        var size = tableRows.size();
        if (size < expectedRowNum + 1) {
            mismatchDescription.appendText(format("%s has %s rows. It lesser than expected row number", item, size));
            return false;
        }

        var result = rowMatcher.matches(tableRows.get(expectedRowNum));

        if (!result) {
            rowMatcher.describeMismatch(tableRows.get(expectedRowNum), mismatchDescription);
        }
        return result;
    }

    @Override
    public String toString() {
        return format("Table has a row that meets the criteria '%s'. Row number is %s", rowMatcher, expectedRowNum);
    }
}
