package ru.tinkoff.qa.neptune.spring.mock.mvc.captors;

import org.springframework.mock.web.MockHttpServletResponse;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Description("Response")
public final class ResponseStringCaptor extends StringCaptor<MockHttpServletResponse> {

    @Override
    public MockHttpServletResponse getCaptured(Object toBeCaptured) {
        if (!(toBeCaptured instanceof MockHttpServletResponse)) {
            return null;
        }

        return (MockHttpServletResponse) toBeCaptured;
    }

    @Override
    public StringBuilder getData(MockHttpServletResponse caught) {
        var result = new StringBuilder()
                .append("Status code: ")
                .append(caught.getStatus())
                .append("\r\n");

        var names = caught.getHeaderNames();
        names.forEach(s -> result.append(s).append(": ")
                .append(String.join(";", caught.getHeaders(s)))
                .append("\r\n"));

        if (isNotBlank(caught.getRedirectedUrl())) {
            result.append("Redirected: ")
                    .append(caught.getRedirectedUrl())
                    .append("\r\n");
        }

        if (isNotBlank(caught.getForwardedUrl())) {
            result.append("Forwarded: ")
                    .append(caught.getForwardedUrl())
                    .append("\r\n");
        }

        if (isNotBlank(caught.getErrorMessage())) {
            result.append("Error message: ")
                    .append(caught.getStatus())
                    .append("\r\n");
        }

        return result;
    }
}
