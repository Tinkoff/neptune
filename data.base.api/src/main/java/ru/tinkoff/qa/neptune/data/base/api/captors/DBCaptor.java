package ru.tinkoff.qa.neptune.data.base.api.captors;

import com.google.gson.GsonBuilder;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.writeStringToFile;

abstract class DBCaptor<T> extends FileCaptor<T> {

    @Override
    protected File getData(T caught) {
        try {
            var json = createTempFile("persistable", UUID.randomUUID().toString() + ".json");
            writeStringToFile(json, new GsonBuilder().setPrettyPrinting().create().toJson(caught),
                    defaultCharset(), true);
            json.deleteOnExit();
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
