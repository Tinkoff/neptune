package ru.tinkoff.qa.neptune.http.api.request;

import ru.tinkoff.qa.neptune.http.api.request.form.FormBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
                var resultUri = new URI(uri.getScheme(),
                        uri.getUserInfo(),
                        uri.getHost(),
                        uri.getPort(),
                        path, resultQuery,
                        uri.getFragment());
                var f = URI.class.getDeclaredField("query");
                f.setAccessible(true);
                f.set(resultUri, resultQuery);
                return resultUri;
            } catch (URISyntaxException | NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
