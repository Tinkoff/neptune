package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.http.api.properties.end.point.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;

final class URIFactory {

    private URIFactory() {
        super();
    }

    static URI toURI(URL url) {
        checkNotNull(url);
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    static URI toURI(String uriOrPart) {
        checkNotNull(uriOrPart);

        if (isBlank(uriOrPart)) {
            return prepareValidURI(EMPTY);
        }

        var result = getURIIfValid(uriOrPart);
        if (nonNull(result)) {
            return returnURIIfValid(result);
        }

        return prepareValidURI(uriOrPart);
    }

    private static URI getURIIfValid(String uriOrPart) {
        try {
            var uri = URI.create(uriOrPart);
            if (nonNull(uri.getScheme())) {
                return uri;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static URI prepareValidURI(String fragment) {
        var propertyValue = DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.get();
        checkState(nonNull(propertyValue), "Value of the property "
                + DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getName()
                + " is not defined");

        var uri = toURI(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.get());

        if (isBlank(fragment)) {
            return uri;
        }

        if (fragment.startsWith("/") || fragment.startsWith("?") || fragment.startsWith("#")) {
            var resultURI = URI.create(uri + fragment);
            return returnURIIfValid(resultURI);
        }

        var resultURI = URI.create(uri + "/" + fragment);
        return returnURIIfValid(resultURI);
    }

    private static URI returnURIIfValid(URI uri) {
        var result = true;
        var reasons = new StringBuilder();

        result = ofNullable(uri.getScheme())
                .map(s -> {
                    s = s.toLowerCase(Locale.US);
                    if (!(s.equals("https") || s.equals("http"))) {
                        reasons.append("invalid URI scheme ").append(s).append(";");
                        return false;
                    }
                    return true;
                })
                .orElseGet(() -> {
                    reasons.append("URI with undefined scheme;");
                    return false;
                });

        if (uri.getHost() == null) {
            reasons.append("empty host URI ").append(uri).append(";");
            result = false;
        }

        if (!result) {
            throw new IllegalArgumentException("Invalid URI " + uri + ". " + reasons);
        }

        return uri;
    }
}
