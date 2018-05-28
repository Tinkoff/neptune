package com.github.toy.constructor.selenium.test;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.By.xpath;

class FakeDOMModel {

    public final static String LABEL_TAG = "label";
    public final static String LABEL_XPATH = ".//*[@class = 'label-element']";
    public final static String LABEL_XPATH2 = ".//ancestor::span[@class = 'mark']";

    public final static String BUTTON_TAG = "button";
    public final static String CUSTOM_BUTTON_CLASS = "CustomButton";

    public final static String ATTR1 = "attribute1";
    public final static String ATTR2 = "attribute2";
    public final static String ATTR3 = "attribute3";
    public final static String ATTR4 = "attribute4";
    public final static String ATTR5 = "attribute5";
    public final static String ATTR6 = "attribute6";
    public final static String ATTR7 = "attribute7";
    public final static String ATTR8 = "attribute8";
    public final static String ATTR9 = "attribute9";
    public final static String ATTR10 = "attribute10";
    public final static String ATTR11 = "attribute11";
    public final static String ATTR12 = "attribute12";
    public final static String ATTR13 = "attribute13";
    public final static String ATTR14 = "attribute14";
    public final static String ATTR15 = "attribute15";
    public final static String ATTR16 = "attribute16";
    public final static String ATTR17 = "attribute17";
    public final static String ATTR18 = "attribute18";
    public final static String ATTR19 = "attribute19";
    public final static String ATTR20 = "attribute20";

    public final static String VALUE1 = "value1";
    public final static String VALUE2 = "value2";
    public final static String VALUE3 = "value3";
    public final static String VALUE4 = "value4";
    public final static String VALUE5 = "value5";
    public final static String VALUE6 = "value6";
    public final static String VALUE7 = "value7";
    public final static String VALUE8 = "value8";
    public final static String VALUE9 = "value9";
    public final static String VALUE10 = "value10";
    public final static String VALUE11 = "value11";
    public final static String VALUE12 = "value12";
    public final static String VALUE13 = "value13";
    public final static String VALUE14 = "value14";
    public final static String VALUE15 = "value15";
    public final static String VALUE16 = "value16";
    public final static String VALUE17 = "value17";
    public final static String VALUE18 = "value18";
    public final static String VALUE19 = "value19";
    public final static String VALUE20 = "value20";


    public final static String CSS1 = "css1";
    public final static String CSS2 = "css2";
    public final static String CSS3 = "css3";
    public final static String CSS4 = "css4";
    public final static String CSS5 = "css5";
    public final static String CSS6 = "css6";
    public final static String CSS7 = "css7";
    public final static String CSS8 = "css8";
    public final static String CSS9 = "css9";
    public final static String CSS10 = "css10";
    public final static String CSS11 = "css11";
    public final static String CSS12 = "css12";
    public final static String CSS13 = "css13";
    public final static String CSS14 = "css14";
    public final static String CSS15 = "css15";
    public final static String CSS16 = "css16";
    public final static String CSS17 = "css17";
    public final static String CSS18 = "css18";
    public final static String CSS19 = "css19";
    public final static String CSS20 = "css20";

    public final static String CSS_VALUE1 = "css_value1";
    public final static String CSS_VALUE2 = "css_value2";
    public final static String CSS_VALUE3 = "css_value3";
    public final static String CSS_VALUE4 = "css_value4";
    public final static String CSS_VALUE5 = "css_value5";
    public final static String CSS_VALUE6 = "css_value6";
    public final static String CSS_VALUE7 = "css_value7";
    public final static String CSS_VALUE8 = "css_value8";
    public final static String CSS_VALUE9 = "css_value9";
    public final static String CSS_VALUE10 = "css_value10";
    public final static String CSS_VALUE11 = "css_value11";
    public final static String CSS_VALUE12 = "css_value12";
    public final static String CSS_VALUE13 = "css_value13";
    public final static String CSS_VALUE14 = "css_value14";
    public final static String CSS_VALUE15 = "css_value15";
    public final static String CSS_VALUE16 = "css_value16";
    public final static String CSS_VALUE17 = "css_value17";
    public final static String CSS_VALUE18 = "css_value18";
    public final static String CSS_VALUE19 = "css_value19";
    public final static String CSS_VALUE20 = "css_value20";

    public final static String BUTTON_TEXT1 = "Button Text1";
    public final static String BUTTON_TEXT2 = "Button Text2";
    public final static String BUTTON_TEXT3 = "Button Text2";
    public final static String BUTTON_TEXT4 = "Button Text4";
    public final static String BUTTON_TEXT5 = "Button Text5";

