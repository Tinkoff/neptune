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
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.attr;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.nested;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class AttributeAndCSSValue extends BaseWebDriverTest {

    @Test
    public void getAttributeValueByWidgetSearchCriteria() {
        assertThat(seleniumSteps.attrValueOf(link(LINK_LABEL_TEXT5), HREF),
                is(LINK_REFERENCE9));
    }

    @Test
    public void getAttributeValueByWebElementSearchCriteria() {
        assertThat(seleniumSteps.attrValueOf(webElement(tagName(LINK_TAG))
                        .criteria(attr(ATTR17, VALUE6)), HREF),
                is(LINK_REFERENCE8));
    }

    @Test
    public void getAttributeValueFromWidget() {
        Table table = seleniumSteps.find(table(TABLE_LABEL_TEXT1));
        assertThat(seleniumSteps.attrValueOf(table, ATTR3),
                is(VALUE9));
    }

    @Test
    public void getAttributeValueFromWebElement() {
        assertThat(seleniumSteps.attrValueOf(CUSTOM_LABELED_LINK3, ATTR18),
                is(VALUE10));
    }

    @Test
    public void getAttributeNullValueByWidgetSearchCriteria() {
        assertThat(seleniumSteps.attrValueOf(link(LINK_LABEL_TEXT9), ATTR19),
                nullValue());
    }

    @Test
    public void getAttributeNullValueByWebElementSearchCriteria() {
        assertThat(seleniumSteps.attrValueOf(webElement(tagName(LINK_TAG))
                        .criteria(nested(webElements(tagName(LABEL_TAG), LINK_LABEL_TEXT1))),
                ATTR16),
                nullValue());
    }

    @Test
    public void getAttributeNullValueFromWidget() {
        Select select = seleniumSteps
                .find(select(SELECT_LABEL_TEXT8)
                        .criteria(nested(webElements(className(ITEM_OPTION_CLASS), OPTION_TEXT34))));
        assertThat(seleniumSteps.attrValueOf(select, ATTR4),
                nullValue());
    }

    @Test
    public void getAttributeNullValueFromWebElement() {
        assertThat(seleniumSteps.attrValueOf(COMMON_LABELED_TAB2, ATTR18),
                nullValue());
    }

    @Test
    public void getCSSValueByWidgetSearchCriteria() {
        assertThat(seleniumSteps.cssValueOf(link(LINK_LABEL_TEXT7), CSS20),
                is(CSS_VALUE10));
    }

    @Test
    public void getCSSValueByWebElementSearchCriteria() {
        assertThat(seleniumSteps.cssValueOf(webElement(cssSelector(CUSTOM_LINK_CSS))
                        .criteria(attr(HREF, LINK_REFERENCE11)), CSS20),
                is(CSS_VALUE10));
    }

    @Test
    public void getCSSValueFromWidget() {
        TextField textField = seleniumSteps.find(textField(INPUT_LABEL_TEXT3));
        assertThat(seleniumSteps.cssValueOf(textField, CSS8),
                is(CSS_VALUE8));
    }

    @Test
    public void getCSSValueFromWebElement() {
        assertThat(seleniumSteps.cssValueOf(CUSTOM_LABELED_LINK1, CSS15),
                is(CSS_VALUE7));
    }

    @Test
    public void getCSSNullValueByWidgetSearchCriteria() {
        assertThat(seleniumSteps.cssValueOf(textField(INPUT_LABEL_TEXT2), CSS10),
                nullValue());
    }

    @Test
    public void getCSSNullValueByWebElementSearchCriteria() {
        assertThat(seleniumSteps.cssValueOf(webElement(xpath(TEXT_FIELD_XPATH))
                        .criteria(nested(webElements(tagName(LABEL_TAG), INPUT_LABEL_TEXT4))),
                CSS11),
                nullValue());
    }

    @Test
    public void getCSSNullValueFromWidget() {
        TextField textField = seleniumSteps.find(textField(INPUT_LABEL_TEXT11)
                .timeOut(FIVE_SECONDS));
        assertThat(seleniumSteps.cssValueOf(textField, CSS12),
                nullValue());
    }

    @Test
    public void getCSSNullValueFromWebElement() {
        assertThat(seleniumSteps.cssValueOf(COMMON_LABELED_TAB4, CSS16),
                nullValue());
    }
}
