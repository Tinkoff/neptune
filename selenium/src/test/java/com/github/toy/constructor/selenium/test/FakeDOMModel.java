package com.github.toy.constructor.selenium.test;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.List;

import static java.util.List.of;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.openqa.selenium.By.*;

public class FakeDOMModel {

    public static final String LABEL_TAG = "label";
    public static final String LABEL_XPATH = ".//*[@class = 'label-element']";
    public static final String LABEL_XPATH2 = ".//ancestor::span[@class = 'mark']";

    public static final String BUTTON_TAG = "button";
    public static final String LINK_TAG = "a";
    public static final String CUSTOM_LINK_CSS = "custom_link";
    public static final String CUSTOM_BUTTON_CLASS = "CustomButton";
    public static final String TEXT_FIELD_XPATH = ".//input[@type = 'text']";
    public static final String TEXT_AREA_TAG    = "textarea";
    public static final String DIV = "div";
    public static final String SPAN = "span";
    public static final String LI = "li";
    public static final String TAB_CLASS = "tabItem";
    public static final String SELECT = "select";
    public static final String OPTION = "option";
    public static final String MULTI_SELECT_CLASS = "multiSelect";
    public static final String ITEM_OPTION_CLASS = "item";
    public static final String TABLE = "table";
    public static final String T_HEAD = "thead";
    public static final String T_BODY = "thead";
    public static final String T_FOOT = "tfoot";
    public static final String TR = "tr";
    public static final String TH = "th";
    public static final String TD = "td";

    public static final String SPREAD_SHEET_CLASS = "spreadSheet";
    public static final String HEADLINE_CLASS = "headline";
    public static final String STRING_CLASS = "string";
    public static final String FOOTER_CLASS = "footer";
    public static final String CELL_CLASS = "cell";

    public static final String CHECK_BOX_XPATH = ".//input[@type = 'checkbox']";
    public static final String RADIO_BUTTON_XPATH = ".//input[@type = 'radio']";

    private static final String INPUT_TAG = "input";

    public static final String ATTR1 = "attribute1";
    public static final String ATTR2 = "attribute2";
    public static final String ATTR3 = "attribute3";
    public static final String ATTR4 = "attribute4";
    public static final String ATTR5 = "attribute5";
    public static final String ATTR6 = "attribute6";
    public static final String ATTR7 = "attribute7";
    public static final String ATTR8 = "attribute8";
    public static final String ATTR9 = "attribute9";
    public static final String ATTR10 = "attribute10";
    public static final String ATTR11 = "attribute11";
    public static final String ATTR12 = "attribute12";
    public static final String ATTR13 = "attribute13";
    public static final String ATTR14 = "attribute14";
    public static final String ATTR15 = "attribute15";
    public static final String ATTR16 = "attribute16";
    public static final String ATTR17 = "attribute17";
    public static final String ATTR18 = "attribute18";
    public static final String ATTR19 = "attribute19";
    public static final String ATTR20 = "attribute20";
    public static final String VALUE = "value";
    public static final String HREF = "href";

    public static final String VALUE1 = "value1";
    public static final String VALUE2 = "value2";
    public static final String VALUE3 = "value3";
    public static final String VALUE4 = "value4";
    public static final String VALUE5 = "value5";
    public static final String VALUE6 = "value6";
    public static final String VALUE7 = "value7";
    public static final String VALUE8 = "value8";
    public static final String VALUE9 = "value9";
    public static final String VALUE10 = "value10";
    public static final String VALUE11 = "value11";
    public static final String VALUE12 = "value12";
    public static final String VALUE13 = "value13";
    public static final String VALUE14 = "value14";
    public static final String VALUE15 = "value15";
    public static final String VALUE16 = "value16";
    public static final String VALUE17 = "value17";
    public static final String VALUE18 = "value18";
    public static final String VALUE19 = "value19";
    public static final String VALUE20 = "value20";


    public static final String CSS1 = "css1";
    public static final String CSS2 = "css2";
    public static final String CSS3 = "css3";
    public static final String CSS4 = "css4";
    public static final String CSS5 = "css5";
    public static final String CSS6 = "css6";
    public static final String CSS7 = "css7";
    public static final String CSS8 = "css8";
    public static final String CSS9 = "css9";
    public static final String CSS10 = "css10";
    public static final String CSS11 = "css11";
    public static final String CSS12 = "css12";
    public static final String CSS13 = "css13";
    public static final String CSS14 = "css14";
    public static final String CSS15 = "css15";
    public static final String CSS16 = "css16";
    public static final String CSS17 = "css17";
    public static final String CSS18 = "css18";
    public static final String CSS19 = "css19";
    public static final String CSS20 = "css20";

    public static final String CSS_VALUE1 = "css_value1";
    public static final String CSS_VALUE2 = "css_value2";
    public static final String CSS_VALUE3 = "css_value3";
    public static final String CSS_VALUE4 = "css_value4";
    public static final String CSS_VALUE5 = "css_value5";
    public static final String CSS_VALUE6 = "css_value6";
    public static final String CSS_VALUE7 = "css_value7";
    public static final String CSS_VALUE8 = "css_value8";
    public static final String CSS_VALUE9 = "css_value9";
    public static final String CSS_VALUE10 = "css_value10";
    public static final String CSS_VALUE11 = "css_value11";
    public static final String CSS_VALUE12 = "css_value12";
    public static final String CSS_VALUE13 = "css_value13";
    public static final String CSS_VALUE14 = "css_value14";
    public static final String CSS_VALUE15 = "css_value15";
    public static final String CSS_VALUE16 = "css_value16";
    public static final String CSS_VALUE17 = "css_value17";
    public static final String CSS_VALUE18 = "css_value18";
    public static final String CSS_VALUE19 = "css_value19";
    public static final String CSS_VALUE20 = "css_value20";

    public static final String BUTTON_TEXT1 = "Button Text1";
    public static final String BUTTON_TEXT2 = "Button Text2";
    public static final String BUTTON_TEXT3 = "Button Text3";
    public static final String BUTTON_TEXT4 = "Button Text4";
    public static final String BUTTON_TEXT5 = "Button Text5";

    public static final String BUTTON_LABEL_TEXT1 = "Button Label Text1";
    public static final String BUTTON_LABEL_TEXT2 = "Button Label Text2";
    public static final String BUTTON_LABEL_TEXT3 = "Button Label Text3";
    public static final String BUTTON_LABEL_TEXT4 = "Button Label Text4";
    public static final String BUTTON_LABEL_TEXT5 = "Button Label Text5";
    public static final String BUTTON_LABEL_TEXT6 = "Button Label Text6";
    public static final String BUTTON_LABEL_TEXT7 = "Button Label Text7";
    public static final String BUTTON_LABEL_TEXT8 = "Button Label Text8";
    public static final String BUTTON_LABEL_TEXT9 = "Button Label Text9";
    public static final String BUTTON_LABEL_TEXT10 = "Button Label Text10";
    public static final String BUTTON_LABEL_TEXT11 = "Button Label Text11";
    public static final String BUTTON_LABEL_TEXT12 = "Button Label Text12";

    public static final String INPUT_TEXT1 = "Input Text1";
    public static final String INPUT_TEXT2 = "Input Text2";
    public static final String INPUT_TEXT3 = "Input Text3";
    public static final String INPUT_TEXT4 = "Input Text4";
    public static final String INPUT_TEXT5 = "Input Text5";
    public static final String INPUT_TEXT6 = "Input Text6";
    public static final String INPUT_TEXT7 = "Input Text7";
    public static final String INPUT_TEXT8 = "Input Text8";
    public static final String INPUT_TEXT9 = "Input Text9";
    public static final String INPUT_TEXT10 = "Input Text10";
    public static final String INPUT_TEXT11 = "Input Text11";
    public static final String INPUT_TEXT12 = "Input Text12";

    public static final String INPUT_LABEL_TEXT1 = "Input Label Text1";
    public static final String INPUT_LABEL_TEXT2 = "Input Label Text2";
    public static final String INPUT_LABEL_TEXT3 = "Input Label Text3";
    public static final String INPUT_LABEL_TEXT4 = "Input Label Text4";
    public static final String INPUT_LABEL_TEXT5 = "Input Label Text5";
    public static final String INPUT_LABEL_TEXT6 = "Input Label Text6";
    public static final String INPUT_LABEL_TEXT7 = "Input Label Text7";
    public static final String INPUT_LABEL_TEXT8 = "Input Label Text8";
    public static final String INPUT_LABEL_TEXT9 = "Input Label Text9";
    public static final String INPUT_LABEL_TEXT10 = "Input Label Text10";
    public static final String INPUT_LABEL_TEXT11 = "Input Label Text11";
    public static final String INPUT_LABEL_TEXT12 = "Input Label Text12";

    public static final String CHECKBOX_LABEL_TEXT1 = "Checkbox Label Text1";
    public static final String CHECKBOX_LABEL_TEXT2 = "Checkbox Label Text2";
    public static final String CHECKBOX_LABEL_TEXT3 = "Checkbox Label Text3";
    public static final String CHECKBOX_LABEL_TEXT4 = "Checkbox Label Text4";
    public static final String CHECKBOX_LABEL_TEXT5 = "Checkbox Label Text5";
    public static final String CHECKBOX_LABEL_TEXT6 = "Checkbox Label Text6";
    public static final String CHECKBOX_LABEL_TEXT7 = "Checkbox Label Text7";
    public static final String CHECKBOX_LABEL_TEXT8 = "Checkbox Label Text8";
    public static final String CHECKBOX_LABEL_TEXT9 = "Checkbox Label Text9";
    public static final String CHECKBOX_LABEL_TEXT10 = "Checkbox Label Text10";
    public static final String CHECKBOX_LABEL_TEXT11 = "Checkbox Label Text11";
    public static final String CHECKBOX_LABEL_TEXT12 = "Checkbox Label Text12";

    public static final String RADIOBUTTON_LABEL_TEXT1 = "Radiobutton Label Text1";
    public static final String RADIOBUTTON_LABEL_TEXT2 = "Radiobutton Label Text2";
    public static final String RADIOBUTTON_LABEL_TEXT3 = "Radiobutton Label Text3";
    public static final String RADIOBUTTON_LABEL_TEXT4 = "Radiobutton Label Text4";
    public static final String RADIOBUTTON_LABEL_TEXT5 = "Radiobutton Label Text5";
    public static final String RADIOBUTTON_LABEL_TEXT6 = "Radiobutton Label Text6";
    public static final String RADIOBUTTON_LABEL_TEXT7 = "Radiobutton Label Text7";
    public static final String RADIOBUTTON_LABEL_TEXT8 = "Radiobutton Label Text8";
    public static final String RADIOBUTTON_LABEL_TEXT9 = "Radiobutton Label Text9";
    public static final String RADIOBUTTON_LABEL_TEXT10 = "Radiobutton Label Text10";
    public static final String RADIOBUTTON_LABEL_TEXT11 = "Radiobutton Label Text11";
    public static final String RADIOBUTTON_LABEL_TEXT12 = "Radiobutton Label Text12";

    public static final String LINK_TEXT1 = "Link Text1";
    public static final String LINK_TEXT2 = "Link Text2";
    public static final String LINK_TEXT3 = "Link Text3";
    public static final String LINK_TEXT4 = "Link Text4";

    public static final String LINK_LABEL_TEXT1 = "Link Label Text1";
    public static final String LINK_LABEL_TEXT2 = "Link Label Text2";
    public static final String LINK_LABEL_TEXT3 = "Link Label Text3";
    public static final String LINK_LABEL_TEXT4 = "Link Label Text4";
    public static final String LINK_LABEL_TEXT5 = "Link Label Text5";
    public static final String LINK_LABEL_TEXT6 = "Link Label Text6";
    public static final String LINK_LABEL_TEXT7 = "Link Label Text7";
    public static final String LINK_LABEL_TEXT8 = "Link Label Text8";
    public static final String LINK_LABEL_TEXT9 = "Link Label Text9";
    public static final String LINK_LABEL_TEXT10 = "Link Label Text10";
    public static final String LINK_LABEL_TEXT11 = "Link Label Text11";
    public static final String LINK_LABEL_TEXT12 = "Link Label Text12";

