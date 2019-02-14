package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.Frame;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.InvalidFrameWebElement;
import ru.tinkoff.qa.neptune.selenium.test.MockWebDriver;
import ru.tinkoff.qa.neptune.selenium.test.ValidFrameWebElement;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameIndexes;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameNames;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.testng.annotations.Test;

import java.util.Random;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameFunction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_FRAME_SWITCHING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_FRAME_SWITCHING_TIME_VALUE;
import static java.lang.String.format;
import static org.apache.commons.lang3.ArrayUtils.removeElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FrameTest extends BaseWebDriverTest {

    private static <T extends Enum> T getRandomEnumItem(T[] elements) {
        return elements[new Random().nextInt(elements.length)];
    }

    @Test
    public void frameIndexPositiveTest() {
        FrameIndexes index1 = getRandomEnumItem(FrameIndexes.values());
        FrameIndexes index2 = getRandomEnumItem(removeElements(FrameIndexes.values(), index1));

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(index(index1.getIndex())));
        setEndBenchMark();

        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        assertThat(frame1.toString(), is(format("frame %s", index1.getIndex())));

        Frame frame2 = seleniumSteps.get(frame(index(index2.getIndex())));
        assertThat(((MockWebDriver) frame2.getWrappedDriver()).getCurrentFrame(), is(index2.getIndex()));
        assertThat(frame2.toString(), is(format("frame %s", index2.getIndex())));

        frame1.switchToMe();
        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));

        frame2.switchToMe();
        assertThat(((MockWebDriver) frame2.getWrappedDriver()).getCurrentFrame(), is(index2.getIndex()));
    }

    @Test
    public void frameIndexWithDefinedTimePositiveTest() {
        FrameIndexes index1 = getRandomEnumItem(FrameIndexes.values());

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(index(index1.getIndex()))
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();

        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
    }

    @Test
    public void frameIndexWithTimeDefinedImplicitlyPositiveTest() {
        FrameIndexes index1 = getRandomEnumItem(FrameIndexes.values());
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            Frame frame1 = seleniumSteps.get(frame(index(index1.getIndex())));
            setEndBenchMark();

            assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));
            assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
        finally {
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameIndexNegativeWithDefinedTimeTest() {
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(index(100))
                    .timeOut(FIVE_SECONDS));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Can't find/switch to the frame by index 100"));
            throw e;
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameIndexNegativeWithTimeDefinedImplicitlyTest() {
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(index(100)));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Can't find/switch to the frame by index 100"));
            throw e;
        }
        finally {
            setEndBenchMark();
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test
    public void frameNameOrIdPositiveTest() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());
        FrameNames name2 = getRandomEnumItem(removeElements(FrameNames.values(), name1));

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(nameOrId(name1.getNameOrId())));
        setEndBenchMark();

        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        assertThat(frame1.toString(), is(format("frame %s", name1.getNameOrId())));

        Frame frame2 = seleniumSteps.get(frame(nameOrId(name2.getNameOrId())));
        assertThat(((MockWebDriver) frame2.getWrappedDriver()).getCurrentFrame(), is(name2.getNameOrId()));
        assertThat(frame2.toString(), is(format("frame %s", name2.getNameOrId())));

        frame1.switchToMe();
        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));

        frame2.switchToMe();
        assertThat(((MockWebDriver) frame2.getWrappedDriver()).getCurrentFrame(), is(name2.getNameOrId()));
    }

    @Test
    public void frameNameOrIdWithDefinedTimePositiveTest() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(nameOrId(name1.getNameOrId()))
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();

        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
    }

    @Test
    public void frameNameOrIdWithTimeDefinedImplicitlyPositiveTest() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            Frame frame1 = seleniumSteps.get(frame(nameOrId(name1.getNameOrId())));
            setEndBenchMark();

            assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
            assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
        finally {
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameNameOrIdNegativeWithDefinedTimeTest() {
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(nameOrId("some name"))
                    .timeOut(FIVE_SECONDS));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Can't find/switch to the frame by name or id some name"));
            throw e;
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameNameOrIdNegativeWithTimeDefinedImplicitlyTest() {
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(nameOrId("some name")));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Can't find/switch to the frame by name or id some name"));
            throw e;
        }
        finally {
            setEndBenchMark();
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test
    public void frameWebElementPositiveTest() {
        ValidFrameWebElement element1 = new ValidFrameWebElement();
        ValidFrameWebElement element2 = new ValidFrameWebElement();

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(insideElement(element1)));
        setEndBenchMark();

        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(element1));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        assertThat(frame1.toString(), is(format("frame %s", element1.toString())));

        Frame frame2 = seleniumSteps.get(frame(insideElement(element2)));
        assertThat(((MockWebDriver) frame2.getWrappedDriver()).getCurrentFrame(), is(element2));

        frame1.switchToMe();
        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(element1));

        frame2.switchToMe();
        assertThat(((MockWebDriver) frame2.getWrappedDriver()).getCurrentFrame(), is(element2));
    }

    @Test
    public void frameWebElementWithDefinedTimePositiveTest() {
        ValidFrameWebElement element1 = new ValidFrameWebElement();

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(insideElement(element1))
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();

        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(element1));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
    }

    @Test
    public void frameWebElementWithTimeDefinedImplicitlyPositiveTest() {
        ValidFrameWebElement element1 = new ValidFrameWebElement();
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            Frame frame1 = seleniumSteps.get(frame(insideElement(element1)));
            setEndBenchMark();

            assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(element1));
            assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
        finally {
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameWebElementNegativeWithDefinedTimeTest() {
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(insideElement(new InvalidFrameWebElement()))
                    .timeOut(FIVE_SECONDS));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Can't find/switch to the frame inside element Invalid frame web element"));
            throw e;
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameWebElementNegativeWithTimeDefinedImplicitlyTest() {
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(insideElement(new InvalidFrameWebElement())));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Can't find/switch to the frame inside element Invalid frame web element"));
            throw e;
        }
        finally {
            setEndBenchMark();
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test
    public void frameWrappedElementPositiveTest() {
        ValidFrameWebElement element1 = new ValidFrameWebElement();
        ValidFrameWebElement element2 = new ValidFrameWebElement();

        WrapsElement wrapsElement1 = new WrapsElement() {
            @Override
            public WebElement getWrappedElement() {
                return element1;
            }

            @Override
            public String toString() {
                return "Wrapped element1";
            }
        };

        WrapsElement wrapsElement2 = new WrapsElement() {
            @Override
            public WebElement getWrappedElement() {
                return element2;
            }

            @Override
            public String toString() {
                return "Wrapped element2";
            }
        };

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(wrappedBy(wrapsElement1)));
        setEndBenchMark();

        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(element1));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        assertThat(frame1.toString(), is(format("frame %s", wrapsElement1.toString())));

        Frame frame2 = seleniumSteps.get(frame(wrappedBy(wrapsElement2)));
        assertThat(((MockWebDriver) frame2.getWrappedDriver()).getCurrentFrame(), is(element2));
        assertThat(frame2.toString(), is(format("frame %s", wrapsElement2.toString())));

        frame1.switchToMe();
        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(element1));

        frame2.switchToMe();
        assertThat(((MockWebDriver) frame2.getWrappedDriver()).getCurrentFrame(), is(element2));
    }


    @Test
    public void frameWrappedElementWithDefinedTimePositiveTest() {
        ValidFrameWebElement element1 = new ValidFrameWebElement();

        WrapsElement wrapsElement1 = new WrapsElement() {
            @Override
            public WebElement getWrappedElement() {
                return element1;
            }

            @Override
            public String toString() {
                return "Wrapped element1";
            }
        };

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(wrappedBy(wrapsElement1))
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();

        assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(element1));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
    }

    @Test
    public void frameWrappedElementWithTimeDefinedImplicitlyPositiveTest() {
        ValidFrameWebElement element1 = new ValidFrameWebElement();
        WrapsElement wrapsElement1 = new WrapsElement() {
            @Override
            public WebElement getWrappedElement() {
                return element1;
            }

            @Override
            public String toString() {
                return "Wrapped element1";
            }
        };

        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            Frame frame1 = seleniumSteps.get(frame(wrappedBy(wrapsElement1)));
            setEndBenchMark();

            assertThat(((MockWebDriver) frame1.getWrappedDriver()).getCurrentFrame(), is(element1));
            assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
        finally {
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameWrappedElementNegativeWithDefinedTimeTest() {
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(wrappedBy(new WrapsElement() {
                @Override
                public WebElement getWrappedElement() {
                    return new InvalidFrameWebElement();
                }

                @Override
                public String toString() {
                    return "Wrapper of invalid frame";
                }
            })).timeOut(FIVE_SECONDS));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Can't find/switch to the frame inside " +
                    "element wrapped by Wrapper of invalid frame"));
            throw e;
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameWrappedElementNegativeWithTimeDefinedImplicitlyTest() {
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName(), "5");
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(wrappedBy(new WrapsElement() {
                @Override
                public WebElement getWrappedElement() {
                    return new InvalidFrameWebElement();
                }

                @Override
                public String toString() {
                    return "Wrapper of invalid frame";
                }
            })));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("Can't find/switch to the frame inside element wrapped " +
                    "by Wrapper of invalid frame"));
            throw e;
        }
        finally {
            setEndBenchMark();
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getPropertyName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }
}
