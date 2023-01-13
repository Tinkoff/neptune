.. code-block:: xml
   :caption: maven/dependencies

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>rabbit.mq</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

        <!--Минимально необходимый набор зависимостей от amqp-client-->
        <dependency>
            <groupId>com.rabbitmq</groupId>
            <artifactId>amqp-client</artifactId>
            <!--Диапазон поддерживаемых версий-->
            <version>[5.13.0,)</version>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        //Минимально необходимый набор зависимостей от amqp-client
        implementation group: 'com.rabbitmq', name: 'amqp-client', version: '[5.13.0,)'
        testImplementation group: 'ru.tinkoff.qa.neptune', name: 'rabbit.mq', version: LATEST_RELEASE_OR_BETA_VERSION
    }