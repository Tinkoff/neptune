package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.enhancement.Persistable;

import javax.jdo.annotations.PersistenceCapable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Boolean.parseBoolean;
import static java.util.Arrays.stream;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Deprecated(forRemoval = true)
public interface IdSetter {

    private static List<PersistableObject> getFlatListOfPersistableObjects(Collection<?> ts,
                                                                           LinkedList<PersistableObject> found) {
        ts.forEach(t -> {
            var fields = t.getClass().getDeclaredFields();
            stream(fields).forEach(field -> {
                var fieldName = field.getName();

                if (Objects.equals(fieldName, "dnStateManager") ||
                        Objects.equals(fieldName, "dnFlags") ||
                        Objects.equals(fieldName, "dnDetachedState") ||
                        Objects.equals(fieldName, "$")) {
                    return;
                }

                var type = field.getType();
                if (!Iterable.class.isAssignableFrom(type) && !type.isArray() && !PersistableObject.class.isAssignableFrom(type)) {
                    return;
                }

                field.setAccessible(true);
                try {
                    var value = field.get(t);

                    if (isNull(value)) {
                        return;
                    }

                    if (PersistableObject.class.isAssignableFrom(type)) {
                        if (parseBoolean(type.getAnnotation(PersistenceCapable.class).embeddedOnly())) {
                            return;
                        }

                        var toBePersisted = (PersistableObject) value;
                        if (found.contains(toBePersisted)) {
                            return;
                        }

                        found.addFirst(toBePersisted);
                        getFlatListOfPersistableObjects(List.of((PersistableObject) value), found);
                        return;
                    }

                    Stream<?> objectStream;
                    if (type.isArray()) {
                        objectStream = stream((Object[]) value);
                    } else {
                        objectStream = StreamSupport.stream(((Iterable<?>) value).spliterator(), false);
                    }

                    var toPersist = objectStream.filter(o -> {
                        if (isNull(o)) {
                            return false;
                        }

                        var clazz = o.getClass();
                        if (!PersistableObject.class.isAssignableFrom(clazz)) {
                            return false;
                        }

                        if (parseBoolean(clazz.getAnnotation(PersistenceCapable.class).embeddedOnly())) {
                            return false;
                        }

                        return found.contains(value);
                    }).map(o -> (PersistableObject) o).collect(toList());

                    toPersist.forEach(found::addFirst);
                    getFlatListOfPersistableObjects(toPersist, found);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        return found;
    }

    private void setRealIdsPrivate(List<?> objects, List<?> objects2) {
        for (int i = 0; i < objects.size(); i++) {
            var id = ((Persistable) objects.get(i)).dnGetObjectId();
            ((PersistableObject) objects2.get(i)).setRealId(id);
        }
    }

    default void setRealIds(List<?> objects, List<?> objects2) {
        var inner1 = getFlatListOfPersistableObjects(objects, new LinkedList<>());
        var inner2 = getFlatListOfPersistableObjects(objects2, new LinkedList<>());

        setRealIdsPrivate(inner1, inner2);
        setRealIdsPrivate(objects, objects2);
    }

    default void setRealId(Object object, Object object2) {
        setRealIds(of(object), of(object2));
    }
}
