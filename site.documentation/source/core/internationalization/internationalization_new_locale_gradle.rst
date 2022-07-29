.. code-block:: groovy
   :caption: gradle

   task generateBundle(type: JavaExec) {
       classpath sourceSets.main.runtimeClasspath
       main = "ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator"
       //нужная локаль
       args "de_DE",
               //или $projectDir/src/main/resources
               //или любая другая директория
               "$projectDir/src/test/resources",
               //кастомный бандл (true) или дефолтный (false)
               //В данном случае false
               false,
               //Имя бандла, который надо сформировать.
               //У каждого модуля Neptune уникальный набор имен бандлов
               //Имена бандлов можно не указывать, тогда сформируются бандлы для
               //всех подключенных модулей
               name_of_the_module,
               //и так далее, имена нужных бандлов
               name_of_the_modul2