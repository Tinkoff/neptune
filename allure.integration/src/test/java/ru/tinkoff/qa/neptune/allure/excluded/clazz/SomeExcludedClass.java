package ru.tinkoff.qa.neptune.allure.excluded.clazz;

import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

@ExcludeFromAllureReport
public class SomeExcludedClass {

    public void someMethod() {

    }

    public class ExcludedInnerClass {

        public void someMethod() {

        }
    }
}
