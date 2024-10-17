package com.github.bobcat33.apc.programs.colourpicker;

import com.github.bobcat33.apc.apcinterface.APCController;

public class ColourPages {
    public static void go(APCController ctrl, int page) {
        if (page > 1) page = 1;
        if (page < 0) page = 0;

        for (int i = 0; i < 64; i++) {
            ctrl.outputToButton(i, i+(64*page));
        }
    }
}
