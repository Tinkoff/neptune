package ru.tinkoff.qa.neptune.data.base.api.queries.sql;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.asList;
import static java.util.List.copyOf;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class SqlQuery<T> implements Function<JDOPersistenceManager, List<T>> {

    private final String sql;
    private final Class<T> classOfRequestedValue;

    public SqlQuery(String sql, Class<T> classOfRequestedValue) {
        checkArgument(isNotBlank(sql), "Sql query should not be a blank string");
        checkArgument(nonNull(classOfRequestedValue), "Please define a class of values to be returned");
        this.classOfRequestedValue = classOfRequestedValue;
        this.sql = sql;
    }

    private static List<List<Object>> getUntypedResult(Query<?> sqlQuery) {
        var result = sqlQuery.executeList();
        var toBeReturned = new ArrayList<List<Object>>();

        result.forEach(o -> {
            if (o.getClass().isArray()) {
                toBeReturned.add(copyOf(asList(((Object[]) o))));
            } else {
                toBeReturned.add(of(o));
            }
        });

        return toBeReturned;
    }

    public static <T extends PersistableObject> SqlQuery<T> bySql(Class<T> classOfRequestedValue, String sql) {
        return new SqlQuery<>(sql, classOfRequestedValue);
    }

    public static SqlQuery<Object> bySql(String sql) {
        return new SqlQuery<>(sql, Object.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> apply(JDOPersistenceManager jdoPersistenceManager) {
        var query = jdoPersistenceManager.newQuery("javax.jdo.query.SQL", sql);

        if (PersistableObject.class.isAssignableFrom(classOfRequestedValue)) {
            query.setClass(classOfRequestedValue);
            return query.executeList();
        }
        else {
            return (List<T>) getUntypedResult(query);
        }
    }
}
