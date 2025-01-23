package com.github.bobcat33.apc.listeners;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.layouts.AllOff;

import java.sql.Time;

public class ShiftHoldStopListener extends APCButtonEventListener {
    private final int secondsUntilArmed;
    private double timeDown = 0;

    public ShiftHoldStopListener() {
        this(10);
    }

    public ShiftHoldStopListener(int secondsUntilArmed) {
        this.secondsUntilArmed = secondsUntilArmed;
    }

    @Override
    public void onButtonDown(APCController controller, Button button) {
        if (!button.getButtonType().equals(ButtonType.SHIFT)) return;

        timeDown = System.currentTimeMillis();
        System.out.println("Shift down, awaiting release.");
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {
        if (timeDown == 0 || !button.getButtonType().equals(ButtonType.SHIFT)) return;


        if (getSecondsDifference(timeDown, System.currentTimeMillis()) < secondsUntilArmed) {
            timeDown = 0;
            System.out.println("Shift up while unarmed, aborting shut down.");
            return;
        }

        System.out.println("Shift up while armed, shutting down.");

        new AllOff().go(controller);
        controller.close();
    }

    @Override
    public void onClose() {}

    private int getSecondsDifference(double first, double second) {
        return (int) ((second - first) / 1000d);
    }
}
