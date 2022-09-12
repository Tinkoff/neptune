.. code-block:: xml
   :caption: Добавить в maven/dependencies

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>database.abstractions</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        api group: 'ru.tinkoff.qa.neptune', name: 'database.abstractions', version: LATEST_RELEASE_OR_BETA_VERSION
    }