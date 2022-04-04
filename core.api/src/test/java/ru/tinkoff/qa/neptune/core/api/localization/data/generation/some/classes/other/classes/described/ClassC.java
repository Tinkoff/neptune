package ru.tinkoff.qa.neptune.core.api.localization.data.generation.some.classes.other.classes.described;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

@Description("Some description")
public class ClassC {

    @StepParameter("someField")
    private Object stepParameter;

    @Description("Do something")
    public void doSomething() {

    }

    @Description("Do something {0}")
    public void doSomething(String param) {

    }

    @Description("Do something {0}")
    public void doSomething(String... params) {

    }
}
