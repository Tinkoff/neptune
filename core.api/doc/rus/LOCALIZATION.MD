# Локализация

Локализация логов / отчетов на разные языки.
Neptune имеет дефолтный механизм, основанный на использовании [ResourceBundle](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ResourceBundle.html),
и предоставляет возможность реализовать альтернативный вариант локализации логов / отчетов.

**Язык отчетов и логов по умолчанию — английский.**

# Оглавление
- [Объекты для механизиа локализации](#Объекты-для-механизиа-локализации)
- [Базовый механизм локализации](#Базовый-механизм-локализации)
    - [Локали, которые на данный момент поддерживаются Neptune](#Локали-которые-на-данный-момент-поддерживаются-neptune)
    - [Создание бандла для локали, которой пока нет среди поддерживаемых](#Создание-бандла-для-локали-которой-нет-среди-перечисленных)
    - [Создание бандла для локали, которая есть среди поддерживаемых](#Создание-бандла-для-локали-которая-есть-среди-перечисленных)
    - [Создание бандла для нового модуля](#Создание-бандла-для-нового-модуля)
- [Собственный механизм локализации](#Собственный-механизм-локализации)

## Объекты для механизиа локализации

- [Шаги](./STEPS.MD)
- [Аттачи](./EVENTS.MD)
- [Матчеры](./HAMCREST.MD)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Базовый механизм локализации

[Настройка](./SETTINGS.MD#Локализация)

Для того чтобы активировать базовый механизм локализации, достаточно добавить в переменные среды или в `neptune.properties` 
или в `neptune.global.properties`

```properties
DEFAULT_LOCALIZATION_ENGINE=ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle
DEFAULT_LOCALE=ru_RU
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Локали, которые на данный момент поддерживаются Neptune

- `ru_RU` - русский

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Создание бандла для локали, которой нет среди [перечисленных](#Локали-которые-на-данный-момент-поддерживаются-Neptune)

Предположим, нам нужен немецкий язык. Тогда

- Maven

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.0.0</version>
            <executions>
                <execution>
                    <id>generate properties</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>java</goal>
                    </goals>
                    <configuration>
                        <mainClass>ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator</mainClass>
                        <arguments>
                            <argument>de_DE</argument>
                            <!--Нужная локаль-->
                            <argument>${basedir}/src/test/resources</argument>
                            <!--или ${basedir}/src/main/resources-->
                            <!--или любая другая директория-->
                        </arguments>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>            
```

- Gradle

```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator"
    args "de_DE", //нужная локаль
            "$projectDir/src/test/resources"
            //или $projectDir/src/main/resources
            //или любая другая директория
}
```

При настройках, указанных выше, в зависимости от того, какие модули Neptune используются в качестве зависимостей, будут созданы
следующие файлы:

- `neptune_Localization_core_de_DE.properties` - для [core](./README.MD)
- `neptune_Localization_check_de_DE.properties` - для [check](./../../../check/doc/rus/README.MD)
- `neptune_Localization_http_de_DE.properties` - для [http](./../../../http.api/doc/rus/README.MD)
- `neptune_Localization_selenium_de_DE.properties` - для [selenium](./../../../selenium/doc/rus/README.MD)
- `neptune_Localization_kafka_de_DE.properties` - для [kafka](./../../../kafka/doc/rus/README.MD)
- `neptune_Localization_kafka_de_DE.properties` - для [rabbit.mq](./../../../rabbit.mq/doc/rus/README.MD)
- `neptune_Localization_retrofit2_de_DE.properties` - для [retrofit2](./../../../retrofit2/doc/rus/README.MD)
- `neptune_Localization_spring.mock.mvc_de_DE.properties` - для [spring.mock.mvc](./../../../spring.mock.mvc/doc/rus/README.MD)
- `neptune_Localization_spring.web.testclient_de_DE.properties` - для [spring.web.testclient](./../../../spring.web.testclient/doc/rus/README.MD)

Ниже пример одного из файлов

```properties
#Values for translation of steps, their parameters, matchers and their descriptions, and attachments are defined here. Format key = value

#============================================ STEPS ============================================ 

######################## core.api.steps.SequentialGetStepSupplier #
#_________________________________Parameters_____________________________________
#Original text = Get:
core.api.steps.SequentialGetStepSupplier.imperative = Get:
#Original text = Result
core.api.steps.SequentialGetStepSupplier.resultDescription = Result
#Original text = Not present:
core.api.steps.SequentialGetStepSupplier.errorMessageStartingOnEmptyResult = Not present:

######################## core.api.steps.SequentialActionSupplier #
#_________________________________Parameters_____________________________________
#Original text = Perform:
core.api.steps.SequentialActionSupplier.imperative = Perform:

######################## core.api.steps.Absence #
#_________________________________Parameters_____________________________________
#Original text = Wait for:
core.api.steps.Absence.imperative = Wait for:
#Original text = Time of the waiting for absence
core.api.steps.Absence.timeOut = Time of the waiting for absence
#Original text = Is absent
core.api.steps.Absence.resultDescription = Is absent
#Original text = Still present:
core.api.steps.Absence.errorMessageStartingOnEmptyResult = Still present:
#__________________________________ Methods _______________________________________
#Original text = Absence of {toBeAbsent}
core.api.steps.Absence.absence(core.api.steps.SequentialGetStepSupplier<T,?,?,?,?>) = Absence of {toBeAbsent}

######################## core.api.steps.Presence #
#_________________________________Parameters_____________________________________
#Original text = Wait for:
core.api.steps.Presence.imperative = Wait for:
#Original text = Is present
core.api.steps.Presence.resultDescription = Is present
#__________________________________ Methods _______________________________________
#Original text = Presence of {toBePresent}
core.api.steps.Presence.presence(core.api.steps.SequentialGetStepSupplier<T,?,?,?,?>) = Presence of {toBePresent}

#============================================ ATTACHMENTS ============================================ 

######################## core.api.event.firing.collections.ArrayCaptor #
#Original text = Resulted array
core.api.event.firing.collections.ArrayCaptor = Resulted array

######################## core.api.event.firing.collections.CollectionCaptor #
#Original text = Resulted collection
core.api.event.firing.collections.CollectionCaptor = Resulted collection

######################## core.api.event.firing.collections.MapCaptor #
#Original text = Resulted map
core.api.event.firing.collections.MapCaptor = Resulted map

#============================================ MATCHERS ============================================ 

######################## core.api.hamcrest.common.AnyThingMatcher #
#Original text = ANYTHING
core.api.hamcrest.common.AnyThingMatcher = ANYTHING

######################## core.api.hamcrest.common.all.AllCriteriaMatcher #
#Original text = {allMatchers}
core.api.hamcrest.common.all.AllCriteriaMatcher = {allMatchers}

######################## core.api.hamcrest.common.any.AnyMatcher #
#Original text = {orExpression}
core.api.hamcrest.common.any.AnyMatcher = {orExpression}

######################## core.api.hamcrest.common.not.NotMatcher #
#Original text = {notExpression}
core.api.hamcrest.common.not.NotMatcher = {notExpression}

######################## core.api.hamcrest.common.only.one.OnlyOneMatcher #
#Original text = {onlyOneExpression}
core.api.hamcrest.common.only.one.OnlyOneMatcher = {onlyOneExpression}

######################## core.api.hamcrest.iterables.MapEntryMatcher #
#Original text = Key: {key} Value: {value}
core.api.hamcrest.iterables.MapEntryMatcher = Key: {key} Value: {value}

######################## core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher #
#Original text = in any order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher = in any order: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$ArrayConsistsOfMatcherInOrder #
#Original text = in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$ArrayConsistsOfMatcherInOrder = in following order: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$IterableConsistsOfMatcherInOrder #
#Original text = in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$IterableConsistsOfMatcherInOrder = in following order: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$MapConsistsOfMatcherInOrder #
#Original text = in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$MapConsistsOfMatcherInOrder = in following order: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher #
#Original text = each item: {matchers}
core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher = each item: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher$MapEachItemMatcher #
#Original text = each entry: {matchers}
core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher$MapEachItemMatcher = each entry: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher #
#Original text = includes in any order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher = includes in any order: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$ArrayIncludesMatcherInOrder #
#Original text = includes in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$ArrayIncludesMatcherInOrder = includes in following order: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$IterableIncludesMatcherInOrder #
#Original text = includes in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$IterableIncludesMatcherInOrder = includes in following order: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$MapIncludesMatcherInOrder #
#Original text = includes in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$MapIncludesMatcherInOrder = includes in following order: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsItemsMatcher #
#Original text = has item(s): {matchers}. Expected count: {count}
core.api.hamcrest.iterables.SetOfObjectsItemsMatcher = has item(s): {matchers}. Expected count: {count}

######################## core.api.hamcrest.iterables.SetOfObjectsItemsMatcher$MapHasItemsMatcher #
#Original text = has entry(es): {matchers}. Expected count: {count}
core.api.hamcrest.iterables.SetOfObjectsItemsMatcher$MapHasItemsMatcher = has entry(es): {matchers}. Expected count: {count}

######################## core.api.hamcrest.pojo.PojoGetterReturnsMatcher #
#Original text = Public getter '{getter}' is expected to return a value: {criteria}
core.api.hamcrest.pojo.PojoGetterReturnsMatcher = Public getter '{getter}' is expected to return a value: {criteria}

######################## core.api.hamcrest.resorce.locator.HasHostMatcher #
#Original text = Host {matcher}
core.api.hamcrest.resorce.locator.HasHostMatcher = Host {matcher}

######################## core.api.hamcrest.resorce.locator.HasPathMatcher #
#Original text = Path {matcher}
core.api.hamcrest.resorce.locator.HasPathMatcher = Path {matcher}

######################## core.api.hamcrest.resorce.locator.HasPortMatcher #
#Original text = Port {matcher}
core.api.hamcrest.resorce.locator.HasPortMatcher = Port {matcher}

######################## core.api.hamcrest.resorce.locator.HasProtocolMatcher #
#Original text = Protocol {matcher}
core.api.hamcrest.resorce.locator.HasProtocolMatcher = Protocol {matcher}

######################## core.api.hamcrest.resorce.locator.HasQueryParameters #
#Original text = has query parameter {key} {value}
core.api.hamcrest.resorce.locator.HasQueryParameters = has query parameter {key} {value}

######################## core.api.hamcrest.resorce.locator.HasQueryStringMatcher #
#Original text = Query {matcher}
core.api.hamcrest.resorce.locator.HasQueryStringMatcher = Query {matcher}

######################## core.api.hamcrest.resorce.locator.HasReferenceMatcher #
#Original text = Url reference {matcher}
core.api.hamcrest.resorce.locator.HasReferenceMatcher = Url reference {matcher}

######################## core.api.hamcrest.resorce.locator.HasSchemeMatcher #
#Original text = Scheme {matcher}
core.api.hamcrest.resorce.locator.HasSchemeMatcher = Scheme {matcher}

######################## core.api.hamcrest.resorce.locator.HasUserInfoMatcher #
#Original text = User info {matcher}
core.api.hamcrest.resorce.locator.HasUserInfoMatcher = User info {matcher}

######################## core.api.hamcrest.text.StringContainsWithSeparator #
#Original text = string has substring(s) separated by <'{separator}'>: {toContain}
core.api.hamcrest.text.StringContainsWithSeparator = string has substring(s) separated by <'{separator}'>: {toContain}

#============================================ MISMATCH DESCRIPTIONS ============================================ 

######################## core.api.hamcrest.NullValueMismatch #
#Original text = Null value. All checks were stopped
core.api.hamcrest.NullValueMismatch = Null value. All checks were stopped

######################## core.api.hamcrest.ObjectIsNotPresentMismatch #
#Original text = Not present {objectName}: {characteristics}
core.api.hamcrest.ObjectIsNotPresentMismatch = Not present {objectName}: {characteristics}

######################## core.api.hamcrest.PropertyValueMismatch #
#Original text = {property} {mismatch}
core.api.hamcrest.PropertyValueMismatch = {property} {mismatch}

######################## core.api.hamcrest.SomethingWentWrongDescriber #
#Original text = Something went wrong. The exception was thrown: {exception}
core.api.hamcrest.SomethingWentWrongDescriber = Something went wrong. The exception was thrown: {exception}

######################## core.api.hamcrest.TypeMismatch #
#Original text = Type mismatch. Object of class that equals or extends following types was expected: 
#{expected}
#Class of passed value is `{actual}`. All checks were stopped
core.api.hamcrest.TypeMismatch = Type mismatch. Object of class that equals or extends following types was expected: \r\n\
{expected}\r\n\
Class of passed value is `{actual}`. All checks were stopped

######################## core.api.hamcrest.common.DoesNotMatchAnyCriteria #
#Original text = <{value}> doesn't match any of listed criteria
core.api.hamcrest.common.DoesNotMatchAnyCriteria = <{value}> doesn't match any of listed criteria

######################## core.api.hamcrest.common.only.one.MatchesMoreThanOneCriteria #
#Original text = Value: {value}. Only one of listed criteria was expected to be matched. Checks of following criteria were positive:
# {matchers}
core.api.hamcrest.common.only.one.MatchesMoreThanOneCriteria = Value: {value}. Only one of listed criteria was expected to be matched. Checks of following criteria were positive:\r\n\
{matchers}

######################## core.api.hamcrest.iterables.descriptions.DifferentSizeMismatch #
#Original text = {actual} items instead of {expected}
core.api.hamcrest.iterables.descriptions.DifferentSizeMismatch = {actual} items instead of {expected}

######################## core.api.hamcrest.iterables.descriptions.EmptyMismatch #
#Original text = <EMPTY>
core.api.hamcrest.iterables.descriptions.EmptyMismatch = <EMPTY>

######################## core.api.hamcrest.iterables.descriptions.OutOfItemsOrderMismatch #
#Original text = The item ['{currentCriteria}'] doesn't go after : [{lastSuccessful}; index: {lastSuccessfulIndex}; criteria: '{lastSuccessfulCriteria}']
core.api.hamcrest.iterables.descriptions.OutOfItemsOrderMismatch = The item ['{currentCriteria}'] doesn't go after : [{lastSuccessful}; index: {lastSuccessfulIndex}; criteria: '{lastSuccessfulCriteria}']

######################## core.api.hamcrest.pojo.NoSuchMethodMismatch #
#Original text = Class {clazz} has no non-static and public method '{getter}' with empty signature and which returns some value
core.api.hamcrest.pojo.NoSuchMethodMismatch = Class {clazz} has no non-static and public method '{getter}' with empty signature and which returns some value

#============================================ MATCHED OBJECTS ============================================ 

######################## core.api.hamcrest.iterables.descriptions.Count #
#Original text = Count
core.api.hamcrest.iterables.descriptions.Count = Count

######################## core.api.hamcrest.iterables.descriptions.Item #
#Original text = item{indexStr}
core.api.hamcrest.iterables.descriptions.Item = item{indexStr}

######################## core.api.hamcrest.iterables.descriptions.Key #
#Original text = Key
core.api.hamcrest.iterables.descriptions.Key = Key

######################## core.api.hamcrest.iterables.descriptions.Value #
#Original text = Value
core.api.hamcrest.iterables.descriptions.Value = Value

######################## core.api.hamcrest.pojo.ReturnedObject #
#Original text = value returned from '{getter}'
core.api.hamcrest.pojo.ReturnedObject = value returned from '{getter}'

######################## core.api.hamcrest.resorce.locator.QueryParameter #
#Original text = URI/URL query parameter{param}
core.api.hamcrest.resorce.locator.QueryParameter = URI/URL query parameter{param}

#============================================ OTHER ============================================ 

######################## core.api.event.firing.Captor #
#Original text = Attachment
core.api.event.firing.Captor = Attachment

######################## core.api.logical.lexemes.Not #
#Original text = not
core.api.logical.lexemes.Not = not

######################## core.api.logical.lexemes.OnlyOne #
#Original text = xor
core.api.logical.lexemes.OnlyOne = xor

######################## core.api.logical.lexemes.Or #
#Original text = or
core.api.logical.lexemes.Or = or
```

Все что теперь нужно — это заменить английский текст в значении каждого из свойств на немецкий, кроме (**ВНИМАНИЕ!!!!!**) 
выражений внутри `{}`, их нужно оставить как есть

```properties
######################## core.api.logical.lexemes.Not #
#Original text = not
core.api.logical.lexemes.Not = nicht

######################## core.api.hamcrest.pojo.ReturnedObject #
#Original text = value returned from '{getter}'
core.api.hamcrest.pojo.ReturnedObject = von '{getter}' zurückgegebener wert'
```

Если нужны не все из перечисленных выше, а например, только [для интеграции с http клиентом Java v11 и выше](./../../../http.api/doc/rus/README.MD), 
тогда

- Maven

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.0.0</version>
            <executions>
                <execution>
                    <id>generate properties</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>java</goal>
                    </goals>
                    <configuration>
                        <mainClass>ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator</mainClass>
                        <arguments>
                            <argument>de_DE</argument>
                            <argument>${basedir}/src/test/resources</argument>
                            <!--кастомный бандл (true) или дефолтный (false). В текущей ситуации false -
                            рекомендуемое значение-->
                            <argument>false</argument>
                            <!--условное обозначение модуля Neptune, для которого предусмотрена локализация.-->
                            <argument>http</argument> <!--Список можно продолжить, через указывая аргументы со значениями
                            "check", "core", "selenium" и т.д.-->
                        </arguments>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>            
```

- Gradle

```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator"
    args "de_DE",
            "$projectDir/src/test/resources",
            "false", //кастомный бандл (true) или дефолтный (false). В текущей ситуации false -
            // рекомендуемое значение
            "http" //условное обозначение модуля Neptune, для которого предусмотрена локализация. 
    // Список можно продолжить, через запятую указывая "check", "core", "selenium" и т.д.
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Создание бандла для локали, которая есть среди [перечисленных](#Локали-которые-на-данный-момент-поддерживаются-Neptune)

Предположим, имеющийся вариант для русского языка нас не устраивает, и нужно создать языковой бандл, работающий поверх 
имеющегося. Тогда

- Maven

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.0.0</version>
            <executions>
                <execution>
                    <id>generate properties</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>java</goal>
                    </goals>
                    <configuration>
                        <mainClass>ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator</mainClass>
                        <arguments>
                            <argument>ru_RU</argument>
                            <argument>${basedir}/src/test/resources</argument>
                            <!--кастомный бандл (true) или дефолтный (false). В текущей ситуации true -
                            рекомендуемое значение-->
                            <argument>true</argument>
                        </arguments>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>            
```

- Gradle

```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator"
    args "ru_RU",
            "$projectDir/src/test/resources",
            "true" //кастомный бандл (true) или дефолтный (false). 
    // В текущей ситуации true - рекомендуемое значение
}
```

При настройках, указанных выше, в зависимости от того, какие модули Neptune используются в качестве зависимостей, будут созданные
следующие файлы:

- `neptune_Localization_core_CUSTOM_ru_RU.properties` - для [core](./README.MD)
- `neptune_Localization_check_CUSTOM_ru_RU.properties` - для [check](./../../../check/doc/rus/README.MD)
- `neptune_Localization_http_CUSTOM_ru_RU.properties` - для [http](./../../../http.api/doc/rus/README.MD)
- `neptune_Localization_selenium_CUSTOM_ru_RU.properties` - для [http](./../../../selenium/doc/rus/README.MD)

Ниже пример одного из файлов (с русской локализацией)

```properties
#Values for translation of steps, their parameters, matchers and their descriptions, and attachments are defined here. Format key = value

#============================================ STEPS ============================================ 

######################## core.api.steps.SequentialGetStepSupplier #
#_________________________________Parameters_____________________________________
#Original text = Get:
core.api.steps.SequentialGetStepSupplier.imperative = Получить:
#Original text = Result
core.api.steps.SequentialGetStepSupplier.resultDescription = Результат
#Original text = Not present:
core.api.steps.SequentialGetStepSupplier.errorMessageStartingOnEmptyResult = Не присутствует / не найдено:

######################## core.api.steps.SequentialActionSupplier #
#_________________________________Parameters_____________________________________
#Original text = Perform:
core.api.steps.SequentialActionSupplier.imperative = Выполнить:

######################## core.api.steps.Absence #
#_________________________________Parameters_____________________________________
#Original text = Wait for:
core.api.steps.Absence.imperative = Ожидать:
#Original text = Time of the waiting for absence
core.api.steps.Absence.timeOut = Время ожидания отсутствия/исчезновения
#Original text = Is absent
core.api.steps.Absence.resultDescription = Отсутствует:
#Original text = Still present:
core.api.steps.Absence.errorMessageStartingOnEmptyResult = Все еще присутствует:
#__________________________________ Methods _______________________________________
#Original text = Absence of {toBeAbsent}
core.api.steps.Absence.absence(core.api.steps.SequentialGetStepSupplier<T,?,?,?,?>) = Отсутствие. {toBeAbsent}

######################## core.api.steps.Presence #
#_________________________________Parameters_____________________________________
#Original text = Wait for:
core.api.steps.Presence.imperative = Ожидать:
#Original text = Is present
core.api.steps.Presence.resultDescription = Присутствует:
#__________________________________ Methods _______________________________________
#Original text = Presence of {toBePresent}
core.api.steps.Presence.presence(core.api.steps.SequentialGetStepSupplier<T,?,?,?,?>) = Наличие. {toBePresent}

#============================================ ATTACHMENTS ============================================ 

######################## core.api.event.firing.collections.ArrayCaptor #
#Original text = Resulted array
core.api.event.firing.collections.ArrayCaptor = Полученный массив

######################## core.api.event.firing.collections.CollectionCaptor #
#Original text = Resulted collection
core.api.event.firing.collections.CollectionCaptor = Полученная коллекция

######################## core.api.event.firing.collections.MapCaptor #
#Original text = Resulted map
core.api.event.firing.collections.MapCaptor = Полученный Map<?,?>

#============================================ MATCHERS ============================================ 

######################## core.api.hamcrest.common.AnyThingMatcher #
#Original text = ANYTHING
core.api.hamcrest.common.AnyThingMatcher = ЛЮБОЕ ЗНАЧЕНИЕ

######################## core.api.hamcrest.common.all.AllCriteriaMatcher #
#Original text = {allMatchers}
core.api.hamcrest.common.all.AllCriteriaMatcher = {allMatchers}

######################## core.api.hamcrest.common.any.AnyMatcher #
#Original text = {orExpression}
core.api.hamcrest.common.any.AnyMatcher = {orExpression}

######################## core.api.hamcrest.common.not.NotMatcher #
#Original text = {notExpression}
core.api.hamcrest.common.not.NotMatcher = {notExpression}

######################## core.api.hamcrest.common.only.one.OnlyOneMatcher #
#Original text = {onlyOneExpression}
core.api.hamcrest.common.only.one.OnlyOneMatcher = {onlyOneExpression}

######################## core.api.hamcrest.iterables.MapEntryMatcher #
#Original text = Key: {key} Value: {value}
core.api.hamcrest.iterables.MapEntryMatcher = Ключ: {key} значение: {value}

######################## core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher #
#Original text = in any order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher = в любом порядке: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$ArrayConsistsOfMatcherInOrder #
#Original text = in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$ArrayConsistsOfMatcherInOrder = в следующем порядке: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$IterableConsistsOfMatcherInOrder #
#Original text = in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$IterableConsistsOfMatcherInOrder = в следующем порядке: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$MapConsistsOfMatcherInOrder #
#Original text = in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher$MapConsistsOfMatcherInOrder = в следующем порядке: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher #
#Original text = each item: {matchers}
core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher = каждый элемент: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher$MapEachItemMatcher #
#Original text = each entry: {matchers}
core.api.hamcrest.iterables.SetOfObjectsEachItemMatcher$MapEachItemMatcher = каждая запись: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher #
#Original text = includes in any order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher = включает в любом порядке: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$ArrayIncludesMatcherInOrder #
#Original text = includes in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$ArrayIncludesMatcherInOrder = включает в следующем порядке: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$IterableIncludesMatcherInOrder #
#Original text = includes in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$IterableIncludesMatcherInOrder = включает в следующем порядке: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$MapIncludesMatcherInOrder #
#Original text = includes in following order: {matchers}
core.api.hamcrest.iterables.SetOfObjectsIncludesMatcher$MapIncludesMatcherInOrder = включает в следующем порядке: {matchers}

######################## core.api.hamcrest.iterables.SetOfObjectsItemsMatcher #
#Original text = has item(s): {matchers}. Expected count: {count}
core.api.hamcrest.iterables.SetOfObjectsItemsMatcher = есть элемент(ы): {matchers}. Ожидаемое количество: {count}

######################## core.api.hamcrest.iterables.SetOfObjectsItemsMatcher$MapHasItemsMatcher #
#Original text = has entry(es): {matchers}. Expected count: {count}
core.api.hamcrest.iterables.SetOfObjectsItemsMatcher$MapHasItemsMatcher = есть запись(записи): {matchers}. Ожидаемое количество: {count}

######################## core.api.hamcrest.pojo.PojoGetterReturnsMatcher #
#Original text = Public getter '{getter}' is expected to return a value: {criteria}
core.api.hamcrest.pojo.PojoGetterReturnsMatcher = Публичный метод '{getter}' должен вернуть значение: {criteria}

######################## core.api.hamcrest.resorce.locator.HasHostMatcher #
#Original text = Host {matcher}
core.api.hamcrest.resorce.locator.HasHostMatcher = хост {matcher}

######################## core.api.hamcrest.resorce.locator.HasPathMatcher #
#Original text = Path {matcher}
core.api.hamcrest.resorce.locator.HasPathMatcher = Path {matcher}

######################## core.api.hamcrest.resorce.locator.HasPortMatcher #
#Original text = Port {matcher}
core.api.hamcrest.resorce.locator.HasPortMatcher = Порт {matcher}

######################## core.api.hamcrest.resorce.locator.HasProtocolMatcher #
#Original text = Protocol {matcher}
core.api.hamcrest.resorce.locator.HasProtocolMatcher = Протокол {matcher}

######################## core.api.hamcrest.resorce.locator.HasQueryParameters #
#Original text = has query parameter {key} {value}
core.api.hamcrest.resorce.locator.HasQueryParameters = query-параметр {key} {value}

######################## core.api.hamcrest.resorce.locator.HasQueryStringMatcher #
#Original text = Query {matcher}
core.api.hamcrest.resorce.locator.HasQueryStringMatcher = Query {matcher}

######################## core.api.hamcrest.resorce.locator.HasReferenceMatcher #
#Original text = Url reference {matcher}
core.api.hamcrest.resorce.locator.HasReferenceMatcher = Url reference {matcher}

######################## core.api.hamcrest.resorce.locator.HasSchemeMatcher #
#Original text = Scheme {matcher}
core.api.hamcrest.resorce.locator.HasSchemeMatcher = Scheme {matcher}

######################## core.api.hamcrest.resorce.locator.HasUserInfoMatcher #
#Original text = User info {matcher}
core.api.hamcrest.resorce.locator.HasUserInfoMatcher = User info {matcher}

######################## core.api.hamcrest.text.StringContainsWithSeparator #
#Original text = string has substring(s) separated by <'{separator}'>: {toContain}
core.api.hamcrest.text.StringContainsWithSeparator = строка с разделителем <'{separator}'> содержит под-строки: {toContain}

#============================================ MISMATCH DESCRIPTIONS ============================================ 

######################## core.api.hamcrest.NullValueMismatch #
#Original text = Null value. All checks were stopped
core.api.hamcrest.NullValueMismatch = Null. Проверка завершена

######################## core.api.hamcrest.ObjectIsNotPresentMismatch #
#Original text = Not present {objectName}: {characteristics}
core.api.hamcrest.ObjectIsNotPresentMismatch = Объект не найден {objectName}: {characteristics}

######################## core.api.hamcrest.PropertyValueMismatch #
#Original text = {property} {mismatch}
core.api.hamcrest.PropertyValueMismatch = {property} {mismatch}

######################## core.api.hamcrest.SomethingWentWrongDescriber #
#Original text = Something went wrong. The exception was thrown: {exception}
core.api.hamcrest.SomethingWentWrongDescriber = Что-то пошло не так. Выброшенное исключение: {exception}

######################## core.api.hamcrest.TypeMismatch #
#Original text = Type mismatch. Object of class that equals or extends following types was expected: 
#{expected}
#Class of passed value is `{actual}`. All checks were stopped
core.api.hamcrest.TypeMismatch = Несоответствие типов. Ожидался объект одного из следующих классов: \r\n\
{expected}\r\n\
Класс объекта: `{actual}`. Проверка завершена

######################## core.api.hamcrest.common.DoesNotMatchAnyCriteria #
#Original text = <{value}> doesn't match any of listed criteria
core.api.hamcrest.common.DoesNotMatchAnyCriteria = <{value}> не соответствует ни одному из указанных параметров

######################## core.api.hamcrest.common.only.one.MatchesMoreThanOneCriteria #
#Original text = Value: {value}. Only one of listed criteria was expected to be matched. Checks of following criteria were positive:
# {matchers}
core.api.hamcrest.common.only.one.MatchesMoreThanOneCriteria = Значение: {value}. Ожидалось соответствие только одному из перечисленных критериев. Соответствия:\r\n\
{matchers}

######################## core.api.hamcrest.iterables.descriptions.DifferentSizeMismatch #
#Original text = {actual} items instead of {expected}
core.api.hamcrest.iterables.descriptions.DifferentSizeMismatch = {actual} элементов вместо {expected}

######################## core.api.hamcrest.iterables.descriptions.EmptyMismatch #
#Original text = <EMPTY>
core.api.hamcrest.iterables.descriptions.EmptyMismatch = <ПУСТО>

######################## core.api.hamcrest.iterables.descriptions.OutOfItemsOrderMismatch #
#Original text = The item ['{currentCriteria}'] doesn't go after : [{lastSuccessful}; index: {lastSuccessfulIndex}; criteria: '{lastSuccessfulCriteria}']
core.api.hamcrest.iterables.descriptions.OutOfItemsOrderMismatch = Элемент ['{currentCriteria}'] не следует за : [{lastSuccessful}; индекс: {lastSuccessfulIndex}; критерий: '{lastSuccessfulCriteria}']

######################## core.api.hamcrest.pojo.NoSuchMethodMismatch #
#Original text = Class {clazz} has no non-static and public method '{getter}' with empty signature and which returns some value
core.api.hamcrest.pojo.NoSuchMethodMismatch = Класс {clazz} не имеет нестатический и публичный метод '{getter}' с пустой сигнатурой и возвращающий значение

#============================================ MATCHED OBJECTS ============================================ 

######################## core.api.hamcrest.iterables.descriptions.Count #
#Original text = Count
core.api.hamcrest.iterables.descriptions.Count = Количество

######################## core.api.hamcrest.iterables.descriptions.Item #
#Original text = item{indexStr}
core.api.hamcrest.iterables.descriptions.Item = элемент{indexStr}

######################## core.api.hamcrest.iterables.descriptions.Key #
#Original text = Key
core.api.hamcrest.iterables.descriptions.Key = Ключ

######################## core.api.hamcrest.iterables.descriptions.Value #
#Original text = Value
core.api.hamcrest.iterables.descriptions.Value = Значение

######################## core.api.hamcrest.pojo.ReturnedObject #
#Original text = value returned from '{getter}'
core.api.hamcrest.pojo.ReturnedObject = значение которое вернул '{getter}'

######################## core.api.hamcrest.resorce.locator.QueryParameter #
#Original text = URI/URL query parameter{param}
core.api.hamcrest.resorce.locator.QueryParameter = query-параметр URI/URL{param}

#============================================ OTHER ============================================ 

######################## core.api.logical.lexemes.Not #
#Original text = not
core.api.logical.lexemes.Not = НЕ

######################## core.api.logical.lexemes.OnlyOne #
#Original text = xor
core.api.logical.lexemes.OnlyOne = xor

######################## core.api.logical.lexemes.Or #
#Original text = or
core.api.logical.lexemes.Or = или
```

Все что теперь нужно — это исправить текст в значении каждого из свойств, кроме (**ВНИМАНИЕ!!!!!**) выражений внутри `{}`, 
их нужно оставить как есть.

Если нужны не все из перечисленных выше, а например, только [для интеграции с http клиентом Java v11 и выше](./../../../http.api/doc/rus/README.MD),
тогда

- Maven

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.0.0</version>
            <executions>
                <execution>
                    <id>generate properties</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>java</goal>
                    </goals>
                    <configuration>
                        <mainClass>ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator</mainClass>
                        <arguments>
                            <argument>ru_RU</argument>
                            <argument>${basedir}/src/test/resources</argument>
                            <argument>true</argument>
                            <!--условное обозначение модуля Neptune, для которого предусмотрена локализация.-->
                            <argument>http</argument> <!--Список можно продолжить, через указывая аргументы со значениями
                            "check", "core", "selenium" и т.д.-->
                        </arguments>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>            
```

- Gradle

```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator"
    args "ru_RU",
            "$projectDir/src/test/resources",
            "true",
            "http" //условное обозначение модуля Neptune, для которого предусмотрена локализация. 
    // Список можно продолжить, через запятую указывая "check", "core", "selenium" и т.д.
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Создание бандла для нового модуля

Допустим, был создан новый модуль для [объектов](#Объекты-для-механизиа-локализации) которого может понадобиться локализация
на разные языки. Тогда нужно просто добавить класс

```java
package org.mypack;

import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

public class MyLocalizationPartition extends LocalizationBundlePartition // <-необходимо унаследовать данный класс
{

    public MyLocalizationPartition() { //необходимо, чтобы конструктор 
        // был доступен и не имел параметров
        super("my.module" //название модуля для механизма локализации
                , "org.mypack"); //имя корневых пакетов, 
        // к которым относятся вложенные объекты 
        // для локализации
    }
}
```

и теперь

- Maven

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.0.0</version>
            <executions>
                <execution>
                    <id>generate properties</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>java</goal>
                    </goals>
                    <configuration>
                        <mainClass>ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator</mainClass>
                        <arguments>
                            <argument>ru_RU</argument> <!--или любая другая локаль-->
                            <argument>${basedir}/src/test/resources</argument>
                            <!--или ${basedir}/src/main/resources-->
                            <!--или любая другая директория-->
                            <argument>false</argument> <!--или true, по ситуации-->
                            <!--условное обозначение модуля Neptune, для которого предусмотрена локализация.-->
                            <argument>my.module</argument> <!--Список можно продолжить, через указывая аргументы со значениями
                            "check", "core", "selenium" и т.д.-->
                        </arguments>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>            
```

- Gradle

```groovy
task generateBundle(type: JavaExec) {
    classpath sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator"
    args "ru_RU", //ru_RU
            "$projectDir/src/test/resources", //или ${basedir}/src/main/resources
             //или любая другая директория
            "false", //или true, по ситуации
            "my.module" //условное обозначение модуля Neptune, для которого предусмотрена локализация. 
    // Список можно продолжить, через запятую указывая "check", "core", "selenium" и т.д.
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Собственный механизм локализации

Так же существует возможность реализовать собственный механизм локализации.

Для этого необходимо:

* заимплементить интерфейс
  `ru.tinkoff.qa.neptune.core.api.localization.StepLocalization`

 ```java
public interface StepLocalization {

  /**
   * Makes translation by usage of a class.
   *
   * @param clz                       is a class whose metadata may be used for auxiliary purposes
   * @param template                  is a template of a string to be translated. It is usually taken
   *                                  from {@link Description} of a class. It may contain named parameters
   *                                  placed between '{}'. Names of parameters should correspond to class fields
   *                                  annotated by {@link DescriptionFragment}
   * @param descriptionTemplateParams is a list of parameters and their values. These parameters are taken from
   *                                  fields annotated by {@link DescriptionFragment}. This parameter is included
   *                                  because the list is possible to be re-used for some reasons.
   * @param locale                    is a used locale
   * @param <T>                       is a type of class
   * @return translated text
   */
  <T> String classTranslation(Class<T> clz,
                              String template,
                              List<TemplateParameter> descriptionTemplateParams,
                              Locale locale);

  /**
   * Makes translation by usage of a method.
   *
   * @param method                    is a method whose metadata may be used for auxiliary purposes
   * @param template                  is a template of a string to be translated. It is usually taken
   *                                  from {@link Description} of a method. It may contain named parameters
   *                                  placed between '{}'. Names of parameters should correspond to
   *                                  method parameters annotated by {@link DescriptionFragment}. In case of methods
   *                                  numbers from 0 are allowed to be used as parameter names between '{}'. Each
   *                                  defined number should be an index of a parameter of the method signature.
   * @param descriptionTemplateParams is a list of parameters and their values. These parameters are taken from
   *                                  method parameters. Name of each {@link TemplateParameter} is formed by
   *                                  {@link DescriptionFragment} or index of the corresponding {@link Parameter} of
   *                                  the method signature. This parameter is included because the list is possible
   *                                  to be re-used for some reasons.
   * @param locale                    is a used locale
   * @return translated text
   */
  String methodTranslation(Method method,
                           String template,
                           List<TemplateParameter> descriptionTemplateParams,
                           Locale locale);

  /**
   * Makes translation by usage of other annotated element that differs from {@link Class} and {@link Method}
   *
   * @param member         is an annotated element whose metadata may be used for auxiliary purposes
   * @param toBeTranslated is a string to be translated. This string is usually taken from the member
   * @param locale         is a used locale
   * @param <T>            is a type of member
   * @return translated text
   */
  <T extends AnnotatedElement & Member> String memberTranslation(T member,
                                                                 String toBeTranslated,
                                                                 Locale locale);

  /**
   * Makes translation of a text.
   *
   * @param text   to be translated
   * @param locale is a used locale
   * @return translated text
   */
  String translation(String text, Locale locale);
}
```

* добавить в переменные среды или в `neptune.properties` или в `neptune.global.properties` (или поменять значение,
  если уже добавлено)

```properties
DEFAULT_LOCALIZATION_ENGINE=org.my.project.MyLocalizationEngine
DEFAULT_LOCALE=ru_RU
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление) 
 
