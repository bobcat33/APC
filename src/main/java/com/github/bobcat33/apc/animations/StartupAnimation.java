package com.github.bobcat33.apc.animations;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.Animation;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

public class StartupAnimation extends Animation {

    public StartupAnimation(APCController controller) {
        super(controller);
    }

    @Override
    protected void start() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                controller.output(Button.Out.createPadData(0x96, (x+(y*8)), 5));
                sleep(50);
            }
        }
        sleep(50);

        for (int i = 7; i >= 0; i--) {
            controller.output(Button.Out.createUIButtonData(ButtonType.TRACK, i, 1));
            controller.output(Button.Out.createUIButtonData(ButtonType.SCENE_LAUNCH, i, 1));
            sleep(100);
        }

        for (int i = 7; i >= 0; i--) {
            controller.output(Button.Out.createUIButtonData(ButtonType.TRACK, i, 0));
            controller.output(Button.Out.createUIButtonData(ButtonType.SCENE_LAUNCH, i, 0));
            sleep(30);
        }
    }
}
