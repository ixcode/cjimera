package org.cjimera;

import ixcode.platform.collection.Action;
import ixcode.platform.collection.FArrayList;
import ixcode.platform.reflect.ObjectBuilder;
import org.cjimera.wwwformurlencoded.WwwFormUrlEncodedParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

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

        return populateObject(type, valueMap);
    }

    private <T> T populateObject(Class<T> type, Map<String, Object> valueMap) {
        final ObjectBuilder objectBuilder = new ObjectBuilder(type);

        withEntriesIn(valueMap).apply(new Action<Map.Entry<String, Object>>() {
            @Override public void to(Map.Entry<String, Object> item, Collection<Map.Entry<String, Object>> tail) {
                populateValue(item, objectBuilder);
            }
        });

        return objectBuilder.build();
    }

    /**
     * @todo Need to introduce a method for setting a collection method - needs to be on ObjectBuilder? we are using too
     * many of its ifc here
     */
    private void populateValue(Map.Entry<String, Object> item, ObjectBuilder objectBuilder) {
        String propertyName = item.getKey();
        Object propertyValue = item.getValue();


        if (objectBuilder.hasProperty(propertyName)
                && objectBuilder.isCollection(propertyName)
                && !objectBuilder.isMap(propertyName)
                && !(propertyValue instanceof Collection)) {

            if (!List.class.isAssignableFrom(objectBuilder.getPropertyType(propertyName))) {
                throw new RuntimeException("Only support List collections right now.");
            }

            objectBuilder.setProperty(propertyName).asObject(asList(propertyValue));
            return;
        }

        if (propertyValue instanceof String) {
            objectBuilder.setProperty(propertyName).fromString(propertyValue.toString());
            return;
        }

        if ((propertyValue instanceof Map)
                && objectBuilder.hasProperty(propertyName)
                && !objectBuilder.isMap(propertyName)) {

            Object child = populateObject(objectBuilder.getPropertyType(propertyName),
                                          (Map<String, Object>) propertyValue);
            objectBuilder.setProperty(propertyName).asObject(child);
            return;
        }

        if ((propertyValue instanceof List)
                && objectBuilder.hasProperty(propertyName)
                && objectBuilder.isList(propertyName)
                && ((List) propertyValue).size() > 0) {

            List processedList = new ArrayList();

            for (Object listChildValues : ((List) propertyValue)) {
                if (listChildValues instanceof Map) {

                    Map<String, Object> listChildValueMap = (Map<String, Object>) listChildValues;

                    Object child = (listChildValueMap.isEmpty())
                            ? null
                            : populateObject(objectBuilder.getTypeOfCollectionCalled(propertyName), listChildValueMap);

                    processedList.add(child);
                } else {
                    processedList.add(listChildValues);
                }
            }

            objectBuilder.setProperty(propertyName).asObject(processedList);
            return;
        }

        objectBuilder.setProperty(propertyName).asObject(propertyValue);
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

        public Parser wwwFormUrlEncoded() {
            parent.useParser(new WwwFormUrlEncodedParser());
            return parent;
        }

    }


}