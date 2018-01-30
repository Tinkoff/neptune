package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public abstract class Table extends Widget {

    public Table(WebElement wrappedElement) {
        super(wrappedElement);
    }

    private List<String> convertCellListToStringList(List<Cell> cells) {
        return cells.stream().map(HasValue::getValue)
                .collect(toList());
    }

    /**
     * @return list of table cells which belong to table header.
     */
    public abstract List<Cell> getHeader();

    /**
     * @param columnNumber is a number of the column. It starts from 0.
     * @return list of table cells which belong to the column.
     * Header and footer cells of the column are excluded.
     */
    public abstract List<Cell> getColumn(int columnNumber);

    /**
     * @param column string header of the column.
     * @return list of table cells which belong to the column.
     * Header and footer cells of the column are excluded.
     */
    public List<Cell> getColumn(String column) {
        int index = getStringHeader().indexOf(column);
        checkArgument(index >= 0, format("There is no column %s", column));
        return getColumn(index);
    }

    /**
     * @param rowNumber is a number of the row. It starts from 0.
     * @return list of table cells which belong to the row.
     * Header and footer of the table are not considered row.
     */
    public abstract List<Cell> getRow(int rowNumber);

    /**
     * @return list of table cells which belong to table footer.
     */
    public abstract List<Cell> getFooter();

    /**
     * @param columnNumber is a number of the column. It starts from 0.
     * @return list of string values of cells which belong to the column.
     * Header and footer cells of the column are excluded.
     */
    public List<String> getStringColumn(int columnNumber) {
        return convertCellListToStringList(getColumn(columnNumber));
    }

    /**
     * @param column string header of the column.
     * @return list of string values of cells which belong to the column.
     * Header and footer cells of the column are excluded.
     */
    public List<String> getStringColumn(String column) {
        return convertCellListToStringList(getColumn(column));
    }

    /**
     * @return list of string values of cells which belong to
     * the header of a table.
     */
    public List<String> getStringHeader() {
        return convertCellListToStringList(getHeader());
    }

    /**
     * @param rowNumber is a number of the row. It starts from 0.
     * @return list of string values of table cells which belong to the row.
     * Header and footer of the table are not considered row.
     */
    public List<String> getStringRow(int rowNumber) {
        return convertCellListToStringList(getRow(rowNumber));
    }

    /**
     * @return list of string values of cells which belong to
     * the footer of a table.
     */
    public List<String> getStringFooter() {
        return convertCellListToStringList(getHeader());
    }

    public static abstract class Cell extends Widget implements HasValue<String> {
        public Cell(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }
}
