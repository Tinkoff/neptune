# Настройки интеграции с JUnit5

Необходимо включить свойство [junit.jupiter.extensions.autodetection.enabled](https://junit.org/junit5/docs/current/user-guide/#extensions-registration-automatic-enabling)

```properties
junit.jupiter.extensions.autodetection.enabled=true
```

Ниже список свойств, который дополняет [базовые](./../../quick_start/settings/index.md)

## JUNIT5_REFRESH_BEFORE

Основная статья: [Обновление контекстов](./../../core/steps/context/refresh.md)

Свойство указывает, перед каким методами следует обновить контекст. Типы этих методов описываются перечислением `ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore`:
- `ALL_STARTING` перед методами, отмеченными `@BeforeAll`
- `EACH_STARTING` перед методами, отмеченными `@BeforeEach`
- `TEST_STARTING` перед методами, отмеченными `@Test`, `@TestFactory`, `@TestTemplate` или `@ParameterizedTest`

Значение свойства/переменной окружения `JUNIT5_REFRESH_BEFORE` должно быть равно одному из перечисленных выше элементов,
или оно может включать в себя несколько, через запятую.

```properties
# обновление происходит перед первым в очереди вызова (до ближайшего @Test-метода) методом
# с аннотацией @BeforeClass
JUNIT5_REFRESH_BEFORE=ALL_STARTING
```

```properties
# обновление происходит перед первым в очереди вызова (до ближайшего тест-метода) методом
# с аннотацией @BeforeAll или @BeforeEach. Если перед тест-методом методы с указанными аннотациями
# не вызывались, то обновление произойдет непосредственно перед началом самого теста. 
# Данный цикл будет выполняться для всех тестов в рамках класса.
JUNIT5_REFRESH_BEFORE=ALL_STARTING,EACH_STARTING,TEST_STARTING
```

При этом учитывается иерархия методов JUnit5. Порядок приведен ниже:

1. `@BeforeAll`

2. `@BeforeEach`

3. `@Test` / `@TestFactory` / `@TestTemplate` / `@ParameterizedTest`

