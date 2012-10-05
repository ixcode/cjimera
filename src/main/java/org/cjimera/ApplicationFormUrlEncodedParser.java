package org.cjimera;

import ixcode.platform.io.IoStreamHandling;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static ixcode.platform.io.IoStreamHandling.readFully;

public class ApplicationFormUrlEncodedParser implements StringParser {

    @Override public Map<String, Object> toMap(InputStream in) {
        String rawForm = readFully(in, "UTF-8");

        String[] parameters = rawForm.split("&");

        Map<String, Object> values = new LinkedHashMap<String, Object>();

        for (String parameter : parameters) {
            String[] tuple = parameter.split("=");
            values.put(tuple[0], tuple[1]);
        }

        return values;
    }
}