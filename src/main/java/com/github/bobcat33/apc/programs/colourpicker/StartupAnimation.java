package com.github.bobcat33.apc.programs.colourpicker;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.listeners.TriggerSpreadFromPoint;

import java.util.function.Consumer;

public class StartupAnimation extends com.github.bobcat33.apc.animations.StartupAnimation {
    public StartupAnimation(APCController controller, Consumer<APCController> onTriggerFinish) {
        super(controller);
        TriggerSpreadFromPoint listener = new TriggerSpreadFromPoint();
        listener.onFinish(onTriggerFinish);
        onFinish(ctrl -> ctrl.addListener(listener));
    }
}
