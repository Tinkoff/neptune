@UseInjectors
=============

.. code-block:: java
   :caption: Пример использования аннотации @UseInjectors

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
    import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;
    import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;

    import java.util.List;

    @UseInjectors(MyInjector.class) //Класс объекта,
    // который добавляет аттач к отчету / логу.
    // Здесь можно указать несколько классов.
    // Можно указать абстрактные классы,
    // реализующие интерфейс CapturedDataInjector<S>, или интерфейсы,
    // расширяющие CapturedDataInjector<S>.
    // Если в classpath есть их неабстрактные наследники / имплементоры,
    // тогда они будут использованы автоматически.
    //
    // ВАЖНО: Имплементоры CapturedDataInjector<S> не
    // должны иметь объявленных конструкторов,
    // или у них должны быть объявлены публичные конструкторы без параметров.
    public class MyCaptor<T, S> extends Captor<T, S> {

        protected MyCaptor(String message) {
            super(message);
        }

        /*
        protected MyCaptor(String message,
                           List<? extends CapturedDataInjector<S>> injectors) {
            super(message, injectors);
        }*/


        /*
        protected MyCaptor(List<? extends CapturedDataInjector<S>> injectors) {
            super(injectors);
        }
        /*

        /*
        //Рекомендуется, чтобы класс-наследник Captor
        //имел объявленный публичный конструктор без параметров,
        //или чтобы у этого класса не было объявленного конструктора вообще
        public MyCaptor() {
            super();
        }
        */
    }