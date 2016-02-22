package org.mmog2048.models;

import java.io.Serializable;

public class Tile implements Serializable {
    private int value;

    public Tile() {
        this(0);
    }

    public Tile(int value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public String getForeground() {
        return value < 16 ? "776e65" : "f9f6f2";
    }

    public String getBackground() {
        switch (value) {
            case 2:    return "eee4da";
            case 4:    return "ede0c8";
            case 8:    return "f2b179";
            case 16:   return "f59563";
            case 32:   return "f67c5f";
            case 64:   return "f65e3b";
            case 128:  return "edcf72";
            case 256:  return "edcc61";
            case 512:  return "edc850";
            case 1024: return "edc53f";
            case 2048: return "edc22e";
        }
        return "cdc1b4";
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
