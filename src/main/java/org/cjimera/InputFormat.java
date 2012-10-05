package org.cjimera;

import java.io.InputStream;
import java.util.Map;

public interface InputFormat {

    Map<String, Object> toMap(InputStream in);
}