# Механизм локализации / интернационализации

Neptune имеет свой механизм локализации / интернационализации вывода информации.

## Для чего применяется

- [Шаги](./steps/steps/index.md)
- [Аттачи](./events/attaches/index.rst)
- Другие объекты

## Механизм

```{eval-rst}
.. include:: internationalization_sample.rst
```

Свойства описаны [тут](../quick_start/settings/internationalization.md)

## Базовый механизм

Дефолтный поддерживаемый механизм основан на использовании [ResourceBundle](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ResourceBundle.html).

```properties
DEFAULT_LOCALIZATION_ENGINE=ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle
DEFAULT_LOCALE=ru_RU
```

На текущий момент поддерживаются локали: 
- `ru_RU` - русский

### Как добавить бандл для локали, которой нет среди поддерживаемых

```{eval-rst}
.. include:: internationalization_new_locale_maven.rst
.. include:: internationalization_new_locale_gradle.rst
```

Будут сформированы файлы вида ``neptune_Localization_<name_of_the_module>_de_DE.properties``.
Их контент будет похож на пример ниже:

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

Все что теперь нужно — это заменить английский текст в значении каждого из свойств на немецкий, кроме ⚠️
выражений внутри `{}`, их нужно оставить как есть

```properties
######################## core.api.logical.lexemes.Not #
#Original text = not
core.api.logical.lexemes.Not = nicht

######################## core.api.hamcrest.pojo.ReturnedObject #
#Original text = value returned from '{getter}'
core.api.hamcrest.pojo.ReturnedObject = von '{getter}' zurückgegebener wert'
```

### Создание бандла для локали, которая есть среди поддерживаемых

```{eval-rst}
.. include:: internationalization_custom_locale_maven.rst
.. include:: internationalization_custom_locale_gradle.rst
```

Будут сформированы файлы вида ``neptune_Localization_<name_of_the_module>_ru_RU.properties``.
Их контент будет похож на пример ниже:

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

Все что теперь нужно — это исправить текст в значении каждого из свойств, кроме ⚠️ выражений внутри `{}`,
их нужно оставить как есть.

## Добавление интернационализации своего модуля на основе бандлов

```{eval-rst}
.. include:: internationalization_new_bundle.rst
```






