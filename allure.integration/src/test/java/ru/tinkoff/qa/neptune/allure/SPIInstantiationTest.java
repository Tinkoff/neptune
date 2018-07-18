package ru.tinkoff.qa.neptune.allure;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedImageInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;
import static org.hamcrest.MatcherAssert.assertThat;

public class SPIInstantiationTest {

    @Test
    public void instantiationOfFileInjectorTest() {
        assertThat(AllureFileInjector.class.isAssignableFrom(loadSPI(CapturedFileInjector.class).get(0).getClass()),
                Matchers.is(true));
    }

    @Test
    public void instantiationOfImageInjectorTest() {
        assertThat(AllureImageInjector.class.isAssignableFrom(loadSPI(CapturedImageInjector.class).get(0).getClass()),
                Matchers.is(true));
    }

    @Test
    public void instantiationOfStringInjectorTest() {
        assertThat(AllureStringInjector.class.isAssignableFrom(loadSPI(CapturedStringInjector.class).get(0).getClass()),
                Matchers.is(true));
    }

    @Test
    public void instantiationOfAllureEventLogger() {
        assertThat(AllureEventLogger.class.isAssignableFrom(loadSPI(EventLogger.class).get(0).getClass()),
                Matchers.is(true));
    }
}
