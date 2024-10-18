package com.github.bobcat33.apc.programs.colourpicker.scene;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

public class ColourPages {
    public static void go(APCController ctrl, int page) {
        if (page > 1) page = 1;
        if (page < 0) page = 0;

        for (int i = 0; i < 64; i++) {
            ctrl.outputToButton(i, i+(64*page));
        }

        for (int i = 0; i < 8; i++) {
            ctrl.outputToButton(ButtonType.TRACK, i, UIButtonBehaviour.OFF);
        }

//        ctrl.outputToButton(ButtonType.TRACK, 7, UIButtonBehaviour.ON); // Select button (view value of any colour)
    }
}
