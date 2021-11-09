package ru.tinkoff.qa.neptune.data.base.api.captors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;
import java.util.UUID;

import static java.io.File.createTempFile;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.FileUtils.writeStringToFile;

@Deprecated(forRemoval = true)
public abstract class DBCaptor<T> extends FileCaptor<T> {

    @Override
    public File getData(T caught) {
        try {
            var json = createTempFile("persistable", UUID.randomUUID() + ".json");
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
