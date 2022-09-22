# Neptune

_Neptune_ - инструмент, который был разработан для интеграционного тестирования. Так же он может подойти для
написания e2e тестов.

В данный момент Neptune проходит ALPHA-тестирование сотрудниками [Tinkoff.ru](https://www.tinkoff.ru/software/).
Сборки пока не доступны в публичных maven-репозиториях. Для обзора в данный момент доступны исходный код, данная документация и [Javadoc](https://tinkoff.github.io/neptune/core.api/index.html)

## Для кого был создан Neptune

- для Java/Kotlin разработчиков

- для проектов, разрабатывающие софт на базе [Spring Boot](https://spring.io/projects/spring-boot)

- для QA-инженеров

## Почему Neptune?

- Neptune - это библиотека готовых шагов для описания тестов со своим DSL
- Neptune имеет средства для написания своих расширений и библиотек шагов
- Neptune не вносит в проект новые зависимости, а адаптируется к стеку, используемому командами разработки
  ⚠️ _Neptune может требовать версии используемых библиотек не ниже указанных_
- Neptune поддерживает  как [junit5](https://junit.org/junit5/docs/current/user-guide/) так и [TestNG](https://testng.org/doc/)

### Принцип

Идея, реализованная в Neptune, состоит в том, что любой фреймворк для unit/интеграционного/приемочного тестирования
можно построить на следующих принципах:

- каждый шаг теста может быть выполнен в рамках некоторого контекста (работа с базой данных, Rest API, экраном web или мобильного приложения)

- все многообразие шагов сводится к комбинации элементарных действий, выполняемых в рамках определенного контекста, описанных один раз



```{toctree}
:caption: Быстрый старт
:hidden:
:maxdepth: 7

quick_start/index.md
quick_start/start_to_contribute.md
```

```{toctree}
:caption: Тест-раннеры
:hidden:
:maxdepth: 7

test_runners/testng/index.md
test_runners/junit5/index.md
```

```{toctree}
:caption: Тест-репортеры
:hidden:
:maxdepth: 7

test_reports/allure.md
```

```{toctree}
:caption: Assert
:hidden:
:maxdepth: 7

check/check.md
common_matchers/index.md
```

```{toctree}
:caption: Spring Boot
:hidden:
:maxdepth: 7

spring/spring.boot.sterter.md
spring/mock.mvc/index.md
spring/web.test.client/index.md
spring/spring.data/index.md
```

```{toctree}
:caption: MQ
:hidden:
:maxdepth: 7

mq/kafka/index.md
```

```{toctree}
:caption: Базы данных
:hidden:
:maxdepth: 7

data.base/database.abstractions.md
data.base/hibernate/index.md
```

```{toctree}
:caption: Core
:hidden:
:maxdepth: 7

core/index
```


  

