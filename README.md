# Neptune

[![Neptune-master Status](https://github.com/TinkoffCreditSystems/neptune/workflows/gradle-ci/badge.svg)](https://github.com/TinkoffCreditSystems/neptune/actions)

[![License][license-badge]][license-link]

[![OpenIssues][openissues-badge]][openissues-link]
[![ClosedIssues][closedissues-badge]][closedissues-link]

[![Version][version-badge]][version-link]
[![CommitSince][commitsince-badge]][commitsince-link]
[![Commit][commit-badge]][commit-link]


[build-badge]: https://travis-ci.com/TinkoffCreditSystems/neptune.svg?branch=master
[build-link]: https://travis-ci.com/TinkoffCreditSystems/neptune

[version-badge]: https://img.shields.io/github/v/tag/TinkoffCreditSystems/neptune?label=release
[version-link]: https://github.com/TinkoffCreditSystems/neptune/releases

[commitsince-badge]: https://img.shields.io/github/commits-since/TinkoffCreditSystems/neptune/0.12.1-ALPHA
[commitsince-link]: https://github.com/TinkoffCreditSystems/neptune/compare/0.12.1-ALPHA...master

[commit-badge]: https://img.shields.io/github/last-commit/TinkoffCreditSystems/neptune/master?color=blue
[commit-link]: https://github.com/TinkoffCreditSystems/neptune/commits/master

[license-badge]: https://img.shields.io/github/license/TinkoffCreditSystems/neptune?color=9cf
[license-link]: https://github.com/TinkoffCreditSystems/neptune/blob/master/LICENSE

[reposize-badge]: https://img.shields.io/github/repo-size/TinkoffCreditSystems/neptune?color=9cf

[openissues-badge]: https://img.shields.io/github/issues-raw/TinkoffCreditSystems/neptune
[openissues-link]: https://github.com/TinkoffCreditSystems/neptune/issues?q=is%3Aopen+is%3Aissue

[closedissues-badge]: https://img.shields.io/github/issues-closed-raw/TinkoffCreditSystems/neptune
[closedissues-link]: https://github.com/TinkoffCreditSystems/neptune/issues?q=is%3Aissue+is%3Aclosed

Test automation framework for automation of E2E/system/integrating testing.

It is under the ALPHA testing by developers/QA engineers of [Tinkoff.ru](https://www.tinkoff.ru/software/) for a while. Built jar are not available outside. For now source code and documentation is available for overview.

Module documentation:

- [Core module](core.api/README.md)
- [Integration of Neptune with Hamcrest matchers](check/README.md)
- [Integration of Neptune with Allure test reporting framework](allure.integration/README.md)
- [Integration of Neptune native http client of Java (since v11)](http.api/README.md)
- [Integration of Neptune with Swagger 3.x](neptune.swagger.codegen/README.md)
- [Integration of Neptune with Retrofit2](retrofit2/README.md)
- [Integration of Neptune with Rabbit MQ](rabbit.mq/README.MD)  
- [Integration of Neptune with Kafka](kafka/README.md)
- [Integration of Neptune with Selenium WebDriver API](selenium/README.md)
- [Neptune Database abstractions](database.abstractions/README.md)

For Spring projects:
- [Integration of Neptune with Spring MockMvc](spring.mock.mvc/README.md)
- [Integration of Neptune with Spring WebTestClient](spring.web.testclient/README.md)

For test runners:
- [Integration of Neptune with TestNG framework](testng.integration/README.md)
- [Neptune + Testng + Allure](allure.testng.bridge/README.md)
- [Integration of Neptune with JUnit5](jupiter.integration/README.md)
- [Neptune + JUnit5 + Allure](allure.jupiter.bridge/README.md)

[Overview of all packages](https://tinkoffcreditsystems.github.io/neptune/overview-summary.html)

[Issues to be fixed/Requred features](https://github.com/TinkoffCreditSystems/neptune/issues)

[Change list](https://github.com/TinkoffCreditSystems/neptune/releases)

Owners: [@TikhomirovSergey](https://github.com/TikhomirovSergey), [@ArisAgnew](https://github.com/ArisAgnew), [@AndrewCharykov](https://github.com/AndrewCharykov), [@Burnouttt](https://github.com/Burnouttt)

First public BETA-versions are coming soon
