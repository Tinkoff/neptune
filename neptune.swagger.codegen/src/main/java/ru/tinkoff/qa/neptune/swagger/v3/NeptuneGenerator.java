package ru.tinkoff.qa.neptune.swagger.v3;

import io.swagger.codegen.v3.*;
import io.swagger.codegen.v3.generators.java.AbstractJavaCodegen;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.PathParameter;

import java.util.*;

import static io.swagger.codegen.v3.CodegenConstants.*;
import static io.swagger.codegen.v3.generators.handlebars.ExtensionHelper.getBooleanValue;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class NeptuneGenerator extends AbstractJavaCodegen {

    private static final String TITLE = "title";
    private static final String CONFIG_PACKAGE = "configPackage";
    private static final String BASE_PACKAGE = "basePackage";
    private static final String USE_TAGS = "useTags";
    private static final String IMPLICIT_HEADERS = "implicitHeaders";
    private static final String SKIP_SUPPORT_FILES = "skipSupportFiles";
    private static final String X_EXPLODE = "x-explode";
    private static final String X_STYLE = "x-style";
    private static final String X_ALLOW_RESERVED = "x-allow-reserved";
    private static final String X_FORM_PARAMETER = "x-form_parameter";
    private static final String X_BODY_PARAM_FORMAT = "x-body_param_format";
    private static final String X_MULTIPART_CONTENT_TRANSFER_ENCODING = "x-multipart-content-transfer-encoding";
    private static final String X_MULTIPART_CONTENT_TYPE = "x-multipart-content-type";
    private static final String X_MULTIPART_DEFINE_FILE_NAME = "x-multipart-define-file-name";
    private static final String X_XML_PROPERTY_NAME = "x-xml-property-name";
    private static final String X_XML_PROPERTY_NAMESPACE = "x-xml-property-namespace";
    private static final String X_XML_WRAPPER_NAME = "x-xml-wrapper-name";
    private static final String X_XML_WRAPPER_NAMESPACE = "x-xml-wrapper-namespace";
    private static final String X_IS_XML_ATTRIBUTE = "x-is-xml-attribute";
    private final Map<String, CodegenModel> handledModels = new HashMap<>();
    // source folder where to write the files
    protected String apiVersion = "1.0.0";
    private String configPackage = "io.swagger.configuration";
    private String basePackage = "io.swagger";
    private boolean useTags = false;


    public NeptuneGenerator() {
        super();

        // set the output folder here
        outputFolder = "generated-code/neptune";

        /**
         * Template Location.  This is the location which templates will be read from.  The generator
         * will use the resource stream to attempt to read the templates.
         */
        templateDir = "neptune";

        /**
         * Api Package.  Optional, if needed, this can be used in templates
         */
        apiPackage = "io.swagger.client.api";

        /**
         * Model Package.  Optional, if needed, this can be used in templates
         */
        modelPackage = "io.swagger.client.model";


        /**
         * Additional Properties.  These values can be passed to the templates and
         * are available in models, apis, and supporting files
         */
        additionalProperties.put("apiVersion", apiVersion);
        init();
        setWithXml(true);
    }

    private static String getStyle(Enum<?> style) {
        return ofNullable(style)
                .map(styleEnum -> {
                    if (styleEnum.toString().equals("simple") || styleEnum.toString().equals("form")) {
                        return null;
                    }

                    String result = null;
                    switch (styleEnum.toString()) {
                        case "matrix":
                            result = "PathStyles.MATRIX";
                            break;
                        case "label":
                            result = "PathStyles.LABEL";
                            break;
                        case "spaceDelimited":
                            result = "FormStyles.SPACE_DELIMITED";
                            break;
                        case "pipeDelimited":
                            result = "FormStyles.PIPE_DELIMITED";
                            break;
                        case "deepObject":
                            result = "FormStyles.DEEP_OBJECT";
                            break;
                    }

                    return result;
                })
                .orElse(null);
    }

    private static Map<String, Object> prepareFormParamExtensions(CodegenOperation op, Encoding e) {
        var extensions = new HashMap<String, Object>();

        var toExplode = ofNullable(e.getExplode()).orElse(false);
        if (toExplode) {
            extensions.put(X_EXPLODE, true);
        }

        var style = getStyle(e.getStyle());
        ofNullable(style).ifPresent(s -> extensions.put(X_STYLE, s));

        var allowReserved = ofNullable(e.getAllowReserved()).orElse(false);
        if (allowReserved) {
            extensions.put(X_ALLOW_RESERVED, true);
        }

        extensions.put(X_FORM_PARAMETER, true);
        if (!toExplode || style != null || allowReserved) {
            if (style != null) {
                op.imports.add("FormStyles");
            }
        }

        return extensions;
    }

    private static Map<String, Object> prepareBodyFormatExtensions(String ct, CodegenParameter param) {
        var extensions = new HashMap<String, Object>();

        if (ct != null && (ct.toLowerCase().contains("json")
                || ct.toLowerCase().contains("xml")
                || ct.toLowerCase().contains("html"))
                && (param == null || !"string".equalsIgnoreCase(param.dataType))) {
            if (ct.toLowerCase().contains("json")) {
                extensions.put(X_BODY_PARAM_FORMAT, "BodyDataFormat.JSON");
                return extensions;
            }

            if (ct.toLowerCase().contains("xml")) {
                extensions.put(X_BODY_PARAM_FORMAT, "BodyDataFormat.XML");
                return extensions;
            }

            if (ct.toLowerCase().contains("html")) {
                extensions.put(X_BODY_PARAM_FORMAT, "BodyDataFormat.HTML");
                return extensions;
            }
        }

        return extensions;
    }

    private static void prepareMultipartExtensions(CodegenOperation op, CodegenParameter param, Encoding e) {
        if (param != null) {
            if (param.vendorExtensions.containsKey(IS_MULTIPART_EXT_NAME)) {
                op.imports.add("MultiPartBody");

                var ct = e.getContentType();
                if (ct != null) {
                    op.imports.add("DefineContentType");
                    param.vendorExtensions.put(X_MULTIPART_CONTENT_TYPE, ct);
                }

                if ("file".equalsIgnoreCase(param.dataType)) {
                    op.imports.add("DefineFileName");
                    param.vendorExtensions.put(X_MULTIPART_DEFINE_FILE_NAME, true);
                }

                var h = e.getHeaders();
                if (h != null) {
                    ofNullable(h.get("Content-Transfer-Encoding"))
                            .ifPresent(header -> {
                                var schema = header.getSchema();
                                if (schema != null) {
                                    var dv = schema.getDefault();
                                    if (dv != null) {
                                        switch (dv.toString().trim().toLowerCase()) {
                                            case "7bit":
                                                op.imports.add("ContentTransferEncoding");
                                                param.vendorExtensions.put(X_MULTIPART_CONTENT_TRANSFER_ENCODING, "ContentTransferEncoding.BIT7");
                                                break;
                                            case "quoted-printable":
                                                op.imports.add("ContentTransferEncoding");
                                                param.vendorExtensions.put(X_MULTIPART_CONTENT_TRANSFER_ENCODING, "ContentTransferEncoding.QUOTED_PRINTABLE");
                                                break;
                                            case "base64":
                                                op.imports.add("ContentTransferEncoding");
                                                param.vendorExtensions.put(X_MULTIPART_CONTENT_TRANSFER_ENCODING, "ContentTransferEncoding.BASE64");
                                                break;
                                            case "8bit":
                                                op.imports.add("ContentTransferEncoding");
                                                param.vendorExtensions.put(X_MULTIPART_CONTENT_TRANSFER_ENCODING, "ContentTransferEncoding.BIT8");
                                                break;
                                            case "binary":
                                                op.imports.add("ContentTransferEncoding");
                                                param.vendorExtensions.put(X_MULTIPART_CONTENT_TRANSFER_ENCODING, "ContentTransferEncoding.BINARY");
                                                break;
                                            case "x-token":
                                                op.imports.add("ContentTransferEncoding");
                                                param.vendorExtensions.put(X_MULTIPART_CONTENT_TRANSFER_ENCODING, "ContentTransferEncoding.X_TOKEN");
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }
                            });
                }
            }
        }
    }

    private static void improveBody(CodegenOperation op) {
        if (op.bodyParams == null || op.bodyParams.size() == 0) {
            return;
        }

        op.bodyParams.forEach(p -> {
            if (op.contents == null || op.contents.size() == 0) {
                return;
            }

            op.contents.forEach(c -> {
                var ct = c.getContentType();
                c.getParameters().forEach(parameter -> {
                    if (Objects.equals(parameter.baseName, p.baseName)) {
                        var bodyFormatExtensions = prepareBodyFormatExtensions(ct, parameter);
                        if (bodyFormatExtensions.size() > 0) {
                            op.imports.add("BodyParamFormat");
                            op.imports.add("BodyDataFormat");
                            parameter.vendorExtensions.putAll(prepareBodyFormatExtensions(ct, parameter));
                        }

                        ofNullable(parameter.vendorExtensions.get(IS_BINARY_EXT_NAME))
                                .ifPresent(o -> {
                                    if ((Boolean) o && "Object".equalsIgnoreCase(parameter.dataType)) {
                                        parameter.dataType = "File";
                                        op.imports.add("File");
                                    }
                                });
                    }
                });
            });
        });
    }

    private static void improveBodyParameters(Operation operation, CodegenOperation op, Map<String, Schema> schemas) {
        var body = operation.getRequestBody();
        if (body == null) {
            return;
        }

        ofNullable(body.getContent())
                .ifPresentOrElse(content -> content
                        .forEach((c, m) -> {
                            var ref = ofNullable(m.getSchema())
                                    .map(Schema::get$ref)
                                    .orElse(null);

                            if (ref != null) {
                                var refComponents = ref.split("/");
                                var name = refComponents[refComponents.length - 1];
                                List<String> requiredFields = ofNullable(schemas.get(name)).map(Schema::getRequired).orElse(null);
                                if (requiredFields != null && requiredFields.size() > 0) {
                                    requiredFields.forEach(o -> op.contents.stream()
                                            .filter(c1 -> c1.getContentType().equals(c))
                                            .findFirst()
                                            .flatMap(c1 -> c1.getParameters().stream().filter(parameter -> Objects.equals(parameter.baseName, o))
                                                    .findFirst())
                                            .ifPresent(parameter -> parameter.required = true));
                                }
                            }

                            var encoding = m.getEncoding();
                            if (encoding == null) {
                                improveBody(op);
                                return;
                            }

                            encoding.forEach((k, e) -> {
                                var param = op.contents.stream()
                                        .filter(c1 -> c1.getContentType().equals(c))
                                        .findFirst()
                                        .flatMap(c1 -> c1.getParameters().stream().filter(parameter -> Objects.equals(parameter.baseName, k))
                                                .findFirst())
                                        .orElse(null);

                                Map<String, Object> extensions = new HashMap<>();
                                var bodyFormatExtensions = prepareBodyFormatExtensions(e.getContentType(), param);
                                var bodyFormParamExtensions = prepareFormParamExtensions(op, e);
                                if (bodyFormatExtensions.size() > 0) {
                                    op.imports.add("BodyParamFormat");
                                    op.imports.add("BodyDataFormat");
                                    extensions.putAll(bodyFormatExtensions);
                                } else if (c.toLowerCase().contains("x-www-form-urlencoded")) {
                                    op.imports.add("FormParam");
                                    extensions.putAll(bodyFormParamExtensions);
                                }

                                if (param != null) {
                                    param.vendorExtensions.putAll(extensions);

                                    if (param.vendorExtensions.containsKey(IS_MULTIPART_EXT_NAME)) {
                                        prepareMultipartExtensions(op, param, e);
                                    }
                                }
                            });
                        }), () -> improveBody(op));
    }

    private static boolean toClear(Object o) {
        var strValue = valueOf(o).toLowerCase();

        if ("body".equals(strValue)) {
            return true;
        }

        if (strValue.startsWith("body")) {
            for (var i = 1; i < 1000000; i++) {
                if (("body" + i).equalsIgnoreCase(strValue)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void improveXml(CodegenModel codegenModel, Schema<?> schema) {
        var modelVars = codegenModel.vars;
        if (modelVars != null) {
            modelVars.forEach(codegenProperty -> {
                String xmlElementProperty;
                String xmlElementNS;

                String xmlWrapperProperty = null;
                String xmlWrapperNS = null;
                boolean isAttribute = false;

                if (codegenProperty.items != null) {
                    xmlElementProperty = codegenProperty.items.xmlName;
                    xmlElementNS = codegenProperty.items.xmlNamespace;

                    if (xmlElementProperty == null) {
                        xmlElementProperty = codegenProperty.items.baseName;
                    }

                    if (xmlElementProperty == null) {
                        xmlElementProperty = codegenProperty.xmlName;
                    }

                    if (xmlElementProperty == null) {
                        xmlElementProperty = codegenProperty.baseName;
                    }

                    if (xmlElementNS == null) {
                        xmlElementNS = codegenProperty.xmlNamespace;
                    }
                } else {
                    xmlElementProperty = codegenProperty.xmlName;
                    xmlElementNS = codegenProperty.xmlNamespace;

                    if (xmlElementProperty == null) {
                        xmlElementProperty = codegenProperty.baseName;
                    }

                }

                if (Objects.equals(codegenProperty.vendorExtensions.get(IS_XML_WRAPPED_EXT_NAME), true)) {
                    xmlWrapperProperty = codegenProperty.xmlName;
                    xmlWrapperNS = codegenProperty.xmlNamespace;

                    if (xmlWrapperProperty == null) {
                        xmlWrapperProperty = codegenProperty.baseName;
                    }
                }

                var property = schema.getProperties().get(codegenProperty.getBaseName());
                if (property != null) {
                    var xml = property.getXml();
                    if (xml != null) {
                        var isAttr = xml.getAttribute();
                        isAttribute = isAttr != null && isAttr;
                    }
                }

                codegenProperty.vendorExtensions.put(X_XML_PROPERTY_NAME, xmlElementProperty);
                codegenProperty.vendorExtensions.put(X_XML_PROPERTY_NAMESPACE, xmlElementNS);

                if (Objects.equals(codegenProperty.vendorExtensions.get(IS_XML_WRAPPED_EXT_NAME), true)) {
                    codegenProperty.vendorExtensions.put(X_XML_WRAPPER_NAME, xmlWrapperProperty);
                    codegenProperty.vendorExtensions.put(X_XML_WRAPPER_NAMESPACE, xmlWrapperNS);
                }

                if (isAttribute) {
                    codegenProperty.vendorExtensions.put(X_IS_XML_ATTRIBUTE, true);
                }
            });
        }
    }

    private static Map<String, Object> clearModels(Map<String, Object> processedModels) {
        var map = new TreeMap<>(processedModels);

        processedModels.forEach((s, o) -> {
            if (o instanceof Map<?, ?>) {
                var className = ((Map<?, ?>) o).get("classname");
                if (ofNullable(className)
                        .map(NeptuneGenerator::toClear)
                        .orElse(false)) {
                    map.remove(s);
                }
            }
        });

        return map;
    }

    /**
     * Configures the type of generator.
     *
     * @return the CodegenType for this generator
     * @see io.swagger.codegen.CodegenType
     */
    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    /**
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -l flag.
     *
     * @return the friendly name for the generator
     */
    public String getName() {
        return "neptune";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     *
     * @return A string value for the help message
     */
    public String getHelp() {
        return "Generates a neptune client library.";
    }

    private void init() {
        outputFolder = "generated-code/javaMicronaut";
        apiPackage = "io.swagger.api";
        modelPackage = "io.swagger.model";
        invokerPackage = "io.swagger.api";
        artifactId = "swagger-neptune";

        additionalProperties.put(CONFIG_PACKAGE, configPackage);
        additionalProperties.put(BASE_PACKAGE, basePackage);

        //neptune uses the jackson lib
        additionalProperties.put("jackson", "true");

        cliOptions.add(new CliOption(TITLE, "server title name or client service name"));
        cliOptions.add(new CliOption(CONFIG_PACKAGE, "configuration package for generated code"));
        cliOptions.add(new CliOption(BASE_PACKAGE, "base package (invokerPackage) for generated code"));
        cliOptions.add(new CliOption(SKIP_SUPPORT_FILES, "skip support files such as pom.xml, mvnw, etc from code generation."));
        cliOptions.add(CliOption.newBoolean(USE_TAGS, "use tags for creating interface and controller classnames"));
        cliOptions.add(CliOption.newBoolean(IMPLICIT_HEADERS, "Use of @ApiImplicitParams for headers."));

        supportedLibraries.put(DEFAULT_LIBRARY, "Java Micronaut Server application.");
        setLibrary(DEFAULT_LIBRARY);

        CliOption library = new CliOption(CodegenConstants.LIBRARY, "library template (sub-template) to use");
        library.setDefault(DEFAULT_LIBRARY);
        library.setEnum(supportedLibraries);
        library.setDefault(DEFAULT_LIBRARY);
        cliOptions.add(library);
    }

    @Override
    public void processOpts() {
        setUseOas2(false);
        additionalProperties.put(CodegenConstants.USE_OAS2, false);

        if (additionalProperties.containsKey("httpMethod")) {
            String httpMethod = (String) additionalProperties.get("httpMethod");
            String httpMethodNormalCase = Character.toUpperCase(httpMethod.charAt(0)) + httpMethod.substring(1);
            additionalProperties.put("httpMethodNormalCase", httpMethodNormalCase);
        }


        // set invokerPackage as basePackage
        if (additionalProperties.containsKey(CodegenConstants.INVOKER_PACKAGE)) {
            this.setBasePackage((String) additionalProperties.get(CodegenConstants.INVOKER_PACKAGE));
            additionalProperties.put(BASE_PACKAGE, basePackage);
        }

        super.processOpts();
        handledModels.clear();
        apiTestTemplateFiles.clear();

        modelDocTemplateFiles.remove("model_doc.mustache");
        apiDocTemplateFiles.remove("api_doc.mustache");

        if (additionalProperties.containsKey(CONFIG_PACKAGE)) {
            this.setConfigPackage((String) additionalProperties.get(CONFIG_PACKAGE));
        }

        if (additionalProperties.containsKey(BASE_PACKAGE)) {
            this.setBasePackage((String) additionalProperties.get(BASE_PACKAGE));
        }

        if (additionalProperties.containsKey(USE_TAGS)) {
            this.setUseTags(Boolean.parseBoolean(additionalProperties.get(USE_TAGS).toString()));
        }

        defaultIncludes.clear();
        defaultIncludes.addAll(asList("double",
                "int",
                "Integer",
                "integer",
                "long",
                "Long",
                "short",
                "Short",
                "char",
                "Character",
                "float",
                "Float",
                "string",
                "String",
                "boolean",
                "Boolean",
                "double",
                "Double",
                "Void"));

        languageSpecificPrimitives = new HashSet<>(
                asList(
                        "String",
                        "Boolean",
                        "Double",
                        "Integer",
                        "Long",
                        "Float",
                        "Object",
                        "byte[]"));

        importMapping.clear();
        importMapping.put("BigDecimal", "java.math.BigDecimal");
        importMapping.put("UUID", "java.util.*");
        importMapping.put("File", "java.io.File");
        importMapping.put("Date", "java.util.*");
        importMapping.put("DateTime", "java.util.*");
        importMapping.put("Timestamp", "java.sql.Timestamp");
        importMapping.put("Map", "java.util.*");
        importMapping.put("HashMap", "java.util.*");
        importMapping.put("List", "java.util.*");
        importMapping.put("array", "java.util.*");
        importMapping.put("ArrayList", "java.util.*");
        importMapping.put("Set", "java.util.*");
        importMapping.put("LocalDateTime", "java.time.*");
        importMapping.put("LocalDate", "java.time.*");
        importMapping.put("LocalTime", "java.time.*");

        prepareNeptuneAnnotations();
        prepareJacksonAnnotations();

        typeMapping.clear();
        typeMapping.put("array", "List");
        typeMapping.put("Array", "List");
        typeMapping.put("map", "Map");
        typeMapping.put("List", "List");
        typeMapping.put("boolean", "Boolean");
        typeMapping.put("string", "String");
        typeMapping.put("int", "Integer");
        typeMapping.put("float", "Float");
        typeMapping.put("number", "BigDecimal");
        typeMapping.put("DateTime", "Date");
        typeMapping.put("long", "Long");
        typeMapping.put("short", "Short");
        typeMapping.put("char", "String");
        typeMapping.put("double", "Double");
        typeMapping.put("object", "Object");
        typeMapping.put("integer", "Integer");
        typeMapping.put("ByteArray", "byte[]");
        typeMapping.put("binary", "File");
        typeMapping.put("file", "File");
        typeMapping.put("UUID", "UUID");
        typeMapping.put("BigDecimal", "BigDecimal");

        setWithXml(true);
    }

    private void prepareNeptuneAnnotations() {
        importMapping.put("QueryParameter", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter");
        importMapping.put("FormParam", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormParam");
        importMapping.put("FormStyles", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles");
        importMapping.put("HeaderParameter", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.header.HeaderParameter");
        importMapping.put("PathParameter", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathParameter");
        importMapping.put("PathStyles", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.path.PathStyles");
        importMapping.put("URLEncodedParameter", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter");
        importMapping.put("BodyParamFormat", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyParamFormat");
        importMapping.put("BodyDataFormat", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat");
        importMapping.put("MultiPartBody", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.MultiPartBody");
        importMapping.put("DefineFileName", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineFileName");
        importMapping.put("ContentTransferEncoding", "ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding");
        importMapping.put("DefineContentType", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineContentType");
        importMapping.put("Body", "ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.Body");
    }

    private void prepareJacksonAnnotations() {
        importMapping.put("JacksonAnnotationsInside", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonAlias", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JacksonInject", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonAnyGetter", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JacksonAnnotation", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonAnySetter", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonBackReference", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonAutoDetect", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonClassDescription", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonCreator", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonEnumDefaultValue", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonFilter", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonFormat", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonGetter", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonIdentityInfo", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonIdentityReference", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonIgnore", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonIgnoreProperties", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonIgnoreType", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonInclude", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonManagedReference", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonMerge", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonProperty", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonPropertyDescription", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonPropertyOrder", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonRawValue", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonRootName", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonSetter", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonSubTypes", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonTypeId", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonTypeInfo", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonTypeName", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonUnwrapped", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonValue", "com.fasterxml.jackson.annotation.*");
        importMapping.put("JsonView", "com.fasterxml.jackson.annotation.*");
    }

    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
        if (!useTags) {
            String basePath = resourcePath;
            if (basePath.startsWith("/")) {
                basePath = basePath.substring(1);
            }
            int pos = basePath.indexOf("/");
            if (pos > 0) {
                basePath = basePath.substring(0, pos);
            }

            if (basePath.equals("")) {
                basePath = "default";
            } else {
                co.subresourceOperation = !co.path.isEmpty();
            }
            List<CodegenOperation> opList = operations.computeIfAbsent(basePath, k -> new ArrayList<>());
            opList.add(co);
            co.baseName = basePath;
        } else {
            super.addOperationToGroup(tag, resourcePath, operation, co, operations);
        }
    }

    @Override
    public String getDefaultTemplateDir() {
        return "neptune";
    }

    @Override
    public void preprocessOpenAPI(OpenAPI openAPI) {
        super.preprocessOpenAPI(openAPI);

        if (openAPI.getPaths() != null) {
            for (String pathname : openAPI.getPaths().keySet()) {
                PathItem pathItem = openAPI.getPaths().get(pathname);
                final List<Operation> operations = pathItem.readOperations();
                for (Operation operation : operations) {
                    ofNullable(operation.getParameters())
                            .ifPresent(parameters -> parameters.forEach(parameter -> {
                                boolean explode;
                                if (explode = ofNullable(parameter.getExplode()).orElse(false)) {
                                    parameter.addExtension(X_EXPLODE, true);
                                }

                                var style = getStyle(parameter.getStyle());
                                ofNullable(style).ifPresent(s -> parameter.addExtension(X_STYLE, s));

                                boolean allowReserved;
                                if (allowReserved = ofNullable(parameter.getAllowReserved()).orElse(false)) {
                                    parameter.addExtension(X_ALLOW_RESERVED, true);
                                }

                                if (!explode || style != null || allowReserved) {
                                    parameter.addExtension(X_FORM_PARAMETER, true);
                                }
                            }));
                }
            }
        }
    }

    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        @SuppressWarnings("unchecked") List<CodegenSecurity> authMethods = (List<CodegenSecurity>) objs.get("authMethods");
        if (authMethods != null) {
            for (CodegenSecurity authMethod : authMethods) {
                authMethod.name = camelize(sanitizeName(authMethod.name), true);
            }
        }
        return objs;
    }

    @Override
    public String toApiName(String name) {
        if (name.length() == 0) {
            return "DefaultApi";
        }
        var newName = camelize(sanitizeName(name));

        if ("api".equalsIgnoreCase(newName)) {
            return newName;
        }
        return newName + "Api";
    }

    public String toBooleanGetter(String name) {
        return getterAndSetterCapitalize(name);
    }

    @SuppressWarnings("WeakerAccess")
    public void setConfigPackage(String configPackage) {
        this.configPackage = configPackage;
    }

    @SuppressWarnings("WeakerAccess")
    public void setBasePackage(String configPackage) {
        this.basePackage = configPackage;
    }

    @SuppressWarnings("WeakerAccess")
    public void setUseTags(boolean useTags) {
        this.useTags = useTags;
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);

        if ("null".equals(property.example)) {
            property.example = null;
        }

        //Add imports for Jackson
        boolean isEnum = getBooleanValue(model, IS_ENUM_EXT_NAME);
        if (!Boolean.TRUE.equals(isEnum)) {
            model.imports.add("JsonProperty");
            boolean hasEnums = getBooleanValue(model, HAS_ENUMS_EXT_NAME);
            if (Boolean.TRUE.equals(hasEnums)) {
                model.imports.add("JsonValue");
            }
        } else { // enum class
            //Needed imports for Jackson's JsonCreator
            if (additionalProperties.containsKey("jackson")) {
                model.imports.add("JsonCreator");
            }
        }
        if (model.discriminator != null && model.discriminator.getPropertyName().equals(property.baseName)) {
            property.vendorExtensions.put("x-is-discriminator-property", true);

            //model.imports.add("JsonTypeId");
        }
    }

    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Schema> schemas, OpenAPI openAPI) {
        var op = super.fromOperation(path, httpMethod, operation, schemas, openAPI);

        if (op.contents != null) {
            op.contents.forEach(cc -> {
                var params = cc.getParameters();
                if (params == null) {
                    return;
                }

                for (var p : params) {
                    var isListContainer = p.vendorExtensions.get(IS_LIST_CONTAINER_EXT_NAME);
                    if (isListContainer != null && (Boolean) isListContainer) {
                        op.imports.add("List");
                        break;
                    }
                }
            });
        }

        if (op.headerParams.size() > 0) {
            op.imports.add("HeaderParameter");
        }

        if (op.pathParams.size() > 0) {
            op.imports.add("PathParameter");
            op.pathParams.forEach(p -> {
                ofNullable(p.vendorExtensions.get(X_STYLE)).ifPresent(o -> op.imports.add("PathStyles"));
                operation.getParameters()
                        .stream()
                        .filter(parameter -> parameter.getName().equals(p.baseName) && parameter.getClass().equals(PathParameter.class))
                        .findFirst()
                        .ifPresent(parameter -> p.required = parameter.getRequired());
            });
        }

        if (op.formParams.size() > 0) {
            op.imports.add("URLEncodedParameter");
        }

        if (op.bodyParams.size() > 0) {
            op.imports.add("Body");
        }

        improveBodyParameters(operation, op, schemas);

        if (op.queryParams.size() > 0) {
            op.imports.add("QueryParameter");
            op.queryParams.forEach(c -> {
                if (c.vendorExtensions.containsKey(X_FORM_PARAMETER)) {
                    op.imports.add("FormParam");
                }

                if (c.vendorExtensions.containsKey(X_STYLE)) {
                    op.imports.add("FormStyles");
                }
            });
        }

        if (op.responses != null) {
            var bodyList = ofNullable(op.bodyParams)
                    .map(codegenParameters -> codegenParameters
                            .stream()
                            .filter(parameter -> nonNull(parameter.baseType) && nonNull(handledModels.get(parameter.baseType)))
                            .map(parameter -> parameter.baseType)
                            .collect(toList()))
                    .orElse(new ArrayList<>());

            op.responses
                    .stream()
                    .filter(codegenResponse -> nonNull(codegenResponse.baseType)
                            && nonNull(handledModels.get(codegenResponse.baseType))
                            && !bodyList.contains(codegenResponse.baseType))
                    .map(codegenResponse -> codegenResponse.baseType)
                    .forEach(s -> op.imports.remove(s));
        }

        return op;
    }

    public Map<String, Object> postProcessAllModels(Map<String, Object> processedModels) {
        var modelsToIterate = new HashMap<>(processedModels);

        modelsToIterate.keySet().forEach(s -> {
            if (handledModels.get(s) == null) {
                processedModels.remove(s);
            }
        });

        return super.postProcessAllModels(clearModels(processedModels));
    }

    @Override
    public CodegenModel fromModel(String name, Schema schema, Map<String, Schema> allSchemas) {
        var codegenModel = super.fromModel(name, schema, allSchemas);
        if (codegenModel.vars.size() > 0) {
            handledModels.put(name, codegenModel);
        } else {
            if (codegenModel.parent != null) {
                return handledModels.get(codegenModel.parent);
            } else {
                handledModels.put(name, codegenModel);
            }
        }
        var imports = new HashSet<>(codegenModel.imports);
        imports.forEach(s -> {
            if (importMapping.get(s) == null) {
                codegenModel.imports.remove(s);
            }
        });

        improveXml(codegenModel, schema);
        return codegenModel;
    }

    @Override
    public String apiFileFolder() {
        var outputRoot = outputFolder;
        if (!outputRoot.endsWith(sourceFolder) && !outputRoot.endsWith(testFolder)) {
            outputRoot = outputFolder + "/" + sourceFolder;
        }
        return outputRoot + "/" + apiPackage().replace('.', '/');
    }

    @Override
    public String modelFileFolder() {
        var outputRoot = outputFolder;
        if (!outputRoot.endsWith(sourceFolder) && !outputRoot.endsWith(testFolder)) {
            outputRoot = outputFolder + "/" + sourceFolder;
        }
        return outputRoot + "/" + modelPackage().replace('.', '/');
    }
}