.. code-block:: xml
   :caption: Добавить в maven/dependencies

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>allure.jupiter.bridge</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

        <!--Минимально необходимый набор зависимостей от junit5-->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <!--Диапазон версий-->
            <version>[5.8.0,)</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <!--Диапазон версий-->
            <version>[5.8.0,)</version>
            <scope>test</scope>
        </dependency>

        <dependency>
           <groupId>org.junit.platform</groupId>
           <artifactId>junit-platform-launcher</artifactId>
           <version>[1.8.0,)</version>
           <scope>test</scope>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'allure.jupiter.bridge', version: LATEST_RELEASE_OR_BETA_VERSION
        //Минимально необходимый набор зависимостей от junit5
        testImplementation group: 'org.junit.jupiter', name: 'junit-jupiter-api', version: '[5.8.0,)' //диапазон поддерживаемых версий
        testImplementation group: 'org.junit.jupiter', name: 'junit-jupiter-engine', version: '[5.8.0,)' //диапазон поддерживаемых версий
        testImplementation group: 'org.junit.platform', name: 'junit-platform-launcher', version: '[1.8.0,)' //диапазон поддерживаемых версий
    }