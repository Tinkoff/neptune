package ru.tinkoff.qa.neptune.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static ru.tinkoff.qa.neptune.mockito.TestEventLogger.*;

public class VerifyNoInvocationTest {

    @BeforeEach
    void afterMethod() {
        stepNames.get().clear();
        thrown.remove();
        isFinished.remove();
    }

    @Test
    void verifyNoInteractionsTest() {
        var someClass = mock(SomeClass.class, "Test instance");
        verifyNoInteractions(someClass);
        assertThat(stepNames.get(), contains(new VerifyNoInteractions() + " Test instance"));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    void verifyNoMoreInteractionsTest() {
        var someClass = mock(SomeClass.class, "Test instance");
        verifyNoMoreInteractions(someClass);
        assertThat(stepNames.get(), contains(new VerifyNoMoreInteractions() + " Test instance"));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    public void verifyInOrderNoInteractionsTest() {
        var someClass = mock(SomeClass.class, "Test instance");
        inOrder(someClass).verifyNoMoreInteractions();

        assertThat(stepNames.get(), contains(new VerifyNoMoreInteractionsInOrder() + " Test instance"));
        assertThat(thrown.get(), nullValue());
        assertThat(isFinished.get(), is(true));
    }

    @Test
    void verifyNoInteractionsStaticTest() {
        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            staticMock.verifyNoInteractions();

            assertThat(stepNames.get(), contains(new VerifyNoInteractions() + " ru.tinkoff.qa.neptune.mockito.SomeUtilClass"));
            assertThat(thrown.get(), nullValue());
            assertThat(isFinished.get(), is(true));
        }
    }

    @Test
    void verifyNoMoreInteractionsStaticTest() {
        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            staticMock.verifyNoMoreInteractions();

            assertThat(stepNames.get(), contains(new VerifyNoMoreInteractions() + " ru.tinkoff.qa.neptune.mockito.SomeUtilClass"));
            assertThat(thrown.get(), nullValue());
            assertThat(isFinished.get(), is(true));
        }
    }

    @Test
    void verifyInOrderNoInteractionsStaticTest() {
        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            inOrder(SomeUtilClass.class).verifyNoMoreInteractions();

            assertThat(stepNames.get(), contains(new VerifyNoMoreInteractionsInOrder() + " ru.tinkoff.qa.neptune.mockito.SomeUtilClass"));
            assertThat(thrown.get(), nullValue());
            assertThat(isFinished.get(), is(true));
        }
    }

    @Test
    void verifyNoInteractionsThrowsExceptionTest() {
        try {
            var someClass = mock(SomeClass.class, "Test instance");
            someClass.doSomething(5);
            verifyNoInteractions(someClass);
        } catch (Throwable t) {
            assertThat(stepNames.get(), contains(new VerifyNoInteractions() + " Test instance"));
            assertThat(thrown.get(), not(nullValue()));
            assertThat(isFinished.get(), is(true));
            return;
        }
        fail("Thrown exception was expected");
    }

    @Test
    void verifyNoMoreInteractionsThrowsExceptionTest() {
        try {
            var someClass = mock(SomeClass.class, "Test instance");
            someClass.doSomething(5);
            verifyNoMoreInteractions(someClass);
        } catch (Throwable t) {
            assertThat(stepNames.get(), contains(new VerifyNoMoreInteractions() + " Test instance"));
            assertThat(thrown.get(), not(nullValue()));
            assertThat(isFinished.get(), is(true));
            return;
        }
        fail("Thrown exception was expected");
    }

    @Test
    public void verifyInOrderNoInteractionsThrowsExceptionTest() {
        try {
            var someClass = mock(SomeClass.class, "Test instance");
            someClass.doSomething(5);
            inOrder(someClass).verifyNoMoreInteractions();
        } catch (Throwable t) {
            assertThat(stepNames.get(), contains(new VerifyNoMoreInteractionsInOrder() + " Test instance"));
            assertThat(thrown.get(), not(nullValue()));
            assertThat(isFinished.get(), is(true));
            return;
        }
        fail("Thrown exception was expected");
    }


    @Test
    void verifyNoInteractionsStaticThrowsExceptionTest() {

        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            try {
                SomeUtilClass.doSomethingStatic(5);
                staticMock.verifyNoInteractions();
            } catch (Throwable t) {
                assertThat(stepNames.get(), contains(new VerifyNoInteractions() + " ru.tinkoff.qa.neptune.mockito.SomeUtilClass"));
                assertThat(thrown.get(), not(nullValue()));
                assertThat(isFinished.get(), is(true));
                return;
            }
        }
        fail("Thrown exception was expected");
    }

    @Test
    void verifyNoMoreInteractionsStaticThrowsExceptionTest() {

        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            try {
                SomeUtilClass.doSomethingStatic(5);
                staticMock.verifyNoMoreInteractions();
            } catch (Throwable t) {
                assertThat(stepNames.get(), contains(new VerifyNoMoreInteractions() + " ru.tinkoff.qa.neptune.mockito.SomeUtilClass"));
                assertThat(thrown.get(), not(nullValue()));
                assertThat(isFinished.get(), is(true));
                return;
            }
        }
        fail("Thrown exception was expected");
    }

    @Test
    public void verifyInOrderNoInteractionsStaticThrowsExceptionTest() {
        try (var staticMock = mockStatic(SomeUtilClass.class)) {
            try {
                SomeUtilClass.doSomethingStatic(5);
                inOrder(SomeUtilClass.class).verifyNoMoreInteractions();
            } catch (Throwable t) {
                assertThat(stepNames.get(), contains(new VerifyNoMoreInteractionsInOrder() + " ru.tinkoff.qa.neptune.mockito.SomeUtilClass"));
                assertThat(thrown.get(), not(nullValue()));
                assertThat(isFinished.get(), is(true));
                return;
            }
        }
        fail("Thrown exception was expected");
    }
}
