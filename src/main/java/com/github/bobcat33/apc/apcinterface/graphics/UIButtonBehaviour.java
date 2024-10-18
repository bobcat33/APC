package com.github.bobcat33.apc.apcinterface.graphics;

public enum UIButtonBehaviour {
    OFF,
    ON,
    BLINK;

    public static UIButtonBehaviour from(int value) {
        return switch (value) {
            case 0 -> OFF;
            case 2 -> BLINK;
            default -> ON;
        };
    }
}
