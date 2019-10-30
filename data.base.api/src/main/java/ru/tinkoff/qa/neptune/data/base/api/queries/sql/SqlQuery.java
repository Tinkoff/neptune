package ru.tinkoff.qa.neptune.data.base.api.queries.sql;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.ListOfDataBaseObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is designed to perform a query to select list of stored objects/list os stored field values by sql
 * @param <T> is a type of values to be selected.
 */
public final class SqlQuery<T> implements Function<JDOPersistenceManager, List<T>> {

    private final String sql;
    private final Class<T> classOfRequestedValue;
    private final Object[] parameters;

    private SqlQuery(String sql, Class<T> classOfRequestedValue, Object... parameters) {
        checkArgument(isNotBlank(sql), "Sql query should not be a blank string");
        checkArgument(nonNull(parameters), "Query parameters should be defined as a value that differs from null");
        this.classOfRequestedValue = classOfRequestedValue;
        this.sql = sql;
        this.parameters = parameters;
    }

    private List<List<Object>> getUntypedResult(Query<?> sqlQuery) {
        var result = sqlQuery.executeList();
        var toBeReturned = new ArrayList<List<Object>>();

        result.forEach(o -> {
            var row = new ListOfDataBaseObjects<>() {
                @Override
                public String toString() {
                    return format("row of field values: %s", super.toString());
                }
            };

            if (o.getClass().isArray()) {
                row.addAll(asList(((Object[]) o)));
            } else {
                row.addAll(of(o));
            }
            toBeReturned.add(row);
        });

        return new ListOfDataBaseObjects<>(toBeReturned) {
            public String toString() {
                return format("%s resulted row/rows selected by sql query '%s' " +
                        "with parameters '%s'",
                        size(),
                        sql,
                        Arrays.toString(parameters));
            }
        };
    }

    /**
     * Constructs an object that performs sql query to select list of stored objects.
     *
     * @param classOfRequestedValue is a type of objects to be returned
     * @param sql is an sql query. Parameter mask ({@code ?}) is supported. It is important!:
     *            <p>Sql query should be defined as below</p>
     *            {@code 'Select * from Persons...'}
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @param <T> is a type of retrieved objects
     * @return new {@link SqlQuery}
     */
    public static <T extends PersistableObject> SqlQuery<T> bySql(Class<T> classOfRequestedValue,
                                                                  String sql,
                                                                  Object... parameters) {
        return new SqlQuery<>(sql, classOfRequestedValue, parameters);
    }

    /**
     * Constructs an object that performs sql query to select list of lists. Each list item contains
     * raw objects as they actually stored without any deserialization.
     *
     * @param sql is an sql query. Parameter mask ({@code ?}) is supported.
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @return new {@link SqlQuery}
     */
    public static SqlQuery<List<Object>> bySql(String sql, Object... parameters) {
        return new SqlQuery<>(sql, null, parameters);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> apply(JDOPersistenceManager jdoPersistenceManager) {
        var query = jdoPersistenceManager.newQuery("javax.jdo.query.SQL", sql);

        if (parameters.length > 0) {
            query.setParameters(parameters);
        }

        if (classOfRequestedValue == null) {
            return (List<T>) getUntypedResult(query);
        }

        query.setClass(classOfRequestedValue);
        var list = query.executeList();
        return new ListOfDataBaseObjects<>(jdoPersistenceManager.detachCopyAll(list)) {
            public String toString() {
                return format("%s objects/object selected by sql query '%s'" +
                                " with parameters '%s'",
                        size(),
                        sql,
                        Arrays.toString(parameters));
            }
        };
    }
}
