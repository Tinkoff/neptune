package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.openqa.selenium.Point;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

public final class PointValueGetter implements ParameterValueGetter<Point> {
    @Override
    public String getParameterValue(Point fieldValue) {
        return "[x=" + fieldValue.getX() + ", y=" + fieldValue.getY() + "]";
    }
}
