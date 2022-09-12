Enum-свойства. Множество элементов
==================================

.. code-block:: java
   :caption: Перечисление для примера

   package org.my.pack;

   public enum MyEnum {

       ITEM1,
       ITEM2,
       ITEM3
   }

.. code-block:: java
   :caption: Пример реализации

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.enums.MultipleEnumPropertySuppler;

   @PropertyName("MY_PROPERTY")
   public class MyEnumItemsProperty implements
           MultipleEnumPropertySuppler<MyEnum> { //<-Указывается тип перечисления

     public static final MyEnumItemsProperty PROPERTY = new MyEnumItemsProperty();
   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY=ITEM1,ITEM3

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import java.util.List;

   import static org.my.pack.MyEnumItemsProperty.PROPERTY;

   public class MyUseCase {

     public void useCase() {
       //чтение
       List<MyEnum> value = PROPERTY.get();
     }
   }