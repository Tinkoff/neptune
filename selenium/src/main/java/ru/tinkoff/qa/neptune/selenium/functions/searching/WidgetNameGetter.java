package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Widget.getWidgetName;

public class WidgetNameGetter implements ParameterValueGetter<Class<? extends Widget>> {

    @Override
    public String getParameterValue(Class<? extends Widget> fieldValue) {
        return getWidgetName(fieldValue);
    }
}
