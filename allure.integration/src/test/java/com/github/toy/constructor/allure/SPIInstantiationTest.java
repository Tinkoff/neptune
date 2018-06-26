package com.github.toy.constructor.allure;

import com.github.toy.constructor.core.api.captors.CapturedFileInjector;
import com.github.toy.constructor.core.api.captors.CapturedImageInjector;
import com.github.toy.constructor.core.api.captors.CapturedStringInjector;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.SPIUtil.loadSPI;
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
}