    public static final String LINK_REFERENCE1 = "https://www.localhost1.com/";
    public static final String LINK_REFERENCE2 = "https://www.localhost2.com/";
    public static final String LINK_REFERENCE3 = "https://www.localhost3.com/";
    public static final String LINK_REFERENCE4 = "https://www.localhost4.com/";
    public static final String LINK_REFERENCE5 = "https://www.localhost5.com/";
    public static final String LINK_REFERENCE6 = "https://www.localhost6.com/";
    public static final String LINK_REFERENCE7 = "https://www.localhost7.com/";
    public static final String LINK_REFERENCE8 = "https://www.localhost8.com/";
    public static final String LINK_REFERENCE9 = "https://www.localhost9.com/";
    public static final String LINK_REFERENCE10 = "https://www.localhost10.com/";
    public static final String LINK_REFERENCE11 = "https://www.localhost11.com/";
    public static final String LINK_REFERENCE12 = "https://www.localhost12.com/";

    public static final String TAB_TEXT1 = "Tab text Text1";
    public static final String TAB_TEXT2 = "Tab text Text2";
    public static final String TAB_TEXT3 = "Tab text Text3";
    public static final String TAB_TEXT4 = "Tab text Text4";
    public static final String TAB_TEXT5 = "Tab text Text5";
    public static final String TAB_TEXT6 = "Tab text Text6";
    public static final String TAB_TEXT7 = "Tab text Text7";
    public static final String TAB_TEXT8 = "Tab text Text8";
    public static final String TAB_TEXT9 = "Tab text Text9";
    public static final String TAB_TEXT10 = "Tab text Text10";
    public static final String TAB_TEXT11 = "Tab text Text11";
    public static final String TAB_TEXT12 = "Tab text Text12";
    public static final String TAB_TEXT13 = "Tab text Text13";
    public static final String TAB_TEXT14 = "Tab text Text14";
    public static final String TAB_TEXT15 = "Tab text Text15";
    public static final String TAB_TEXT16 = "Tab text Text16";

    public static final ByChained CHAINED_FIND_TAB = new ByChained(tagName(DIV), tagName(SPAN), tagName(LI));

    public static final String SELECT_LABEL_TEXT1 = "Select Label Text1";
    public static final String SELECT_LABEL_TEXT2 = "Select Label Text2";
    public static final String SELECT_LABEL_TEXT3 = "Select Label Text3";
    public static final String SELECT_LABEL_TEXT4 = "Select Label Text4";
    public static final String SELECT_LABEL_TEXT6 = "Select Label Text6";
    public static final String SELECT_LABEL_TEXT7 = "Select Label Text7";
    public static final String SELECT_LABEL_TEXT8 = "Select Label Text8";
    public static final String SELECT_LABEL_TEXT9 = "Select Label Text9";
    public static final String SELECT_LABEL_TEXT10 = "Select Label Text10";
    public static final String SELECT_LABEL_TEXT11 = "Select Label Text11";
    public static final String SELECT_LABEL_TEXT12 = "Select Label Text12";

    public static final String TABLE_LABEL_TEXT1 = "Table Label Text1";
    public static final String TABLE_LABEL_TEXT2 = "Table Label Text2";
    public static final String TABLE_LABEL_TEXT3 = "Table Label Text3";
    public static final String TABLE_LABEL_TEXT4 = "Table Label Text4";
    public static final String TABLE_LABEL_TEXT5 = "Table Label Text5";
    public static final String TABLE_LABEL_TEXT6 = "Table Label Text6";
    public static final String TABLE_LABEL_TEXT9 = "Table Label Text9";
    public static final String TABLE_LABEL_TEXT10 = "Table Label Text10";

    public static final String OPTION_TEXT1 = "Option text Text1";
    public static final String OPTION_TEXT2 = "Option text Text2";
    public static final String OPTION_TEXT3 = "Option text Text3";
    public static final String OPTION_TEXT4 = "Option text Text4";
    public static final String OPTION_TEXT5 = "Option text Text5";
    public static final String OPTION_TEXT6 = "Option text Text6";
    public static final String OPTION_TEXT7 = "Option text Text7";
    public static final String OPTION_TEXT8 = "Option text Text8";
    public static final String OPTION_TEXT9 = "Option text Text9";
    public static final String OPTION_TEXT10 = "Option text Text10";
    public static final String OPTION_TEXT11 = "Option text Text11";
    public static final String OPTION_TEXT12 = "Option text Text12";
    public static final String OPTION_TEXT13 = "Option text Text13";
    public static final String OPTION_TEXT14 = "Option text Text14";
    public static final String OPTION_TEXT15 = "Option text Text15";
    public static final String OPTION_TEXT16 = "Option text Text16";
    public static final String OPTION_TEXT17 = "Option text Text17";
    public static final String OPTION_TEXT18 = "Option text Text18";
    public static final String OPTION_TEXT19 = "Option text Text19";
    public static final String OPTION_TEXT20 = "Option text Text20";
    public static final String OPTION_TEXT21 = "Option text Text21";
    public static final String OPTION_TEXT22 = "Option text Text22";
    public static final String OPTION_TEXT23 = "Option text Text23";
    public static final String OPTION_TEXT24 = "Option text Text24";
    public static final String OPTION_TEXT25 = "Option text Text25";
    public static final String OPTION_TEXT26 = "Option text Text26";
    public static final String OPTION_TEXT27 = "Option text Text27";
    public static final String OPTION_TEXT28 = "Option text Text28";
    public static final String OPTION_TEXT29 = "Option text Text29";
    public static final String OPTION_TEXT30 = "Option text Text30";
    public static final String OPTION_TEXT31 = "Option text Text31";
    public static final String OPTION_TEXT32 = "Option text Text32";
    public static final String OPTION_TEXT33 = "Option text Text33";
    public static final String OPTION_TEXT34 = "Option text Text34";
    public static final String OPTION_TEXT35 = "Option text Text35";

    public static final String HEADER_TEXT1 = "Header Text1";
    public static final String HEADER_TEXT2 = "Header Text2";
    public static final String HEADER_TEXT3 = "Header Text3";
    public static final String HEADER_TEXT4 = "Header Text4";
    public static final String HEADER_TEXT5 = "Header Text5";
    public static final String HEADER_TEXT6 = "Header Text6";
    public static final String HEADER_TEXT7 = "Header Text7";
    public static final String HEADER_TEXT8 = "Header Text8";
    public static final String HEADER_TEXT9 = "Header Text9";
    public static final String HEADER_TEXT10 = "Header Text10";
    public static final String HEADER_TEXT11 = "Header Text11";
    public static final String HEADER_TEXT12 = "Header Text12";
    public static final String HEADER_TEXT13 = "Header Text13";
    public static final String HEADER_TEXT14 = "Header Text14";
    public static final String HEADER_TEXT15 = "Header Text15";
    public static final String HEADER_TEXT16 = "Header Text16";
    public static final String HEADER_TEXT17 = "Header Text17";
    public static final String HEADER_TEXT18 = "Header Text18";
    public static final String HEADER_TEXT19 = "Header Text19";
    public static final String HEADER_TEXT20 = "Header Text20";
    public static final String HEADER_TEXT21 = "Header Text21";
    public static final String HEADER_TEXT22 = "Header Text22";
    public static final String HEADER_TEXT23 = "Header Text23";
    public static final String HEADER_TEXT24 = "Header Text24";
    public static final String HEADER_TEXT25 = "Header Text25";
    public static final String HEADER_TEXT26 = "Header Text26";
    public static final String HEADER_TEXT27 = "Header Text27";
    public static final String HEADER_TEXT28 = "Header Text28";
    public static final String HEADER_TEXT29 = "Header Text29";
    public static final String HEADER_TEXT30 = "Header Text30";

    public static final String FOOTER_TEXT1 = "Footer Text1";
    public static final String FOOTER_TEXT2 = "Footer Text2";
    public static final String FOOTER_TEXT3 = "Footer Text3";
    public static final String FOOTER_TEXT4 = "Footer Text4";
    public static final String FOOTER_TEXT5 = "Footer Text5";
    public static final String FOOTER_TEXT6 = "Footer Text6";
    public static final String FOOTER_TEXT7 = "Footer Text7";
    public static final String FOOTER_TEXT8 = "Footer Text8";
    public static final String FOOTER_TEXT9 = "Footer Text9";
    public static final String FOOTER_TEXT10 = "Footer Text10";
    public static final String FOOTER_TEXT11 = "Footer Text11";
    public static final String FOOTER_TEXT12 = "Footer Text12";
    public static final String FOOTER_TEXT13 = "Footer Text13";
    public static final String FOOTER_TEXT14 = "Footer Text14";
    public static final String FOOTER_TEXT15 = "Footer Text15";
    public static final String FOOTER_TEXT16 = "Footer Text16";
    public static final String FOOTER_TEXT17 = "Footer Text17";
    public static final String FOOTER_TEXT18 = "Footer Text18";
    public static final String FOOTER_TEXT19 = "Footer Text19";
    public static final String FOOTER_TEXT20 = "Footer Text20";
    public static final String FOOTER_TEXT21 = "Footer Text21";
    public static final String FOOTER_TEXT22 = "Footer Text22";
    public static final String FOOTER_TEXT23 = "Footer Text23";
    public static final String FOOTER_TEXT24 = "Footer Text24";
    public static final String FOOTER_TEXT25 = "Footer Text25";
    public static final String FOOTER_TEXT26 = "Footer Text26";
    public static final String FOOTER_TEXT27 = "Footer Text27";
    public static final String FOOTER_TEXT28 = "Footer Text28";
    public static final String FOOTER_TEXT29 = "Footer Text29";
    public static final String FOOTER_TEXT30 = "Footer Text30";

