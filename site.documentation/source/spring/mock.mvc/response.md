# Mock MVC. Request / Response

```java
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test(description = "Простое выполнение запроса и получение ответа")
    public void myTest() {
        MockHttpServletResponse response = mockMvcGet(
            //описывается запрос:
            // метод (GET, POST, PUT или Delete)
            response(get("/something") //и
                .contentType(APPLICATION_JSON) //параметры
                .content("{\"someString\":\"1\"}"))
        );
    }
}
```

Ниже пример с явным использованием объекта `org.springframework.test.web.servlet.MockMvc`

```java
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Autowired //Или любой другой вариант
    private MockMvc mockMvc; //инициализации поля или переменной

    @Test(description = "Простое выполнение запроса и получение ответа")
    public void myTest() {
        MockHttpServletResponse response = mockMvcGet(
            response(
                mockMvc, //явная передача объекта MockMvc
                get("/something")
                    .contentType(APPLICATION_JSON)
                    .content("{\"someString\":\"1\"}"))
        );
    }
}
```

## Mock MVC. Описание ожиданий

Методы, с помощью которых можно описать ожидаемый ответ, можно
[посмотреть в документации. Названия методов начинаются на _expect_](https://tinkoff.github.io/neptune/spring.mock.mvc/ru/tinkoff/qa/neptune/spring/mock/mvc/GetMockMvcResponseResultSupplier.html).

Ниже простой пример

```java
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .GetMockMvcResponseResultSupplier.response;
import static ru.tinkoff.qa.neptune.spring.mock.mvc
    .MockMvcContext.mockMvcGet;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTest {

    @Test(description = "Простое выполнение запроса и получение ответа")
    public void myTest() {
        var response = mockMvcGet(
            response(get("/something")
                .contentType(APPLICATION_JSON)
                .content("{\"someString\":\"1\"}"))
                // Часто используемые ожидания имеют  
                .expectStatus(200) //упрощенную запись
                .expectContent(containsString("SUCCESS"))
                /////////////////////////////////////////
                //
                //Так же, при необходимости, есть возможности описывать
                //ожидания в функциональном стиле
                .expectStatus(resultStatus -> {
                    //некое вычисление или алгоритм
                    return resultStatus.isOk();
                })
                .expectContent(contentResultMatchers -> {
                    //некое вычисление или алгоритм
                    return contentResultMatchers.string(containsString("SUCCESS"));
                })
        );
    }
}
```



