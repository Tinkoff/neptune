.. code-block:: xml
   :caption: Добавить в maven/dependencies

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>allure.integration</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'allure.integration', version: LATEST_RELEASE_OR_BETA_VERSION
    }