package com.github.bobcat33.apc.listeners;

import com.github.bobcat33.apc.animations.SpreadFromPointAnimation;
import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.Animation;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.programs.colourpicker.InputListener;
import com.github.bobcat33.apc.programs.colourpicker.StartUpLayout;

import java.util.function.Consumer;

public class TriggerSpreadFromPoint extends APCButtonEventListener {
    private volatile boolean active = true;
    private volatile Consumer<APCController> onFinish;

    @Override
    public void onButtonDown(APCController controller, Button button) {
        if (!active || !button.getButtonType().equals(ButtonType.PAD)) return;

        Animation spread = new SpreadFromPointAnimation(controller, button);
        if (onFinish != null) spread.onFinish(onFinish);
        spread.run();
        onClose();
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {}

    @Override
    public void onClose() {
        active = false;
    }

    public void onFinish(Consumer<APCController> consumer) {
        onFinish = consumer;
    }
}
