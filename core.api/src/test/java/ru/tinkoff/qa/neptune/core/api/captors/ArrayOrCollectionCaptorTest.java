package ru.tinkoff.qa.neptune.core.api.captors;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.ArrayCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ArrayOrCollectionCaptorTest {

    @Test
    public void nullValueTest() {
        var value = new ArrayCaptor().getCaptured(null);
        assertThat(value, nullValue());
    }

    @Test
    public void notArrayCapturedTest() {
        var value = new ArrayCaptor()
                .getCaptured(List.of());
        assertThat(value, nullValue());
    }

    @Test
    public void nullCollectionCaptor() {
        var value = new CollectionCaptor()
                .getCaptured(null);
        assertThat(value, nullValue());
    }

    @Test
    public void emptyCollectionCaptor() {
        var value = new CollectionCaptor()
                .getCaptured(List.of());
        assertThat(value, nullValue());
    }

    @Test
    public void collectionCaptor() {
        var value = new CollectionCaptor()
                .getCaptured(List.of(1, 2, 3));
        assertThat(value, contains(1, 2, 3));
    }

    @Test
    public void intArrayTest() {
        var value = new ArrayCaptor()
                .getCaptured(new int[]{1, 2, 3, 4});
        assertThat(value, contains(1, 2, 3, 4));
    }

    @Test
    public void floatArrayTest() {
        var value = new ArrayCaptor()
                .getCaptured(new float[]{1, 2, 3, 4});
        assertThat(value, contains(1F, 2F, 3F, 4F));
    }

    @Test
    public void byteArrayTest() {
        var value = new ArrayCaptor()
                .getCaptured(new byte[]{1, 2, 3, 4});
        assertThat(value, contains((byte) 1, (byte) 2, (byte) 3, (byte) 4));
    }

    @Test
    public void shortArrayTest() {
        var value = new ArrayCaptor()
                .getCaptured(new short[]{1, 2, 3, 4});
        assertThat(value, contains((short) 1, (short) 2, (short) 3, (short) 4));
    }

    @Test
    public void longArrayTest() {
        var value = new ArrayCaptor()
                .getCaptured(new long[]{1, 2, 3, 4});
        assertThat(value, contains(1L, 2L, 3L, 4L));
    }

    @Test
    public void doubleArrayTest() {
        var value = new ArrayCaptor()
                .getCaptured(new double[]{1, 2, 3, 4});
        assertThat(value, contains(1D, 2D, 3D, 4D));
    }

    @Test
    public void booleanArrayTest() {
        var value = new ArrayCaptor()
                .getCaptured(new boolean[]{true, false});
        assertThat(value, contains(true, false));
    }

    @Test
    public void charArrayTest() {
        var value = new ArrayCaptor()
                .getCaptured("ABCD".toCharArray());
        assertThat(value, contains('A', 'B', 'C', 'D'));
    }

    @Test
    public void arrayCaptureTest() {
        var object = new Object();
        var value = new ArrayCaptor()
            .getCaptured(new Object[]{
                null,
                2,
                true,
                "Hello",
                List.of(1, "ABC", true),
                Map.of("key1", "value"),
                object,
                new Object[]{1, 2, 3}
            });

        assertThat(value,
                contains(
                    null,
                    2,
                    true,
                    "Hello",
                    List.of(1, "ABC", true),
                    Map.of("key1", "value"),
                    object,
                    new Object[]{1, 2, 3}
                ));
    }

    @Test
    public void resultedValueTest() {
        var data = new ArrayCaptor()
                .getCaptured(new Object[]{
                        null,
                        2,
                        true,
                        "Hello",
                        List.of(1, "ABC", true),
                        Map.of("key1", "value"),
                        new Object(),
                        new Object[]{1, 2, 3}
                });

        var result = new ArrayCaptor().getData(data).toString().split("\r\n");
        assertThat(result, arrayContaining(
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
