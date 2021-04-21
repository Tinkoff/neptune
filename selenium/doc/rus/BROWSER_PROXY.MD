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

        boolean areRequestsPresent = inBrowser().presenceOfOrThrow(proxiedRequests());
        // вернется true если хотя бы один запрос, подходящий под описание выше, 
        // был пойман, если нет - будет выброшено исключение
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

## Как достать локальный прокси-сервер, который используется/будет использован текущей/вновь открываемой сессией Webdriver'а

Вообще, этого в большинстве случаев делать не рекомендуется. Но иногда бывают ситуации, когда важно узнать
статус/параметры локального прокси сервера. Например, нужно настроить Capabilities (до открытия браузера).

Для таких случаев пример ниже

```java
import ru.tinkoff.qa.neptune.selenium.BrowserProxy;

import static ru.tinkoff.qa.neptune.selenium.BrowserProxy.getCurrentProxy;

public class SomeClass {
    
    public void someMethod() {
        //Если USE_BROWSER_PROXY=true, вернется объект, который используется
        //текущей сессией WebDriver, или будет использован новой сессией WebDriver,
        //открываемой в текущем потоке.
        //Если USE_BROWSER_PROXY!=true, вернется null
        BrowserProxy proxy = getCurrentProxy();

        //Если инстанс прокси сервера еще не создан,
        //можно его заранее создать. Запуск тут не происходит.
        //Если инстанс уже создан, то никакого эффекта это действие не имеет
        proxy.createProxy();
        
        //данный объект через свои методы позволяет узнать данные локального прокси-сервера 
        int port = proxy.getLocalPort(); //можно узнать порт где работает/будет работать сервер
        String ip = proxy.getHostIP(); //можно узнать ip4 локальной машины, на localhost
        boolean isStarted = proxy.isStarted(); //запущен прокси сервер или нет
        Har har = proxy.getHar(); //Актуальный har-файл
    }
}
```


