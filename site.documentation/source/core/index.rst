Neptune Core
============

Тул кит, на основе которого построены все модули *Neptune*,
который эти модули предоставляют транзитивно для создания расширений.

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



Gradle
######

.. code-block:: groovy
   :caption: build.gradle

    dependencies {
        testImplementation  group: 'ru.tinkoff.qa.neptune', name: 'core.api', version: LATEST_RELEASE_OR_BETA_VERSION
    }

`API <https://tinkoff.github.io/neptune/core.api/index.html>`_


.. toctree::
   :hidden:

   steps/context/index.md
   steps/steps/index.md
   steps/criteria/index.md
   steps/annotations/index.md
   events/index.md
   settings/index.md
   internationalization/internationalization.md
   serialize_deserialize.rst
   dependency_injection.rst
   hooks.rst
   class_binding.rst
   aspects.md

