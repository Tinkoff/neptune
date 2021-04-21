package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import org.openqa.selenium.NoSuchFrameException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.Frame;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWebDriver;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameIndexes;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameNames;

import java.util.Random;

import static java.lang.String.format;
import static org.apache.commons.lang3.ArrayUtils.removeElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_FRAME_SWITCHING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_FRAME_SWITCHING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.FRAME_ELEMENT_VALID1;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.FRAME_ELEMENT_VALID2;

public class FrameTest extends BaseWebDriverTest {

    private static <T extends Enum<?>> T getRandomEnumItem(T[] elements) {
        return elements[new Random().nextInt(elements.length)];
    }

    @BeforeMethod(groups = "basicPositive")
    public void clearSwitchToParentFrame() {
        ((MockWebDriver) wrappedWebDriver.getWrappedDriver()).setSwitchedToParentFrame(false);
    }

    @Test(groups = "basicPositive")
    public void frameIndexPositiveTest() {
        FrameIndexes index1 = getRandomEnumItem(FrameIndexes.values());
        FrameIndexes index2 = getRandomEnumItem(removeElements(FrameIndexes.values(), index1));

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(index1.getIndex()));
        setEndBenchMark();

        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        assertThat(frame1.toString(), is(format("frame %s", index1.getIndex())));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(true));

        ((MockWebDriver) wrappedWebDriver.getWrappedDriver()).setSwitchedToParentFrame(false);

        Frame frame2 = seleniumSteps.get(frame(index2.getIndex()));
        assertThat(frame2.toString(), is(format("frame %s", index2.getIndex())));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(true));

        frame1.switchToMe();
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(false));

        ((MockWebDriver) wrappedWebDriver.getWrappedDriver()).setSwitchedToParentFrame(true);
        frame2.switchToMe();
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(index2.getIndex()));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(false));
    }

    @Test
    public void frameIndexWithDefinedTimePositiveTest() {
        FrameIndexes index1 = getRandomEnumItem(FrameIndexes.values());

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(index1.getIndex())
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();

        assertThat(((MockWebDriver) seleniumSteps.switchTo(frame1).getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
    }

    @Test
    public void frameIndexWithTimeDefinedImplicitlyPositiveTest() {
        FrameIndexes index1 = getRandomEnumItem(FrameIndexes.values());
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            Frame frame1 = seleniumSteps.get(frame(index1.getIndex()));
            setEndBenchMark();

            assertThat(((MockWebDriver) seleniumSteps.switchTo(frame1).getWrappedDriver()).getCurrentFrame(), is(index1.getIndex()));
            assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        } finally {
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName());
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameIndexNegativeWithDefinedTimeTest() {
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(100)
                    .timeOut(FIVE_SECONDS));
        } finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameIndexNegativeWithTimeDefinedImplicitlyTest() {
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(100));
        } finally {
            setEndBenchMark();
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(groups = "basicPositive")
    public void frameNameOrIdPositiveTest() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());
        FrameNames name2 = getRandomEnumItem(removeElements(FrameNames.values(), name1));

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(name1.getNameOrId()));
        setEndBenchMark();

        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        assertThat(frame1.toString(), is(format("frame %s", name1.getNameOrId())));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(true));

        ((MockWebDriver) wrappedWebDriver.getWrappedDriver()).setSwitchedToParentFrame(false);

        Frame frame2 = seleniumSteps.get(frame(name2.getNameOrId()));
        assertThat(frame2.toString(), is(format("frame %s", name2.getNameOrId())));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(true));


        frame1.switchToMe();
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(false));

        ((MockWebDriver) wrappedWebDriver.getWrappedDriver()).setSwitchedToParentFrame(true);

        frame2.switchToMe();
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(name2.getNameOrId()));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(false));
    }

    @Test
    public void frameNameOrIdWithDefinedTimePositiveTest() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());

        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(name1.getNameOrId())
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();

        assertThat(((MockWebDriver) seleniumSteps.switchTo(frame1).getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
    }

    @Test
    public void frameNameOrIdWithTimeDefinedImplicitlyPositiveTest() {
        FrameNames name1 = getRandomEnumItem(FrameNames.values());
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            Frame frame1 = seleniumSteps.get(frame(name1.getNameOrId()));
            setEndBenchMark();

            assertThat(((MockWebDriver) seleniumSteps.switchTo(frame1).getWrappedDriver()).getCurrentFrame(), is(name1.getNameOrId()));
            assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        } finally {
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName());
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameNameOrIdNegativeWithDefinedTimeTest() {
        try {
            setStartBenchMark();
            seleniumSteps.get(frame("some name").timeOut(FIVE_SECONDS));
        } finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameNameOrIdNegativeWithTimeDefinedImplicitlyTest() {
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            seleniumSteps.get(frame("some name"));
        } finally {
            setEndBenchMark();
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(groups = "basicPositive")
    public void frameWebElementPositiveTest() {
        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(tagName("valid_frame1")));
        setEndBenchMark();

        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(true));

        ((MockWebDriver) wrappedWebDriver.getWrappedDriver()).setSwitchedToParentFrame(false);

        Frame frame2 = seleniumSteps.get(frame(tagName("valid_frame2")));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(true));

        frame1.switchToMe();
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(FRAME_ELEMENT_VALID1));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(false));

        ((MockWebDriver) wrappedWebDriver.getWrappedDriver()).setSwitchedToParentFrame(true);

        frame2.switchToMe();
        assertThat(((MockWebDriver) seleniumSteps.getWrappedDriver()).getCurrentFrame(), is(FRAME_ELEMENT_VALID2));
        assertThat(((MockWebDriver) wrappedWebDriver.getWrappedDriver()).isSwitchedToParentFrame(), is(false));
    }

    @Test
    public void frameWebElementWithDefinedTimePositiveTest() {
        setStartBenchMark();
        Frame frame1 = seleniumSteps.get(frame(tagName("valid_frame1"))
                .timeOut(FIVE_SECONDS));
        setEndBenchMark();

        assertThat(((MockWebDriver) seleniumSteps.switchTo(frame1).getWrappedDriver()).getCurrentFrame(), is(FRAME_ELEMENT_VALID1));
        assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
    }

    @Test
    public void frameWebElementWithTimeDefinedImplicitlyPositiveTest() {
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            Frame frame1 = seleniumSteps.get(frame(tagName("valid_frame1")));
            setEndBenchMark();

            assertThat(((MockWebDriver) seleniumSteps.switchTo(frame1).getWrappedDriver()).getCurrentFrame(), is(FRAME_ELEMENT_VALID1));
            assertThat(getTimeDifference(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        } finally {
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName());
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameWebElementNegativeWithDefinedTimeTest() {
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(tagName("invalid_frame"))
                    .timeOut(FIVE_SECONDS));
        } finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = NoSuchFrameException.class)
    public void frameWebElementNegativeWithTimeDefinedImplicitlyTest() {
        setProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName(), "SECONDS");
        setProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName(), "5");
        try {
            setStartBenchMark();
            seleniumSteps.get(frame(tagName("not_existing_frame")));
        } finally {
            setEndBenchMark();
            removeProperty(WAITING_FRAME_SWITCHING_TIME_UNIT.getName());
            removeProperty(WAITING_FRAME_SWITCHING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(HALF_SECOND.toMillis()));
        }
    }
}
