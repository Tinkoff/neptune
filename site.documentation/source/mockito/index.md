# Интеграция с Mockito

Интеграция с [Mockito](https://site.mockito.org/).

Данный модуль формирует шаги при вызове следующих методов:

- `org.mockito.Mockito.verify`
- `org.mockito.InOrder.verify`
- `org.mockito.MockedStatic.verify`
- `org.mockito.Mockito.verifyNoInteractions`
- `org.mockito.Mockito.verifyNoMoreInteractions`
- `org.mockito.InOrder.verifyNoMoreInteractions`
- `org.mockito.MockedStatic.verifyNoInteractions`
- `org.mockito.MockedStatic.verifyNoMoreInteractions`

```{eval-rst}
.. include:: mockito_dependencies.rst
```

[API](https://tinkoff.github.io/neptune/mockito.integration/index.html)

## Поддерживаемые версии Mockito

Зависимости от Mockito транзитивными не являются. Ниже таблица версий Neptune
и поддерживаемых ими версий Mockito:

| Диапазон версий Neptune | Диапазон поддерживаемых  версий Mockito |
|-------------------------|-----------------------------------------|
| [0.25.0-ALPHA,)         | [4.8.1,)                                |
                                           