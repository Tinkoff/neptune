package ru.tinkoff.qa.neptune.core.api.hamcrest;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Type mismatch. " +
        "Object of class that equals or extends `{expected}` was expected. " +
        "Class of passed value is `{actual}`. All checks were stopped")
public final class TypeMismatch extends MismatchDescriber {

    final Class<?> expected;
    final Class<?> actual;

    public TypeMismatch(Class<?> expected, Class<?> actual) {
        this.expected = expected;
        this.actual = actual;
    }
}
