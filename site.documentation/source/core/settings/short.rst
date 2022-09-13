Short-свойства
================

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.shorts.ShortValuePropertySupplier;

   @PropertyName("MY_PROPERTY")
   public class MyShortProperty implements ShortValuePropertySupplier {

     public static final MyShortProperty PROPERTY = new MyShortProperty();

   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=2

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;


   import static org.my.pack.MyShortProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       Short value = PROPERTY.get();
     }
   }