    public final static String BUTTON_LABEL_TEXT1 = "Button Label Text1";
    public final static String BUTTON_LABEL_TEXT2 = "Button Label Text2";
    public final static String BUTTON_LABEL_TEXT3 = "Button Label Text3";
    public final static String BUTTON_LABEL_TEXT4 = "Button Label Text4";
    public final static String BUTTON_LABEL_TEXT5 = "Button Label Text5";
    public final static String BUTTON_LABEL_TEXT6 = "Button Label Text6";
    public final static String BUTTON_LABEL_TEXT7 = "Button Label Text7";
    public final static String BUTTON_LABEL_TEXT8 = "Button Label Text8";
    public final static String BUTTON_LABEL_TEXT9 = "Button Label Text9";
    public final static String BUTTON_LABEL_TEXT10 = "Button Label Text10";
    public final static String BUTTON_LABEL_TEXT11 = "Button Label Text11";
    public final static String BUTTON_LABEL_TEXT12 = "Button Label Text12";

    public final static MockWebElement COMMON_BUTTON1 = new MockWebElement(tagName(BUTTON_TAG),
            Map.ofEntries(entry(ATTR1, VALUE8),
                    entry(ATTR4, VALUE2),
                    entry(ATTR3, VALUE4)),
            Map.ofEntries(entry(CSS1, CSS_VALUE2),
                    entry(CSS4, CSS_VALUE5),
                    entry(CSS16, CSS_VALUE17)), new Point(10, 20), new Dimension(20, 30), false,
            false, BUTTON_TAG, BUTTON_TEXT1, List.of());

    public final static MockWebElement COMMON_BUTTON2 = new MockWebElement(tagName(BUTTON_TAG),
            Map.ofEntries(entry(ATTR1, VALUE7),
                    entry(ATTR4, VALUE5),
                    entry(ATTR3, VALUE3)),
            Map.ofEntries(entry(CSS1, CSS_VALUE7),
                    entry(CSS4, CSS_VALUE5),
                    entry(CSS16, CSS_VALUE18)), new Point(15, 25), new Dimension(25, 35), true,
            false, BUTTON_TAG, BUTTON_TEXT2, List.of());

    public final static MockWebElement COMMON_BUTTON3 = new MockWebElement(tagName(BUTTON_TAG),
            Map.ofEntries(entry(ATTR1, VALUE7),
                    entry(ATTR4, VALUE6),
                    entry(ATTR3, VALUE5)),
            Map.ofEntries(entry(CSS1, CSS_VALUE6),
                    entry(CSS4, CSS_VALUE5),
                    entry(CSS16, CSS_VALUE16)), new Point(35, 45), new Dimension(45, 44), true,
            false, BUTTON_TAG, BUTTON_TEXT3, List.of());

    public final static MockWebElement COMMON_BUTTON4 = new MockWebElement(tagName(BUTTON_TAG),
            Map.ofEntries(entry(ATTR1, VALUE9),
                    entry(ATTR4, VALUE9),
                    entry(ATTR3, VALUE11)),
            Map.ofEntries(entry(CSS1, CSS_VALUE20),
                    entry(CSS4, CSS_VALUE15),
                    entry(CSS16, CSS_VALUE18)), new Point(53, 70), new Dimension(100, 70), false,
            false, BUTTON_TAG, BUTTON_TEXT4, List.of());


    public final static MockWebElement COMMON_LABELED_BUTTON1 = new MockWebElement(tagName(BUTTON_TAG),
            Map.ofEntries(entry(ATTR1, VALUE1),
                    entry(ATTR2, VALUE10)),
            Map.ofEntries(entry(CSS2, CSS_VALUE1),
                    entry(CSS3, CSS_VALUE3)), new Point(11, 21), new Dimension(21, 32), true,
            false, BUTTON_TAG, BUTTON_TEXT5, List.of(
                    new MockWebElement(tagName(LABEL_TAG), Map.ofEntries(), Map.ofEntries(), new Point(11, 21),
                            new Dimension(21, 32), true,
                            false, LABEL_TAG, BUTTON_LABEL_TEXT1, List.of())
    ));

