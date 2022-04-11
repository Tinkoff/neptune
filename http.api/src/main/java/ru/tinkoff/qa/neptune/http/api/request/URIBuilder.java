package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

final class URIBuilder {

    private final URI original;
    private final QueryBuilder builder;
    private final String path;

    URIBuilder(URI original, QueryBuilder builder, String path) {
        this.original = returnURIIfValid(original);
        this.builder = builder;
        this.path = path;
    }

    private static URI returnURIIfValid(URI uri) {
        var result = true;
        var reasons = new StringBuilder();


        result = ofNullable(uri.getScheme())
                .map(s -> {
                    var scheme = s.toLowerCase(Locale.US);

                    if (!(scheme.equals("https") || scheme.equals("http"))) {
                        reasons.append("invalid URI scheme ").append(scheme).append(";");
                        return false;
                    }

                    return true;
                })
                .orElseGet(() -> {
                    reasons.append("empty URI scheme").append(";");
                    return false;
                });

        if (uri.getHost() == null) {
            reasons.append("empty host URI").append(";");
            result = false;
        }

        if (!result) {
            throw new IllegalArgumentException("Invalid URI " + uri + ". " + reasons);
        }

        return uri;
    }

    URI build() {
        var scheme = original.getScheme();
        var userInfo = original.getUserInfo();
        var host = original.getHost();
        var port = original.getPort();
        var oldPath = original.getPath();
        var oldQuery = original.getQuery();
        var fragment = original.getFragment();

        String newPath;
        if (isBlank(path)) {
            newPath = oldPath;
        } else {
            newPath = isBlank(oldPath) ?
                    path
                    : (oldPath + "/" + path).replace("//", "/");
        }

        if (isNotBlank(newPath) && !newPath.startsWith("/")) {
            newPath = "/" + newPath;
        }

        var additionalQuery = builder.buildForm();
        String newQuery;
        if (isBlank(additionalQuery)) {
            newQuery = oldQuery;
        } else {
            newQuery = isBlank(oldQuery) ? additionalQuery : oldQuery + "&" + additionalQuery;
        }

        try {
            var resultURI = new URI(scheme, userInfo, host, port, null, null, null).toString();

            resultURI = resultURI + newPath;

            if (isNotBlank(newQuery)) {
                resultURI = resultURI + "?" + newQuery;
            }

            if (isNotBlank(fragment)) {
                resultURI = resultURI + "#" + fragment;
            }

            return new URI(resultURI);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
