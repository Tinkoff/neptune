# Настройки интеграции с TestNG

Ниже список свойств, который дополняет [базовые](./../../quick_start/settings/index.md)

## TESTNG_REFRESH_BEFORE

Основная статья: [Обновление контекстов](./../../core/steps/context/refresh.md)

Свойство указывает, перед каким методами следует обновить контекст. Типы этих методов описываются перечислением `ru.tinkoff.qa.neptune.testng.integration.properties.RefreshEachTimeBefore`:
- `SUITE_STARTING` перед методами, отмеченными `@BeforeSuite`
- `TEST_STARTING` перед методами, отмеченными `@BeforeTest`
- `CLASS_STARTING` перед методами, отмеченными `@BeforeClass`
- `GROUP_STARTING` перед методами, отмеченными `@BeforeGroups`
- `BEFORE_METHOD_STARTING` перед методами, отмеченными `@BeforeMethod`
- `METHOD_STARTING` перед методами, отмеченными `@Test`

Значение свойства/переменной окружения `TESTNG_REFRESH_BEFORE` должно быть равно одному из перечисленных выше элементов,
или оно может включать в себя несколько, через запятую.

```properties
# обновление происходит перед первым в очереди вызова (до ближайшего @Test-метода) методом
# с аннотацией @BeforeClass
TESTNG_REFRESH_BEFORE=CLASS_STARTING
```

```properties
# обновление происходит перед первым в очереди вызова (до ближайшего @Test-метода) методом
# с аннотацией @BeforeClass или @BeforeMethod. Если перед @Test-методом методы с указанными аннотациями
# не вызывались, то обновление произойдет непосредственно перед началом самого теста. 
# Данный цикл будет выполняться для всех тестов в рамках класса.
TESTNG_REFRESH_BEFORE=CLASS_STARTING,BEFORE_METHOD_STARTING,METHOD_STARTING
```

При этом учитывается иерархия методов TestNG. Порядок приведен ниже:

1. `@BeforeSuite`

2. `@BeforeTest`

3. `@BeforeClass`

4. `@BeforeGroups`

5. `@BeforeMethod`

6. `@Test`