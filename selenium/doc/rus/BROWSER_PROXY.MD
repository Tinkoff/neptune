# Проксирование/запись трафика браузера

Полезные страницы:
- [О принципах работы шагов, которые возвращают результат](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат).

Бывает, что необходимо в ходе тестирования front-end проверить трафик, возникающий входе того или иного действия на странице 
приложения. 

Для этого нужно выполнить настройки [проксирования трафика браузера](./SETTINGS.MD#Проксирование-трафика-браузера)

Далее, каждая новая сессия WebDriver`a будет проходить через локально работающий прокси-сервер. И теперь можно извлекать записанный 
трафик

```java
import java.util.List;
import org.openqa.selenium.WebElement;
import com.browserup.harreader.model.HarEntry;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        List<HarEntry> requests = inBrowser().navigateTo("/some/url/path")
                .click(button("Submit"))
                .get(proxiedRequests() //получаем запросы, записанные входе действий выше
                        .criteria(recordedRequestMethod(GET)) //можно перечислить критерии,
                        .criteria(recordedResponseStatusCode(200)) //которым должны соответствовать извлекаемые 
                        .criteria(recordedRequestUrlMatches("https://www.google.com")) //запросы
                        .timeOut(ofSeconds(10))); //можно указать время, за которое желаемые запросы 
        // должны быть получены
        
        //а так проверяется простой факт того, что ожидаемые запросы были отправлены/ответы получены
        boolean areRequestsPresent = inBrowser().presenceOf(proxiedRequests()
                .criteria(recordedRequestMethod(GET)) //можно перечислить критерии,
                .criteria(recordedResponseStatusCode(200)) //которым должны соответствовать извлекаемые 
                .criteria(recordedRequestUrlMatches("https://www.google.com")) //запросы
                .timeOut(ofSeconds(10))); //можно указать время, за которое желаемые запросы
        // должны быть получены
        // вернется true если хотя бы один запрос, подходящий под описание выше, 
        // был пойман, если нет - false

        boolean areRequestsPresent = inBrowser().presenceOf(proxiedRequests(), 
                "Запрос не был отправлен/ответ не был получен"); //Текст выбрасываемой ошибки
        // вернется true если хотя бы один запрос, подходящий под описание выше, 
        // был пойман, если нет - будет выброшено исключение с текстом 
        // 'Запрос не был отправлен/ответ не был получен'
    }
}
```

При каждом [обновлении контекста](./../../../core.api/doc/rus/REFRESHING_STOPPING.MD#Обновление) ранее записанный трафик 
сбрасывается автоматически. Но если его надо сбросить входе выполнения теста, тогда

```java
import java.util.List;
import org.openqa.selenium.WebElement;
import com.browserup.harreader.model.HarEntry;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {       
        
        List<HarEntry> requests = inBrowser().navigateTo("/some/url/path")
                //какие-то промежуточные действия в браузере
                .resetProxyRecording() //<-сброс ранее записанного трафика
                .navigateTo("/some/url/path2") //новая запись трафика начинается тут
                .click(button("Submit"))
                .get(proxiedRequests() //получаем запросы, записанные входе действий выше
                        .criteria(recordedRequestMethod(GET)) 
                        .criteria(recordedResponseStatusCode(200))  
                        .criteria(recordedRequestUrlMatches("https://www.google.com")) 
                        .timeOut(ofSeconds(10)));
    }
}
```

## Что если еще одна прокси была передана через объект Capabilities?

Локально работающий прокси-сервер и прокси, переданная через `Capabilities` выстраиваются в цепочку, в которой локальный
прокси сервер стоит первым.


