package ru.tinkoff.qa.neptune.data.base.api.captors;

public class StringQueryCaptor extends AbstractQueryCaptor<String> {

    @Override
    public String getCaptured(Object toBeCaptured) {
        return tryToExtractQuery(toBeCaptured, String.class);
    }
}
