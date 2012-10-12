package org.cjimera.wwwformurlencoded;

public class OgnlKeyParser {


    public OgnlKey parse(String key) {
        return new OgnlKey(key, false, -1);
    }

    public static class OgnlKey {

        public final String name;
        public final boolean isArray;
        public final int arrayIndex;

        public OgnlKey(String name, boolean isArray, int arrayIndex) {
            this.isArray = isArray;
            this.name = name;
            this.arrayIndex = arrayIndex;
        }
    }


}