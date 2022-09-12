# Название / сообщение аттача

Рекомендуется указывать название аттача через
аннотацию [@Description](./../../steps/annotations/@Description_@DescriptionFragment.rst)

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;

@Description("My attachment") // сообщение / название аттача,
//которое наследуется, 
//может быть перекрыто другой аннотацией @Description
//в классе-наследнике
public class MyCaptor<T, S> extends Captor<T, S> {

    protected MyCaptor(List<? extends CapturedDataInjector<S>> injectors) {
        super(injectors);
    }
    
    /*
    protected MyCaptor(String message,
                       List<? extends CapturedDataInjector<S>> injectors) {
        super(message, injectors);
    }*/

    /*
    public MyCaptor(String message) {
        super(message);
    }*/
    

    /*
    //Рекомендуется, чтобы класс-наследник Captor
    //имел объявленный публичный конструктор без параметров, 
    //или чтобы у этого класса не было объявленного конструктора вообще
    public MyCaptor() {
        super();
    }
    */    
}
```