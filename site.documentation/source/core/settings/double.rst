Double-свойства
===============

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.doubles.DoubleValuePropertySupplier;


   @PropertyName("MY_PROPERTY")
   public class MyDoubleProperty implements DoubleValuePropertySupplier {

     public static final MyDoubleProperty PROPERTY = new MyDoubleProperty();

   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=2.5

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import static org.my.pack.MyDoubleProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       Double value = PROPERTY.get();
     }
   }