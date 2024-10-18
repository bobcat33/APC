package com.github.bobcat33.apc.layouts;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.Layout;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

public class AllOff implements Layout {

    public void go(APCController controller) {
        for (int i = 0; i < 64; i++) {
            controller.outputToButton(i, 0);
        }
        for (int i = 0; i < 8; i++) {
            controller.outputToButton(ButtonType.TRACK, i, 0);
            controller.outputToButton(ButtonType.SCENE_LAUNCH, i, 0);
        }
        controller.outputToButton(ButtonType.SHIFT, 0, 0);
    }

}
