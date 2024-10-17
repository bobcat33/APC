package com.github.bobcat33.apc.programs.colourpicker;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonFaderEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Fader;

import java.awt.*;
import java.util.HashMap;

public class InputListener extends APCButtonFaderEventListener {
    private volatile int selectedColour = 0, faderRed = 0, faderGreen = 0, faderBlue = 0;
//    private ButtonMode currentMode = ButtonMode.VIEW_STORED;

    private volatile Scene currentScene = Scene.COLOURS_1;
//    private HashMap<Integer, Integer> stored = new HashMap();
    private enum Scene {
        COLOURS_1,
        COLOURS_2,
        FADERS,
        PAGE_1,
        PAGE_2,
        PAGE_3,
        PAGE_4
    }
    private enum ButtonMode {
        LOAD_FROM_FADERS,
        LOAD_FROM_CYCLE,
        VIEW_STORED
    }

    @Override
    public void onButtonDown(APCController controller, Button button) {
        /*if (button.getButtonType().equals(ButtonType.PAD)) {
            switch (button.getLocalIdentifier()) {
                case 63,62,55,54,47,46,39,38 -> System.out.println("Cycle colour is currently " + cycleColour);
                case 31,30,23,22,15,14,7,6 -> System.out.println("Fader colour is currently " + faderColour);
                default -> {
                    switch (currentMode) {
                        case VIEW_STORED -> System.out.println("Currently stored at position " + button.getLocalIdentifier() + ": " + stored.get(button.getLocalIdentifier()));
                        case LOAD_FROM_CYCLE -> storeButton(controller, button, cycleColour);
                        case LOAD_FROM_FADERS -> storeButton(controller, button, faderColour);
                    }
                }
            }
        }*/
//        else if (button.getButtonType())

        switch (button.getButtonType()) {
            case SCENE_LAUNCH -> changeScene(controller, button);
            case PAD -> {
                if (currentScene.equals(Scene.COLOURS_1) || currentScene.equals(Scene.COLOURS_2)) selectColour(button);
            }
            case TRACK -> pressTrackButton(controller, button);
        }

    }

    private void pressTrackButton(APCController ctrl, Button trackButton) {
        switch (trackButton.getLocalIdentifier()) {
            case 0,1,2 -> {
                if (currentScene.equals(Scene.FADERS)) {
                    FaderGraphics.updateSolidColour(ctrl, faderRed, faderGreen, faderBlue);
                }
            }
            case 7 -> {
                // TODO SELECT MODE BUTTON
            }
            default -> {}
        }
    }

    private void changeScene(APCController ctrl, Button sceneButton) {
        if (sceneButton.getLocalIdentifier() != 3) SceneButtonsLayout.go(ctrl, sceneButton);
        switch (sceneButton.getLocalIdentifier()) {
            case 0 -> switchToScene(ctrl, Scene.COLOURS_1);
            case 1 -> switchToScene(ctrl, Scene.COLOURS_2);
            case 2 -> switchToScene(ctrl, Scene.FADERS);
            case 4 -> switchToScene(ctrl, Scene.PAGE_1);
            case 5 -> switchToScene(ctrl, Scene.PAGE_2);
            case 6 -> switchToScene(ctrl, Scene.PAGE_3);
            case 7 -> switchToScene(ctrl, Scene.PAGE_4);
        }
    }

    private void switchToScene(APCController ctrl, Scene scene) {
        currentScene = scene;

        switch (scene) {
            case COLOURS_1, COLOURS_2 -> ColourPages.go(ctrl, scene.ordinal());
            case FADERS -> FaderGraphics.buildBase(ctrl, faderRed, faderGreen, faderBlue);
        }
    }

    private void selectColour(Button pad) {
        if (!pad.getButtonType().equals(ButtonType.PAD)) throw new RuntimeException("Cannot select colour from non pad button.");

        int colour = switch (currentScene) {
            case COLOURS_1 -> pad.getLocalIdentifier();
            case COLOURS_2 -> pad.getLocalIdentifier() + 64;
            default -> -1;
        };

        if (colour == -1) {

        }
        else selectedColour = colour;

    }

    /*private void storeButton(APCController ctrl, Button button, int colour) {
        stored.put(button.getLocalIdentifier(), colour);
        ctrl.outputToButton(button.getLocalIdentifier(), colour);
    }*/


    @Override
    public void onFaderMove(APCController controller, Fader fader) {
        switch (fader.getFaderNum()) {
            case 1,2,3 -> {
                if (currentScene.equals(Scene.FADERS)) FaderGraphics.updateColumn(controller, fader);
                FaderGraphics.updateTrackBlink(controller, true);
                switch (fader.getFaderNum()) {
                    case 1 -> faderRed = fader.getStandardLevel();
                    case 2 -> faderGreen = fader.getStandardLevel();
                    case 3 -> faderBlue = fader.getStandardLevel();
                }
            }
        }
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {}
    @Override
    public void onClose() {}




}
