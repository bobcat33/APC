package com.github.bobcat33.apc.apcinterface.listener;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

public class ShiftStopListener extends APCButtonEventListener {
    @Override
    public void onButtonDown(APCController controller, Button button) {
        if (button.getButtonType().equals(ButtonType.SHIFT)) {
            System.out.println("Shift pressed, shutting down!");
            controller.close();
        }
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {}

    @Override
    public void onClose() {}
}
