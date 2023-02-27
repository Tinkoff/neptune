# Hibernate

Интеграция Neptune и [Hibernate](https://hibernate.org/), которая формирует шаги для 
CRUD-операций и операций поиска данных. Является расширением [абстракций для работы с базами данных в Neptune](./../database.abstractions.md)

```{eval-rst}
.. include:: hibernate_dependencies.rst
```

[API](https://tinkoff.github.io/neptune/hibernate/index.html)

## Поддерживаемые версии Hibernate

Зависимости от Hibernate и связанный библиотек транзитивными не являются. Ниже таблица версий Neptune
и поддерживаемых ими версий

| Диапазон версий Neptune | Диапазон поддерживаемых  версий Hibernate | Диапазон поддерживаемых версий QueryDSL |
|-------------------------|-------------------------------------------|-----------------------------------------|
| [0.22.0-ALPHA,)         | [5.6.0.Final,)                            | [5.0.0,)                                |

```{toctree}
:hidden:

settings/index.md
select/index.md
delete/delete.md
save/save.md
```