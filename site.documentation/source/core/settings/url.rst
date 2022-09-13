Url-свойства
================

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

   @PropertyName("MY_PROPERTY")
   public class MyURLProperty implements URLValuePropertySupplier {

     public static final MyURLProperty PROPERTY = new MyURLProperty();
   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=https://www.google.com

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import java.net.URL;

   import static org.my.pack.MyURLProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       URL value = PROPERTY.get();
     }
   }