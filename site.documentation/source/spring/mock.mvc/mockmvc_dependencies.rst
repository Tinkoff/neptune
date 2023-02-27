.. code-block:: xml
   :caption: maven/dependencies

        <dependency>
            <!--необходимо иметь в classpath-->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <!--диапазон поддерживаемых версий-->
            <version>[2.6.1,)</version>
        </dependency>

        <dependency>
            <!--необходимо иметь в test classpath-->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <!--диапазон поддерживаемых версий-->
            <version>[2.6.1,)</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>ru.tinkoff.qa.neptune</groupId>
            <artifactId>spring.mock.mvc</artifactId>
            <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
            <scope>test</scope>
        </dependency>

.. code-block:: groovy
   :caption: Добавить в build.gradle

    dependencies {
        //необходимо иметь в classpath
        implementation group: 'org.springframework.boot', name: 'spring-boot-starter', version: '[2.6.1,)' //диапазон поддерживаемых версий
        //необходимо иметь в test classpath
        testImplementation group: 'org.springframework.boot', name: 'spring-boot-starter-test', version: '[2.6.1,)' //диапазон поддерживаемых версий
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'spring.mock.mvc', version: LATEST_RELEASE_OR_BETA_VERSION
    }