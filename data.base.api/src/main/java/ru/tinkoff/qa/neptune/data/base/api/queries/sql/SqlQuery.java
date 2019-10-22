package ru.tinkoff.qa.neptune.data.base.api.queries.sql;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.ListOfDataBaseObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.copyOf;
import static java.util.List.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class SqlQuery<T> implements Function<JDOPersistenceManager, List<T>> {

    private final String sql;
    private final Class<T> classOfRequestedValue;

    private SqlQuery(String sql, Class<T> classOfRequestedValue) {
        checkArgument(isNotBlank(sql), "Sql query should not be a blank string");
        this.classOfRequestedValue = classOfRequestedValue;
        this.sql = sql;
    }

    private List<List<Object>> getUntypedResult(Query<?> sqlQuery) {
        var result = sqlQuery.executeList();
        var toBeReturned = new ArrayList<List<Object>>();

        result.forEach(o -> {
            if (o.getClass().isArray()) {
                toBeReturned.add(copyOf(asList(((Object[]) o))));
            } else {
                toBeReturned.add(of(o));
            }
        });

        return new ListOfDataBaseObjects<>(toBeReturned) {
            public String toString() {
                return format("%s resulted row/rows selected by sql query %s", size(), sql);
            }
        };
    }

    public static <T extends PersistableObject> SqlQuery<T> bySql(Class<T> classOfRequestedValue, String sql) {
        return new SqlQuery<>(sql, classOfRequestedValue);
    }

    public static SqlQuery<List<Object>> bySql(String sql) {
        return new SqlQuery<>(sql, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> apply(JDOPersistenceManager jdoPersistenceManager) {
        var query = jdoPersistenceManager.newQuery("javax.jdo.query.SQL", sql);

        if (classOfRequestedValue == null) {
            return (List<T>) getUntypedResult(query);
        }

        query.setClass(classOfRequestedValue);
        return new ListOfDataBaseObjects<>(query.executeList()) {
            public String toString() {
                return format("%s objects/object selected by sql query %s", size(), sql);
            }
        };
    }
}