    public final static MockWebElement COMMON_LABELED_BUTTON2 = new MockWebElement(tagName(BUTTON_TAG),
            Map.ofEntries(entry(ATTR1, VALUE2),
                    entry(ATTR2, VALUE10)),
            Map.ofEntries(entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE8)), new Point(13, 23), new Dimension(24, 35), false,
            false, BUTTON_TAG, EMPTY, List.of(
            new MockWebElement(tagName(LABEL_TAG), Map.ofEntries(), Map.ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT2, List.of())
    ));

    public final static MockWebElement COMMON_LABELED_BUTTON3 = new MockWebElement(tagName(BUTTON_TAG),
            Map.ofEntries(entry(ATTR1, VALUE2),
                    entry(ATTR2, VALUE10)),
            Map.ofEntries(entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE8)), new Point(13, 23), new Dimension(25, 36), false,
            false, BUTTON_TAG, EMPTY, List.of(
            new MockWebElement(tagName(LABEL_TAG), Map.ofEntries(), Map.ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT3, List.of())
    ));

    public final static MockWebElement COMMON_LABELED_BUTTON4 = new MockWebElement(tagName(BUTTON_TAG),
            Map.ofEntries(entry(ATTR1, VALUE2),
                    entry(ATTR2, VALUE10)),
            Map.ofEntries(entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE8)), new Point(13, 23), new Dimension(44, 55), true,
            false, BUTTON_TAG, EMPTY, List.of(
            new MockWebElement(tagName(LABEL_TAG), Map.ofEntries(), Map.ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT4, List.of())
    ));











































    public final static MockWebElement CUSTOM_LABELED_BUTTON1 = new MockWebElement(className(CUSTOM_BUTTON_CLASS),
            Map.ofEntries(entry(ATTR5, VALUE10),
                    entry(ATTR6, VALUE11)),
            Map.ofEntries(entry(CSS5, CSS_VALUE7),
                    entry(CSS6, CSS_VALUE8)), new Point(55, 47), new Dimension(45, 69), true,
            false, BUTTON_TAG, EMPTY, List.of(
            new MockWebElement(xpath(LABEL_XPATH), Map.ofEntries(), Map.ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT5, List.of()),

            new MockWebElement(xpath(LABEL_XPATH2), Map.ofEntries(), Map.ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT9, List.of()
            )));

    public final static MockWebElement CUSTOM_LABELED_BUTTON2 = new MockWebElement(className(CUSTOM_BUTTON_CLASS),
            Map.ofEntries(entry(ATTR5, VALUE11),
                    entry(ATTR6, VALUE12)),
            Map.ofEntries(entry(CSS5, CSS_VALUE8),
                    entry(CSS6, CSS_VALUE9)), new Point(56, 48), new Dimension(46, 70), false,
            false, BUTTON_TAG, EMPTY, List.of(
            new MockWebElement(xpath(LABEL_XPATH), Map.ofEntries(), Map.ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT6, List.of()),

            new MockWebElement(xpath(LABEL_XPATH2), Map.ofEntries(), Map.ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT10, List.of()
            )));

    public final static MockWebElement CUSTOM_LABELED_BUTTON3 = new MockWebElement(className(CUSTOM_BUTTON_CLASS),
            Map.ofEntries(entry(ATTR5, VALUE12),
                    entry(ATTR6, VALUE13)),
            Map.ofEntries(entry(CSS5, CSS_VALUE9),
                    entry(CSS6, CSS_VALUE10)), new Point(57, 49), new Dimension(47, 71), true,
            false, BUTTON_TAG, EMPTY, List.of(
            new MockWebElement(xpath(LABEL_XPATH), Map.ofEntries(), Map.ofEntries(), new Point(52, 43),
                    new Dimension(34, 46), true,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT7, List.of()),

            new MockWebElement(xpath(LABEL_XPATH2), Map.ofEntries(), Map.ofEntries(), new Point(33, 43),
                    new Dimension(53, 64), true,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT11, List.of()
            )));

    public final static MockWebElement CUSTOM_LABELED_BUTTON4 = new MockWebElement(className(CUSTOM_BUTTON_CLASS),
            Map.ofEntries(entry(ATTR5, VALUE13),
                    entry(ATTR6, VALUE14)),
            Map.ofEntries(entry(CSS5, CSS_VALUE10),
                    entry(CSS6, CSS_VALUE11)), new Point(58, 50), new Dimension(48, 72), false,
            false, BUTTON_TAG, EMPTY, List.of(
            new MockWebElement(xpath(LABEL_XPATH), Map.ofEntries(), Map.ofEntries(), new Point(53, 44),
                    new Dimension(35, 47), false,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT8, List.of()),

            new MockWebElement(xpath(LABEL_XPATH2), Map.ofEntries(), Map.ofEntries(), new Point(34, 44),
                    new Dimension(54, 65), false,
                    false, LABEL_TAG, BUTTON_LABEL_TEXT12, List.of()
            )));


    private final static List<MockWebElement> fakeMock = List.of(
            COMMON_BUTTON1, COMMON_BUTTON2, COMMON_BUTTON3, COMMON_BUTTON4,
            COMMON_LABELED_BUTTON1, COMMON_LABELED_BUTTON2, COMMON_LABELED_BUTTON3, COMMON_LABELED_BUTTON4,
            CUSTOM_LABELED_BUTTON1, CUSTOM_LABELED_BUTTON2, CUSTOM_LABELED_BUTTON3, CUSTOM_LABELED_BUTTON4
    );

    static List<MockWebElement> getFakeDOM() {
        return fakeMock;
    }
}
