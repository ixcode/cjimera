package org.cjimera;

import java.io.InputStream;
import java.util.Map;

public interface StringParser {

    Map<String, Object> toMap(InputStream in);
}