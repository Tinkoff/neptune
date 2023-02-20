.. code-block:: xml
   :caption: Добавить в maven/dependencies

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>mockito.integration</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

         <!--Минимально необходимый набор зависимостей от mockito-->
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <!--Диапазон версий-->
            <version>[4.8.1,)</version>
            <scope>test</scope>
        </dependency>

        <!--Для моков статических методов-->
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-inline</artifactId>
            <!--Диапазон версий-->
            <version>[4.8.1,)</version>
            <scope>test</scope>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'mockito.integration', version: LATEST_RELEASE_OR_BETA_VERSION
        //Минимально необходимый набор зависимостей от mockito
        testImplementation group: 'org.mockito', name: 'mockito-core', version: '[4.8.1,)' //Диапазон версий
        //Для моков статических методов
        testImplementation group: 'org.mockito', name: 'mockito-inline', version: '[4.8.1,)' //Диапазон версий
    }