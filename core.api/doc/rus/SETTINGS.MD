# Механизм настроек

## neptune.global.properties и neptune.properties

## Автоматическая генерация neptune.global.properties и neptune.properties

## Свойства для возврата чисел
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для возврата величины продолжительности
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для возврата объектов перечислений
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для создания и возврата объектов URL
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для создания и возврата объектов
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для возврата Boolean
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Основной набор настроек/свойств

### Освобождение ресурсов

## Локализация
[Основная статья](./LOCALIZATION.MD)
#####DEFAULT_LOCALE
Определяет языковай стандарт по умолчанию
```properties
#Значение свойства указывается так
DEFAULT_LOCALE=ru_RU
```
#####DEFAULT_LOCALIZATION_ENGINE
Определяет какой механизм перевода будет использоваться
```properties
#В значение свойства указывается полное пакетное имя класса реализующее интерфейс
# ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization
DEFAULT_LOCALIZATION_ENGINE=org.my.project.MyLocalizationEngine
```
