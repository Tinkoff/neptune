@Description и @DescriptionFragment
====================================

Аннотация ``@Description`` применима к классам-шагам, критериям и произвольным объектам.

С помощью этих аннотаций можно указать:

- **ЧТО** получат get-step
- **ЧТО** выполняет action-step
- **ЧТО** это за критерий и **КАКИЕ** его ожидаемые значения
- **ЧТО** это за объект и какие у него характеристики

.. code-block:: java
   :caption: пример для класса-шага. Пример реализации ExampleGetParameterValue можно посмотреть :doc:`тут <parameter_string_presentation>`

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
   import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
   import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
   import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

   //Использование данной аннотации на уровне класса
   //присваивает название по умолчанию.
   //Может быть перекрыто на уровне класса-наследника
   //или собственного статического фабричного метода, см. ниже.
   @Description("Something with parameters [{a}, {b}, {c}]")
   //Название может быть как
   // статическим, так и динамическим. 
   // В случае с динамическим названием, параметры заключаются в {}
   public class MyStepClass 
       /*extends a subclass of SequentialGetStepSupplier, or SequentialActionSupplier*/
   {

       @DescriptionFragment("a") //значение, которое,
       //формирует динамическое название. Название фрагмента
       //должно соответствовать маске параметра, заключенного в {}
       private final Object a;
    
       @DescriptionFragment("b")
       private final Object b;

       //Строковое представление объекта не всегда бывает удобным для чтения.
       //Для таких случаев можно
       //использовать классы, реализующие интерфейс
       // ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter
       @DescriptionFragment(value = "c", makeReadableBy = ExampleGetParameterValue.class)
       private final Object c;

       //пример присвоения названия на уровне метода
       @Description("Something else with parameters [{b}, {c}]")
       public static MyStepClass myStep(
               @DescriptionFragment(
                       value = "b",
                       makeReadableBy = ExampleGetParameterValue.class) boolean param,
               @DescriptionFragment("c") String param2)
       {
           return new MyStepClass(1, param, param2);
       }
   }

.. code-block:: java
   :caption: пример для util-класса, создающего объекты критериев

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
   import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
   import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

   import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

   public final class MyCriteriaLib {

       private MyCriteriaLib() {
           super();
       }

       //Для критериев присвоение названия с помощью аннотаций
       //работает только на уровне статических фабричных методов
       @Description("Some criteria with parameters: {param1}, {param2}")
       public static <T> Criteria<T> someCriteria(
               @DescriptionFragment(
                       value = "param1",
                       makeReadableBy = ExampleGetParameterValue.class) Object param1,
               @DescriptionFragment("param2") Object param2)
       {
           return condition(t -> /* предикат*/);
       }

       @Description("One more criteria with parameters: {param1}, {param2}")
       public static <T> Criteria<T> oneMoreCriteria(
               @DescriptionFragment("param1") Object param1,
               @DescriptionFragment("param2") Object param2) {
           return condition(t -> /* предикат*/);
       }
   }

.. code-block:: java
   :caption: пример для произвольного объекта. Данный механизм можно использовать, если требуется :doc:`интернационализация <./../../internationalization/internationalization>` объектов класса, который находится на поддержке и не входит в описанные выше примеры

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

   import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

   @Description("Some custom pojo")
   public class MyCustomObject {

       public String toString() {
           //Вызов метода, предназначенного для перевода/локализации
           return translate(this);
       }
   }



