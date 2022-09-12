Duration-свойства
=================

.. code-block:: java
   :caption: Свойство для указания единиц измерения времени

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

   import java.time.temporal.ChronoUnit;

   @PropertyName("MY_PROPERTY_CHRONO_UNIT")
   public class MyChronoUnitProperty implements EnumPropertySuppler<ChronoUnit> {

     public static final MyChronoUnitProperty MY_CHRONO_UNIT_PROPERTY
            = new MyChronoUnitProperty();
   }

.. code-block:: java
   :caption: Свойство для указания величины времени времени

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
   import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

   @PropertyName("MY_PROPERTY_CHRONO_VALUE")
   public class MyChronoValueProperty implements LongValuePropertySupplier {

     public static final MyChronoValueProperty MY_CHRONO_VALUE_PROPERTY
            = new MyChronoValueProperty();
   }

.. code-block:: java
   :caption: Класс, который превращает значения свойств в Duration

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;

   import static org.my.pack.MyChronoUnitProperty.MY_CHRONO_UNIT_PROPERTY;
   import static org.my.pack.MyChronoValueProperty.MY_CHRONO_VALUE_PROPERTY;

   public class MyDurationSupplier extends
           DurationSupplier { //<--наследоваться от этого класса

       //просто, для быстрого доступа
       public static DurationSupplier MY_DURATION = new MyDurationSupplier();

       protected MyDurationSupplier() {
           super(MY_CHRONO_UNIT_PROPERTY, MY_CHRONO_VALUE_PROPERTY);
       }
   }

.. code-block:: properties
   :caption: Как указывается в ``neptune.properties`` / ``neptune.global.properties``

   MY_PROPERTY_CHRONO_UNIT=SECONDS
   MY_PROPERTY_CHRONO_VALUE=5

.. code-block:: java
   :caption: Пример использования

   package org.my.pack;

   import java.time.Duration;

   import static org.my.pack.MyDurationSupplier.MY_DURATION;

   public class MyUseCase {

     public void useCase() {
       //чтение
       Duration value = MY_DURATION.get(); //вернет 5 сек
     }
   }