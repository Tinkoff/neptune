package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.enhancement.Persistable;
import org.datanucleus.identity.ObjectId;
import ru.tinkoff.qa.neptune.core.api.steps.LoggableObject;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import java.util.Objects;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static javax.jdo.JDOHelper.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This abstract class is designed to mark persistable classes.
 */
public abstract class PersistableObject extends OrmObject implements Cloneable, LoggableObject {

    public static final String[] DATA_NUCLEOUS_ENHANCED_FIELDS = new String[]{"dnStateManager", "dnFlags", "dnDetachedState", "$"};

    @NotPersistent
    private Object $;

    /**
     * Returns table name
     *
     * @return name of the table or name of the class when this object is not received from the data store
     */
    public static <T extends PersistableObject> String getTable(Class<T> getFrom) {
        var persistenceCapable = getFrom.getAnnotation(PersistenceCapable.class);
        return ofNullable(persistenceCapable)
                .map(persistenceCapable1 -> {
                    var table = persistenceCapable1.table();
                    if (isNotBlank(table)) {
                        return table;
                    }
                    return format("class:%s <no table defined>", getFrom.getSimpleName());
                })
                .orElseGet(() -> format("class:%s <no table defined>", getFrom.getSimpleName()));
    }

    @Override
    public String toString() {
        var name = getTable(this.getClass());

        return ofNullable(getRealId()).map(o -> {
            Object id;

            if (ObjectId.class.isAssignableFrom(o.getClass())) {
                id = ((ObjectId) o).getKey();
            }
            else {
                id = o;
            }

            return format("%s [%s]", name, id);
        }).orElseGet(() -> format("%s <no id>", name));
    }

    public Object clone() {
        try {
            var cloned = super.clone();
            var thisClass = this.getClass();

            var dnStateManager = thisClass.getDeclaredField("dnStateManager");
            dnStateManager.setAccessible(true);
            dnStateManager.set(cloned, null);

            var dnFlags = thisClass.getDeclaredField("dnFlags");
            dnFlags.setAccessible(true);
            dnFlags.set(cloned, Byte.valueOf("0"));

            var dnDetachedState = thisClass.getDeclaredField("dnDetachedState");
            dnDetachedState.setAccessible(true);
            dnDetachedState.set(cloned, null);

            ((PersistableObject) cloned).setRealId(null);

            return cloned;
        } catch (CloneNotSupportedException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        var thisObj = this;
        return ofNullable(obj)
                .map(o -> {
                    if (thisObj == o) {
                        return true;
                    }

                    var thisClazz = thisObj.getClass();

                    if (!Objects.equals(thisClazz, o.getClass())) {
                        return false;
                    }

                    if (parseBoolean(thisClazz.getAnnotation(PersistenceCapable.class).embeddedOnly())) {
                        return super.equalsByFields(o, DATA_NUCLEOUS_ENHANCED_FIELDS);
                    }

                    var isThisPersistent = isPersistent(thisObj);
                    if (!Objects.equals(isThisPersistent, isPersistent(o))) {
                        return false;
                    }

                    if (isThisPersistent) {
                        return Objects.equals(((Persistable) thisObj).dnGetObjectId(),
                                ((Persistable) o).dnGetObjectId());
                    }

                    var isThisDetached = isDetached(thisObj);
                    if (!Objects.equals(isThisDetached, isDetached(o))) {
                        return false;
                    }

                    if (isThisDetached) {
                        var idToCheck = ((PersistableObject) o).getRealId();
                        var thisId = thisObj.getRealId();
                        if (thisId == null && idToCheck == null) {
                            return super.equalsByFields(o, DATA_NUCLEOUS_ENHANCED_FIELDS);
                        }

                        return Objects.equals(thisObj.getRealId(),
                                ((PersistableObject) o).getRealId());
                    }

                    var isThisDeleted = isDeleted(thisObj);
                    if (!Objects.equals(isThisDeleted, isDetached(o))) {
                        return false;
                    }

                    return super.equalsByFields(o, DATA_NUCLEOUS_ENHANCED_FIELDS);
                })
                .orElse(false);
    }

    public Object getRealId() {
        if (isPersistent(this)) {
            return ((Persistable) this).dnGetObjectId();
        }
        return $;
    }

    void setRealId(Object $) {
        this.$ = $;
    }
}
