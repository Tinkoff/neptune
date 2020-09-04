package ru.tinkoff.qa.neptune.http.api.request;

import ru.tinkoff.qa.neptune.http.api.request.form.FormBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

final class QueryBuilder extends FormBuilder {

    protected void addParameter(String name,
                                boolean toExpand,
                                FormValueDelimiters delimiter,
                                boolean allowReserved,
                                Object... values) {
        super.addParameter(name, toExpand, delimiter, allowReserved, values);
    }

    URI appendURI(URI uri) {
        checkNotNull(uri);
        var query = buildForm();

        if (isBlank(query)) {
            return uri;
        } else {
            var path = uri.getPath();
            path = isBlank(path) ? "/" : path;
            var resultQuery = uri.getQuery();
            resultQuery = isBlank(resultQuery) ? query : resultQuery + "&" + query;
            try {
                var newURI = new URI(uri.getScheme(),
                        uri.getUserInfo(),
                        uri.getHost(),
                        uri.getPort(),
                        path, null, null).toString();

                if (isNotBlank(resultQuery)) {
                    newURI = newURI + "?" + resultQuery;
                }

                if (isNotBlank(uri.getFragment())) {
                    newURI = newURI + "#" + uri.getFragment();
                }

                return new URI(newURI);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}