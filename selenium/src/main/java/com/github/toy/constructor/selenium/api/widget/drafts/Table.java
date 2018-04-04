package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Name("Table")
public abstract class Table extends Widget {

    public Table(WebElement wrappedElement) {
        super(wrappedElement);
    }

    private static List<String> convertCellListToStringList(List<Cell> cells) {
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
    public List<Cell> getColumn(int columnNumber) {
        return getRows()
                .stream()
                .map(row -> row.getCells().get(columnNumber)).collect(toList());
    }

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
     * @return a count of table rows.
     * Header and footer of the table are not considered row.
     */
    public int size() {
        return getRows().size();
    }

    /**
     * @return returns a list of table rows.
     * Header and footer of the table are not considered row.
     */
    public abstract List<Row> getRows();

    /**
     * @param rowNumber is a number of the row. It starts from 0.
     * @return a single table row. Header and footer of the table are not considered row.
     */
    public Row getRow(int rowNumber) {
        int size = size();
        checkArgument(rowNumber + 1 <= size
                && rowNumber >= 0, format("The row number %s is out of the table size (from 0 to %s)",
                rowNumber, size));
        return getRows().get(rowNumber);
    }

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
     * @return list of string values of cells which belong to
     * the footer of a table.
     */
    public List<String> getStringFooter() {
        return convertCellListToStringList(getHeader());
    }

    @Name("Row")
    public static abstract class Row extends Widget {

        public Row(WebElement wrappedElement) {
            super(wrappedElement);
        }

        /**
         * @return list of cells which belong to the row.
         */
        public abstract List<Cell> getCells();

        public List<String> getStringCells() {
            return convertCellListToStringList(getCells());
        }
    }

    @Name("Cell")
    public static abstract class Cell extends Widget implements HasValue<String> {
        public Cell(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }

    @Name("Header")
    public static abstract class Header extends Widget {
        public Header(WebElement wrappedElement) {
            super(wrappedElement);
        }

        /**
         * @return list of cells which belong to the header.
         */
        public abstract List<Cell> getCells();

        public List<String> getStringCells() {
            return convertCellListToStringList(getCells());
        }
    }

    @Name("Header")
    public static abstract class Footer extends Widget {
        public Footer(WebElement wrappedElement) {
            super(wrappedElement);
        }

        /**
         * @return list of cells which belong to the footer.
         */
        public abstract List<Cell> getCells();

        public List<String> getStringCells() {
            return convertCellListToStringList(getCells());
        }
    }
}
