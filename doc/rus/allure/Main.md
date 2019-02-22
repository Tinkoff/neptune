# Интеграция с Allure 2

Модуль разработан для автоматического построения отчетов Allure. [API](https://tinkoffcreditsystems.github.io/neptune/allure.integration/) 
Интеграция с [Allure 2](https://docs.qameta.io/allure/). [Пример отчета](https://demo.qameta.io/allure/)

# Начало работы

## Требования
 
 - Операционнаяя система - Windows/Mac Os X/Linux
 - Java Development Kit 11
 - [maven](https://maven.apache.org/) или [gradle](https://gradle.org/)
 
## Зависимости

### Maven

```xml
<dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>check</artifactId>
            <version>${neptune.version}</version>
        </dependency>
</dependencies>

``` 

### Gradle

`compile group: 'ru.tinkoff.qa.neptune', name: 'check', version: neptuneVersion`

Дополнительные действия не нужны. Далее Allure работает как обычный логгер. Для построения оттчета Allure ознакомьтесь с документацией.
