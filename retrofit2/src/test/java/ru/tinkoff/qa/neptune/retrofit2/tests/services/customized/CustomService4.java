package ru.tinkoff.qa.neptune.retrofit2.tests.services.customized;

import retrofit2.http.GET;
import ru.tinkoff.qa.neptune.retrofit2.tests.DtoObject;

import java.util.List;

public interface CustomService4 extends CustomService {

    @GET("/get/json")
    List<DtoObject> getJson();

    @GET("/get/json")
    DtoObject[] getJsonArray();

    @GET("/get/xml")
    List<DtoObject> getXml();

    @GET("/get/xml")
    DtoObject[] getXmlArray();
}
