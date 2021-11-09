package ru.tinkoff.qa.neptune.data.base.api.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * This collection contains results of SQL/JDO selection. This result contains values of data store table/
 * {@link ru.tinkoff.qa.neptune.data.base.api.PersistableObject} fields.
 */
@Deprecated(forRemoval = true)
public class TableResultList extends ArrayList<List<Object>> {

    protected TableResultList(List<List<Object>> from) {
        super(toRows(from));
    }

    private static List<ResultTableRow> toRows(Collection<List<Object>> original) {
        return original.stream().map(ResultTableRow::new).collect(toList());
    }

    public String toString() {
        return format("%s selected row/rows",
                size());
    }

    /**
     * Gets values of the specific column by it's number
     *
     * @param columnIndex is a column index. Starts from zero
     * @param mapper is a function that transforms some row value to desired
     * @param <T> is desired type a column item.
     * @return list that represents column of a query result table
     */
    public <T> List<T> getColumn(int columnIndex, Function<Object, T> mapper) {
        checkArgument(columnIndex >=0, "Index value should be positive or zero");

        return stream().map(objects -> {
            try {
                var object = objects.get(columnIndex);
                return ofNullable(object).map(mapper).orElse(null);
            } catch (IndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException(format("%s. It is possible that %s is greater than index of the last column " +
                        "or the result table is invalid due to some row has lower size ", e.getMessage(), columnIndex));
            }
        }).collect(toCollection(ResultTableColumn::new));
    }

    /**
     * Gets values of the specific column by it's number
     *
     * @param columnIndex is a column index. Starts from zero
     * @return list that represents column of a query result table
     */
    public List<Object> getColumn(int columnIndex) {
        return getColumn(columnIndex, o -> o);
    }
}
