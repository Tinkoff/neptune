.. code-block:: xml
   :caption: Добавить в maven/dependencies

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>check</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

        <!--Минимально необходимый набор зависимостей от hamcrest-->
        <dependency>
            <groupId>org.hamcrest</groupId>
            <artifactId>${artefact.name.from.hamcrest.group}</artifactId>
            <version>${actual.hamcrest.version}</version>
            <scope>test</scope>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'check', version: LATEST_RELEASE_OR_BETA_VERSION
        testImplementation group: 'org.hamcrest', name: 'artefact.name.from.hamcrest.group', version: 'actual.hamcrest.version'
    }