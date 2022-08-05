# Как контрибъютить

- Рекомендуемая IDE: [IntelliJ IDEA](https://www.jetbrains.com/idea/). Community Edition достаточно
- Java >= 11
- [gradle](https://gradle.org/) >= 7.1

## Локальная сборка

Достаточно выполнить

> gradlew clean install

или

> gradlew clean publishToMavenLocal

## Для написания / обновления документации

- python 3.x
- [sphinx](https://www.sphinx-doc.org/en/master/usage/installation.html)
- установить [furo](https://github.com/pradyunsg/furo)

  > pip install furo

  или

  > pip3 install furo

- Должны быть установлены модуля из списка ниже:
  `sphinx.ext.autodoc`

  `sphinx.ext.extlinks`

  `sphinx.ext.intersphinx`

  `sphinx.ext.mathjax`

  `sphinx.ext.todo`

  `sphinx.ext.viewcode`

  `furo.sphinxext`

  `myst_parser`

  `sphinx_copybutton`

  `sphinx_design`

  `sphinx_inline_tabs`

- для IntelliJ IDEA рекомендуется установить [Python Plugin](https://plugins.jetbrains.com/plugin/631-python)

Для предварительного просмотра можно выполнить из директории, где находится модуль `site.documentation`

> sphinx-build /source /path/to/site/directory

## Что должен включать [Pull Request](https://github.com/Tinkoff/neptune/pulls)

- краткое описание, что сделано и зачем. Можно на английском, можно на русском.
- если был добавлен новый модуль / поменялась логика существующей функциональности - обязательна документация в модуле
  `site.documentation` и тесты