    public static final String CELL_TEXT1 = "Cell Text1";
    public static final String CELL_TEXT2 = "Cell Text2";
    public static final String CELL_TEXT3 = "Cell Text3";
    public static final String CELL_TEXT4 = "Cell Text4";
    public static final String CELL_TEXT5 = "Cell Text5";
    public static final String CELL_TEXT6 = "Cell Text6";
    public static final String CELL_TEXT7 = "Cell Text7";
    public static final String CELL_TEXT8 = "Cell Text8";
    public static final String CELL_TEXT9 = "Cell Text9";
    public static final String CELL_TEXT10 = "Cell Text10";
    public static final String CELL_TEXT11 = "Cell Text11";
    public static final String CELL_TEXT12 = "Cell Text12";
    public static final String CELL_TEXT13 = "Cell Text13";
    public static final String CELL_TEXT14 = "Cell Text14";
    public static final String CELL_TEXT15 = "Cell Text15";
    public static final String CELL_TEXT16 = "Cell Text16";
    public static final String CELL_TEXT17 = "Cell Text17";
    public static final String CELL_TEXT18 = "Cell Text18";
    public static final String CELL_TEXT19 = "Cell Text19";
    public static final String CELL_TEXT20 = "Cell Text20";
    public static final String CELL_TEXT21 = "Cell Text21";
    public static final String CELL_TEXT22 = "Cell Text22";
    public static final String CELL_TEXT23 = "Cell Text23";
    public static final String CELL_TEXT24 = "Cell Text24";
    public static final String CELL_TEXT25 = "Cell Text25";
    public static final String CELL_TEXT26 = "Cell Text26";
    public static final String CELL_TEXT27 = "Cell Text27";
    public static final String CELL_TEXT28 = "Cell Text28";
    public static final String CELL_TEXT29 = "Cell Text29";
    public static final String CELL_TEXT30 = "Cell Text30";
    public static final String CELL_TEXT31 = "Cell Text31";
    public static final String CELL_TEXT32 = "Cell Text32";
    public static final String CELL_TEXT33 = "Cell Text33";
    public static final String CELL_TEXT34 = "Cell Text34";
    public static final String CELL_TEXT35 = "Cell Text35";
    public static final String CELL_TEXT36 = "Cell Text36";
    public static final String CELL_TEXT37 = "Cell Text37";
    public static final String CELL_TEXT38 = "Cell Text38";
    public static final String CELL_TEXT39 = "Cell Text39";
    public static final String CELL_TEXT40 = "Cell Text40";
    public static final String CELL_TEXT41 = "Cell Text41";
    public static final String CELL_TEXT42 = "Cell Text42";
    public static final String CELL_TEXT43 = "Cell Text43";
    public static final String CELL_TEXT44 = "Cell Text44";
    public static final String CELL_TEXT45 = "Cell Text45";
    public static final String CELL_TEXT46 = "Cell Text46";
    public static final String CELL_TEXT47 = "Cell Text47";
    public static final String CELL_TEXT48 = "Cell Text48";
    public static final String CELL_TEXT49 = "Cell Text49";
    public static final String CELL_TEXT50 = "Cell Text50";
    public static final String CELL_TEXT51 = "Cell Text51";
    public static final String CELL_TEXT52 = "Cell Text52";
    public static final String CELL_TEXT53 = "Cell Text53";
    public static final String CELL_TEXT54 = "Cell Text54";
    public static final String CELL_TEXT55 = "Cell Text55";
    public static final String CELL_TEXT56 = "Cell Text56";
    public static final String CELL_TEXT57 = "Cell Text57";
    public static final String CELL_TEXT58 = "Cell Text58";
    public static final String CELL_TEXT59 = "Cell Text59";
    public static final String CELL_TEXT60 = "Cell Text60";
    public static final String CELL_TEXT61 = "Cell Text61";
    public static final String CELL_TEXT62 = "Cell Text62";
    public static final String CELL_TEXT63 = "Cell Text63";
    public static final String CELL_TEXT64 = "Cell Text64";
    public static final String CELL_TEXT65 = "Cell Text65";
    public static final String CELL_TEXT66 = "Cell Text66";
    public static final String CELL_TEXT67 = "Cell Text67";
    public static final String CELL_TEXT68 = "Cell Text68";
    public static final String CELL_TEXT69 = "Cell Text69";
    public static final String CELL_TEXT70 = "Cell Text70";
    public static final String CELL_TEXT71 = "Cell Text71";
    public static final String CELL_TEXT72 = "Cell Text72";
    public static final String CELL_TEXT73 = "Cell Text73";
    public static final String CELL_TEXT74 = "Cell Text74";
    public static final String CELL_TEXT75 = "Cell Text75";
    public static final String CELL_TEXT76 = "Cell Text76";
    public static final String CELL_TEXT77 = "Cell Text77";
    public static final String CELL_TEXT78 = "Cell Text78";
    public static final String CELL_TEXT79 = "Cell Text79";
    public static final String CELL_TEXT80 = "Cell Text80";
    public static final String CELL_TEXT81 = "Cell Text81";
    public static final String CELL_TEXT82 = "Cell Text82";
    public static final String CELL_TEXT83 = "Cell Text83";
    public static final String CELL_TEXT84 = "Cell Text84";
    public static final String CELL_TEXT85 = "Cell Text85";
    public static final String CELL_TEXT86 = "Cell Text86";
    public static final String CELL_TEXT87 = "Cell Text87";
    public static final String CELL_TEXT88 = "Cell Text88";
    public static final String CELL_TEXT89 = "Cell Text89";

    private static final String INVISIBLE_SPAN = "Invisible span";
    public static final String VISIBLE_DIV = "Visible div";

    private static final ByChained CHAINED_FIND_HEADER = new ByChained(tagName(T_HEAD), tagName(TR));
    private static final ByChained CHAINED_FIND_ROW = new ByChained(tagName(T_BODY), tagName(TR));
    private static final ByChained CHAINED_FIND_FOOTER = new ByChained(tagName(T_FOOT), tagName(TR));
    private static final ByAll CUSTOM_LABEL_BY = new ByAll(xpath(LABEL_XPATH), xpath(LABEL_XPATH2));
    public static final By INVISIBLE_SPAN_BY = tagName(SPAN);
    public static final By VISIBLE_DIV_BY = tagName(DIV);

    private static final MockWebElement VISIBLE_DIV_ELEMENT = new MockWebElement(VISIBLE_DIV_BY,
            ofEntries(entry(ATTR1, VALUE10),
                    entry(ATTR4, VALUE4),
                    entry(ATTR3, VALUE6)),
            ofEntries(entry(CSS1, CSS_VALUE4),
                    entry(CSS4, CSS_VALUE7),
                    entry(CSS16, CSS_VALUE19)), new Point(10, 20), new Dimension(20, 30), true,
            true, false, DIV, VISIBLE_DIV, of());

    private static final MockWebElement INVISIBLE_SPAN_ELEMENT = new MockWebElement(INVISIBLE_SPAN_BY,
            ofEntries(entry(ATTR1, VALUE9),
                    entry(ATTR4, VALUE3),
                    entry(ATTR3, VALUE5)),
            ofEntries(entry(CSS1, CSS_VALUE3),
                    entry(CSS4, CSS_VALUE6),
                    entry(CSS16, CSS_VALUE18)), new Point(10, 20), new Dimension(20, 30), false,
            false, false, SPAN, INVISIBLE_SPAN, of());

    public static final MockWebElement COMMON_BUTTON1 = new MockWebElement(tagName(BUTTON_TAG),
            ofEntries(entry(ATTR1, VALUE8),
                    entry(ATTR4, VALUE2),
                    entry(ATTR3, VALUE4)),
            ofEntries(entry(CSS1, CSS_VALUE2),
                    entry(CSS4, CSS_VALUE5),
                    entry(CSS16, CSS_VALUE17)), new Point(10, 20), new Dimension(20, 30), false,
            false, false, LINK_TAG, BUTTON_TEXT1, of());

    public static final MockWebElement COMMON_BUTTON2 = new MockWebElement(tagName(BUTTON_TAG),
            ofEntries(entry(ATTR1, VALUE7),
                    entry(ATTR4, VALUE5),
                    entry(ATTR3, VALUE3)),
            ofEntries(entry(CSS1, CSS_VALUE7),
                    entry(CSS4, CSS_VALUE5),
                    entry(CSS16, CSS_VALUE18)), new Point(15, 25), new Dimension(25, 35), true,
            false, false, BUTTON_TAG, BUTTON_TEXT2, of());

    public static final MockWebElement COMMON_BUTTON3 = new MockWebElement(tagName(BUTTON_TAG),
            ofEntries(entry(ATTR1, VALUE7),
                    entry(ATTR4, VALUE6),
                    entry(ATTR3, VALUE5)),
            ofEntries(entry(CSS1, CSS_VALUE6),
                    entry(CSS4, CSS_VALUE5),
                    entry(CSS16, CSS_VALUE16)), new Point(35, 45), new Dimension(45, 44), true,
            true, false, BUTTON_TAG, BUTTON_TEXT3, of());

    public static final MockWebElement COMMON_BUTTON4 = new MockWebElement(tagName(BUTTON_TAG),
            ofEntries(entry(ATTR1, VALUE9),
                    entry(ATTR4, VALUE9),
                    entry(ATTR3, VALUE11)),
            ofEntries(entry(CSS1, CSS_VALUE20),
                    entry(CSS4, CSS_VALUE15),
                    entry(CSS16, CSS_VALUE18)), new Point(53, 70), new Dimension(100, 70), false,
            true, false, BUTTON_TAG, BUTTON_TEXT4, of());


