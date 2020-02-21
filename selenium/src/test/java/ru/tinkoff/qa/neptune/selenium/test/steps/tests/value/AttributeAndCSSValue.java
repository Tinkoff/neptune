package ru.tinkoff.qa.neptune.selenium.test.steps.tests.value;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Select;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TextField;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class AttributeAndCSSValue extends BaseWebDriverTest {

    @Test
    public void getAttributeValueByWidgetSearchCriteria() {
        assertThat(seleniumSteps.get(attributeValue(HREF).of(link(LINK_LABEL_TEXT5))),
                is(LINK_REFERENCE9));
    }

    @Test
    public void getAttributeValueByWebElementSearchCriteria() {
        assertThat(seleniumSteps.get(attributeValue(HREF)
                        .of(webElement(tagName(LINK_TAG))
                                .criteria(attr(ATTR17, VALUE6)))),
                is(LINK_REFERENCE8));
    }

    @Test
    public void getAttributeValueFromWidget() {
        Table table = seleniumSteps.find(table(TABLE_LABEL_TEXT1));
        assertThat(seleniumSteps.get(attributeValue(ATTR3).of(table)),
                is(VALUE9));
    }

    @Test
    public void getAttributeValueFromWebElement() {
        assertThat(seleniumSteps.get(attributeValue(ATTR18).of(CUSTOM_LABELED_LINK3)),
                is(VALUE10));
    }

    @Test
    public void getAttributeNullValueByWidgetSearchCriteria() {
        assertThat(seleniumSteps.get(attributeValue(ATTR19).of(link(LINK_LABEL_TEXT9))),
                nullValue());
    }

    @Test
    public void getAttributeNullValueByWebElementSearchCriteria() {
        assertThat(seleniumSteps.get(attributeValue(ATTR16).of(webElement(tagName(LINK_TAG))
                        .criteria(nested(webElements(tagName(LABEL_TAG)).criteria(text(LINK_LABEL_TEXT1)))))),
                nullValue());
    }

    @Test
    public void getAttributeNullValueFromWidget() {
        Select select = seleniumSteps
                .find(select(SELECT_LABEL_TEXT8, SELECT_LABEL_TEXT12)
                        .criteria(nested(webElements(className(ITEM_OPTION_CLASS)).criteria(text(OPTION_TEXT34)))));
        assertThat(seleniumSteps.get(attributeValue(ATTR4).of(select)),
                nullValue());
    }

    @Test
    public void getAttributeNullValueFromWebElement() {
        assertThat(seleniumSteps.get(attributeValue(ATTR18).of(COMMON_LABELED_TAB2)),
                nullValue());
    }

    @Test
    public void getCSSValueByWidgetSearchCriteria() {
        assertThat(seleniumSteps.get(cssValue(CSS20).of(link(LINK_LABEL_TEXT7))),
                is(CSS_VALUE10));
    }

    @Test
    public void getCSSValueByWebElementSearchCriteria() {
        assertThat(seleniumSteps.get(cssValue(CSS20).of(webElement(cssSelector(CUSTOM_LINK_CSS))
                        .criteria(attr(HREF, LINK_REFERENCE11)))),
                is(CSS_VALUE10));
    }

    @Test
    public void getCSSValueFromWidget() {
        TextField textField = seleniumSteps.find(textField(INPUT_LABEL_TEXT3));
        assertThat(seleniumSteps.get(cssValue(CSS8).of(textField)),
                is(CSS_VALUE8));
    }

    @Test
    public void getCSSValueFromWebElement() {
        assertThat(seleniumSteps.get(cssValue(CSS15).of(CUSTOM_LABELED_LINK1)),
                is(CSS_VALUE7));
    }

    @Test
    public void getCSSNullValueByWidgetSearchCriteria() {
        assertThat(seleniumSteps.get(cssValue(CSS10).of(textField(INPUT_LABEL_TEXT2))),
                nullValue());
    }

    @Test
    public void getCSSNullValueByWebElementSearchCriteria() {
        assertThat(seleniumSteps.get(cssValue(CSS11).of(webElement(xpath(TEXT_FIELD_XPATH))
                        .criteria(nested(webElements(tagName(LABEL_TAG)).criteria(text(INPUT_LABEL_TEXT4)))))),
                nullValue());
    }

    @Test
    public void getCSSNullValueFromWidget() {
        TextField textField = seleniumSteps
                .find(textField(INPUT_LABEL_TEXT7, INPUT_LABEL_TEXT11)
                        .timeOut(FIVE_SECONDS));
        assertThat(seleniumSteps.get(cssValue(CSS12).of(textField)),
                nullValue());
    }

    @Test
    public void getCSSNullValueFromWebElement() {
        assertThat(seleniumSteps.get(cssValue(CSS16).of(COMMON_LABELED_TAB4)),
                nullValue());
    }
}
