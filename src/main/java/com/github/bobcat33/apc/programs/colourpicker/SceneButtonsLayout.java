package com.github.bobcat33.apc.programs.colourpicker;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

public class SceneButtonsLayout {
    public static void go(APCController controller, Button sceneButton) {
        if (!sceneButton.getButtonType().equals(ButtonType.SCENE_LAUNCH)) throw new RuntimeException("Cannot build SceneButtonsLayout, invalid scene Button"); // TODO create exception

        for (int i = 0; i < 8; i++) {
            if (i == 3) controller.outputToButton(ButtonType.SCENE_LAUNCH, i, UIButtonBehaviour.OFF);
            else if (i == sceneButton.getLocalIdentifier()) controller.outputToButton(ButtonType.SCENE_LAUNCH, i, UIButtonBehaviour.BLINK);
            else controller.outputToButton(ButtonType.SCENE_LAUNCH, i, UIButtonBehaviour.ON);
        }
    }
}
