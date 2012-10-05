package org.cjimera;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;
import ixcode.platform.reflect.ObjectBuilder;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public class ParseRepresentation {

    private StringParser parser;
    private InputStream in;

    public static ParseRepresentation parse() {
        return new ParseRepresentation();
    }


    private ParseRepresentation() {
    }

    public ParseRepresentation inputStream(InputStream in) {
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
                objectBuilder.setProperty(item.getKey()).fromString(item.getValue().toString());
            }
        });

        return objectBuilder.build();
    }

    private FArrayList<Map.Entry<String, Object>> withEntriesIn(Map<String, Object> valueMap) {
        return new FArrayList<Map.Entry<String, Object>>(valueMap.entrySet());
    }

    private void useParser(StringParser parser) {
        this.parser = parser;
    }

    public static class SourceType {

        private ParseRepresentation parent;

        public SourceType(ParseRepresentation parent) {
            this.parent = parent;
        }

        public ParseRepresentation applicationFormUrlEncoded() {
            parent.useParser(new ApplicationFormUrlEncodedParser());
            return parent;
        }

    }


}