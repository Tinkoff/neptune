package ru.tinkoff.qa.neptune.data.base.api;

import ru.tinkoff.qa.neptune.core.api.steps.LoggableObject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This abstract class is designed to mark persistable classes.
 */
public abstract class PersistableObject extends OrmObject implements Cloneable, LoggableObject {

    @Override
    public String toString() {
        var name = fromTable();
        var id = getIdValue();

        return ofNullable(id).map(o -> format("%s %s", name, o))
                .orElseGet(() -> format("%s <no id>", name));
    }

    /**
     * Returns object id
     *
     * @return the value of id.
     */
    private Object getIdValue() {
        var fields = this.getClass().getDeclaredFields();
        var listOfPrimaryKeys = new ArrayList<>();

        var thisReference = this;
        stream(fields).forEach(f -> {
            var primary = f.getAnnotation(PrimaryKey.class);
            var persistent = f.getAnnotation(Persistent.class);
            var column = f.getAnnotation(Column.class);

            var isPrimary = (primary != null) || (persistent != null && Boolean.parseBoolean(persistent.primaryKey()));

            if (isPrimary) {
                var columnName = ofNullable(column)
                        .map(column1 -> {
                            var nameStr = column1.name();
                            if (isNotBlank(nameStr)) {
                                return nameStr;
                            }

                            return null;
                        })
                        .orElseGet(() -> ofNullable(persistent).map(persistent1 -> {
                            var nameStr = persistent1.name();

                            if (isNotBlank(nameStr)) {
                                return nameStr;
                            }

                            return null;
                        }).orElse(null));

                f.setAccessible(true);
                try {
                    var fieldValue = f.get(thisReference);
                    if (fieldValue == null) {
                        return;
                    }

                    ofNullable(columnName).ifPresentOrElse(s -> listOfPrimaryKeys.add(format("%s = [%s]", s, fieldValue)),
                            () -> listOfPrimaryKeys.add(fieldValue));
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        });

        if (listOfPrimaryKeys.size() == 0) {
            return null;
        }

        if (listOfPrimaryKeys.size() == 1) {
            return listOfPrimaryKeys.get(0);
        }

        return new ArrayList<>(listOfPrimaryKeys) {
            @Override
            public String toString() {
                return this.stream().map(Object::toString).collect(Collectors.joining(", "));
            }
        };
    }

    public Object clone() {
        try {
            var cloned =  super.clone();
            var thisClass = this.getClass();

            var dnStateManager  = thisClass.getDeclaredField("dnStateManager");
            dnStateManager.setAccessible(true);
            dnStateManager.set(cloned, null);

            var dnFlags  = thisClass.getDeclaredField("dnFlags");
            dnFlags.setAccessible(true);
            dnFlags.set(cloned, Byte.valueOf("0"));

            var dnDetachedState  = thisClass.getDeclaredField("dnDetachedState");
            dnDetachedState.setAccessible(true);
            dnDetachedState.set(cloned, null);

            return cloned;
        } catch (CloneNotSupportedException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj, "dnStateManager", "dnFlags", "dnDetachedState");
    }

    /**
     * Returns table name
     *
     * @return name of the table or name of the class when this object is not received from the data store
     */
    public String fromTable() {
        var persistenceCapable = this.getClass().getAnnotation(PersistenceCapable.class);
        var thisClass = this.getClass();
        return ofNullable(persistenceCapable)
                .map(persistenceCapable1 -> {
                    var table = persistenceCapable1.table();
                    if (isNotBlank(table)) {
                        return table;
                    }
                    return format("class:%s <no table defined>", thisClass.getSimpleName());
                })
                .orElseGet(() -> format("class:%s <no table defined>", thisClass.getSimpleName()));
    }
}
