package ru.tinkoff.qa.neptune.http.api.request;

import ru.tinkoff.qa.neptune.http.api.request.form.FormBuilder;

final class QueryBuilder extends FormBuilder {

    @Override
    protected void addParameter(String name,
                                boolean toExpand,
                                FormValueDelimiters delimiter,
                                boolean allowReserved,
                                Object... values) {
        super.addParameter(name, toExpand, delimiter, allowReserved, values);
    }

    @Override
    protected String buildForm() {
        return super.buildForm();
    }
}