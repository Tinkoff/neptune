package ru.tinkoff.qa.neptune.http.api.test;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FunctionToGetXMLTagList implements Function<String, List<Node>> {

    private final String tagName;

    public static Function<String, List<Node>> toNodeList(String tagName) {
        return new FunctionToGetXMLTagList(tagName);
    }

    private FunctionToGetXMLTagList(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public List<Node> apply(String s) {
        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            var documentBuilder = documentBuilderFactory.newDocumentBuilder();
            var inputSource = new InputSource(new StringReader(s));
            var nodeList = documentBuilder.parse(inputSource).getElementsByTagName(tagName);
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                nodes.add(nodeList.item(i));
            }
            return nodes;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
