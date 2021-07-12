package ru.tinkoff.qa.neptune.retrofit2.steps;

import okhttp3.Request;

import java.util.LinkedHashMap;
import java.util.Map;

final class RequestReader {

    Map<String, String> getRequestParameters(Request r) {
        if (r != null) {
            var result = new LinkedHashMap<String, String>();
            result.put("URL", r.url().toString());
            var h = r.headers();
            var headerMap = h.toMultimap();

            headerMap.forEach((k, v) -> result.put("Header " + k, String.join(",", v)));
            return result;
        }

        return null;
    }
}
