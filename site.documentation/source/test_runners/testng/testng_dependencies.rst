.. code-block:: xml
   :caption: Добавить в maven/dependencies

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>testng.integration</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

        <!--Минимально необходимый набор зависимостей от testng-->
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <!--Диапазон версий-->
            <version>[7.4.0,)</version>
            <scope>test</scope>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'testng.integration', version: LATEST_RELEASE_OR_BETA_VERSION
        testImplementation group: 'org.testng', name: 'testng', version: '[7.4.0,)' //диапазон поддерживаемых версий
    }