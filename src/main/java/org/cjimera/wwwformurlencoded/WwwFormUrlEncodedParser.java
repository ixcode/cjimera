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

    private final OgnlKeyParser ognlKeyParser = new OgnlKeyParser();

    @Override public Map<String, Object> toMap(InputStream in) {
        String rawForm = readFully(in, "UTF-8");

        String[] parameters = rawForm.split("&");

        Map<String, Object> values = new LinkedHashMap<String, Object>();

        for (String parameter : parameters) {
            String[] tuple = parameter.split("=");

            String key = tuple[0];
            String urlEncodedValue = tuple[1];

            processOgnlName(values, key, unencode(urlEncodedValue));
        }

        return values;
    }

    private void processOgnlName(Map<String, Object> values, String key, String value) {
        String[] path = key.split("\\.");

        recursePath(path, values, value);

    }

    private void recursePath(String[] path, Map<String, Object> values, String value) {
        String currentKey = path[0];

        if (path.length == 1) {
            addValueToMap(values, currentKey, value);
            return;
        }

        recursePath(path, 1, values, value);
    }

    private void recursePath(String[] path, int index, Map<String, Object> values, String value) {
        String currentKey = path[index];

        OgnlKeyParser.OgnlKey ognlKey = ognlKeyParser.parse(path[index - 1]);

        String parentKeyName = ognlKey.name;

        Map<String, Object> childValues = null;

        if (ognlKey.isArray) {

            if (!values.containsKey(parentKeyName)) {
                values.put(parentKeyName, new ArrayList<Map<String, Object>>());
            }

            List<Map<String, Object>> childList = (List<Map<String, Object>>) values.get(parentKeyName);

            if (childList.size() < ognlKey.arrayIndex + 1) {
                for (int i = 0; i < (ognlKey.arrayIndex + 1) - childList.size(); ++i) {
                    childList.add(new LinkedHashMap<String, Object>());
                }
            }

            childValues = childList.get(ognlKey.arrayIndex);

        } else {

            if (!values.containsKey(parentKeyName)) {
                values.put(parentKeyName, new LinkedHashMap<String, Object>());
            }

            childValues = (Map<String, Object>) values.get(parentKeyName);
        }


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