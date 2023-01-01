.. code-block:: xml
   :caption: maven/dependencies

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>kafka</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

        <!--Минимально необходимый набор зависимостей от kafka-clients-->
        <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>kafka-clients</artifactId>
            <!--Диапазон поддерживаемых версий-->
            <version>[3.1.1,)</version>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        //Минимально необходимый набор зависимостей от kafka-clients
        implementation group: 'org.apache.kafka', name: 'kafka-clients', version: '[3.1.1,)'
        testImplementation group: 'ru.tinkoff.qa.neptune', name: 'kafka', version: LATEST_RELEASE_OR_BETA_VERSION
    }