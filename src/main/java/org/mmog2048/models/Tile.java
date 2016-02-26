package org.mmog2048.models;

import java.io.Serializable;

public class Tile implements Serializable {
    public int value;

    public Tile() {
        this(0);
    }

    public Tile(int value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
