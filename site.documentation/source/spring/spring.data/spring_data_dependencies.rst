.. code-block:: xml
   :caption: maven/dependencies

        <dependency>
            <!--необходимо иметь в classpath-->
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-commons</artifactId>
            <!--диапазон поддерживаемых версий-->
            <version>[2.7.0,)</version>
        </dependency>

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>spring.data</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
           dependencies {
           //необходимо иметь в classpath
           implementation group: 'org.springframework.data', name: 'spring-data-commons', version: '[2.7.0,)' //диапазон поддерживаемых версий
           testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'spring.data', version: LATEST_RELEASE_OR_BETA_VERSION
       }
    }