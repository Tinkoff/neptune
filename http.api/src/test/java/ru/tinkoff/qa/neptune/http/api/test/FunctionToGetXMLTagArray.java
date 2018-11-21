package ru.tinkoff.qa.neptune.http.api.test;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.function.Function;

public class FunctionToGetXMLTagArray implements Function<String, Node[]> {

    private final String tagName;

    public static Function<String, Node[]> toNodeArray(String tagName) {
        return new FunctionToGetXMLTagArray(tagName);
    }

    private FunctionToGetXMLTagArray(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public Node[] apply(String s) {
        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            var documentBuilder = documentBuilderFactory.newDocumentBuilder();
            var inputSource = new InputSource(new StringReader(s));
            var nodeList = documentBuilder.parse(inputSource).getElementsByTagName(tagName);
            Node[] nodes = new Node[nodeList.getLength()];
            for (int i = 0; i < nodeList.getLength(); i++) {
                nodes[i] = nodeList.item(i);
            }
            return nodes;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
