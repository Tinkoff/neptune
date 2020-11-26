#Интеграция с Allure Framework и TestNG

Данный модуль используется для связывания функциональности **Neptune**, **Allure framework** и **TestNG**.

#Maven

В pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>allure.testng.bridge</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

#Gradle

```groovy
    dependencies {
        testCompile group: 'ru.tinkoff.qa.neptune', name: 'allure.testng.bridge', version: LATEST_RELEASE_OR_BETA_VERSION    
    }
```



Описанных выше действий достаточно.