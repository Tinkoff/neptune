package ru.tinkoff.qa.neptune.http.api.request.body.url.encoded;

import ru.tinkoff.qa.neptune.http.api.request.QueryValueDelimiters;
import ru.tinkoff.qa.neptune.http.api.request.form.FormBuilder;

final class URLEncodedFormBuilder extends FormBuilder {

    @Override
    protected void addParameter(String name,
                                boolean toExpand,
                                QueryValueDelimiters delimiter,
                                boolean allowReserved,
                                Object... values) {
        super.addParameter(name, toExpand, delimiter, allowReserved, values);
    }

    @Override
    protected void addParameter(String name,
                                Object value) {
        super.addParameter(name, value);
    }

    @Override
    protected String buildForm() {
        return super.buildForm();
    }
}
