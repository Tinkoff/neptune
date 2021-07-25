package ru.tinkoff.qa.neptune.retrofit2.tests.services.common;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.tinkoff.qa.neptune.retrofit2.tests.DtoObject;

import java.util.List;

public interface CallService {

    @GET("/get/json")
    Call<List<DtoObject>> getJson();

    @GET("/get/json")
    Call<DtoObject[]> getJsonArray();

    @GET("/get/xml")
    Call<List<DtoObject>> getXml();

    @GET("/get/xml")
    Call<DtoObject[]> getXmlArray();
}
