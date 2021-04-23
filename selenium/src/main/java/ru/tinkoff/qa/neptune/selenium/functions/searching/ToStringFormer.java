package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionMetadataFactory.getMultipleNameMetadata;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionMetadataFactory.getNameMetadata;

final class ToStringFormer {

    static String getMultipleToString(List<?> object) {
        return object.size() + " : " + object.stream()
                .map(o -> translate(getMultipleNameMetadata(o.getClass())))
                .distinct()
                .collect(toList());
    }

    private static String getText(String elementText, String name) {
        String text;
        if (isBlank(elementText)) {
            text = EMPTY;
        } else if (elementText.length() < 15) {
            text = elementText;
        } else {
            text = format("%s...", elementText.substring(0, 15));
        }

        if (isBlank(text)) {
            return name;
        }
        return format("%s [%s]", name, text);
    }

    static String widgetToString(Widget o) {
        var name = translate(getNameMetadata(o.getClass()));

        try {
            return getText(o.getText().trim(), name);
        } catch (Exception e) {
            return name;
        }
    }

    static String webElementToString(WebElement o, By by) {
        var name = translate(getNameMetadata(WebElement.class));

        try {
            return getText(o.getText().trim(), name + " [" + by + "]");
        } catch (Exception e) {
            return name + " [" + by + "]";
        }
    }
}
