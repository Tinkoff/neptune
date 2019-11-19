package ru.tinkoff.qa.neptune.data.base.api.queries.sql;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;
import ru.tinkoff.qa.neptune.data.base.api.result.TableResultList;

import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is designed to perform a query to select list of stored objects/list os stored field values by sql
 * @param <T> is a type of values to be selected.
 */
public abstract class SqlQuery<T, R extends List<T>> implements Function<JDOPersistenceManager, R>, IdSetter {

    TableResultList getUntypedResult(Query<?> sqlQuery) {
        var result = sqlQuery.executeList();
        var toBeReturned = new ArrayList<List<Object>>();

        result.forEach(o -> {
            var row = new ArrayList<>();
            if (o.getClass().isArray()) {
                row.addAll(asList(((Object[]) o)));
            } else {
                row.addAll(of(o));
            }
            toBeReturned.add(row);
        });

        return new TableResultList(toBeReturned) {
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
    @SuppressWarnings("unchecked")
    public static <T extends PersistableObject> SqlQuery<T, List<T>> bySql(Class<T> classOfRequestedValue,
                                                                  String sql,
                                                                  Object... parameters) {
        checkNotNull(classOfRequestedValue, "Class of objects to select should not be null value");
        checkArgument(isNotBlank(sql), "Sql query should not be a blank string");
        checkArgument(nonNull(parameters), "Query parameters should be defined as a value that differs from null");

        return new SqlQuery<>() {
            @Override
            public List<T> apply(JDOPersistenceManager jdoPersistenceManager) {
                var query = jdoPersistenceManager.newQuery("javax.jdo.query.SQL", sql);

                if (parameters.length > 0) {
                    query.setParameters(parameters);
                }

                query.setClass(classOfRequestedValue);
                var list = query.executeList();
                var toReturn =  new ListOfPersistentObjects<>(jdoPersistenceManager.detachCopyAll(list)) {
                };

                setRealIds(list, toReturn);
                return (List<T>) toReturn;
            }
        };
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
    public static SqlQuery<List<Object>, TableResultList> bySql(String sql, Object... parameters) {
        checkArgument(isNotBlank(sql), "Sql query should not be a blank string");
        checkArgument(nonNull(parameters), "Query parameters should be defined as a value that differs from null");

        return new SqlQuery<>() {
            @Override
            public TableResultList apply(JDOPersistenceManager jdoPersistenceManager) {
                var query = jdoPersistenceManager.newQuery("javax.jdo.query.SQL", sql);

                if (parameters.length > 0) {
                    query.setParameters(parameters);
                }

                return getUntypedResult(query);
            }
        };
    }
}
