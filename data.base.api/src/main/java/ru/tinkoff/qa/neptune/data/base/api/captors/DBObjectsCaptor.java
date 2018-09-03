package ru.tinkoff.qa.neptune.data.base.api.captors;

import com.google.gson.stream.JsonWriter;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.data.base.api.PersistableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static java.io.File.createTempFile;
import static java.lang.String.format;

@SuppressWarnings("unchecked")
public class DBObjectsCaptor extends FileCaptor<PersistableList> {

    public void capture(PersistableList caught, String message) {
        super.capture(caught, format("Received objects from the DB. Step: '%s'", message));
    }

    @Override
    protected File getData(PersistableList caught) {
        try {
            File json = createTempFile("persistableList", UUID.randomUUID().toString() + ".json");
            JsonWriter writer = new JsonWriter(new FileWriter(json));
            writer.setHtmlSafe(true);
            writer.beginArray();

            caught.forEach(o -> {
                try {
                    writer.beginObject()
                            .name(o.getClass().getSimpleName().toLowerCase())
                            .jsonValue(o.toString()).endObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.endArray().close();
            json.deleteOnExit();
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Class<PersistableList> getTypeToBeCaptured() {
        return PersistableList.class;
    }
}
