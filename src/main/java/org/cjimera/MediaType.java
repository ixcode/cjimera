package org.cjimera;

public enum MediaType {

    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded");

    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

}