.. code-block:: java
   :caption: Класс для описания бандла

   package org.mypack;

   import ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition;

   public class MyLocalizationPartition
          //необходимо унаследовать данный класс
          extends LocalizationBundlePartition {

       public MyLocalizationPartition() { //необходимо, чтобы конструктор
           // был доступен и не имел параметров
           super("my.module" //название бандла
                   , "org.mypack"); //имя корневых пакетов,
           // к которым относятся вложенные объекты
           // для локализации
       }
   }

.. code-block:: none
   :caption: Настройка SPI в `main/resources/` или `test/resources/`

   $ META-INF
   ├── services
       ├── ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition

В файл ``ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition`` внести строку ``org.mypack.MyLocalizationPartition``
