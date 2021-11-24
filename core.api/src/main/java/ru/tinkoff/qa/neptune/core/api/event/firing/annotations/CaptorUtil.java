package ru.tinkoff.qa.neptune.core.api.event.firing.annotations;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.ArrayCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.MapCaptor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;
import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class CaptorUtil {

    private static final List<Captor> CAPTORS = initCaptors();


    private static List<Captor> initCaptors() {
        var iterator = load(Captor.class).iterator();
        Iterable<Captor> iterable = () -> iterator;
        var result = StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(toCollection(LinkedList::new));

        result.addFirst(new ArrayCaptor());
        result.addFirst(new CollectionCaptor());
        result.addFirst(new MapCaptor());
        return result;
    }

    private static List<Captor<Object,Object>> filterCaptors(Predicate<Captor> predicate) {
        return CAPTORS.stream().filter(predicate).map(c -> (Captor<Object,Object>) c).collect(toList());
    }

    public static List<Captor<Object, Object>> getCaptors(Class<? extends Captor>[] classes) {
        var result = new ArrayList<Captor<Object, Object>>();
        stream(classes)
                .forEach(cls -> {
                    var m = cls.getModifiers();
                    if (!isAbstract(m)) {
                        result.addAll(filterCaptors(c -> c.getClass().equals(cls)));
                        return;
                    }

                    result.addAll(filterCaptors(c -> cls.isAssignableFrom(c.getClass())));
                });
        return result;
    }
}
