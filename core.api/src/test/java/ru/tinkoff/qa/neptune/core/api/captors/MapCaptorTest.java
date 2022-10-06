package ru.tinkoff.qa.neptune.core.api.captors;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.MapCaptor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.mapEntry;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.mapInOrder;

public class MapCaptorTest {

    @Test
    public void nullMapTest() {
        var value = new MapCaptor().getCaptured(null);
        assertThat(value, nullValue());
    }

    @Test
    public void emptyMapTest() {
        var value = new MapCaptor().getCaptured(Map.of());
        assertThat(value, nullValue());
    }

    @Test
    public void notMapTest() {
        var value = new MapCaptor().getCaptured(new Object());
        assertThat(value, nullValue());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void mapTest() {
        var map = new LinkedHashMap<>();
        map.put(null, "value");
        map.put("key1", null);
        map.put(new Object(), "value 2");
        map.put("key2", new Object());
        map.put(List.of(1, 2, 3, 4), "value 3");
        map.put("key3", List.of(1, 2, 3, 4));
        map.put(Map.of(1, 2, 3, 4), "value 4");
        map.put("key4", Map.of(1, 2, 3, 4));
        map.put(new int[]{1, 2, 3, 4}, "value 4");
        map.put("key5", new int[]{1, 2, 3, 4});
        map.put(new Object(), new Object());

        Map<Object, Object> value = (Map<Object, Object>) new MapCaptor().getCaptured(map);
        assertThat(value,
                mapInOrder(
                    mapEntry(nullValue(), "value"),
                    mapEntry("key1", nullValue()),
                    mapEntry(not(nullValue()), "value 2"),
                    mapEntry("key2", not(nullValue())),
                    mapEntry(List.of(1, 2, 3, 4), "value 3"),
                    mapEntry("key3", List.of(1, 2, 3, 4)),
                    mapEntry(Map.of(1, 2, 3, 4), "value 4"),
                    mapEntry("key4", Map.of(1, 2, 3, 4)),
                    mapEntry(new int[]{1, 2, 3, 4}, "value 4"),
                    mapEntry("key5", new int[]{1, 2, 3, 4}),
                    mapEntry(instanceOf(Object.class), instanceOf(Object.class))
                ));
    }

    @Test
    public void resultedValueTest() {
        var map = new LinkedHashMap<>();
        map.put(null, "value");
        map.put("key1", null);
        map.put(new Object(), "value 2");
        map.put("key2", new Object());
        map.put(List.of(1, 2, 3, 4), "value 3");
        map.put("key3", List.of(1, 2, 3, 4));
        map.put(Map.of(1, 2, 3, 4), "value 4");
        map.put("key4", Map.of(1, 2, 3, 4));
        map.put(new int[]{1, 2, 3, 4}, "value 4");
        map.put("key5", new int[]{1, 2, 3, 4});
        map.put(new Object(), new Object());

        var value = new MapCaptor().getCaptured(map);
        var result = new MapCaptor().getData(value).toString().split("\r\n");
        assertThat(result, arrayContaining(
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString()),
            not(emptyOrNullString())
        ));
    }
}
