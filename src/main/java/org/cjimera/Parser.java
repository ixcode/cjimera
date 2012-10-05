package org.cjimera;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;
import ixcode.platform.reflect.ObjectBuilder;
import org.cjimera.wwwformurlencoded.WwwFormUrlEncodedParser;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public class Parser {

    private InputFormat parser;
    private InputStream in;

    public static Parser parse() {
        return new Parser();
    }


    private Parser() {
    }

    public Parser inputStream(InputStream in) {
        this.in = in;
        return this;
    }

    public SourceType from() {
        return new SourceType(this);
    }

    public <T> T to(Class<T> type) {
        Map<String, Object> valueMap = parser.toMap(in);

        final ObjectBuilder objectBuilder = new ObjectBuilder(type);

        withEntriesIn(valueMap).apply(new Action<Map.Entry<String, Object>>() {
            @Override public void to(Map.Entry<String, Object> item, Collection<Map.Entry<String, Object>> tail) {
                populateValue(item, objectBuilder);
            }
        });

        return objectBuilder.build();
    }

    private void populateValue(Map.Entry<String, Object> item, ObjectBuilder objectBuilder) {
        if (item.getValue() instanceof String) {
            objectBuilder.setProperty(item.getKey()).fromString(item.getValue().toString());
            return;
        }

        objectBuilder.setProperty(item.getKey()).asObject(item.getValue());
    }

    private FArrayList<Map.Entry<String, Object>> withEntriesIn(Map<String, Object> valueMap) {
        return new FArrayList<Map.Entry<String, Object>>(valueMap.entrySet());
    }

    private void useParser(InputFormat parser) {
        this.parser = parser;
    }

    public static class SourceType {

        private Parser parent;

        public SourceType(Parser parent) {
            this.parent = parent;
        }

        public Parser applicationFormUrlEncoded() {
            parent.useParser(new WwwFormUrlEncodedParser());
            return parent;
        }

    }


}