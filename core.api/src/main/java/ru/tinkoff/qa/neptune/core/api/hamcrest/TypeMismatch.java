package ru.tinkoff.qa.neptune.core.api.hamcrest;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Type mismatch. " +
        "Object of class that equals or extends following types was expected: \r\n{expected}\r\n" +
        "Class of passed value is `{actual}`. All checks were stopped")
final class TypeMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "expected", makeReadableBy = TypesValueGetter.class)
    final Class<?>[] expected;
    @DescriptionFragment("actual")
    final Class<?> actual;

    public TypeMismatch(Class<?> actual, Class<?>... expected) {
        this.expected = expected;
        this.actual = actual;
    }
}
