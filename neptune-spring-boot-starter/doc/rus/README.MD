# Основные фичи модуля авто-конфигураций для Spring Boot 

## Оглавление

- [Работа со свойствами / настройками тестируемого приложения и Neptune](#Работа-со-свойствами--настройками-тестируемого-приложения-и-neptune)
- [Если для тестирования используется не Junit5](#Если-для-тестирования-используется-не-junit5)

## Работа со свойствами / настройками тестируемого приложения и Neptune

Важная информация:

- [Механизм свойств/настроек Neptune](./../../../core.api/doc/rus/SETTINGS.MD)
- [Базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроексвойств)
- [Настройки Neptune. Http](./../../../http.api/doc/rus/SETTINGS.MD)
- [Настройки Neptune. Selenium WebDriver](./../../../selenium/doc/rus/SETTINGS.MD)
- [Настройки Neptune. Kafka](./../../../kafka/doc/rus/SETTINGS.MD)
- [Настройки Neptune.RabbitMQ](./../../../rabbit.mq/doc/rus/SETTINGS.MD)
- [Настройки Neptune. Retrofit 2.0](./../../../retrofit2/doc/rus/SETTINGS.MD)
- [Настройки Neptune. TestNG](./../../../testng.integration/doc/rus/README.MD#Настройки)
- [Настройки Neptune. JUnit5](./../../../jupiter.integration/doc/rus/README.MD#Настройки)
- [Настройки Neptune. Spring Mock MVC](./../../../spring.mock.mvc/doc/rus/SETTINGS.MD)
- [Настройки Neptune. Spring Data](./../../../spring.data/doc/rus/README.MD#Настройки)


Данный модуль предоставляет доступ Neptune к [настройкам](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config) Spring-приложения,
дефолтным либо настройкам активного в данный момент [профиля приложения](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles). 
Подробно о том, как это работает, можно прочитать в этой [статье](./../../../core.api/doc/rus/SETTINGS.MD#Повторное-использование-настроек).

Пример: 

```properties
# Настройка базового URL клиента Retrofit 2.0, 
# указанная в переменных окружения или в файлах neptune.global.properties или neptune.properties,
# значение которой строится с использованием значения server.port - стандартного свойства Spring
DEFAULT_RETROFIT_URL = http://127.0.0.1:${server.port}/
```

Так же есть возможность менять значения свойств / настроек Neptune непосредственно в тесте, используя 
стандартные механизмы [Spring Boot Test](https://spring.io/guides/gs/testing-web/), если в этом есть необходимость.

```java
package org.my.pack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.testng.AssertJUnit.assertEquals;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents.FAILURE;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;

//DO_CAPTURES_OF - это стандартное свойство Neptune, которое описывает,
//когда следует делать аттачи для логов и тест-репортов (успешные шаги, неуспешные, или во всех случаях).
//В данном примере мы указали, что в данном тесте следует делать аттачи для неуспешных шагов
@SpringBootTest(properties = { "DO_CAPTURES_OF=FAILURE" })
public class MyBootTest {

  @Test
  public void test() {
    assertEquals(DO_CAPTURES_OF_INSTANCE.get(), FAILURE);
  }
}
```

[к оглавлению документа](#Оглавление)

## Если для тестирования используется не Junit5

Для [Junit 5](https://junit.org/junit5/docs/current/user-guide/) ничего особенного делать не надо, 
чтобы писать тесты. Написание тестов для Spring-приложений с использованием TestNg имеет некоторую 
особенность, которую необходимо учесть, чтобы поддержать корректную работу [со свойствами / настройками](#Работа-со-свойствами--настройками-тестируемого-приложения-и-Neptune),
а так же с провайдерами [MockMvc](#MockMvc) или [WebTestClient](#WebTestClient)

```java
package org.my.pack;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.spring.boot.starter.test.TestWithKnownCurrentApplicationContext;

@SpringBootTest(properties = {"example.firstProperty=annotation"})
@AutoConfigureMockMvc
public class MyBootTest2 extends AbstractTestNGSpringContextTests
        implements TestWithKnownCurrentApplicationContext {//нужно просто имплементить
        //этот интерфейс

  @Test
  public void test() {
  }
}
```

[к оглавлению документа](#Оглавление)
