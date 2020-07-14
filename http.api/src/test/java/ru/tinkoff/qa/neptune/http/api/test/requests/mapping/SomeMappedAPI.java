package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject;
import ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.Body;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineContentType;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineFileName;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.FormParameter;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding.BINARY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.*;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.JSON;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.XML;

@Deprecated
public interface SomeMappedAPI extends HttpAPI<SomeMappedAPI> {

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
