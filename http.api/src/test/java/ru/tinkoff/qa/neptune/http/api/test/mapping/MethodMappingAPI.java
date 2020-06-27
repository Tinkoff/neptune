package ru.tinkoff.qa.neptune.http.api.test.mapping;

import ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject;
import ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.Header;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.PathParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.QueryParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.Body;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart.DefineContentType;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart.DefineFileName;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.url.encoded.FormParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.URIPath;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding.BINARY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.BodyDataFormat.JSON;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.BodyDataFormat.XML;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.*;

public interface MethodMappingAPI extends HttpAPI<MethodMappingAPI> {

    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomething();

    @HttpMethod(httpMethod = GET)
    RequestBuilder getSomething();

    @HttpMethod(httpMethod = PUT)
    RequestBuilder putSomething();

    @HttpMethod(httpMethod = DELETE)
    RequestBuilder deleteSomething();

    @HttpMethod(httpMethod = PATCH)
    RequestBuilder patchSomething();

    @HttpMethod(httpMethod = HEAD)
    RequestBuilder headSomething();

    @HttpMethod(httpMethod = OPTIONS)
    RequestBuilder optionsSomething();

    @HttpMethod(httpMethod = TRACE)
    RequestBuilder traceSomething();

    @HttpMethod(httpMethodStr = "CUSTOM_METHOD")
    RequestBuilder customMethod();

    @Header(name = "header1", headerValues = {"abc", "one more value"})
    @Header(name = "header2", headerValues = "one more value")
    @Header(name = "header3", headerValues = "one more value again")
    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomethingWithHeaders();

    @HttpMethod(httpMethod = GET)
    @URIPath("path/to/target/end/point")
    RequestBuilder getSomethingWithConstantPath();

    @HttpMethod(httpMethod = GET)
    @URIPath("{path begin}/{next}/and/then/{third}/end/point")
    RequestBuilder getSomethingWithVariablePath(@PathParameter("path begin") String start,
                                                @PathParameter("next") float next,
                                                @PathParameter("third") String third);

    @HttpMethod(httpMethod = GET)
    @URIPath("{path begin}/{next}/and/then/{next}/end/point")
    RequestBuilder getSomethingWithVariablePath(@PathParameter("path begin") String start,
                                                @PathParameter("next") String next);

    @HttpMethod(httpMethod = GET)
    @URIPath("{path begin}/{next}/and/then/third/end/point")
    RequestBuilder getSomethingWithVariablePathFailed(@PathParameter("path begin") String start,
                                                      @PathParameter("next") float next,
                                                      @PathParameter("third") String third);

    @HttpMethod(httpMethod = GET)
    @URIPath("{path begin}/{next}/and/then/third/end/point")
    RequestBuilder getSomethingWithVariablePathFailed(@PathParameter("path begin") String start,
                                                      @PathParameter("next") float next,
                                                      @PathParameter("next") boolean third);

    @HttpMethod(httpMethod = GET)
    RequestBuilder getSomethingWithQuery(@QueryParameter("param1") String value1,
                                         @QueryParameter("param1") int value2,
                                         @QueryParameter("param1") String value3,
                                         @QueryParameter("param2") Boolean value4);

    @HttpMethod(httpMethod = GET)
    @URIPath("path/to/target/end/point")
    RequestBuilder getSomethingWithQueryAndPath(@QueryParameter("param1") String value1,
                                                @QueryParameter("param1") int value2,
                                                @QueryParameter("param1") String value3,
                                                @QueryParameter("param2") Boolean value4);

    @HttpMethod(httpMethod = GET)
    RequestBuilder getSomethingWithQuery(@QueryParameter("param1") List<Object> param1,
                                         @QueryParameter("param2") Boolean param2);

    @HttpMethod(httpMethod = GET)
    @URIPath("path/to/target/end/point")
    RequestBuilder getSomethingWithQueryAndPath(@QueryParameter("param1") List<Object> param1,
                                                @QueryParameter("param2") Boolean param2);

    @HttpMethod(httpMethod = GET)
    RequestBuilder getSomethingWithQuery(@QueryParameter("param1") Object[] param1,
                                         @QueryParameter("param2") Boolean param2);

    @HttpMethod(httpMethod = GET)
    @URIPath("path/to/target/end/point")
    RequestBuilder getSomethingWithQueryAndPath(@QueryParameter("param1") Object[] param1,
                                                @QueryParameter("param2") Boolean param2);

    @HttpMethod(httpMethod = GET)
    RequestBuilder getSomethingWithQuery(@QueryParameter("param1") int[] param1,
                                         @QueryParameter("param2") Boolean param2);

    @HttpMethod(httpMethod = GET)
    @URIPath("path/to/target/end/point")
    RequestBuilder getSomethingWithQueryAndPath(@QueryParameter("param1") int[] param1,
                                                @QueryParameter("param2") Boolean param2);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postSomeBody(@Body String body);

    @HttpMethod(httpMethod = GET)
    RequestBuilder getSomeBody(@Body String body);

    @HttpMethod(httpMethod = PUT)
    RequestBuilder putSomeBody(@Body String body);

    @HttpMethod(httpMethod = DELETE)
    RequestBuilder deleteBody(@Body String body);

    @HttpMethod(httpMethodStr = "PATCH")
    RequestBuilder patchSomeBody(@Body String body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postByteBody(@Body byte[] body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postFileBody(@Body File body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postFileBody(@Body Path body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postJsoupBody(@Body org.jsoup.nodes.Document body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postW3CBody(@Body org.w3c.dom.Document body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postMap(@Body Map<String, String> body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postStream(@Body InputStream body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postSupplier(@Body Supplier<InputStream> body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postJson(@Body JsonDTObject body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postXml(@Body XmlDTObject body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postBoolean(@Body Boolean body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postListJson(@Body(format = JSON) List<Object> body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postXmlMap(@Body(format = XML, mixIns = MapRootElementMixIn.class) Map<?, ?> body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postXmlArray(@Body(format = XML, mixIns = ArrayMixIn.class) Object... body);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postForm(@FormParameter("form_string_param1") String param1,
                            @FormParameter("form_int_param2") int param2,
                            @FormParameter("form_bool_param3") boolean param3);

    @HttpMethod(httpMethod = POST)
    RequestBuilder postMultipart(@MultiPartBody(name = "test_file", contentTransferEncoding = BINARY) File file,
                                 @MultiPartBody(name = "test_xml",
                                         format = XML,
                                         mixIns = ArrayMixIn.class)
                                 @DefineContentType(contentType = "application/xml") Object[] array,

                                 @MultiPartBody(name = "test_file2")
                                 @DefineFileName(useGivenFileName = true) Path path,

                                 @MultiPartBody(name = "test_binary")
                                 @DefineFileName
                                 @DefineContentType byte[] binary,

                                 @MultiPartBody(name = "test_file3")
                                 @DefineContentType Path path2,

                                 @MultiPartBody(name = "test_binary2")
                                 @DefineFileName(fileName = "tezzt_file")
                                 @DefineContentType byte[] binary2,

                                 @MultiPartBody(name = "test_file4")
                                 @DefineFileName(fileName = "tezzt_file") Path path3);
}
