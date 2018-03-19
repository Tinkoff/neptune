package com.github.toy.constructor.selenium.functions.value;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.WebElement;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;

public final class SequentialGetAttributeValueSupplier extends
        SequentialGetSupplier<SeleniumSteps, String, WebElement, SequentialGetAttributeValueSupplier> {
    private final String attr;

    public SequentialGetAttributeValueSupplier(String attr) {
        this.attr = attr;
    }

    @Override
    protected Function<WebElement, String> getEndFunction() {
        return toGet(format("Get value of the attribute '%s'", attr), webElement -> webElement.getAttribute(attr));
    }
}
