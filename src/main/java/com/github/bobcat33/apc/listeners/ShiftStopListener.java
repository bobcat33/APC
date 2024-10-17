package com.github.bobcat33.apc.listeners;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.layouts.AllOff;

public class ShiftStopListener extends APCButtonEventListener {
    private final boolean clearLEDs;

    public ShiftStopListener() {
        this(false);
    }

    public ShiftStopListener(boolean clearLEDs) {
        this.clearLEDs = clearLEDs;
    }

    @Override
    public void onButtonDown(APCController controller, Button button) {
        if (button.getButtonType().equals(ButtonType.SHIFT)) {
            System.out.println("Shift pressed, shutting down!");
            if (clearLEDs) new AllOff().go(controller);
            controller.close();
        }
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {}

    @Override
    public void onClose() {}
}
