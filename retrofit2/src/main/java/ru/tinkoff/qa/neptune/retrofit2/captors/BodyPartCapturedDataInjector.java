package ru.tinkoff.qa.neptune.retrofit2.captors;

import okhttp3.MultipartBody;
import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;

import java.util.List;

import static java.lang.String.join;

final class BodyPartCapturedDataInjector implements CapturedDataInjector<List<MultipartBody.Part>> {

    @Override
    public void inject(List<MultipartBody.Part> toBeInjected, String message) {

        int i = 0;
        for (var p : toBeInjected) {
            i++;
            var sb = new StringBuilder();
            sb.append("Part ").append(i);

            var headers = p.headers();

            if (headers != null && !headers.toMultimap().isEmpty()) {
                sb.append("\r\n");
                headers.toMultimap().forEach((k, v) -> sb.append(k).append(": ").append(join(",", v)).append("\r\n"));
            }

            var commonCaptor = new CommonRequestBodyCaptor(sb.toString());
            var captured = commonCaptor.getCaptured(p.body());
            if (captured != null) {
                commonCaptor.capture(captured);
            }
        }
    }
}
