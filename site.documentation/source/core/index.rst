Neptune Core
============

Тул кит, на основе которого построены все модули *Neptune*.

Что описано в данном разделе
############################

- принцип и базовые элементы, на которых строятся все модули *Neptune*
- что нужно, если необходимо написать свой собственный модуль *Neptune*

Maven
#####

.. code-block:: xml
   :caption: dependencies

   <dependency>
      <groupId>ru.tinkoff.qa.neptune</groupId>
      <artifactId>core.api</artifactId>
      <version>${LATEST_RELEASE_OR_BETA_VERSION}</version>
      <scope>test</scope>
   </dependency>

   <!--In case if you are going to implement our own functionality-->
   <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>${aspectj.version}</version>
      <scope>test</scope>
   </dependency>

.. code-block:: xml
   :caption: plugins, если необходимо реализовать свою функциональность используя Neptune Core

   <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.0.0-M5</version>
      <configuration>
         <argLine>
             -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar"
         </argLine>
      </configuration>
   </plugin>



Gradle
######

.. code-block:: groovy
   :caption: build.gradle

    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'core.api', version: LATEST_RELEASE_OR_BETA_VERSION
    }

.. code-block:: groovy
   :caption: build.gradle, если необходимо реализовать свою функциональность используя Neptune Core

    plugins {
        id "io.freefair.aspectj.post-compile-weaving" version 'ACTUAL_PLUGIN_VERSION'
    }

    dependencies {
        testInpath(group: 'ru.tinkoff.qa.neptune', name: 'core.api', version: 'NEPTUNE_BETA_OR_RELEASE') {
           transitive = false
        }
    }


.. toctree::
   :hidden:

   steps/context/index.md
   steps/steps/index.md
   steps/criteria/index.md
   steps/annotations/index.md
   events/index.md
   settings/index.md
   internationalization.md
   serialize_deserialize.rst
   dependency_injection.rst
   hooks.rst
   class_binding.rst

