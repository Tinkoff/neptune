package ru.tinkoff.qa.neptune.retrofit2.tests.capturing;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CapturingService {

    @POST("/success.html")
    Call<String> getSuccessful(@Body String body);

    @POST("/failure.html")
    Call<String> getFailed(@Body String body);
}
