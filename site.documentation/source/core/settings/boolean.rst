Boolean-свойства
================

.. code-block:: java
   :caption: Пример реализации

    package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;


   @PropertyName("MY_PROPERTY")
   public class MyBooleanProperty implements BooleanValuePropertySupplier {

       public static final MyBooleanProperty PROPERTY = new MyBooleanProperty();

   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=true

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;


   @PropertyName("MY_PROPERTY")
   public class MyBooleanProperty implements BooleanValuePropertySupplier {

       public static final MyBooleanProperty PROPERTY = new MyBooleanProperty();

   }