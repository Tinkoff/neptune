package ru.tinkoff.qa.neptune.core.api.localization.data.generation;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

public class ClassD {

    public void doSomething() {

    }

    public void doSomething(String param) {

    }

    @Description("Do something {0}")
    public void doSomething(String... params) {

    }
}
