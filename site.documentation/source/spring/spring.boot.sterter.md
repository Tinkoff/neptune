# Neptune Spring boot starter

Данный модуль содержит
дополнительные [авто конфигурации](https://docs.spring.io/spring-boot/docs/2.1.13.RELEASE/reference/html/boot-features-developing-auto-configuration.html)
для [Spring Boot](https://spring.io/guides/gs/spring-boot/), предназначенные для тестирования.

```{eval-rst}
.. include:: spring.boot.starter_dependencies.rst
```

Является транзитивной зависимостью:

- [Spring Data](./spring.data/index.md)
- [Spring MockMvc](./mock.mvc/index.md)
- [Spring WebTestClient](./web.test.client/index.md)

[API](https://tinkoff.github.io/neptune/neptune-spring-boot-starter/index.html)

## Работа со свойствами тестируемого приложения и _Neptune_

Данный модуль предоставляет доступ Neptune
к [настройкам](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
Spring-приложения, дефолтным либо настройкам активного в данный
момент [профиля приложения](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
.

Подробнее:

- [Настройки свойств Neptune](./../quick_start/settings/index.md)
- [Механизм использования сторонних свойств](./../core/settings/property_sources.rst)

```properties
# Настройка базового URI HTTP-клиента 
# указанная в переменных окружения или в файлах 
# neptune.global.properties или neptune.properties,
# значение которой строится с использованием значения server.port - 
# стандартного свойства Spring
END_POINT_OF_TARGET_API = http://127.0.0.1:${server.port}/
```

Так же есть возможность менять значения свойств _Neptune_ непосредственно в тесте, используя стандартные
механизмы [Spring Boot Test](https://spring.io/guides/gs/testing-web/), если в этом есть необходимость.

```java
package org.my.pack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.testng.AssertJUnit.assertEquals;
import static ru.tinkoff.qa.neptune.core.api.properties
    .general.events.CapturedEvents.FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties
    .general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;

//DO_CAPTURES_OF - это стандартное свойство Neptune, которое описывает,
//когда следует делать аттачи для логов и тест-репортов 
// (успешные шаги, неуспешные, или во всех случаях).
//В данном примере мы указали, что в данном тесте следует 
// делать аттачи для неуспешных шагов
@SpringBootTest(properties = { "DO_CAPTURES_OF=FAILURE" })
public class MyBootTest {

  @Test
  public void test() {
    assertEquals(DO_CAPTURES_OF_INSTANCE.get(), FAILURE);
  }
}
```

## Если для тестирования используется не Junit5

Для [Junit 5](https://junit.org/junit5/docs/current/user-guide/) ничего особенного делать не надо, чтобы писать тесты.
Написание тестов для Spring-приложений с использованием TestNg имеет некоторую особенность, которую необходимо учесть,
чтобы поддержать корректную работу со свойствами.

```java
package org.my.pack;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.spring.boot.starter
    .test.TestWithKnownCurrentApplicationContext;

@SpringBootTest(properties = {"example.firstProperty=annotation"})
public class MyBootTest2 extends AbstractTestNGSpringContextTests
        //нужно просто имплементить
        implements TestWithKnownCurrentApplicationContext { //этот интерфейс

  @Test
  public void test() {
  }
}
```

## Получение текущего контекста приложения

Иногда может возникнуть необходимость иметь доступ до активного в данный
момент [контекста приложения](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)
.

```java
package org.my.pack;

import org.springframework.context.ApplicationContext;

import static ru.tinkoff.qa.neptune.spring.boot.starter
    .application.contexts
    .CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

public class SomeClass {
    
  public void someMethod() {
      ApplicationContext context = getCurrentApplicationContext();
      //дальнейшие действия
  }
}
```