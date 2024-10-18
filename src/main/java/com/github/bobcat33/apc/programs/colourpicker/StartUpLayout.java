package com.github.bobcat33.apc.programs.colourpicker;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.graphics.Layout;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.programs.colourpicker.scene.ColourPages;

public class StartUpLayout implements Layout {
    @Override
    public void go(APCController controller) {
        ColourPages.go(controller, 0);
        controller.outputToButton(ButtonType.SCENE_LAUNCH, 0, UIButtonBehaviour.BLINK); // Page 1 colours
        controller.outputToButton(ButtonType.SCENE_LAUNCH, 1, UIButtonBehaviour.ON);    // Page 2 colours
        controller.outputToButton(ButtonType.SCENE_LAUNCH, 2, UIButtonBehaviour.ON);    // Fader Control
        controller.outputToButton(ButtonType.SCENE_LAUNCH, 3, UIButtonBehaviour.OFF);   // NONE
        controller.outputToButton(ButtonType.SCENE_LAUNCH, 4, UIButtonBehaviour.ON);    // Page 1 Stored
        controller.outputToButton(ButtonType.SCENE_LAUNCH, 5, UIButtonBehaviour.ON);    // Page 2 Stored
        controller.outputToButton(ButtonType.SCENE_LAUNCH, 6, UIButtonBehaviour.ON);    // Page 3 Stored
        controller.outputToButton(ButtonType.SCENE_LAUNCH, 7, UIButtonBehaviour.ON);    // Page 4 Stored
    }
}
