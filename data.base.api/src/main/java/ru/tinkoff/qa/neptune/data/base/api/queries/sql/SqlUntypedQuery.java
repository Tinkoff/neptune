package ru.tinkoff.qa.neptune.data.base.api.queries.sql;

import org.datanucleus.api.jdo.JDOPersistenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.Optional.ofNullable;

@Deprecated(forRemoval = true)
@SuppressWarnings("unchecked")
public class SqlUntypedQuery extends SqlQuery<List<Object>, List<List<Object>>> {

    SqlUntypedQuery(String sql, Object parameters) {
        super(sql, parameters);
    }

    @Override
    public List<List<Object>> execute(JDOPersistenceManager jdoPersistenceManager) {
        var query = jdoPersistenceManager.newQuery("javax.jdo.query.SQL", sql);
        query.addExtension("datanucleus.query.resultCacheType", "none");
        query.setIgnoreCache(true);

        ofNullable(parameters).ifPresent(o -> {
            var cls = o.getClass();
            if (cls.isArray()) {
                var array = (Object[]) o;
                if (array.length > 0) {
                    query.setParameters(array);
                }
                return;
            }
            query.setNamedParameters((Map<String, ?>) o);
        });

        var result = query.executeList();
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

        return toBeReturned;
    }
}
