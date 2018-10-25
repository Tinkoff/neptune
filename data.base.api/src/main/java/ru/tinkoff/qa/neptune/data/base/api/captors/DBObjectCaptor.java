package ru.tinkoff.qa.neptune.data.base.api.captors;

import com.google.gson.stream.JsonWriter;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static java.io.File.createTempFile;
import static java.lang.String.format;

public class DBObjectCaptor extends FileCaptor<PersistableObject> {

    public void capture(PersistableObject caught, String message) {
        super.capture(caught, format("Received object from the DB. Step: '%s'", message));
    }

    @Override
    protected File getData(PersistableObject caught) {
        try {
            var json = createTempFile("persistable", UUID.randomUUID().toString() + ".json");
            var writer = new JsonWriter(new FileWriter(json));
            writer.setHtmlSafe(true);
            writer.beginObject()
                    .name(caught.getClass().getSimpleName().toLowerCase())
                    .jsonValue(caught.toString()).endObject().close();
            json.deleteOnExit();
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Class<PersistableObject> getTypeToBeCaptured() {
        return PersistableObject.class;
    }
}
