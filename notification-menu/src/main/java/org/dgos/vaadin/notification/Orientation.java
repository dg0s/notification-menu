package org.dgos.vaadin.notification;

public enum Orientation {
    LEFT("right"),
    RIGHT("left");

    private final String key;

    Orientation(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
