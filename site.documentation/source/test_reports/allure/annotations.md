# Allure. Аннотации

Перечисленные ниже аннотации расширяют [функциональность allure](https://docs.qameta.io/allure-report/#_features_2)

## ExcludeFromAllureReport

Использование данной аннотации выборочно выключает добавление тестов/фикстур в allure-отчет. 

### Исключение пакета

В пакет, содержащий тесты, которые должны быть исключены из репорта, следует добавить `package-info.java` 
со следующим контентом

```java
//Все тесты из данного пакета
//будут исключены из Allure-отчета
@ExcludeFromAllureReport
//Имя аннотируемого пакета    
package com.your.oganisation.pack.name;

import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;
```

Аннотация не применяется к вложенным пакетам

### Исключение класса

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

//Из allure-отчета будут исключены все задекларированные 
//тест-методы и методы фикстуры данного класса
@ExcludeFromAllureReport
public class SomeTest {

    @BeforeAll
    public static void beforeAll() {
    }

    @AfterAll
    public static void afterAll() {
    }

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
    }

    @Test
    public void test1() {
    }

    @Test
    public void test2() {
    }
    
    //Данный класс так же будет исключен
    public static class NestedTest {
        
        @Test
        public void nestedTest1() {
        }

        @Test
        public void nestedTest2() {
        }
    }
}
```

### Исключение методов

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;

public class SomeTest {

    @BeforeAll
    public static void beforeAll2() {
    }

    @AfterAll
    public static void afterAll2() {
    }

    //данная фикстура в отчете отображаться 
    //не будет
    @ExcludeFromAllureReport
    @BeforeEach
    public void beforeEach2() {
    }

    @AfterEach
    public void afterEach2() {
    }

    //данная фикстура в отчете отображаться 
    //не будет
    @ExcludeFromAllureReport
    @AfterEach
    public void afterEachExcluded2() {
    }

    //данный тест в отчете отображаться 
    //не будет
    @ExcludeFromAllureReport
    @Test
    public void test1() {
    }

    @Test
    public void test2() {
    }
}
```

Так же из allure-отчета исключаются унаследованные методы, задекларированные
в классах, исключенных из репортинга. 