# Кратко о проекте

Данный проект разработан для автоматизации интеграционного тестирования. На данный момент возможно взаимодействие с:
- UI прилоложения (браузер)
- базами данных
- http API

Поддерживаемые фреймворки:
- TestNg. Поддержка многопоточного выполнения тестов.

Интеграция с инструментами для построения отчетов:
- Allure

Проект включает в себя модули:
- [core.api](https://tinkoffcreditsystems.github.io/neptune/core.api/) - ядро. Общая функциональнольность.
- [check](https://tinkoffcreditsystems.github.io/neptune/check/) - инструменты для осуществления проверок
- [selenium](https://tinkoffcreditsystems.github.io/neptune/selenium/) - инструменты для взаимодействия с браузером. Интеграция с [Selenium API](https://www.seleniumhq.org/)
- [data.base.api](https://tinkoffcreditsystems.github.io/neptune/data.base.api/) - инструменты для взаимодействия с базами данных. Интеграция с [Datanucleus](http://www.datanucleus.org/)
- [http.api](https://tinkoffcreditsystems.github.io/neptune/http.api/) - инструменты для взаимодействия с http/web/rest API
- [testng.integration](https://tinkoffcreditsystems.github.io/neptune/testng.integration/) - интеграция с [TestNg](https://testng.org/doc/index.html)
- [allure.integration](https://tinkoffcreditsystems.github.io/neptune/allure.integration/) - интеграция c [Allure 2](https://docs.qameta.io/allure/)
- **allure.testng.bridge** - связывает `testng.integration` и `allure.integration`.

В других главах описаны перечисленные выше модули и как начать с ними работать.

## Требования
 
 - Операционнаяя система - Windows/Mac Os X/Linux
 - Java Development Kit 11
 - [maven](https://maven.apache.org/) или [gradle](https://gradle.org/) 





