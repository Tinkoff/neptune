package ru.tinkoff.qa.neptune.retrofit2.tests;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BaseBodyDataTest {

    static void prepareMock() {
        stubFor(get(urlPathEqualTo("/get/json"))
                .willReturn(aResponse().withBody("[\n" +
                        "    {\n" +
                        "      \"bool\": true,\n" +
                        "      \"color\": \"gold\",\n" +
                        "      \"number\": 123,\n" +
                        "      \"object\": {\n" +
                        "        \"a\": \"b\",\n" +
                        "        \"c\": \"d\"\n" +
                        "      },\n" +
                        "      \"string\": \"Hello World\"\n" +
                        "    },\n" +
                        "    \n" +
                        "    {\n" +
                        "      \"bool\": false,\n" +
                        "      \"color\": \"red\",\n" +
                        "      \"number\": 567,\n" +
                        "      \"object\": {\n" +
                        "        \"d\": [123, 4567],\n" +
                        "        \"e\": \"d\"\n" +
                        "      },\n" +
                        "      \"string\": \"Goodbye\"\n" +
                        "    }\n" +
                        "]").withStatus(200)
                        .withStatusMessage("Successful json")
                        .withHeader("Custom header", "1", "Some String", "true")));

        stubFor(get(urlPathEqualTo("/get/xml"))
                .willReturn(aResponse().withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>")
                        .withStatus(200)
                        .withStatusMessage("Successful xml")
                        .withHeader("Custom header2", "2", "Some String 2", "false")));
    }
}
