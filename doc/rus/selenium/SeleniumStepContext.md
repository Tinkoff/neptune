# Контекст шагов для Selenium

[SeleniumStepContext](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html) - [контекст](/doc/rus/core/Context.md) для шагов, осуществляющих интерактивное взаимодействие с web-клиентом тестируемого приложения

- хранит ссылку на открытый локальный/удаленный браузер;

- в зависимости от [настроек](/doc/rus/selenium/Settings.md#Обновление-вебдрайвера) производит [обновление](/doc/rus/core/Context.md#Обновление-контекста), используя следующие опции:

  - полный перезапуск браузера
  - очистка cookie браузера
  - переход на стартовую страницу приложения

