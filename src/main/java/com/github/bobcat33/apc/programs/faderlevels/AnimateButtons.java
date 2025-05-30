package com.github.bobcat33.apc.programs.faderlevels;

import com.github.bobcat33.apc.apcinterface.APCController;

public class AnimateButtons {

    public static void animateColumn(APCController ctrl, int column, int value) {
        if (column > 7) column = 7; // TODO Make master (8) increase and decrease all columns
        if (column < 0) column = 0;
        if (value > 127) value = 127;
        int distance = value / 16;
        System.out.println("DISTANCE: " + distance);

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
