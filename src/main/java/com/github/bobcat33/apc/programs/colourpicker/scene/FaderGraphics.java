package com.github.bobcat33.apc.programs.colourpicker.scene;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.APCColour;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Fader;

import java.awt.*;

public class FaderGraphics {
    private static volatile boolean trackBlinking = false;
    private static volatile int currentColour = 0;

    public static int getCurrentColour() {
        return currentColour;
    }

    public static void buildBase(APCController ctrl, int red, int green, int blue) {
        int colour = APCColour.getClosestColour(new Color(red, green, blue));
        currentColour = colour;
        updateColumns(ctrl, red, green, blue);
        trackBlinking = false;

        for (int i = 0; i < 8; i++) {
            ctrl.outputToButton(((8*i)+3), colour);
            ctrl.outputToButton(((8*i)+4), colour);
            ctrl.outputToButton(((8*i)+5), 0);
            ctrl.outputToButton(((8*i)+6), 0);
            ctrl.outputToButton(((8*i)+7), 0);
        }

        ctrl.outputToButton(ButtonType.TRACK, 0, UIButtonBehaviour.ON);
        ctrl.outputToButton(ButtonType.TRACK, 1, UIButtonBehaviour.ON);
        ctrl.outputToButton(ButtonType.TRACK, 2, UIButtonBehaviour.ON);
        ctrl.outputToButton(ButtonType.TRACK, 3, UIButtonBehaviour.OFF);
        ctrl.outputToButton(ButtonType.TRACK, 4, UIButtonBehaviour.OFF);
        ctrl.outputToButton(ButtonType.TRACK, 5, UIButtonBehaviour.OFF);
        ctrl.outputToButton(ButtonType.TRACK, 6, UIButtonBehaviour.OFF);
        ctrl.outputToButton(ButtonType.TRACK, 7, UIButtonBehaviour.ON);
    }

    public static void updateSolidColour(APCController ctrl, int red, int green, int blue) {
        int colour = APCColour.getClosestColour(new Color(red, green, blue));
        currentColour = colour;

        for (int i = 0; i < 8; i++) {
            ctrl.outputToButton(((8*i)+3), colour);
            ctrl.outputToButton(((8*i)+4), colour);
        }

        updateTrackBlink(ctrl, false);
    }

    public static void updateTrackBlink(APCController ctrl, boolean blink) {
        if (blink == trackBlinking) return;
        trackBlinking = blink;
        UIButtonBehaviour behaviour = (blink) ? UIButtonBehaviour.BLINK : UIButtonBehaviour.ON;

        ctrl.outputToButton(ButtonType.TRACK, 0, behaviour);
        ctrl.outputToButton(ButtonType.TRACK, 1, behaviour);
        ctrl.outputToButton(ButtonType.TRACK, 2, behaviour);
    }

    public static void updateColumns(APCController ctrl, int red, int green, int blue) {
        updateColumn(ctrl, 0, red);
        updateColumn(ctrl, 1, green);
        updateColumn(ctrl, 2, blue);
    }

    /**
     *
     * @param ctrl
     * @param value Value from 0 to 255 representing the amount of a colour
     */
    public static void updateColumn(APCController ctrl, int column, int value) {
        int distance = value / 32;

        int colour = switch (column) {
            case 0 -> 5; // RED
            case 1 -> 21; // GREEN
            case 2 -> 45; // BLUE
            default -> 0; // Edge case
        };

        for (int i = 0; i <= 7; i++) {
            if (value == 0 || i > distance) colour = 0; // OFF
            ctrl.outputToButton((i*8+column), colour);
        }
    }

    public static void updateColumn(APCController ctrl, Fader fader) {
        if (fader.getFaderNum() < 1 || fader.getFaderNum() > 3) return;
        updateColumn(ctrl, fader.getFaderNum()-1, fader.getStandardLevel());
    }

}
