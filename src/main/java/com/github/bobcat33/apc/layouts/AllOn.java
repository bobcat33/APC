package com.github.bobcat33.apc.layouts;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.layout.Layout;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

public class AllOn implements Layout {
    private final int colour;

    public AllOn() {
        this(5);
    }

    public AllOn(int colour) {
        this.colour = colour;
    }

    public void go(APCController controller) {
        for (int i = 0; i < 64; i++) {
            controller.outputToButton(i, colour);
        }
        for (int i = 0; i < 8; i++) {
            controller.outputToButton(ButtonType.TRACK, i, UIButtonBehaviour.ON);
            controller.outputToButton(ButtonType.SCENE_LAUNCH, i, UIButtonBehaviour.ON);
        }
        controller.outputToButton(ButtonType.SHIFT, 0, UIButtonBehaviour.ON);
    }

}
