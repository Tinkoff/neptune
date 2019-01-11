package ru.tinkoff.qa.neptune.data.base.api.query;

import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.Query;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.String.format;
import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This function builds a typed SQL query.
 *
 * @param <T> type of objects to be selected.
 */
public final class SQLQueryBuilderFunction<T> implements Function<DataBaseStepContext, Query<T>> {

    private final String sql;
    private final Class<T> type;

    private SQLQueryBuilderFunction(String sql, Class<T> type) {
        checkArgument(!isBlank(sql), "SQL query should differ from null/empty string.");
        this.sql = sql;
        this.type = type;
    }

    /**
     * Creates an instance of function that builds SQL query. It is supposed that query should look like
     * {@code "SELECT FIELD1, FIELD2 FROM TABLE"} etc.
     *
     * @param sql a SELECT-query to perform.
     * @return a new instance of {{@link SQLQueryBuilderFunction}}
     */
    public static SQLQueryBuilderFunction<List<Object>> bySQL(String sql) {
        return new SQLQueryBuilderFunction<>(sql, null);
    }

    /**
     * Creates an instance of function that builds typed SQL query. It is supposed that query should look like
     * {@code "SELECT * FROM TABLE"} etc.
     *
     * @param tClass is a class of objects to be selected.
     * @param sql a SELECT-query to perform.
     * @param <T> is a type of objects to be selected.
     * @return a new instance of {{@link SQLQueryBuilderFunction}}
     */
    public static <T extends PersistableObject> SQLQueryBuilderFunction<T> byTypedSQL(Class<T> tClass, String sql) {
        return new SQLQueryBuilderFunction<>(sql, tClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Query<T> apply(DataBaseStepContext dataBaseSteps) {
        var manager = dataBaseSteps.getCurrentPersistenceManager();
        var query = manager.newQuery("javax.jdo.query.SQL", sql);
        ofNullable(type).ifPresent(query::setClass);

        return (Query<T>) newProxyInstance(getSystemClassLoader(),
                new Class[] {Query.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("toString") && method.getParameterTypes().length == 0) {
                        return format("SQL query: %s", sql);
                    }
                    return method.invoke(query, args);
                });
    }

    Class<?> getTypeOfRequiredValue() {
        return type;
    }
}
