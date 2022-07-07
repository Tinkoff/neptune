package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.Response;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static java.lang.String.join;

@Description("Response")
public final class ResponseCaptor extends StringCaptor<Response> {

    @Override
    public Response getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof Response) {
            return (Response) toBeCaptured;
        }
        return null;
    }

    @Override
    public StringBuilder getData(Response caught) {
        var sb = new StringBuilder();
        sb.append("Protocol: ").append(caught.protocol()).append("\r\n")
                .append("Code: ").append(caught.code()).append("\r\n")
                .append("Message: ").append(caught.message()).append("\r\n")
                .append("URL: ").append(caught.request().url()).append("\r\n")
                .append("Is redirect: ").append(caught.isRedirect()).append("\r\n");

        var headers = caught.headers().toMultimap();
        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> sb.append(k).append(": ").append(join(",", v)).append("\r\n"));
        }

        return sb;
    }
}
