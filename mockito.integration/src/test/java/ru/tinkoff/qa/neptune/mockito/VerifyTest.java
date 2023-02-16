package ru.tinkoff.qa.neptune.mockito;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static ru.tinkoff.qa.neptune.mockito.TestEventLogger.*;

@TestInstance(PER_CLASS)
@Execution(CONCURRENT)
public class VerifyTest {

    @Captor
    private ArgumentCaptor<Object> captor;

    @Mock(name = "Test instance")
    SomeClass someClass;

    @BeforeEach
    void afterMethod() {
        stepNames.get().clear();
        thrown.remove();
        isFinished.remove();
    }

    @BeforeAll
    void prepare() {
        openMocks(this);
        someClass.getSomething();
        someClass.doSomething(5);
    }

    @Test
    void verifyTest() {
        verify(someClass).doSomething(captor.capture());
        assertThat(stepNames.get(), contains(new VerifyInvocation() + " Test instance.doSomething(\n" +
            "    <Capturing argument: Object>\n" +
            ");"));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    void noVerificationTest() {
        verify(someClass, timeout(5000).times(1));
        assertThat(stepNames.get(), emptyIterable());
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), nullValue());
    }

    @Test
    void verifyTimeOutTest() {
        verify(someClass, timeout(5000).times(1)).doSomething(5);
        assertThat(stepNames.get(), contains(new VerifyInvocation()
            + " Test instance.doSomething(5); "
            + timeout(5000).times(1)));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    public void verifyInOrderTest() {
        inOrder(someClass)
            .verify(someClass, timeout(5000).times(1)).doSomething(5);

        assertThat(stepNames.get(), contains(new VerifyInvocationInOrder()
            + " Test instance.doSomething(5); "
            + timeout(5000).times(1)));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }


    @Test
    public void verifyThrowsExceptionTest() {
        try {
            verify(someClass, timeout(5000).times(2)).doSomething(5);
        } catch (Throwable t) {
            assertThat(stepNames.get(), contains(new VerifyInvocation()
                + " Test instance.doSomething(5); "
                + timeout(5000).times(2)));
            assertThat(thrown.get(), not(nullValue()));
            assertThat(isFinished.get(), is(true));
            return;
        }
        fail("Thrown exception was expected");
    }

    @Test
    public void verifyInorderThrowsExceptionTest() {
        try {
            inOrder(someClass)
                .verify(someClass, timeout(5000).times(2)).doSomething(5);
        } catch (Throwable t) {
            assertThat(stepNames.get(), contains(new VerifyInvocationInOrder()
                + " Test instance.doSomething(5); "
                + timeout(5000).times(2)));
            assertThat(thrown.get(), not(nullValue()));
            assertThat(isFinished.get(), is(true));
            return;
        }
        fail("Thrown exception was expected");
    }

    @Test
    void verifyStaticTest() {
        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            SomeUtilClass.doSomethingStatic(5);
            staticMock.verify(() -> SomeUtilClass.doSomethingStatic(5));
        }

        assertThat(stepNames.get(), contains(new VerifyInvocation()
            + " SomeUtilClass.class.doSomethingStatic(5); "
            + times(1)));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    void verifyStaticInOrderTest() {
        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            SomeUtilClass.doSomethingStatic(5);
            inOrder(SomeUtilClass.class).verify(staticMock,
                () -> SomeUtilClass.doSomethingStatic(5),
                timeout(5000).times(1));
        }

        assertThat(stepNames.get(), contains(new VerifyInvocationInOrder()
            + " SomeUtilClass.class.doSomethingStatic(5); "
            + timeout(5000).times(1)));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    public void verifyStaticThrowsExceptionTest() {
        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            SomeUtilClass.doSomethingStatic(5);
            staticMock.verify(
                () -> SomeUtilClass.doSomethingStatic(5),
                timeout(5000).times(2));
        } catch (Throwable t) {
            assertThat(stepNames.get(), contains(new VerifyInvocation()
                + " SomeUtilClass.class.doSomethingStatic(5); "
                + timeout(5000).times(2)));
            assertThat(thrown.get(), not(nullValue()));
            assertThat(isFinished.get(), is(true));
        }

    }

    @Test
    public void verifyStaticInorderThrowsExceptionTest() {
        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            SomeUtilClass.doSomethingStatic(5);
            inOrder(SomeUtilClass.class).verify(staticMock,
                () -> SomeUtilClass.doSomethingStatic(5),
                timeout(5000).times(2));
        } catch (Throwable t) {
            assertThat(stepNames.get(), contains(new VerifyInvocationInOrder()
                + " SomeUtilClass.class.doSomethingStatic(5); "
                + timeout(5000).times(2)));
            assertThat(thrown.get(), not(nullValue()));
            assertThat(isFinished.get(), is(true));
            return;
        }
        fail("Thrown exception was expected");
    }
}
