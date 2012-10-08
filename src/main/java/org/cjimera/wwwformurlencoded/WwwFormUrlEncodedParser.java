package org.cjimera.wwwformurlencoded;

import ixcode.platform.exception.RuntimeExceptionX;
import org.cjimera.InputFormat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ixcode.platform.io.IoStreamHandling.readFully;
import static java.lang.String.format;

/**
 * See http://commons.apache.org/ognl/language-guide.html for more information about the syntax for declaring the object
 * graph mapping.
 */
public class WwwFormUrlEncodedParser implements InputFormat {

    @Override public Map<String, Object> toMap(InputStream in) {
        String rawForm = readFully(in, "UTF-8");

        String[] parameters = rawForm.split("&");

        Map<String, Object> values = new LinkedHashMap<String, Object>();

        for (String parameter : parameters) {
            String[] tuple = parameter.split("=");

            String key = tuple[0];
            String urlEncodedValue = tuple[1];

            if (key.contains(".")) {
                processOgnlName(values, key, unencode(urlEncodedValue));
            } else {
                addValueToMap(values, key, unencode(urlEncodedValue));
            }
        }

        return values;
    }

    private static void processOgnlName(Map<String, Object> values, String key, String value) {
        String[] path = key.split("\\.");

        recursePath(path, 1, values, value);

    }

    private static void recursePath(String[] path, int index, Map<String, Object> values, String value) {
        String parentKey = path[index-1];
        String currentKey = path[index];

        if (!values.containsKey(parentKey)) {
            values.put(parentKey, new LinkedHashMap<String, Object>());
        }

        Map<String, Object> childValues = (Map<String, Object>) values.get(parentKey);

        if (index == path.length - 1) {
            addValueToMap(childValues, currentKey, value);
            return;
        }

        recursePath(path, index++, childValues, value);
    }

    private static void addValueToMap(Map<String, Object> values, String key, String value) {
        if (values.containsKey(key)) {
            if (values.get(key) instanceof String) {
                List<String> list = new ArrayList<String>();
                list.add((String) values.get(key));
                values.put(key, list);
            }

            ((List<String>) values.get(key)).add(value);
            return;
        }
        values.put(key, value);
    }

    private String unencode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeExceptionX(format("Could not decode [%s]", s), e);
        }
    }
}