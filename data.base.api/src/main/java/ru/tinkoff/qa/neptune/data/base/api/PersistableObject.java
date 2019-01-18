package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.enhancement.Persistable;
import ru.tinkoff.qa.neptune.core.api.steps.LoggableObject;
import ru.tinkoff.qa.neptune.data.base.api.captors.IsQueryCaptured;

import javax.jdo.annotations.NotPersistent;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

/**
 * This abstract class is designed to mark persistable classes.
 */
public abstract class PersistableObject extends OrmObject implements Cloneable, LoggableObject, GotByQuery {

    @NotPersistent
    private transient Object query;

    @Override
    public String toString() {
        var name = this.getClass().getName();
        if (!Persistable.class.isAssignableFrom(this.getClass())) {
            return format("Not stored data base element of type %s", name);
        }

        return ofNullable(((Persistable) this).dnGetObjectId())
                .map(o -> format("Stored item of type %s Id = [%s]", name, o))
                .orElseGet(() -> format("Stored item of type %s without id", name));
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        var otherClass = obj.getClass();
        if (!PersistableObject.class.isAssignableFrom(otherClass)) {
            return false;
        }

        var thisClass  = this.getClass();

        if (!thisClass.equals(otherClass)) {
            return false;
        }

        if (!Persistable.class.isAssignableFrom(thisClass) && !Persistable.class.isAssignableFrom(otherClass)) {
            return super.equals(obj);
        }

        if ((!Persistable.class.isAssignableFrom(thisClass) && Persistable.class.isAssignableFrom(otherClass)) ||
                (Persistable.class.isAssignableFrom(thisClass) && !Persistable.class.isAssignableFrom(otherClass))) {
            return false;
        }

        return ((Persistable) this).dnGetObjectId().equals(((Persistable) obj).dnGetObjectId());
    }

    public Object getQuery() {
        return query;
    }

    PersistableObject setQuery(Object query) {
        this.query = ofNullable(query).map(QueryInfo::new).orElse(null);
        return this;
    }

    private static class QueryInfo implements IsQueryCaptured, GotByQuery {
        private final Object query;
        private boolean isCaptured;

        private QueryInfo(Object query) {
            this.query = query;
        }

        @Override
        public Object getQuery() {
            return query;
        }

        @Override
        public boolean isCaptured() {
            return isCaptured;
        }

        @Override
        public void setCaptured() {
            isCaptured = true;
        }

        @Override
        public String toString() {
            return query.toString();
        }
    }
}
