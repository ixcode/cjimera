package org.cjimera;

import java.io.InputStream;

public class ParseRepresentation {

    public static ParseRepresentationBuilder parse() {
        return new ParseRepresentationBuilder();
    }



    public static class ParseRepresentationBuilder {

        public ParseRepresentationBuilder() {

        }

        public ParseRepresentationBuilder inputStream(InputStream in) {
            return this;
        }

        public ParseRepresentationBuilder from(MediaType mediaType) {
            return this;
        }

        public <T> T to(Class<T> type) {
            return null;
        }

    }

}