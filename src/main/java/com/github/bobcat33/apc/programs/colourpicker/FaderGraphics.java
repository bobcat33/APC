package com.github.bobcat33.apc.programs.colourpicker;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Fader;

public class FaderGraphics {

    public static void updateColumn(APCController ctrl, Fader fader) {
        if (fader.getFaderNum() < 1 || fader.getFaderNum() > 3) return;
        int column = fader.getFaderNum() - 1;
        int value = fader.getData();
        int distance = value / 16;

        for (int i = 0; i <= 7; i++) {
            int colour = 0;
            if (i <= distance && value != 0) {
                colour = switch (i) {
                    case 0, 1, 2, 3 -> 21; // Green
                    case 4 -> 12;
                    case 5, 6 -> 9; // Orange
                    case 7 -> 5; // Red
                    default -> 0;
                };
            }

            ctrl.outputToButton((i*8+column), colour);
        }

    }

}
