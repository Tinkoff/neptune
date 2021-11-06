package ru.tinkoff.qa.neptune.data.base.api.queries.sql;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.KeepResultPersistent;
import ru.tinkoff.qa.neptune.data.base.api.queries.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Deprecated(forRemoval = true)
public abstract class SqlQuery<T, S extends List<T>> implements Query<T, S> {

    final String sql;
    final Object parameters;

    SqlQuery(String sql, Object parameters) {
        checkArgument(isNotBlank(sql), "Sql query should not be black or null string");
        this.sql = sql;
        checkNotNull(parameters);
        this.parameters = parameters;
    }

    /**
     * Constructs an object that performs sql query to select list of stored objects.
     *
     * @param classOfRequestedValue is a type of objects to be returned
     * @param sql                   is an sql query. Parameter mask ({@code ?}) is supported. It is important!:
     *                              <p>Sql query should be defined as below</p>
     *                              {@code 'Select * from Persons...'}
     * @param parameters            is an array of query parameters. It is necessary to define for queries as below
     *                              <p>
     *                              {@code 'Select * from Persons where Some_Field=?'}
     *                              </p>
     * @param <T>                   is a type of retrieved objects
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery}
     */
    public static <T extends PersistableObject> SqlQuery<T, List<T>> bySql(Class<T> classOfRequestedValue,
                                                                           String sql,
                                                                           KeepResultPersistent keepResultPersistent,
                                                                           Object... parameters) {
        return new SqlTypedQuery<>(sql, classOfRequestedValue, parameters, keepResultPersistent);
    }

    /**
     * Constructs an object that performs sql query to select list of stored objects.
     *
     * @param classOfRequestedValue is a type of objects to be returned
     * @param sql                   is an sql query. Parameter naming is supported. It is important!:
     *                              <p>Sql query should be defined as below</p>
     *                              {@code 'Select * from Persons...'}
     * @param parameters            is a map of parameter names and values. It is necessary to define for queries as below
     *                              <p>
     *                              {@code 'Select * from Persons where Some_Field=:paramName'}
     *                              </p>
     * @param <T>                   is a type of retrieved objects
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery}
     */
    public static <T extends PersistableObject> SqlQuery<T, List<T>> bySql(Class<T> classOfRequestedValue,
                                                                           String sql,
                                                                           KeepResultPersistent keepResultPersistent,
                                                                           Map<String, ?> parameters) {
        return new SqlTypedQuery<>(sql, classOfRequestedValue, parameters, keepResultPersistent);
    }

    /**
     * Constructs an object that performs sql query to select list of lists. Each list item contains
     * raw objects as they actually stored without any deserialization.
     *
     * @param sql        is an sql query. Parameter mask ({@code ?}) is supported.
     * @param parameters is an array of query parameters. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=?'}
     *                   </p>
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery}
     */
    public static SqlQuery<List<Object>, List<List<Object>>> bySql(String sql, Object... parameters) {
        return new SqlUntypedQuery(sql, parameters);
    }

    /**
     * Constructs an object that performs sql query to select list of lists. Each list item contains
     * raw objects as they actually stored without any deserialization.
     *
     * @param sql        is an sql query. Parameter naming is supported.
     * @param parameters is a map of parameter names and values. It is necessary to define for queries as below
     *                   <p>
     *                   {@code 'Select * from Persons where Some_Field=:paramName'}
     *                   </p>
     * @return new {@link ru.tinkoff.qa.neptune.data.base.api.queries.sql.SqlQuery}
     */
    public static SqlQuery<List<Object>, List<List<Object>>> bySql(String sql, Map<String, ?> parameters) {
        return new SqlUntypedQuery(sql, parameters);
    }

    public String toString() {
        var stringBuilder = new StringBuilder("Sql ").append("'").append(sql).append("'");
        ofNullable(parameters).ifPresent(o -> {
            stringBuilder.append(" with parameters ");
            var cls = o.getClass();
            if (cls.isArray()) {
                stringBuilder.append(Arrays.toString((Object[]) o));
            } else {

                stringBuilder.append(o);
            }
        });

        return stringBuilder.toString();
    }
}
