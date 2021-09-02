package ru.tinkoff.qa.neptune.data.base.api.queries.sql;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.KeepResultPersistent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
class SqlTypedQuery<T extends PersistableObject> extends SqlQuery<T, List<T>> implements IdSetter {

    private final Class<T> classOfRequestedValue;
    private final KeepResultPersistent keepResultPersistent;

    SqlTypedQuery(String sql, Class<T> classOfRequestedValue, Object parameters, KeepResultPersistent keepResultPersistent) {
        super(sql, parameters);
        this.keepResultPersistent = keepResultPersistent;
        checkNotNull(classOfRequestedValue);
        this.classOfRequestedValue = classOfRequestedValue;
    }

    @Override
    public List<T> execute(JDOPersistenceManager jdoPersistenceManager) {
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

        query.setClass(classOfRequestedValue);
        var list = query.executeList();

        List<T> toReturn;
        if (!keepResultPersistent.toKeepOnPersistent()) {
            toReturn = new ArrayList<T>(jdoPersistenceManager.detachCopyAll(list)) {
            };
            setRealIds(list, toReturn);
        } else {
            toReturn = new ArrayList<T>(list) {
            };
        }

        return toReturn;
    }
}
