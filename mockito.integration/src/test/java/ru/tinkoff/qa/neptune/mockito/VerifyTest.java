package ru.tinkoff.qa.neptune.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.mockito.TestEventLogger.*;

@TestInstance(PER_CLASS)
@Execution(CONCURRENT)
public class VerifyTest {

    @BeforeEach
    void afterMethod() {
        stepNames.get().clear();
        thrown.remove();
        isFinished.remove();
    }

    @Test
    void verifyTest() {
        var someClass = mock(SomeClass.class, "Test instance");
        someClass.doSomething(5);

        verify(someClass).doSomething(5);
        assertThat(stepNames.get(), contains(new VerifyInvocation() + " Test instance.doSomething(5);"));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    void noVerificationTest() {

        try {
            var someClass = mock(SomeClass.class, "Test instance");
            someClass.doSomething(5);
            verify(someClass, timeout(5000).times(1));
        } catch (Throwable ignored) {
        }

        assertThat(stepNames.get(), emptyIterable());
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), nullValue());
    }

    @Test
    void verifyTimeOutTest() {
        var someClass = mock(SomeClass.class, "Test instance");
        someClass.doSomething(5);

        verify(someClass, timeout(5000).times(1)).doSomething(5);
        assertThat(stepNames.get(), contains(new VerifyInvocation()
            + " Test instance.doSomething(5); "
            + timeout(5000).times(1)));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    public void verifyInOrderTest() {
        var someClass = mock(SomeClass.class, "Test instance");
        someClass.doSomething(5);

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
            var someClass = mock(SomeClass.class, "Test instance");
            someClass.doSomething(5);

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
            var someClass = mock(SomeClass.class, "Test instance");
            someClass.doSomething(5);

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
