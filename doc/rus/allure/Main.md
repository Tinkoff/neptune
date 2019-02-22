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
            <artifactId>allure.integration</artifactId>
            <version>${neptune.version}</version>
        </dependency>
</dependencies>

``` 

#### Allure + TestNg

Данный артефакт уже включает в себя зависимость `allure.integration`, [allure-testng](https://docs.qameta.io/allure/#_testng) и [testng.integration](/doc/rus/testng/Main.md) 

```xml
<dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>allure.testng.bridge</artifactId>
            <version>${neptune.version}</version>
        </dependency>
</dependencies>

``` 


### Gradle

`compile group: 'ru.tinkoff.qa.neptune', name: 'allure.integration', version: neptuneVersion`

#### Allure + TestNg

Данный артефакт уже включает в себя зависимость `allure.integration`, [allure-testng](https://docs.qameta.io/allure/#_testng) и [testng.integration](/doc/rus/testng/Main.md) 

`compile group: 'ru.tinkoff.qa.neptune', name: 'allure.testng.bridge', version: neptuneVersion`

_______________________________________________________________________________________________________________________________________

Дополнительные действия не нужны. Далее Allure работает как обычный логгер. Для построения оттчета Allure ознакомьтесь с документацией.
