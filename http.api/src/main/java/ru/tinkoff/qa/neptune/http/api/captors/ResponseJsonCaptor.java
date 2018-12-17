package ru.tinkoff.qa.neptune.http.api.captors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;

import static java.io.File.createTempFile;
import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FileUtils.writeStringToFile;

public class ResponseJsonCaptor extends FileCaptor<HttpResponse<String>> {

    public void capture(HttpResponse<String> caught, String message) {
        super.capture(caught, format("Received response. %s", message));
    }

    @Override
    protected File getData(HttpResponse<String> caught) {
        var uuid = randomUUID().toString();
        try {
            var json = createTempFile("json_response_body", uuid + ".json");
            writeStringToFile(json, new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(caught.body())),
                    defaultCharset(), true);
            json.deleteOnExit();
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public HttpResponse<String> getCaptured(Object toBeCaptured) {
        if (!HttpResponse.class.isAssignableFrom(toBeCaptured.getClass())) {
            return null;
        }

        HttpResponse<?> response = (HttpResponse<?>) toBeCaptured;
        return ofNullable(response.body()).map(o -> {
            if (!String.class.isAssignableFrom(o.getClass())) {
                return null;
            }

            String stringBody = String.valueOf(o);
            try {
                var mapper = new ObjectMapper();
                mapper.readTree(stringBody);
                return (HttpResponse<String>) response;
            } catch (IOException e) {
                return null;
            }

        }).orElse(null);
    }
}
