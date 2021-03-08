package ru.tinkoff.qa.neptune.selenium.test.steps.tests.interaction;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Sequence;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.time.Duration.ofSeconds;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.Keys.TAB;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.SequenceSpy.getActions;
import static ru.tinkoff.qa.neptune.selenium.test.SequenceSpy.setActions;

@SuppressWarnings("unchecked")
public class InteractionTest extends BaseWebDriverTest {

    public static <T, V> Matcher<Map<T, V>> hasAllEntries(Map<Object, Object> entries) {
        List<Matcher<? super Map<T, V>>> matchers = new ArrayList<>(entries.size());
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            var value = entry.getValue();
            if (value instanceof Matcher<?>) {
                matchers.add(Matchers.hasEntry((Matcher<T>) entry.getKey(), (Matcher<V>) value));
            } else {
                matchers.add(Matchers.hasEntry(entry.getKey(), value));
            }
        }
        return AllOf.allOf(matchers);
    }

    @BeforeMethod
    public void beforeEveryTest() {
        setActions(null);
    }

    @DataProvider
    public Object[][] commonData() {
        return new Object[][]{
                {COMMON_BUTTON1},
                {button()},
                {seleniumSteps.find(button())}
        };
    }

    @DataProvider
    public Object[][] moveData() {
        return new Object[][]{
                {COMMON_BUTTON1, null},
                {button(), null},
                {seleniumSteps.find(button()), null},
                {COMMON_BUTTON1, new Point(2, 3)},
                {button(), new Point(2, 3)},
                {seleniumSteps.find(button()), new Point(2, 3)}
        };
    }

    @DataProvider
    public Object[][] dragAndDropData() {
        return new Object[][]{
                {COMMON_BUTTON1, COMMON_LABELED_BUTTON1},
                {COMMON_BUTTON1, button(BUTTON_LABEL_TEXT1)},
                {COMMON_BUTTON1, seleniumSteps.find(button(BUTTON_LABEL_TEXT1))},
                {button(), COMMON_LABELED_BUTTON1},
                {button(), button(BUTTON_LABEL_TEXT1)},
                {button(), seleniumSteps.find(button(BUTTON_LABEL_TEXT1))},
                {seleniumSteps.find(button()), COMMON_LABELED_BUTTON1},
                {seleniumSteps.find(button()), button(BUTTON_LABEL_TEXT1)},
                {seleniumSteps.find(button()), seleniumSteps.find(button(BUTTON_LABEL_TEXT1))},
        };
    }

    @Test
    public void test1() {
        seleniumSteps.interactive(click());
        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test2(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(click((WebElement) o));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(click((Widget) o));
        } else {
            seleniumSteps.interactive(click((SearchSupplier<?>) o));
        }

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 0),
                        entry("y", 0),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                )),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test
    public void test3() {
        seleniumSteps.interactive(keyDown(CONTROL));

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry("type", "keyDown"),
                        entry("value", CONTROL.toString()))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test4(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(keyDown((WebElement) o, CONTROL));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(keyDown((Widget) o, CONTROL));
        } else {
            seleniumSteps.interactive(keyDown((SearchSupplier<?>) o, CONTROL));
        }

        var sequences = getActions().stream().map(Sequence::toJson).collect(toList());
        assertThat(sequences, hasSize(2));
        assertThat(sequences, everyItem(hasKey("actions")));

        var actions = sequences.stream().map(s -> (List<Map<String, Object>>) s.get("actions")).collect(toList());

        assertThat(actions, containsInAnyOrder(
                contains(
                        hasAllEntries(ofEntries(
                                entry(equalTo("duration"), instanceOf(Long.class)),
                                entry("x", 0),
                                entry("y", 0),
                                entry("type", "pointerMove"),
                                entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                        )),
                        hasAllEntries(ofEntries(
                                entry("button", 0),
                                entry("type", "pointerDown")
                        )),
                        hasAllEntries(ofEntries(
                                entry("button", 0),
                                entry("type", "pointerUp")
                        )),
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        ))
                ),
                contains(
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),

                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),

                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),

                        hasAllEntries(ofEntries(
                                entry("type", "keyDown"),
                                entry("value", CONTROL.toString())
                        ))
                )
        ));
    }

    @Test
    public void test5() {
        seleniumSteps.interactive(keyUp(CONTROL));

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry("type", "keyUp"),
                        entry("value", CONTROL.toString()))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test6(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(keyUp((WebElement) o, CONTROL));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(keyUp((Widget) o, CONTROL));
        } else {
            seleniumSteps.interactive(keyUp((SearchSupplier<?>) o, CONTROL));
        }

        var sequences = getActions().stream().map(Sequence::toJson).collect(toList());
        assertThat(sequences, hasSize(2));
        assertThat(sequences, everyItem(hasKey("actions")));

        var actions = sequences.stream().map(s -> (List<Map<String, Object>>) s.get("actions")).collect(toList());

        assertThat(actions, containsInAnyOrder(
                contains(
                        hasAllEntries(ofEntries(
                                entry(equalTo("duration"), instanceOf(Long.class)),
                                entry("x", 0),
                                entry("y", 0),
                                entry("type", "pointerMove"),
                                entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                        )),
                        hasAllEntries(ofEntries(
                                entry("button", 0),
                                entry("type", "pointerDown")
                        )),
                        hasAllEntries(ofEntries(
                                entry("button", 0),
                                entry("type", "pointerUp")
                        )),
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        ))
                ),
                contains(
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),

                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),

                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),

                        hasAllEntries(ofEntries(
                                entry("type", "keyUp"),
                                entry("value", CONTROL.toString())
                        ))
                )
        ));
    }

    @Test
    public void test7() {
        seleniumSteps.interactive(sendKeys(TAB, "A"));

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry("type", "keyDown"),
                        entry("value", TAB.toString()))
                ),

                hasAllEntries(ofEntries(
                        entry("type", "keyUp"),
                        entry("value", TAB.toString()))
                ),

                hasAllEntries(ofEntries(
                        entry("type", "keyDown"),
                        entry("value", "A"))
                ),

                hasAllEntries(ofEntries(
                        entry("type", "keyUp"),
                        entry("value", "A"))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test8(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(sendKeys((WebElement) o, TAB, "A"));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(sendKeys((Widget) o, TAB, "A"));
        } else {
            seleniumSteps.interactive(sendKeys((SearchSupplier<?>) o, TAB, "A"));
        }

        var sequences = getActions().stream().map(Sequence::toJson).collect(toList());
        assertThat(sequences, hasSize(2));
        assertThat(sequences, everyItem(hasKey("actions")));

        var actions = sequences.stream().map(s -> (List<Map<String, Object>>) s.get("actions")).collect(toList());

        assertThat(actions, containsInAnyOrder(
                contains(
                        hasAllEntries(ofEntries(
                                entry(equalTo("duration"), instanceOf(Long.class)),
                                entry("x", 0),
                                entry("y", 0),
                                entry("type", "pointerMove"),
                                entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                        )),
                        hasAllEntries(ofEntries(
                                entry("button", 0),
                                entry("type", "pointerDown")
                        )),
                        hasAllEntries(ofEntries(
                                entry("button", 0),
                                entry("type", "pointerUp")
                        )),
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        ))
                ),
                contains(
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),
                        hasAllEntries(ofEntries(
                                entry("duration", 0L),
                                entry("type", "pause")
                        )),
                        hasAllEntries(ofEntries(
                                entry("type", "keyDown"),
                                entry("value", TAB.toString()))
                        ),

                        hasAllEntries(ofEntries(
                                entry("type", "keyUp"),
                                entry("value", TAB.toString()))
                        ),

                        hasAllEntries(ofEntries(
                                entry("type", "keyDown"),
                                entry("value", "A"))
                        ),

                        hasAllEntries(ofEntries(
                                entry("type", "keyUp"),
                                entry("value", "A"))
                        )
                )
        ));
    }

    @Test
    public void test9() {
        seleniumSteps.interactive(clickAndHold());

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test10(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(clickAndHold((WebElement) o));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(clickAndHold((Widget) o));
        } else {
            seleniumSteps.interactive(clickAndHold((SearchSupplier<?>) o));
        }

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 0),
                        entry("y", 0),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                )),
                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                )
        ));
    }


    @Test
    public void test11() {
        seleniumSteps.interactive(release());

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test12(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(release((WebElement) o));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(release((Widget) o));
        } else {
            seleniumSteps.interactive(release((SearchSupplier<?>) o));
        }

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 0),
                        entry("y", 0),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                )),
                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test(dataProvider = "moveData")
    public void test13(Object o, Point c) {
        var cls = o.getClass();
        ofNullable(c).ifPresentOrElse(p -> {
                    if (WebElement.class.isAssignableFrom(cls)) {
                        seleniumSteps.interactive(moveToElement((WebElement) o, p.x, p.y));
                    } else if (Widget.class.isAssignableFrom(cls)) {
                        seleniumSteps.interactive(moveToElement((Widget) o, p.x, p.y));
                    } else {
                        seleniumSteps.interactive(moveToElement((SearchSupplier<?>) o, p.x, p.y));
                    }
                },
                () -> {
                    if (WebElement.class.isAssignableFrom(cls)) {
                        seleniumSteps.interactive(moveToElement((WebElement) o));
                    } else if (Widget.class.isAssignableFrom(cls)) {
                        seleniumSteps.interactive(moveToElement((Widget) o));
                    } else {
                        seleniumSteps.interactive(moveToElement((SearchSupplier<?>) o));
                    }
                });

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", ofNullable(c).map(Point::getX).orElse(0)),
                        entry("y", ofNullable(c).map(Point::getY).orElse(0)),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                ))
        ));
    }

    @Test
    public void test14() {
        seleniumSteps.interactive(moveByOffset(2, 3));

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 2),
                        entry("y", 3),
                        entry("type", "pointerMove"),
                        entry("origin", "pointer")
                ))
        ));
    }

    @Test
    public void test15() {
        seleniumSteps.interactive(contextClick());
        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry("button", 2),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 2),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test16(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(contextClick((WebElement) o));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(contextClick((Widget) o));
        } else {
            seleniumSteps.interactive(contextClick((SearchSupplier<?>) o));
        }

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 0),
                        entry("y", 0),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                )),

                hasAllEntries(ofEntries(
                        entry("button", 2),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 2),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test
    public void test17() {
        seleniumSteps.interactive(doubleClick());
        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test18(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(doubleClick((WebElement) o));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(doubleClick((Widget) o));
        } else {
            seleniumSteps.interactive(doubleClick((SearchSupplier<?>) o));
        }

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 0),
                        entry("y", 0),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                )),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test(dataProvider = "commonData")
    public void test19(Object o) {
        var cls = o.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(dragAndDropBy((WebElement) o, 2, 3));
        } else if (Widget.class.isAssignableFrom(cls)) {
            seleniumSteps.interactive(dragAndDropBy((Widget) o, 2, 3));
        } else {
            seleniumSteps.interactive(dragAndDropBy((SearchSupplier<?>) o, 2, 3));
        }

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 0),
                        entry("y", 0),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                )),
                hasAllEntries(ofEntries(
                        entry(equalTo("button"), is(0)),
                        entry(equalTo("type"), equalTo("pointerDown"))
                )),
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 2),
                        entry("y", 3),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo("pointer"))
                )),
                hasAllEntries(ofEntries(
                        entry(equalTo("button"), is(0)),
                        entry(equalTo("type"), equalTo("pointerUp"))
                ))

        ));
    }

    @Test(dataProvider = "dragAndDropData")
    public void test20(Object o1, Object o2) {
        var cls1 = o1.getClass();
        var cls2 = o2.getClass();

        if (WebElement.class.isAssignableFrom(cls1)) {
            var we = (WebElement) o1;

            if (WebElement.class.isAssignableFrom(cls2)) {
                seleniumSteps.interactive(dragAndDrop(we, (WebElement) o2));
            } else if (Widget.class.isAssignableFrom(cls2)) {
                seleniumSteps.interactive(dragAndDrop(we, (Widget) o2));
            } else {
                seleniumSteps.interactive(dragAndDrop(we, (SearchSupplier<?>) o2));
            }
        } else if (Widget.class.isAssignableFrom(cls1)) {
            var wi = (Widget) o1;

            if (WebElement.class.isAssignableFrom(cls2)) {
                seleniumSteps.interactive(dragAndDrop(wi, (WebElement) o2));
            } else if (Widget.class.isAssignableFrom(cls2)) {
                seleniumSteps.interactive(dragAndDrop(wi, (Widget) o2));
            } else {
                seleniumSteps.interactive(dragAndDrop(wi, (SearchSupplier<?>) o2));
            }
        } else {
            var ss = (SearchSupplier<?>) o1;

            if (WebElement.class.isAssignableFrom(cls2)) {
                seleniumSteps.interactive(dragAndDrop(ss, (WebElement) o2));
            } else if (Widget.class.isAssignableFrom(cls2)) {
                seleniumSteps.interactive(dragAndDrop(ss, (Widget) o2));
            } else {
                seleniumSteps.interactive(dragAndDrop(ss, (SearchSupplier<?>) o2));
            }
        }

        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, contains(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 0),
                        entry("y", 0),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_BUTTON1))
                )),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerDown"))
                ),

                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), instanceOf(Long.class)),
                        entry("x", 0),
                        entry("y", 0),
                        entry("type", "pointerMove"),
                        entry(equalTo("origin"), equalTo(COMMON_LABELED_BUTTON1))
                )),

                hasAllEntries(ofEntries(
                        entry("button", 0),
                        entry("type", "pointerUp"))
                )
        ));
    }

    @Test(description = "pause test")
    public void test21() {
        seleniumSteps.interactive(click()
                .pauseBefore(ofSeconds(10))
                .pauseAfter(ofSeconds(15)));
        var sequences = getActions();
        assertThat(sequences, hasSize(1));

        var map = new ArrayList<>(sequences).get(0).toJson();
        assertThat(map, hasKey("actions"));

        var actions = (List<Map<String, Object>>) map.get("actions");
        assertThat(actions, hasItems(
                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), is(10000L)),
                        entry(equalTo("type"), is("pause"))
                )),

                hasAllEntries(ofEntries(
                        entry(equalTo("duration"), is(15000L)),
                        entry(equalTo("type"), is("pause"))
                ))
        ));
    }
}