    public static final MockWebElement COMMON_LABELED_BUTTON1 = new MockWebElement(tagName(BUTTON_TAG),
            ofEntries(entry(ATTR1, VALUE1),
                    entry(ATTR2, VALUE10)),
            ofEntries(entry(CSS2, CSS_VALUE1),
                    entry(CSS3, CSS_VALUE3)), new Point(11, 21), new Dimension(21, 32), true,
            false, false, BUTTON_TAG, BUTTON_TEXT5, of(
                    new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                            new Dimension(21, 32), true,
                            false, false, LABEL_TAG, BUTTON_LABEL_TEXT1, of())
    ));

    public static final MockWebElement COMMON_LABELED_BUTTON2 = new MockWebElement(tagName(BUTTON_TAG),
            ofEntries(entry(ATTR1, VALUE2),
                    entry(ATTR2, VALUE10)),
            ofEntries(entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE8)), new Point(13, 23), new Dimension(24, 35), false,
            true, false, BUTTON_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    false, false, LABEL_TAG, BUTTON_LABEL_TEXT2, of())
    ));

    public static final MockWebElement COMMON_LABELED_BUTTON3 = new MockWebElement(tagName(BUTTON_TAG),
            ofEntries(entry(ATTR1, VALUE2),
                    entry(ATTR2, VALUE10)),
            ofEntries(entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE8)), new Point(13, 23), new Dimension(25, 36), false,
            false, false, BUTTON_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    false, false, LABEL_TAG, BUTTON_LABEL_TEXT3, of())
    ));

    public static final MockWebElement COMMON_LABELED_BUTTON4 = new MockWebElement(tagName(BUTTON_TAG),
            ofEntries(entry(ATTR1, VALUE2),
                    entry(ATTR2, VALUE10)),
            ofEntries(entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE8)), new Point(13, 23), new Dimension(44, 55), true,
            true, false, BUTTON_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    true, false, LABEL_TAG, BUTTON_LABEL_TEXT4, of())
    ));


    public static final MockWebElement CUSTOM_LABELED_BUTTON1 = new MockWebElement(className(CUSTOM_BUTTON_CLASS),
            ofEntries(entry(ATTR5, VALUE10),
                    entry(ATTR6, VALUE11)),
            ofEntries(entry(CSS5, CSS_VALUE7),
                    entry(CSS6, CSS_VALUE8)), new Point(55, 47), new Dimension(45, 69), true,
            true, false, BUTTON_TAG, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    true, false, LABEL_TAG, BUTTON_LABEL_TEXT5, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    true, false, LABEL_TAG, BUTTON_LABEL_TEXT9, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_BUTTON2 = new MockWebElement(className(CUSTOM_BUTTON_CLASS),
            ofEntries(entry(ATTR5, VALUE11),
                    entry(ATTR6, VALUE12)),
            ofEntries(entry(CSS5, CSS_VALUE8),
                    entry(CSS6, CSS_VALUE9)), new Point(56, 48), new Dimension(46, 70), false,
            true, false, BUTTON_TAG, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    true, false, LABEL_TAG, BUTTON_LABEL_TEXT6, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    true, false, LABEL_TAG, BUTTON_LABEL_TEXT10, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_BUTTON3 = new MockWebElement(className(CUSTOM_BUTTON_CLASS),
            ofEntries(entry(ATTR5, VALUE12),
                    entry(ATTR6, VALUE13)),
            ofEntries(entry(CSS5, CSS_VALUE9),
                    entry(CSS6, CSS_VALUE10)), new Point(57, 49), new Dimension(47, 71), true,
            false, false, BUTTON_TAG, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(52, 43),
                    new Dimension(34, 46), true,
                    false, false, LABEL_TAG, BUTTON_LABEL_TEXT7, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(33, 43),
                    new Dimension(53, 64), true,
                    false, false, LABEL_TAG, BUTTON_LABEL_TEXT11, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_BUTTON4 = new MockWebElement(className(CUSTOM_BUTTON_CLASS),
            ofEntries(entry(ATTR5, VALUE13),
                    entry(ATTR6, VALUE14)),
            ofEntries(entry(CSS5, CSS_VALUE10),
                    entry(CSS6, CSS_VALUE11)), new Point(58, 50), new Dimension(48, 72), false,
            false, false, BUTTON_TAG, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(53, 44),
                    new Dimension(35, 47), false,
                    false, false, LABEL_TAG, BUTTON_LABEL_TEXT8, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(34, 44),
                    new Dimension(54, 65), false,
                    false, false, LABEL_TAG, BUTTON_LABEL_TEXT12, of()
            )));

    public static final MockWebElement COMMON_TEXT_INPUT1 = new MockWebElement(xpath(TEXT_FIELD_XPATH),
            ofEntries(entry(ATTR7, VALUE3),
                    entry(ATTR8, VALUE4),
                    entry(ATTR9, VALUE5)),
            ofEntries(entry(CSS7, CSS_VALUE3),
                    entry(CSS8, CSS_VALUE4),
                    entry(CSS9, CSS_VALUE5)), new Point(10, 20), new Dimension(20, 30), false,
            true, false, INPUT_TAG, INPUT_TEXT1, of());

    public static final MockWebElement COMMON_TEXT_INPUT2 = new MockWebElement(xpath(TEXT_FIELD_XPATH),
            ofEntries(entry(ATTR7, VALUE4),
                    entry(ATTR8, VALUE5),
                    entry(ATTR9, VALUE6)),
            ofEntries(entry(CSS7, CSS_VALUE4),
                    entry(CSS8, CSS_VALUE5),
                    entry(CSS9, CSS_VALUE6)), new Point(15, 25), new Dimension(25, 35), true,
            true, false, INPUT_TAG, INPUT_TEXT2, of());

    public static final MockWebElement COMMON_TEXT_INPUT3 = new MockWebElement(xpath(TEXT_FIELD_XPATH),
            ofEntries(entry(ATTR7, VALUE5),
                    entry(ATTR8, VALUE6),
                    entry(ATTR9, VALUE7)),
            ofEntries(entry(CSS7, CSS_VALUE5),
                    entry(CSS8, CSS_VALUE6),
                    entry(CSS9, CSS_VALUE7)), new Point(35, 45), new Dimension(45, 44), true,
            true, false, INPUT_TAG, INPUT_TEXT3, of());

    public static final MockWebElement COMMON_TEXT_INPUT4 = new MockWebElement(xpath(TEXT_FIELD_XPATH),
            ofEntries(entry(ATTR7, VALUE5),
                    entry(ATTR8, VALUE6),
                    entry(ATTR9, VALUE8)),
            ofEntries(entry(CSS7, CSS_VALUE5),
                    entry(CSS8, CSS_VALUE7),
                    entry(CSS9, CSS_VALUE7)), new Point(53, 70), new Dimension(100, 70), false,
            true, false, INPUT_TAG, INPUT_TEXT4, of());


    public static final MockWebElement COMMON_LABELED_INPUT1 = new MockWebElement(xpath(TEXT_FIELD_XPATH),
            ofEntries(entry(ATTR7, VALUE6),
                    entry(ATTR8, VALUE7),
                    entry(ATTR9, VALUE8),
                    entry(VALUE, INPUT_TEXT5)),
            ofEntries(entry(CSS7, CSS_VALUE6),
                    entry(CSS8, CSS_VALUE7),
                    entry(CSS9, CSS_VALUE8)), new Point(11, 21), new Dimension(21, 32), true,
            true, false, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(21, 32), true,
                    true, false, LABEL_TAG, INPUT_LABEL_TEXT1, of())
    ));

    public static final MockWebElement COMMON_LABELED_INPUT2 = new MockWebElement(xpath(TEXT_FIELD_XPATH),
            ofEntries(entry(ATTR7, VALUE7),
                    entry(ATTR8, VALUE8),
                    entry(ATTR9, VALUE9),
                    entry(VALUE, INPUT_TEXT6)),
            ofEntries(entry(CSS7, CSS_VALUE7),
                    entry(CSS8, CSS_VALUE8),
                    entry(CSS9, CSS_VALUE9)), new Point(13, 23), new Dimension(24, 35), false,
            false, false, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    false, false, LABEL_TAG, INPUT_LABEL_TEXT2, of())
    ));

    public static final MockWebElement COMMON_LABELED_INPUT3 = new MockWebElement(xpath(TEXT_FIELD_XPATH),
            ofEntries(entry(ATTR7, VALUE7),
                    entry(ATTR8, VALUE8),
                    entry(ATTR9, VALUE9),
                    entry(VALUE, INPUT_TEXT7)),
            ofEntries(entry(CSS7, CSS_VALUE7),
                    entry(CSS8, CSS_VALUE8),
                    entry(CSS9, CSS_VALUE9)), new Point(13, 23), new Dimension(25, 36), false,
            true, false, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    true, false, LABEL_TAG, INPUT_LABEL_TEXT3, of())
    ));

    public static final MockWebElement COMMON_LABELED_INPUT4 = new MockWebElement(xpath(TEXT_FIELD_XPATH),
            ofEntries(entry(ATTR7, VALUE8),
                    entry(ATTR8, VALUE9),
                    entry(ATTR9, VALUE10),
                    entry(VALUE, INPUT_TEXT8)),
            ofEntries(entry(CSS7, CSS_VALUE8),
                    entry(CSS8, CSS_VALUE9),
                    entry(CSS9, CSS_VALUE10)), new Point(13, 23), new Dimension(44, 55), true,
            true, false, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    true, false, LABEL_TAG, INPUT_LABEL_TEXT4, of())
    ));


    public static final MockWebElement TEXT_AREA1 = new MockWebElement(tagName(TEXT_AREA_TAG),
            ofEntries(entry(ATTR10, VALUE11),
                    entry(ATTR11, VALUE12),
                    entry(VALUE, INPUT_TEXT9)),
            ofEntries(entry(CSS10, CSS_VALUE10),
                    entry(CSS11, CSS_VALUE11)), new Point(55, 47), new Dimension(45, 69), true,
            false, false, TEXT_AREA_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    false, false, LABEL_TAG, INPUT_LABEL_TEXT5, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    false, false, LABEL_TAG, INPUT_LABEL_TEXT9, of()
            )));

    public static final MockWebElement TEXT_AREA2 = new MockWebElement(tagName(TEXT_AREA_TAG),
            ofEntries(entry(ATTR10, VALUE12),
                    entry(ATTR11, VALUE13),
                    entry(VALUE, INPUT_TEXT10)),
            ofEntries(entry(CSS10, CSS_VALUE11),
                    entry(CSS11, CSS_VALUE12)), new Point(56, 48), new Dimension(46, 70), false,
            false, false, TEXT_AREA_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    false, false, LABEL_TAG, INPUT_LABEL_TEXT6, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    false, false, LABEL_TAG, INPUT_LABEL_TEXT10, of()
            )));

    public static final MockWebElement TEXT_AREA3 = new MockWebElement(tagName(TEXT_AREA_TAG),
            ofEntries(entry(ATTR10, VALUE13),
                    entry(ATTR11, VALUE14),
                    entry(VALUE, INPUT_TEXT11)),
            ofEntries(entry(CSS10, CSS_VALUE12),
                    entry(CSS11, CSS_VALUE13)), new Point(57, 49), new Dimension(47, 71), true,
            true, false, TEXT_AREA_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(52, 43),
                    new Dimension(34, 46), true,
                    true, false, LABEL_TAG, INPUT_LABEL_TEXT7, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(33, 43),
                    new Dimension(53, 64), true,
                    true, false, LABEL_TAG, INPUT_LABEL_TEXT11, of()
            )));

    public static final MockWebElement TEXT_AREA4 = new MockWebElement(tagName(TEXT_AREA_TAG),
            ofEntries(entry(ATTR10, VALUE14),
                    entry(ATTR11, VALUE15),
                    entry(VALUE, INPUT_TEXT12)),
            ofEntries(entry(CSS10, CSS_VALUE13),
                    entry(CSS11, CSS_VALUE14)), new Point(58, 50), new Dimension(48, 72), false,
            true, false, TEXT_AREA_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(53, 44),
                    new Dimension(35, 47), false,
                    true, false, LABEL_TAG, INPUT_LABEL_TEXT8, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(34, 44),
                    new Dimension(54, 65), false,
                    true, false, LABEL_TAG, INPUT_LABEL_TEXT12, of()
            )));

    public static final MockWebElement COMMON_CHECKBOX1 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR11, VALUE11),
                    entry(ATTR12, VALUE12),
                    entry(ATTR13, VALUE13)),
            ofEntries(entry(CSS12, CSS_VALUE11),
                    entry(CSS13, CSS_VALUE12),
                    entry(CSS14, CSS_VALUE13)), new Point(10, 20), new Dimension(20, 30), false,
            false, true, INPUT_TAG, EMPTY, of());

    public static final MockWebElement COMMON_CHECKBOX2 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR11, VALUE12),
                    entry(ATTR12, VALUE13),
                    entry(ATTR13, VALUE14)),
            ofEntries(entry(CSS12, CSS_VALUE12),
                    entry(CSS13, CSS_VALUE13),
                    entry(CSS14, CSS_VALUE14)), new Point(15, 25), new Dimension(25, 35), true,
            false, true, INPUT_TAG, EMPTY, of());

    public static final MockWebElement COMMON_CHECKBOX3 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR11, VALUE13),
                    entry(ATTR12, VALUE14),
                    entry(ATTR13, VALUE15)),
            ofEntries(entry(CSS12, CSS_VALUE13),
                    entry(CSS13, CSS_VALUE14),
                    entry(CSS14, CSS_VALUE15)), new Point(35, 45), new Dimension(45, 44), true,
            false, true, INPUT_TAG, EMPTY, of());

    public static final MockWebElement COMMON_CHECKBOX4 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR11, VALUE14),
                    entry(ATTR12, VALUE15),
                    entry(ATTR13, VALUE16)),
            ofEntries(entry(CSS12, CSS_VALUE14),
                    entry(CSS13, CSS_VALUE15),
                    entry(CSS14, CSS_VALUE16)), new Point(53, 70), new Dimension(100, 70), false,
            false, true, INPUT_TAG, EMPTY, of());


    public static final MockWebElement COMMON_LABELED_CHECKBOX1 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR1, VALUE17),
                    entry(ATTR2, VALUE18)),
            ofEntries(entry(CSS2, CSS_VALUE17),
                    entry(CSS3, CSS_VALUE18)), new Point(11, 21), new Dimension(21, 32), true,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(21, 32), true,
                    true, false, LABEL_TAG, CHECKBOX_LABEL_TEXT1, of())
    ));

    public static final MockWebElement COMMON_LABELED_CHECKBOX2 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR1, VALUE18),
                    entry(ATTR2, VALUE19)),
            ofEntries(entry(CSS2, CSS_VALUE19),
                    entry(CSS3, CSS_VALUE20)), new Point(13, 23), new Dimension(24, 35), false,
            false, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    false, false, LABEL_TAG, CHECKBOX_LABEL_TEXT2, of())
    ));

    public static final MockWebElement COMMON_LABELED_CHECKBOX3 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR1, VALUE19),
                    entry(ATTR2, VALUE20)),
            ofEntries(entry(CSS2, CSS_VALUE20),
                    entry(CSS3, CSS_VALUE1)), new Point(13, 23), new Dimension(25, 36), false,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    true, false, LABEL_TAG, CHECKBOX_LABEL_TEXT3, of())
    ));

    public static final MockWebElement COMMON_LABELED_CHECKBOX4 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR1, VALUE20),
                    entry(ATTR2, VALUE1)),
            ofEntries(entry(CSS2, CSS_VALUE1),
                    entry(CSS3, CSS_VALUE2)), new Point(13, 23), new Dimension(44, 55), true,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    true, false, LABEL_TAG, CHECKBOX_LABEL_TEXT4, of())
    ));


    public static final MockWebElement COMMON_LABELED_CHECKBOX5 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR1, VALUE1),
                    entry(ATTR2, VALUE2)),
            ofEntries(entry(CSS2, CSS_VALUE2),
                    entry(CSS3, CSS_VALUE3)), new Point(55, 47), new Dimension(45, 69), true,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    false, false, LABEL_TAG, CHECKBOX_LABEL_TEXT5, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    false, false, LABEL_TAG, CHECKBOX_LABEL_TEXT9, of()
            )));

    public static final MockWebElement COMMON_LABELED_CHECKBOX6 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR1, VALUE2),
                    entry(ATTR2, VALUE3)),
            ofEntries(entry(CSS2, CSS_VALUE3),
                    entry(CSS3, CSS_VALUE4)), new Point(56, 48), new Dimension(46, 70), false,
            false, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    false, false, LABEL_TAG, CHECKBOX_LABEL_TEXT6, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    false, false, LABEL_TAG, CHECKBOX_LABEL_TEXT10, of()
            )));

    public static final MockWebElement COMMON_LABELED_CHECKBOX7 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR1, VALUE3),
                    entry(ATTR2, VALUE4)),
            ofEntries(entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE5)), new Point(57, 49), new Dimension(47, 71), true,
            false, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(52, 43),
                    new Dimension(34, 46), true,
                    false, false, LABEL_TAG, CHECKBOX_LABEL_TEXT7, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(33, 43),
                    new Dimension(53, 64), true,
                    false, false, LABEL_TAG, CHECKBOX_LABEL_TEXT11, of()
            )));

    public static final MockWebElement COMMON_LABELED_CHECKBOX8 = new MockWebElement(xpath(CHECK_BOX_XPATH),
            ofEntries(entry(ATTR1, VALUE4),
                    entry(ATTR2, VALUE5)),
            ofEntries(entry(CSS2, CSS_VALUE5),
                    entry(CSS3, CSS_VALUE6)), new Point(58, 50), new Dimension(48, 72), false,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(53, 44),
                    new Dimension(35, 47), false,
                    true, false, LABEL_TAG, CHECKBOX_LABEL_TEXT8, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(34, 44),
                    new Dimension(54, 65), false,
                    true, false, LABEL_TAG, CHECKBOX_LABEL_TEXT12, of()
            )));

    public static final MockWebElement COMMON_RADIOBUTTON1 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE5),
                    entry(ATTR15, VALUE6),
                    entry(ATTR16, VALUE7)),
            ofEntries(entry(CSS17, CSS_VALUE6),
                    entry(CSS18, CSS_VALUE7),
                    entry(CSS19, CSS_VALUE8)), new Point(10, 20), new Dimension(20, 30), false,
            true, true, INPUT_TAG, EMPTY, of());

    public static final MockWebElement COMMON_RADIOBUTTON2 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE6),
                    entry(ATTR15, VALUE7),
                    entry(ATTR16, VALUE8)),
            ofEntries(entry(CSS17, CSS_VALUE7),
                    entry(CSS18, CSS_VALUE8),
                    entry(CSS19, CSS_VALUE9)), new Point(15, 25), new Dimension(25, 35), true,
            false, true, INPUT_TAG, EMPTY, of());

    public static final MockWebElement COMMON_RADIOBUTTON3 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE7),
                    entry(ATTR15, VALUE8),
                    entry(ATTR16, VALUE9)),
            ofEntries(entry(CSS17, CSS_VALUE8),
                    entry(CSS18, CSS_VALUE9),
                    entry(CSS19, CSS_VALUE10)), new Point(35, 45), new Dimension(45, 44), true,
            true, true, INPUT_TAG, EMPTY, of());

    public static final MockWebElement COMMON_RADIOBUTTON4 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE8),
                    entry(ATTR15, VALUE9),
                    entry(ATTR16, VALUE10)),
            ofEntries(entry(CSS17, CSS_VALUE9),
                    entry(CSS18, CSS_VALUE10),
                    entry(CSS19, CSS_VALUE11)), new Point(53, 70), new Dimension(100, 70), false,
            true, true, INPUT_TAG, EMPTY, of());


    public static final MockWebElement COMMON_LABELED_RADIOBUTTON1 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE11),
                    entry(ATTR15, VALUE12)),
            ofEntries(entry(CSS18, CSS_VALUE12),
                    entry(CSS19, CSS_VALUE13)), new Point(11, 21), new Dimension(21, 32), true,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(21, 32), true,
                    false, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT1, of())
    ));

    public static final MockWebElement COMMON_LABELED_RADIOBUTTON2 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE13),
                    entry(ATTR15, VALUE14)),
            ofEntries(entry(CSS18, CSS_VALUE13),
                    entry(CSS19, CSS_VALUE14)), new Point(13, 23), new Dimension(24, 35), false,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    true, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT2, of())
    ));

    public static final MockWebElement COMMON_LABELED_RADIOBUTTON3 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE14),
                    entry(ATTR15, VALUE15)),
            ofEntries(entry(CSS18, CSS_VALUE14),
                    entry(CSS19, CSS_VALUE15)), new Point(13, 23), new Dimension(25, 36), false,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    true, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT3, of())
    ));

    public static final MockWebElement COMMON_LABELED_RADIOBUTTON4 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE15),
                    entry(ATTR15, VALUE16)),
            ofEntries(entry(CSS18, CSS_VALUE15),
                    entry(CSS19, CSS_VALUE16)), new Point(13, 23), new Dimension(44, 55), true,
            false, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    false, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT4, of())
    ));


    public static final MockWebElement COMMON_LABELED_RADIOBUTTON5 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE16),
                    entry(ATTR15, VALUE17)),
            ofEntries(entry(CSS18, CSS_VALUE16),
                    entry(CSS19, CSS_VALUE17)), new Point(55, 47), new Dimension(45, 69), true,
            false, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    false, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT5, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    false, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT9, of()
            )));

    public static final MockWebElement COMMON_LABELED_RADIOBUTTON6 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE17),
                    entry(ATTR15, VALUE18)),
            ofEntries(entry(CSS18, CSS_VALUE17),
                    entry(CSS19, CSS_VALUE18)), new Point(56, 48), new Dimension(46, 70), false,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    true, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT6, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    true, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT10, of()
            )));

    public static final MockWebElement COMMON_LABELED_RADIOBUTTON7 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE18),
                    entry(ATTR15, VALUE19)),
            ofEntries(entry(CSS18, CSS_VALUE18),
                    entry(CSS19, CSS_VALUE19)), new Point(57, 49), new Dimension(47, 71), true,
            true, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(52, 43),
                    new Dimension(34, 46), true,
                    true, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT7, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(33, 43),
                    new Dimension(53, 64), true,
                    true, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT11, of()
            )));

    public static final MockWebElement COMMON_LABELED_RADIOBUTTON8 = new MockWebElement(xpath(RADIO_BUTTON_XPATH),
            ofEntries(entry(ATTR14, VALUE19),
                    entry(ATTR15, VALUE20)),
            ofEntries(entry(CSS18, CSS_VALUE19),
                    entry(CSS19, CSS_VALUE20)), new Point(58, 50), new Dimension(48, 72), false,
            false, true, INPUT_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(53, 44),
                    new Dimension(35, 47), false,
                    false, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT8, of()),

            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(34, 44),
                    new Dimension(54, 65), false,
                    false, false, LABEL_TAG, RADIOBUTTON_LABEL_TEXT12, of()
            )));

    public static final MockWebElement COMMON_LINK1 = new MockWebElement(tagName(LINK_TAG),
            ofEntries(entry(ATTR17, VALUE20),
                    entry(ATTR18, VALUE1),
                    entry(HREF, LINK_REFERENCE1)),
            ofEntries(entry(CSS15, CSS_VALUE20),
                    entry(CSS20, CSS_VALUE1)), new Point(10, 20), new Dimension(20, 30), false,
            false, false, LINK_TAG, LINK_TEXT1, of());

    public static final MockWebElement COMMON_LINK2 = new MockWebElement(tagName(LINK_TAG),
            ofEntries(entry(ATTR17, VALUE1),
                    entry(ATTR18, VALUE2),
                    entry(HREF, LINK_REFERENCE2)),
            ofEntries(entry(CSS15, CSS_VALUE1),
                    entry(CSS20, CSS_VALUE2)), new Point(15, 25), new Dimension(25, 35), true,
            false, false, LINK_TAG, LINK_TEXT2, of());

    public static final MockWebElement COMMON_LINK3 = new MockWebElement(tagName(LINK_TAG),
            ofEntries(entry(ATTR17, VALUE2),
                    entry(ATTR18, VALUE3),
                    entry(HREF, LINK_REFERENCE3)),
            ofEntries(entry(CSS15, CSS_VALUE2),
                    entry(CSS20, CSS_VALUE3)), new Point(35, 45), new Dimension(45, 44), true,
            true, false, LINK_TAG, LINK_TEXT3, of());

    public static final MockWebElement COMMON_LINK4 = new MockWebElement(tagName(LINK_TAG),
            ofEntries(entry(ATTR17, VALUE3),
                    entry(ATTR18, VALUE4),
                    entry(HREF, LINK_REFERENCE4)),
            ofEntries(entry(CSS15, CSS_VALUE3),
                    entry(CSS20, CSS_VALUE4)), new Point(53, 70), new Dimension(100, 70), false,
            true, false, LINK_TAG, LINK_TEXT4, of());


    public static final MockWebElement COMMON_LABELED_LINK1 = new MockWebElement(tagName(LINK_TAG),
            ofEntries(entry(ATTR17, VALUE4),
                    entry(ATTR18, VALUE5),
                    entry(HREF, LINK_REFERENCE5)),
            ofEntries(entry(CSS15, CSS_VALUE4),
                    entry(CSS20, CSS_VALUE5)), new Point(11, 21), new Dimension(21, 32), true,
            true, false, LINK_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(21, 32), true,
                    true, false, LABEL_TAG, LINK_LABEL_TEXT1, of())
    ));

    public static final MockWebElement COMMON_LABELED_LINK2 = new MockWebElement(tagName(LINK_TAG),
            ofEntries(entry(ATTR17, VALUE4),
                    entry(ATTR18, VALUE5),
                    entry(HREF, LINK_REFERENCE6)),
            ofEntries(entry(CSS15, CSS_VALUE4),
                    entry(CSS20, CSS_VALUE5)), new Point(13, 23), new Dimension(24, 35), false,
            false, false, LINK_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    false, false, LABEL_TAG, LINK_LABEL_TEXT2, of())
    ));

    public static final MockWebElement COMMON_LABELED_LINK3 = new MockWebElement(tagName(LINK_TAG),
            ofEntries(entry(ATTR17, VALUE5),
                    entry(ATTR18, VALUE6),
                    entry(HREF, LINK_REFERENCE7)),
            ofEntries(entry(CSS15, CSS_VALUE5),
                    entry(CSS20, CSS_VALUE6)), new Point(13, 23), new Dimension(25, 36), false,
            false, false, LINK_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    false, false, LABEL_TAG, LINK_LABEL_TEXT3, of())
    ));

    public static final MockWebElement COMMON_LABELED_LINK4 = new MockWebElement(tagName(LINK_TAG),
            ofEntries(entry(ATTR17, VALUE6),
                    entry(ATTR18, VALUE7),
                    entry(HREF, LINK_REFERENCE8)),
            ofEntries(entry(CSS15, CSS_VALUE6),
                    entry(CSS20, CSS_VALUE7)), new Point(13, 23), new Dimension(44, 55), true,
            false, false, LINK_TAG, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    false, false, LABEL_TAG, LINK_LABEL_TEXT4, of())
    ));


    public static final MockWebElement CUSTOM_LABELED_LINK1 = new MockWebElement(cssSelector(CUSTOM_LINK_CSS),
            ofEntries(entry(ATTR17, VALUE7),
                    entry(ATTR18, VALUE8),
                    entry(HREF, LINK_REFERENCE9)),
            ofEntries(entry(CSS15, CSS_VALUE7),
                    entry(CSS20, CSS_VALUE8)), new Point(55, 47), new Dimension(45, 69), true,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    true, false, LABEL_TAG, LINK_LABEL_TEXT5, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    true, false, SPAN, LINK_LABEL_TEXT9, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_LINK2 = new MockWebElement(cssSelector(CUSTOM_LINK_CSS),
            ofEntries(entry(ATTR17, VALUE8),
                    entry(ATTR18, VALUE9),
                    entry(HREF, LINK_REFERENCE10)),
            ofEntries(entry(CSS15, CSS_VALUE8),
                    entry(CSS20, CSS_VALUE9)), new Point(56, 48), new Dimension(46, 70), false,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    true, false, LABEL_TAG, LINK_LABEL_TEXT6, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    true, false, SPAN, LINK_LABEL_TEXT10, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_LINK3 = new MockWebElement(cssSelector(CUSTOM_LINK_CSS),
            ofEntries(entry(ATTR17, VALUE9),
                    entry(ATTR18, VALUE10),
                    entry(HREF, LINK_REFERENCE11)),
            ofEntries(entry(CSS15, CSS_VALUE9),
                    entry(CSS20, CSS_VALUE10)), new Point(57, 49), new Dimension(47, 71), true,
            false, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(52, 43),
                    new Dimension(34, 46), true,
                    false, false, LABEL_TAG, LINK_LABEL_TEXT7, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(33, 43),
                    new Dimension(53, 64), true,
                    false, false, SPAN, LINK_LABEL_TEXT11, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_LINK4 = new MockWebElement(cssSelector(CUSTOM_LINK_CSS),
            ofEntries(entry(ATTR17, VALUE10),
                    entry(ATTR18, VALUE11),
                    entry(HREF, LINK_REFERENCE12)),
            ofEntries(entry(CSS15, CSS_VALUE10),
                    entry(CSS20, CSS_VALUE11)), new Point(58, 50), new Dimension(48, 72), false,
            false, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(53, 44),
                    new Dimension(35, 47), false,
                    false, false, LABEL_TAG, LINK_LABEL_TEXT8, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(34, 44),
                    new Dimension(54, 65), false,
                    false, false, SPAN, LINK_LABEL_TEXT12, of()
            )));

    public static final MockWebElement COMMON_TAB1 = new MockWebElement(CHAINED_FIND_TAB,
            ofEntries(entry(ATTR19, VALUE11),
                    entry(ATTR20, VALUE12)),
            ofEntries(entry(CSS15, CSS_VALUE11),
                    entry(CSS20, CSS_VALUE12)), new Point(10, 20), new Dimension(20, 30), false,
            false, false, LI, TAB_TEXT1, of());

    public static final MockWebElement COMMON_TAB2 = new MockWebElement(CHAINED_FIND_TAB,
            ofEntries(entry(ATTR19, VALUE12),
                    entry(ATTR20, VALUE13)),
            ofEntries(entry(CSS15, CSS_VALUE12),
                    entry(CSS20, CSS_VALUE13)), new Point(15, 25), new Dimension(25, 35), true,
            true, false, LI, TAB_TEXT2, of());

    public static final MockWebElement COMMON_TAB3 = new MockWebElement(CHAINED_FIND_TAB,
            ofEntries(entry(ATTR19, VALUE13),
                    entry(ATTR20, VALUE14)),
            ofEntries(entry(CSS15, CSS_VALUE13),
                    entry(CSS20, CSS_VALUE14)), new Point(35, 45), new Dimension(45, 44), true,
            true, false, LI, TAB_TEXT3, of());

    public static final MockWebElement COMMON_TAB4 = new MockWebElement(CHAINED_FIND_TAB,
            ofEntries(entry(ATTR19, VALUE14),
                    entry(ATTR20, VALUE15)),
            ofEntries(entry(CSS15, CSS_VALUE14),
                    entry(CSS20, CSS_VALUE15)), new Point(53, 70), new Dimension(100, 70), false,
            true, false, LI, TAB_TEXT4, of());


    public static final MockWebElement COMMON_LABELED_TAB1 = new MockWebElement(CHAINED_FIND_TAB,
            ofEntries(entry(ATTR19, VALUE15),
                    entry(ATTR20, VALUE16)),
            ofEntries(entry(CSS15, CSS_VALUE15),
                    entry(CSS20, CSS_VALUE16)), new Point(11, 21), new Dimension(21, 32), true,
            true, false, LI, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(21, 32), true,
                    true, false, LABEL_TAG, TAB_TEXT5, of())
    ));

    public static final MockWebElement COMMON_LABELED_TAB2 = new MockWebElement(CHAINED_FIND_TAB,
            ofEntries(entry(ATTR19, VALUE16),
                    entry(ATTR20, VALUE17)),
            ofEntries(entry(CSS15, CSS_VALUE16),
                    entry(CSS20, CSS_VALUE17)), new Point(13, 23), new Dimension(24, 35), false,
            false, false, LI, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    false, false, LABEL_TAG, TAB_TEXT6, of())
    ));

    public static final MockWebElement COMMON_LABELED_TAB3 = new MockWebElement(CHAINED_FIND_TAB,
            ofEntries(entry(ATTR19, VALUE17),
                    entry(ATTR20, VALUE18)),
            ofEntries(entry(CSS15, CSS_VALUE17),
                    entry(CSS20, CSS_VALUE18)), new Point(13, 23), new Dimension(25, 36), false,
            false, false, LI, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    false, false, LABEL_TAG, TAB_TEXT7, of())
    ));

    public static final MockWebElement COMMON_LABELED_TAB4 = new MockWebElement(CHAINED_FIND_TAB,
            ofEntries(entry(ATTR19, VALUE18),
                    entry(ATTR20, VALUE19)),
            ofEntries(entry(CSS15, CSS_VALUE18),
                    entry(CSS20, CSS_VALUE19)), new Point(13, 23), new Dimension(44, 55), true,
            false, false, LI, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    false, false, LABEL_TAG, TAB_TEXT8, of())
    ));


    public static final MockWebElement CUSTOM_LABELED_TAB1 = new MockWebElement(className(TAB_CLASS),
            ofEntries(entry(ATTR19, VALUE19),
                    entry(ATTR20, VALUE20)),
            ofEntries(entry(CSS15, CSS_VALUE19),
                    entry(CSS20, CSS_VALUE20)), new Point(55, 47), new Dimension(45, 69), true,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    true, false, LABEL_TAG, TAB_TEXT9, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    true, false, SPAN, TAB_TEXT13, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_TAB2 = new MockWebElement(className(TAB_CLASS),
            ofEntries(entry(ATTR19, VALUE20),
                    entry(ATTR20, VALUE1)),
            ofEntries(entry(CSS15, CSS_VALUE20),
                    entry(CSS20, CSS_VALUE1)), new Point(56, 48), new Dimension(46, 70), false,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    true, false, LABEL_TAG, TAB_TEXT10, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    true, false, SPAN, TAB_TEXT14, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_TAB3 = new MockWebElement(className(TAB_CLASS),
            ofEntries(entry(ATTR19, VALUE1),
                    entry(ATTR20, VALUE2)),
            ofEntries(entry(CSS15, CSS_VALUE1),
                    entry(CSS20, CSS_VALUE2)), new Point(57, 49), new Dimension(47, 71), true,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(52, 43),
                    new Dimension(34, 46), true,
                    true, false, LABEL_TAG, TAB_TEXT11, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(33, 43),
                    new Dimension(53, 64), true,
                    true, false, SPAN, TAB_TEXT15, of()
            )));

    public static final MockWebElement CUSTOM_LABELED_TAB4 = new MockWebElement(className(TAB_CLASS),
            ofEntries(entry(ATTR19, VALUE2),
                    entry(ATTR20, VALUE3)),
            ofEntries(entry(CSS15, CSS_VALUE2),
                    entry(CSS20, CSS_VALUE3)), new Point(58, 50), new Dimension(48, 72), false,
            false, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(53, 44),
                    new Dimension(35, 47), false,
                    false, false, LABEL_TAG, TAB_TEXT12, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(34, 44),
                    new Dimension(54, 65), false,
                    false, false, SPAN, TAB_TEXT16, of()
            )));

    public static final MockWebElement COMMON_SELECT1 = new MockWebElement(tagName(SELECT),
            ofEntries(entry(ATTR1, VALUE3),
                    entry(ATTR2, VALUE4),
                    entry(ATTR3, VALUE5)),
            ofEntries(entry(CSS1, CSS_VALUE3),
                    entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE5)), new Point(10, 20), new Dimension(20, 30), false,
            false, false, SELECT, EMPTY, of(
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                            false, false, true, OPTION, OPTION_TEXT1, of()),
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                            false, false, true, OPTION, OPTION_TEXT2, of()),
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                            false, false, true, OPTION, OPTION_TEXT3, of())
    ));

    public static final MockWebElement COMMON_SELECT2 = new MockWebElement(tagName(SELECT),
            ofEntries(entry(ATTR1, VALUE4),
                    entry(ATTR2, VALUE5),
                    entry(ATTR3, VALUE6)),
            ofEntries(entry(CSS1, CSS_VALUE4),
                    entry(CSS2, CSS_VALUE5),
                    entry(CSS3, CSS_VALUE6)), new Point(15, 25), new Dimension(25, 35), true,
            true, false, SELECT, EMPTY, of(
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                            false, true, true, OPTION, OPTION_TEXT4, of()),
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                            false, true, true, OPTION, OPTION_TEXT5, of()),
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                            false, true, true, OPTION, OPTION_TEXT6, of())
    ));

    public static final MockWebElement COMMON_SELECT3 = new MockWebElement(tagName(SELECT),
            ofEntries(entry(ATTR1, VALUE5),
                    entry(ATTR2, VALUE6),
                    entry(ATTR3, VALUE7)),
            ofEntries(entry(CSS1, CSS_VALUE5),
                    entry(CSS2, CSS_VALUE6),
                    entry(CSS3, CSS_VALUE7)), new Point(35, 45), new Dimension(45, 44), true,
            true, false, SELECT, EMPTY, of(
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                            false, true, true, OPTION, OPTION_TEXT7, of()),
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                            false, true, true, OPTION, OPTION_TEXT8, of()),
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                            false, true, true, OPTION, OPTION_TEXT9, of())

    ));

    public static final MockWebElement COMMON_SELECT4 = new MockWebElement(tagName(SELECT),
            ofEntries(entry(ATTR1, VALUE6),
                    entry(ATTR2, VALUE7),
                    entry(ATTR3, VALUE8)),
            ofEntries(entry(CSS1, CSS_VALUE6),
                    entry(CSS2, CSS_VALUE7),
                    entry(CSS3, CSS_VALUE8)), new Point(53, 70), new Dimension(100, 70), false,
            false, false, SELECT, EMPTY, of(
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                            false, false, true, OPTION, OPTION_TEXT10, of()),
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                            false, false, true, OPTION, OPTION_TEXT11, of()),
                    new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                            false, false, true, OPTION, OPTION_TEXT12, of())
    ));


    public static final MockWebElement COMMON_LABELED_SELECT1 = new MockWebElement(tagName(SELECT),
            ofEntries(entry(ATTR1, VALUE7),
                    entry(ATTR2, VALUE8),
                    entry(ATTR3, VALUE9)),
            ofEntries(entry(CSS1, CSS_VALUE7),
                    entry(CSS2, CSS_VALUE8),
                    entry(CSS3, CSS_VALUE9)), new Point(11, 21), new Dimension(21, 32), true,
            false, false, SELECT, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(21, 32), true,
                    false, false, LABEL_TAG, SELECT_LABEL_TEXT1, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                    false, false, true, OPTION, OPTION_TEXT13, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                    false, false, true, OPTION, OPTION_TEXT14, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                    false, false, true, OPTION, OPTION_TEXT15, of())
    ));

    public static final MockWebElement COMMON_LABELED_SELECT2 = new MockWebElement(tagName(SELECT),
            ofEntries(entry(ATTR1, VALUE8),
                    entry(ATTR2, VALUE9),
                    entry(ATTR3, VALUE10)),
            ofEntries(entry(CSS1, CSS_VALUE8),
                    entry(CSS2, CSS_VALUE9),
                    entry(CSS3, CSS_VALUE10)), new Point(13, 23), new Dimension(24, 35), false,
            false, false, SELECT, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    false, false, LABEL_TAG, SELECT_LABEL_TEXT2, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                    false, false, true, OPTION, OPTION_TEXT16, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                    false, false, true, OPTION, OPTION_TEXT17, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                    false, false, true, OPTION, OPTION_TEXT18, of())
    ));

    public static final MockWebElement COMMON_LABELED_SELECT3 = new MockWebElement(tagName(SELECT),
            ofEntries(entry(ATTR1, VALUE8),
                    entry(ATTR2, VALUE9),
                    entry(ATTR3, VALUE10)),
            ofEntries(entry(CSS1, CSS_VALUE8),
                    entry(CSS2, CSS_VALUE9),
                    entry(CSS3, CSS_VALUE10)), new Point(13, 23), new Dimension(25, 36), false,
            false, false, SELECT, OPTION_TEXT19, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    false, false, LABEL_TAG, SELECT_LABEL_TEXT3, of()),

            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                    false, false, true, OPTION, OPTION_TEXT20, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                    false, false, true, OPTION, OPTION_TEXT21, of())
    ));

    public static final MockWebElement COMMON_LABELED_SELECT4 = new MockWebElement(tagName(SELECT),
            ofEntries(entry(ATTR1, VALUE9),
                    entry(ATTR2, VALUE10),
                    entry(ATTR3, VALUE11)),
            ofEntries(entry(CSS1, CSS_VALUE9),
                    entry(CSS2, CSS_VALUE10),
                    entry(CSS3, CSS_VALUE11)), new Point(13, 23), new Dimension(44, 55), true,
            true, false, SELECT, OPTION_TEXT23, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    true, false, LABEL_TAG, SELECT_LABEL_TEXT4, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                    false, true, true, OPTION, OPTION_TEXT22, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                    false, true, true, OPTION, OPTION_TEXT22, of()),
            new MockWebElement(tagName(OPTION), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                    false, true, true, OPTION, OPTION_TEXT24, of())
    ));


    public static final MockWebElement MULTI_SELECT1 = new MockWebElement(className(MULTI_SELECT_CLASS),
            ofEntries(entry(ATTR1, VALUE10),
                    entry(ATTR2, VALUE11),
                    entry(ATTR3, VALUE12)),
            ofEntries(entry(CSS1, CSS_VALUE10),
                    entry(CSS2, CSS_VALUE11),
                    entry(CSS3, CSS_VALUE12)), new Point(55, 47), new Dimension(45, 69), true,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    true, false, LABEL_TAG, SELECT_LABEL_TEXT1, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    true, false, SPAN, SELECT_LABEL_TEXT9, of()),

            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                    false, true, true, DIV, OPTION_TEXT25, of()),
            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                    false, true, true, DIV, OPTION_TEXT26, of()),
            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                    false, true, true, DIV, OPTION_TEXT27, of())
    ));

    public static final MockWebElement MULTI_SELECT2 = new MockWebElement(className(MULTI_SELECT_CLASS),
            ofEntries(entry(ATTR1, VALUE11),
                    entry(ATTR2, VALUE12),
                    entry(ATTR3, VALUE13)),
            ofEntries(entry(CSS1, CSS_VALUE11),
                    entry(CSS2, CSS_VALUE12),
                    entry(CSS3, CSS_VALUE13)), new Point(56, 48), new Dimension(46, 70), false,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    true, false, LABEL_TAG, SELECT_LABEL_TEXT6, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    true, false, SPAN, SELECT_LABEL_TEXT10, of()),

            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                    false, true, true, DIV, OPTION_TEXT28, of()),
            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                    false, true, true, DIV, OPTION_TEXT29, of()),
            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                    false, true, true, DIV, OPTION_TEXT30, of())
    ));

    public static final MockWebElement MULTI_SELECT3 = new MockWebElement(className(MULTI_SELECT_CLASS),
            ofEntries(entry(ATTR1, VALUE12),
                    entry(ATTR2, VALUE13),
                    entry(ATTR3, VALUE14)),
            ofEntries(entry(CSS1, CSS_VALUE12),
                    entry(CSS2, CSS_VALUE13),
                    entry(CSS3, CSS_VALUE14)), new Point(57, 49), new Dimension(47, 71), true,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(52, 43),
                    new Dimension(34, 46), true,
                    true, false, LABEL_TAG, SELECT_LABEL_TEXT7, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(33, 43),
                    new Dimension(53, 64), true,
                    true, false, SPAN, SELECT_LABEL_TEXT11, of()),

            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                    false, true, true, DIV, OPTION_TEXT31, of()),
            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                    false, true, true, DIV, OPTION_TEXT32, of()),
            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(3, 4), new Dimension(5, 6),
                    false, true, true, DIV, OPTION_TEXT33, of())
    ));

    public static final MockWebElement MULTI_SELECT4 = new MockWebElement(className(MULTI_SELECT_CLASS),
            ofEntries(entry(ATTR1, VALUE13),
                    entry(ATTR2, VALUE14),
                    entry(ATTR3, VALUE15)),
            ofEntries(entry(CSS1, CSS_VALUE13),
                    entry(CSS2, CSS_VALUE14),
                    entry(CSS3, CSS_VALUE15)), new Point(58, 50), new Dimension(48, 72), false,
            false, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(53, 44),
                    new Dimension(35, 47), false,
                    false, false, LABEL_TAG, SELECT_LABEL_TEXT8, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(34, 44),
                    new Dimension(54, 65), false,
                    false, false, SPAN, SELECT_LABEL_TEXT12, of()),

            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(1, 2), new Dimension(2, 3),
                    false, false, true, DIV, OPTION_TEXT34, of()),
            new MockWebElement(className(ITEM_OPTION_CLASS), ofEntries(), ofEntries(), new Point(2, 3), new Dimension(4, 5),
                    false, false, true, DIV, OPTION_TEXT35, of())
    ));

    public static final MockWebElement COMMON_TABLE1 = new MockWebElement(tagName(TABLE),
            ofEntries(entry(ATTR1, VALUE3),
                    entry(ATTR2, VALUE4),
                    entry(ATTR3, VALUE5)),
            ofEntries(entry(CSS1, CSS_VALUE3),
                    entry(CSS2, CSS_VALUE4),
                    entry(CSS3, CSS_VALUE5)), new Point(10, 20), new Dimension(20, 30), false,
            false, false, TABLE, EMPTY,
            of(new MockWebElement(CHAINED_FIND_HEADER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, HEADER_TEXT1,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, HEADER_TEXT2,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, HEADER_TEXT3,
                                    of())
                    )),
                    new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                            of(
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT1,
                                            of()),
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT2,
                                            of()),
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT3,
                                            of())
                            )),
                    new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                            of(
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT4,
                                            of()),
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT5,
                                            of()),
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT6,
                                            of())
                            )),
                    new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                            of(
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT7,
                                            of()),
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT8,
                                            of()),
                                    new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT9,
                                            of())
                            )),
                    new MockWebElement(CHAINED_FIND_FOOTER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                            of(
                                    new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, FOOTER_TEXT1,
                                            of()),
                                    new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, FOOTER_TEXT2,
                                            of()),
                                    new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, FOOTER_TEXT3,
                                            of())
                            ))
            ));

    public static final MockWebElement COMMON_TABLE2 = new MockWebElement(tagName(TABLE),
            ofEntries(entry(ATTR1, VALUE4),
                    entry(ATTR2, VALUE5),
                    entry(ATTR3, VALUE6)),
            ofEntries(entry(CSS1, CSS_VALUE4),
                    entry(CSS2, CSS_VALUE5),
                    entry(CSS3, CSS_VALUE6)), new Point(15, 25), new Dimension(25, 35), true,
            false, false, TABLE, EMPTY, of(new MockWebElement(CHAINED_FIND_HEADER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, HEADER_TEXT4,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, HEADER_TEXT5,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, HEADER_TEXT6,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT10,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT11,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT12,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT13,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT14,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT15,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT16,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT17,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT18,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_FOOTER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, FOOTER_TEXT4,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, FOOTER_TEXT5,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, FOOTER_TEXT6,
                                    of())
                    ))
    ));

    public static final MockWebElement COMMON_TABLE3 = new MockWebElement(tagName(TABLE),
            ofEntries(entry(ATTR1, VALUE5),
                    entry(ATTR2, VALUE6),
                    entry(ATTR3, VALUE7)),
            ofEntries(entry(CSS1, CSS_VALUE5),
                    entry(CSS2, CSS_VALUE6),
                    entry(CSS3, CSS_VALUE7)), new Point(35, 45), new Dimension(45, 44), true,
            true, false, TABLE, EMPTY, of(new MockWebElement(CHAINED_FIND_HEADER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, HEADER_TEXT7,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, HEADER_TEXT8,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, HEADER_TEXT9,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT19,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT20,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT21,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT22,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT23,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT24,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT25,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT26,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT27,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_FOOTER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, FOOTER_TEXT7,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, FOOTER_TEXT8,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, FOOTER_TEXT9,
                                    of())
                    ))
    ));

    public static final MockWebElement COMMON_TABLE4 = new MockWebElement(tagName(TABLE),
            ofEntries(entry(ATTR1, VALUE6),
                    entry(ATTR2, VALUE7),
                    entry(ATTR3, VALUE8)),
            ofEntries(entry(CSS1, CSS_VALUE6),
                    entry(CSS2, CSS_VALUE7),
                    entry(CSS3, CSS_VALUE8)), new Point(53, 70), new Dimension(100, 70), false,
            true, false, TABLE, EMPTY, of(new MockWebElement(CHAINED_FIND_HEADER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, HEADER_TEXT10,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, HEADER_TEXT11,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, HEADER_TEXT12,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT28,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT29,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT30,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT31,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT32,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT33,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT34,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT35,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT36,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_FOOTER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, FOOTER_TEXT10,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, FOOTER_TEXT11,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, FOOTER_TEXT12,
                                    of())
                    ))
    ));


    public static final MockWebElement COMMON_LABELED_TABLE1 = new MockWebElement(tagName(TABLE),
            ofEntries(entry(ATTR1, VALUE7),
                    entry(ATTR2, VALUE8),
                    entry(ATTR3, VALUE9)),
            ofEntries(entry(CSS1, CSS_VALUE7),
                    entry(CSS2, CSS_VALUE8),
                    entry(CSS3, CSS_VALUE9)),  new Point(11, 21), new Dimension(21, 32), true,
            false, false, TABLE, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(21, 32), true,
                    false, false, LABEL_TAG, TABLE_LABEL_TEXT1, of()),

            new MockWebElement(CHAINED_FIND_HEADER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, HEADER_TEXT13,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, HEADER_TEXT14,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, HEADER_TEXT15,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT37,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT38,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT39,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT40,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT41,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT42,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT43,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT44,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TD, CELL_TEXT45,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_FOOTER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, FOOTER_TEXT13,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, FOOTER_TEXT14,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, TH, FOOTER_TEXT15,
                                    of())
                    ))
    ));

    public static final MockWebElement COMMON_LABELED_TABLE2 = new MockWebElement(tagName(TABLE),
            ofEntries(entry(ATTR1, VALUE8),
                    entry(ATTR2, VALUE9),
                    entry(ATTR3, VALUE10)),
            ofEntries(entry(CSS1, CSS_VALUE8),
                    entry(CSS2, CSS_VALUE9),
                    entry(CSS3, CSS_VALUE10)), new Point(13, 23), new Dimension(24, 35), false,
            true, false, TABLE, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(11, 21),
                    new Dimension(25, 31), false,
                    true, false, LABEL_TAG, TABLE_LABEL_TEXT2, of()),

            new MockWebElement(CHAINED_FIND_HEADER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, HEADER_TEXT16,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, HEADER_TEXT17,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, HEADER_TEXT18,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT46,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT47,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT48,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT49,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT50,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT51,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT52,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT53,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TD, CELL_TEXT54,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_FOOTER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, FOOTER_TEXT16,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, FOOTER_TEXT17,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, TH, FOOTER_TEXT18,
                                    of())
                    ))
    ));

    public static final MockWebElement COMMON_LABELED_TABLE3 = new MockWebElement(tagName(TABLE),
            ofEntries(entry(ATTR1, VALUE9),
                    entry(ATTR2, VALUE10),
                    entry(ATTR3, VALUE11)),
            ofEntries(entry(CSS1, CSS_VALUE9),
                    entry(CSS2, CSS_VALUE10),
                    entry(CSS3, CSS_VALUE11)), new Point(13, 23), new Dimension(25, 36), false,
            false, false, TABLE, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(12, 22),
                    new Dimension(44, 37), false,
                    false, false, LABEL_TAG, TABLE_LABEL_TEXT3, of()),

            new MockWebElement(CHAINED_FIND_HEADER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, HEADER_TEXT19,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, HEADER_TEXT20,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, HEADER_TEXT21,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT55,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT56,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT57,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT58,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT59,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT60,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT61,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT62,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TD, CELL_TEXT63,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_FOOTER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, FOOTER_TEXT19,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, FOOTER_TEXT20,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, false, false, TH, FOOTER_TEXT21,
                                    of())
                    ))
    ));

    public static final MockWebElement COMMON_LABELED_TABLE4 = new MockWebElement(tagName(TABLE),
            ofEntries(entry(ATTR1, VALUE10),
                    entry(ATTR2, VALUE11),
                    entry(ATTR3, VALUE12)),
            ofEntries(entry(CSS1, CSS_VALUE10),
                    entry(CSS2, CSS_VALUE11),
                    entry(CSS3, CSS_VALUE12)), new Point(13, 23), new Dimension(44, 55), true,
            true, false, TABLE, EMPTY, of(
            new MockWebElement(tagName(LABEL_TAG), ofEntries(), ofEntries(), new Point(13, 27),
                    new Dimension(36, 52), true,
                    true, false, LABEL_TAG, TABLE_LABEL_TEXT4, of()),

            new MockWebElement(CHAINED_FIND_HEADER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, HEADER_TEXT22,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, HEADER_TEXT23,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, HEADER_TEXT24,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT64,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT65,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT66,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT67,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT68,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT69,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_ROW, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT70,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT71,
                                    of()),
                            new MockWebElement(tagName(TD), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TD, CELL_TEXT72,
                                    of())
                    )),
            new MockWebElement(CHAINED_FIND_FOOTER, ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TR, EMPTY,
                    of(
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, FOOTER_TEXT22,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, FOOTER_TEXT23,
                                    of()),
                            new MockWebElement(tagName(TH), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, true, false, TH, FOOTER_TEXT24,
                                    of())
                    ))
    ));


    public static final MockWebElement SPREAD_SHEET_TABLE1 = new MockWebElement(className(SPREAD_SHEET_CLASS),
            ofEntries(entry(ATTR1, VALUE11),
                    entry(ATTR2, VALUE12),
                    entry(ATTR3, VALUE13)),
            ofEntries(entry(CSS1, CSS_VALUE11),
                    entry(CSS2, CSS_VALUE12),
                    entry(CSS3, CSS_VALUE13)), new Point(55, 47), new Dimension(45, 69), true,
            false, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(50, 41),
                    new Dimension(33, 45), true,
                    false, false, LABEL_TAG, TABLE_LABEL_TEXT5, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(31, 41),
                    new Dimension(51, 62), true,
                    false, false, SPAN, TABLE_LABEL_TEXT9, of()),

            new MockWebElement(className(HEADLINE_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, SPAN, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, HEADER_TEXT25,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, HEADER_TEXT26,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, HEADER_TEXT27,
                                    of(INVISIBLE_SPAN_ELEMENT))
                    )),
            new MockWebElement(className(STRING_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT73,
                                    of(CUSTOM_LABELED_BUTTON1)),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT74,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT75,
                                    of())
                    )),
            new MockWebElement(className(STRING_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT76,
                                    of(TEXT_AREA1)),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT77,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT78,
                                    of())
                    )),
            new MockWebElement(className(STRING_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT79,
                                    of(COMMON_LABELED_CHECKBOX5)),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT80,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, CELL_TEXT81,
                                    of())
                    )),
            new MockWebElement(className(FOOTER_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, SPAN, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, FOOTER_TEXT25,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, FOOTER_TEXT26,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), true, false, false, DIV, FOOTER_TEXT27,
                                    of())
                    ))
    ));

    public static final MockWebElement SPREAD_SHEET_TABLE2 = new MockWebElement(className(SPREAD_SHEET_CLASS),
            ofEntries(entry(ATTR1, VALUE12),
                    entry(ATTR2, VALUE13),
                    entry(ATTR3, VALUE15)),
            ofEntries(entry(CSS1, CSS_VALUE12),
                    entry(CSS2, CSS_VALUE13),
                    entry(CSS3, CSS_VALUE15)), new Point(56, 48), new Dimension(46, 70), false,
            true, false, DIV, EMPTY, of(
            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(51, 42),
                    new Dimension(33, 45), false,
                    true, false, LABEL_TAG, TABLE_LABEL_TEXT6, of()),

            new MockWebElement(CUSTOM_LABEL_BY, ofEntries(), ofEntries(), new Point(32, 42),
                    new Dimension(52, 63), false,
                    true, false, SPAN, TABLE_LABEL_TEXT10, of()),

            new MockWebElement(className(HEADLINE_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, SPAN, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, HEADER_TEXT28,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, HEADER_TEXT29,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, HEADER_TEXT30,
                                    of())
                    )),
            new MockWebElement(className(STRING_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT81,
                                    of(CUSTOM_LABELED_BUTTON2)),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT82,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT83,
                                    of())
                    )),
            new MockWebElement(className(STRING_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT84,
                                    of(TEXT_AREA2)),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT85,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT86,
                                    of())
                    )),
            new MockWebElement(className(STRING_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT87,
                                    of(COMMON_LABELED_CHECKBOX6)),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT88,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, CELL_TEXT89,
                                    of())
                    )),
            new MockWebElement(className(FOOTER_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, SPAN, EMPTY,
                    of(
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, FOOTER_TEXT28,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, FOOTER_TEXT29,
                                    of()),
                            new MockWebElement(className(CELL_CLASS), ofEntries(), ofEntries(), new Point(0, 0), new Dimension(0, 0), false, true, false, DIV, FOOTER_TEXT30,
                                    of())
                    ))
    ));

    private final static List<MockWebElement> fakeMock = of(
            VISIBLE_DIV_ELEMENT,
            INVISIBLE_SPAN_ELEMENT,
            COMMON_BUTTON1, COMMON_BUTTON2, COMMON_BUTTON3, COMMON_BUTTON4,
            COMMON_LABELED_BUTTON1, COMMON_LABELED_BUTTON2, COMMON_LABELED_BUTTON3, COMMON_LABELED_BUTTON4,
            CUSTOM_LABELED_BUTTON1, CUSTOM_LABELED_BUTTON2, CUSTOM_LABELED_BUTTON3, CUSTOM_LABELED_BUTTON4,
            COMMON_LABELED_INPUT1, COMMON_LABELED_INPUT2, COMMON_LABELED_INPUT3, COMMON_LABELED_INPUT4,
            COMMON_TEXT_INPUT1, COMMON_TEXT_INPUT2, COMMON_TEXT_INPUT3, COMMON_TEXT_INPUT4,
            TEXT_AREA1, TEXT_AREA2, TEXT_AREA3, TEXT_AREA4,
            COMMON_CHECKBOX1, COMMON_CHECKBOX2, COMMON_CHECKBOX3, COMMON_CHECKBOX4,
            COMMON_LABELED_CHECKBOX1, COMMON_LABELED_CHECKBOX2, COMMON_LABELED_CHECKBOX3, COMMON_LABELED_CHECKBOX4,
            COMMON_LABELED_CHECKBOX5, COMMON_LABELED_CHECKBOX6, COMMON_LABELED_CHECKBOX7, COMMON_LABELED_CHECKBOX8,
            COMMON_RADIOBUTTON1, COMMON_RADIOBUTTON2, COMMON_RADIOBUTTON3, COMMON_RADIOBUTTON4,
            COMMON_LABELED_RADIOBUTTON1, COMMON_LABELED_RADIOBUTTON2, COMMON_LABELED_RADIOBUTTON3, COMMON_LABELED_RADIOBUTTON4,
            COMMON_LABELED_RADIOBUTTON5, COMMON_LABELED_RADIOBUTTON6, COMMON_LABELED_RADIOBUTTON7, COMMON_LABELED_RADIOBUTTON8,
            COMMON_LABELED_LINK1, COMMON_LABELED_LINK2, COMMON_LABELED_LINK3, COMMON_LABELED_LINK4,
            CUSTOM_LABELED_LINK1, CUSTOM_LABELED_LINK2, CUSTOM_LABELED_LINK3, CUSTOM_LABELED_LINK4,
            COMMON_LINK1, COMMON_LINK2, COMMON_LINK3, COMMON_LINK4,
            COMMON_LABELED_TAB1, COMMON_LABELED_TAB2, COMMON_LABELED_TAB3, COMMON_LABELED_TAB4,
            COMMON_TAB1, COMMON_TAB2, COMMON_TAB3, COMMON_TAB4, CUSTOM_LABELED_TAB1, CUSTOM_LABELED_TAB2, CUSTOM_LABELED_TAB3,
            CUSTOM_LABELED_TAB4,
            COMMON_LABELED_SELECT1, COMMON_LABELED_SELECT2, COMMON_LABELED_SELECT3, COMMON_LABELED_SELECT4, COMMON_SELECT1,
            COMMON_SELECT2, COMMON_SELECT3, COMMON_SELECT4, MULTI_SELECT1, MULTI_SELECT2, MULTI_SELECT3, MULTI_SELECT4,
            COMMON_LABELED_TABLE1, COMMON_LABELED_TABLE2, COMMON_LABELED_TABLE3, COMMON_LABELED_TABLE4,
            COMMON_TABLE1, COMMON_TABLE2, COMMON_TABLE3, COMMON_TABLE4, SPREAD_SHEET_TABLE1, SPREAD_SHEET_TABLE2
    );

    static List<MockWebElement> getFakeDOM() {
        return fakeMock;
    }
}
