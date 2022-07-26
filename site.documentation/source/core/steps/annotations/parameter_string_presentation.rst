Строковое представление параметра
=================================

Названия шагов и критериев могут быть сформированы динамически с использованием маски, в которую подставляются строковые представления
объектов. Так же в виде строки должны быть представлены значения параметров шагов.

Объекты классов, которые реализуют интерфейс ``ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter``,
помогают представить фрагменты названий / значения параметров шагов в читаемом виде, когда это нужно

.. code-block:: java
   :caption: Пример реализации ``ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter``

    package org.my.pack;

    import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

    public class ExampleGetParameterValue
            //Данный интерфейс имеет тип-параметр.
            implements ParameterValueGetter<Object>
        //Реализация должна иметь тот же тип-парамер (либо родительский тип),
        // что и тип поля класса / параметра сигнатуры метода,
        //для которого данная реализация используется.
    {
        @Override
        public String getParameterValue(Object fieldValue) {
            return /*Преобразование объекта в читаемую строку*/;
        }
    }


