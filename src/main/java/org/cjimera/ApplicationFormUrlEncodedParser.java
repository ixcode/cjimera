package org.cjimera;

import ixcode.platform.exception.RuntimeExceptionX;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import static ixcode.platform.io.IoStreamHandling.readFully;
import static java.lang.String.format;

public class ApplicationFormUrlEncodedParser implements StringParser {

    @Override public Map<String, Object> toMap(InputStream in) {
        String rawForm = readFully(in, "UTF-8");

        String[] parameters = rawForm.split("&");

        Map<String, Object> values = new LinkedHashMap<String, Object>();

        for (String parameter : parameters) {
            String[] tuple = parameter.split("=");
            values.put(tuple[0], unencode(tuple[1]));
        }

        return values;
    }

    private String unencode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeExceptionX(format("Could not decode [%s]", s), e);
        }
    }
}