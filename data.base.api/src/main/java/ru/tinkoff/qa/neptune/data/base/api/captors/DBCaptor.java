package ru.tinkoff.qa.neptune.data.base.api.captors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.writeStringToFile;

abstract class DBCaptor<T> extends FileCaptor<T> {

    public DBCaptor(String message) {
        super(message);
    }

    @Override
    public File getData(T caught) {
        try {
            var json = createTempFile("persistable", UUID.randomUUID().toString() + ".json");
            writeStringToFile(json, new GsonBuilder()
                            .setPrettyPrinting()
                            .setExclusionStrategies(new DataNucleousEnhancedExclusionStrategy())
                            .create()
                            .toJson(new Gson().toJsonTree(caught)),
                    defaultCharset(), true);
            json.deleteOnExit();
            return json;
        } catch (Exception e) {
            return null;
        }
    }
}
