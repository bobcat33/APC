package com.github.bobcat33.apc.programs.setup;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonFaderEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Fader;
import com.github.bobcat33.apc.layouts.AllOff;
import com.github.bobcat33.apc.layouts.AllOn;

public class SetupInputListener extends APCButtonFaderEventListener {
    private boolean buttonsActive = true, pad, scene, track, fadersActive;
    private int fadersTested = 0b000000000;

    @Override
    public void onButtonDown(APCController controller, Button button) {
        if (!buttonsActive) return;
        switch (button.getButtonType()) {
            case PAD -> {
                if (!pad) {
                    pad = true;
                    for (int i = 0; i < 64; i++) {
                        controller.outputToButton(i, 0);
                    }
                }
            }
            case SCENE_LAUNCH -> {
                if (!scene) {
                    scene = true;
                    for (int i = 0; i < 8; i++) {
                        controller.outputToButton(ButtonType.SCENE_LAUNCH, i, 0);
                    }
                }
            }
            case TRACK -> {
                if (!track) {
                    track = true;
                    for (int i = 0; i < 8; i++) {
                        controller.outputToButton(ButtonType.TRACK, i, 0);
                    }
                }
            }
        }

        if (!pad || !scene || !track) return;
        buttonsActive = false;
        fadersActive = true;
        System.out.println("All button sections active.\n------------------------\n-> Move each fader and the LED above should turn on.");
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {}

    @Override
    public void onFaderMove(APCController controller, Fader fader) {
        if (buttonsActive) return;
        if (fader.getFaderNum() < 9) {
            controller.outputToButton(fader.getFaderNum()-1, 5);
        }
        else controller.outputToButton(ButtonType.SCENE_LAUNCH, 7, UIButtonBehaviour.ON);

        int binary = (int) Math.pow(2, (fader.getFaderNum() - 1));

        fadersTested = fadersTested | binary;
        if (fadersTested != 0b111111111) return;

        fadersActive = false;
        System.out.println("Data received from all faders.\n------------------------\nSetup complete! Closing APCConnector.");

        new AllOn(21).go(controller);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            controller.close();
            throw new RuntimeException(e);
        }
        new AllOff().go(controller);
        controller.close();
    }

    @Override
    public void onClose() {}
}
