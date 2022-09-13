@StepParameter
==============

С помощью аннотации можно обозначить произвольные параметры шагов и менять строковые представления их значений.

.. code-block:: java
   :caption: пример для класса-шага. Пример реализации ExampleGetParameterValue можно посмотреть :doc:`тут <parameter_string_presentation>`

   package org.my.pack;


    import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

    public class MyStepClass
       /*extends a subclass of SequentialGetStepSupplier, or SequentialActionSupplier*/
    {

        @StepParameter("Parameter A") //Так обозначается название параметра.
        //Параметр и его значение будут выведены в консоль, лог или репорт.
        private Object a;

        @StepParameter(value = "Parameter B",
                //Если на момент выполнения построенного шага
                doNotReportNullValues = true) //это поле не будет заполнено,
                //тогда параметр со значением не будут выведены
                //в консоль, лог или репорт
        private Object b;

        @StepParameter(value = "Parameter C",
                //Строковое представление объекта не всегда бывает удобным для чтения.
                //Для таких случаев можно
                //использовать классы, реализующие интерфейс
                // ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter
                makeReadableBy = ExampleGetParameterValue.class)
        private Object c;

        public MyStepClass setA(Object a) {
            this.a = a;
            return this;
        }

        public MyStepClass setB(Object b) {
            this.b = b;
            return this;
        }

        public MyStepClass setC(Object c) {
            this.c = c;
            return this;
        }
    }

Бывает параметров много, и они повторяются в различных классах-шагах. Тогда их можно группировать в POJO

.. code-block:: java
   :caption: пример для произвольного POJO

   package org.my.pack;

   import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
   import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

   public class ParameterPojo implements StepParameterPojo{ //такие POJO
       //должны реализовывать данный интерфейс

       @StepParameter("Pojo Parameter 1")
       private Boolean param1;

       @StepParameter(value = "Pojo Parameter 2", doNotReportNullValues = true)
       private String param2;

       @StepParameter(value = "Pojo Parameter 3",
                makeReadableBy = ExampleGetParameterValue.class)
       private Object param3;

       public Boolean getParam1() {
           return param1;
       }

       public ParameterPojo setParam1(Boolean param1) {
           this.param1 = param1;
           return this;
       }

       public String getParam2() {
           return param2;
       }

       public ParameterPojo setParam2(String param2) {
           this.param2 = param2;
           return this;
       }

       public Object getParam3() {
           return param3;
       }

       public ParameterPojo setParam3(Object param3) {
           this.param3 = param3;
           return this;
       }
   }