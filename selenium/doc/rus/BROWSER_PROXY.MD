Полезные страницы:

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser`

- [О принципах работы шагов, которые возвращают результат](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

- О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).  

- [Критерии для поиска нужных записей траффика](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/functions/browser/proxy/BrowserProxyCriteria.html)


## Проксирование/запись трафика браузера

Бывает, что необходимо в ходе тестирования front-end проверить трафик, возникающий входе того или иного действия на
странице приложения.

Для начала перехвата сетевого трафика необходимо активировать Network

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;

public class Tezzt {

  @Test
  public void tezzt() {
    //Результатом этого вызова будет:
    //            - активация и начало прослушивание трафика, если до этого Network был отключен
    //            - сброс прослушенного ранее трафика, если Network был вклчен
    inBrowser().enableAndRefreshNetwork();
  }
}
```

Далее можно извлекать записанный трафик

```java
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;

import java.util.List;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class Tezzt {

  @Test
  public void tezzt() {
    List<HttpTraffic> httpTrafficList = inBrowser()
            .enableAndRefreshNetwork()// активируем Network или сбрасываем уже прослушенный трафик
            .navigateTo("/some/url/path")
            .click(button("Submit"))
            .get(proxiedRequests() //получаем запросы, записанные входе действий выше
                    .recordedRequestMethod(GET) //можно перечислить критерии,
                    .recordedResponseStatusCode(200) //которым должны соответствовать извлекаемые 
                    .recordedRequestUrlMatches("https://www.google.com") //запросы
                    .timeOut(ofSeconds(10))); //можно указать время, за которое желаемые запросы должны быть получены

    //а так проверяется простой факт того, что ожидаемые запросы были отправлены/ответы получены
    boolean areRequestsPresent = inBrowser().presenceOf(proxiedRequests()
            .recordedRequestMethod(GET)//можно перечислить критерии,
            .recordedResponseStatusCode(200) //которым должны соответствовать извлекаемые 
            .recordedRequestUrlMatches("https://www.google.com") //запросы
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
import ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.HttpTraffic;

import java.util.List;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class Tezzt {

  @Test
  public void tezzt() {

    List<HarEntry> requests = inBrowser().navigateTo("/some/url/path")
            //какие-то промежуточные действия в браузере
            .enableAndRefreshNetwork() //<-сброс ранее записанного трафика
            .navigateTo("/some/url/path2") //новая запись трафика начинается тут
            .click(button("Submit"))
            .get(proxiedRequests() //получаем запросы, записанные входе действий выше
                    .recordedRequestMethod(GET)
                    .recordedResponseStatusCode(200)
                    .recordedRequestUrlMatches("https://www.google.com")
                    .timeOut(ofSeconds(10)));
  }
}
```
[к списку документов](README.MD#Оглавление)


