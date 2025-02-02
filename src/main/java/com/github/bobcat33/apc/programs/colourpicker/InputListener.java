package com.github.bobcat33.apc.programs.colourpicker;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonFaderEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Fader;
import com.github.bobcat33.apc.programs.colourpicker.scene.ColourPages;
import com.github.bobcat33.apc.programs.colourpicker.scene.FaderGraphics;
import com.github.bobcat33.apc.programs.colourpicker.scene.SceneButtonsLayout;
import com.github.bobcat33.apc.programs.colourpicker.scene.UserPage;

import java.util.ArrayList;

public class InputListener extends APCButtonFaderEventListener {
    private volatile int faderRed = 0, faderGreen = 0, faderBlue = 0, faderBrightness = 0;
    private volatile boolean copyMode = false;
    private volatile Button copiedButton = new Button(0x90, 0, 0);
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

    private final ArrayList<UserPage> userPages = new ArrayList<>();


    public InputListener(UserPage... pages) {
        if (pages.length > 4) throw new RuntimeException("Maximum of 4 UserPages allowed."); // TODO Create exception
        for (int i = 0; i < 4; i++) {
            if (i < pages.length) userPages.add(pages[i]);
            else userPages.add(new UserPage());
        }
    }


    @Override
    public void onButtonDown(APCController controller, Button button) {
        switch (button.getButtonType()) {
            case SCENE_LAUNCH -> changeScene(controller, button);
            case PAD -> pressPadButton(controller, button);
            case TRACK -> pressTrackButton(controller, button);
        }

    }

    private void pressPadButton(APCController ctrl, Button padButton) {
        if (currentScene.equals(Scene.COLOURS_1) || currentScene.equals(Scene.COLOURS_2) || copyMode) copyButton(ctrl, padButton);
        else if (isUserPage()) storeButtonInUserPage(ctrl, copiedButton.as(ButtonType.PAD).at(padButton.getLocalIdentifier()), getUserPage());
    }

    private void pressTrackButton(APCController ctrl, Button trackButton) {
        if (isUserPage()) {
            if (copyMode) copyButton(ctrl, trackButton);
            else storeButtonInUserPage(ctrl, copiedButton.as(ButtonType.TRACK).at(trackButton.getLocalIdentifier()), getUserPage());
            return;
        }
        switch (trackButton.getLocalIdentifier()) {
            case 0,1,2 -> {
                if (currentScene.equals(Scene.FADERS)) {
                    FaderGraphics.updateSolidColour(ctrl, faderRed, faderGreen, faderBlue);
                }
            }
            default -> {}
        }
    }

    private void storeButtonInUserPage(APCController ctrl, Button copiedButton, UserPage page) {
        Button button;
        if (faderBrightness == 0) button = new Button(copiedButton.getBehaviour(), copiedButton.getIdentifier(), 0);
        else if (copiedButton.getButtonType().equals(ButtonType.PAD)) {
            int behaviour;
            if (faderBrightness <= 10) behaviour = 0x90;
            else if (faderBrightness <= 25) behaviour = 0x91;
            else if (faderBrightness <= 50) behaviour = 0x92;
            else if (faderBrightness <= 65) behaviour = 0x93;
            else if (faderBrightness <= 75) behaviour = 0x94;
            else if (faderBrightness <= 90) behaviour = 0x95;
            else behaviour = 0x96;
            button = new Button(behaviour, copiedButton.getIdentifier(), copiedButton.getData());
        } else button = copiedButton;

        page.storeButton(button);
        ctrl.output(button);
    }

    private void toggleCopyMode(APCController ctrl) {
        setCopyMode(ctrl, !copyMode);
    }

    private void setCopyMode(APCController ctrl, boolean copy) {
        if (currentScene.equals(Scene.COLOURS_1) || currentScene.equals(Scene.COLOURS_2)) return;

        if (copy) {
            copyMode = true;
            ctrl.outputToButton(ButtonType.SCENE_LAUNCH, 3, UIButtonBehaviour.BLINK);
            return;
        }
        copyMode = false;
        ctrl.outputToButton(ButtonType.SCENE_LAUNCH, 3, UIButtonBehaviour.OFF);
    }

    private void changeScene(APCController ctrl, Button sceneButton) {
        if (sceneButton.getLocalIdentifier() != 3) SceneButtonsLayout.go(ctrl, sceneButton);
        switch (sceneButton.getLocalIdentifier()) {
            case 0 -> switchToScene(ctrl, Scene.COLOURS_1);
            case 1 -> switchToScene(ctrl, Scene.COLOURS_2);
            case 2 -> switchToScene(ctrl, Scene.FADERS);
            case 3 -> toggleCopyMode(ctrl);
            case 4 -> switchToScene(ctrl, Scene.PAGE_1);
            case 5 -> switchToScene(ctrl, Scene.PAGE_2);
            case 6 -> switchToScene(ctrl, Scene.PAGE_3);
            case 7 -> switchToScene(ctrl, Scene.PAGE_4);
        }
    }

    private void switchToScene(APCController ctrl, Scene scene) {
        setCopyMode(ctrl, false);
        currentScene = scene;

        switch (scene) {
            case COLOURS_1, COLOURS_2 -> ColourPages.go(ctrl, scene.ordinal());
            case FADERS -> FaderGraphics.buildBase(ctrl, faderRed, faderGreen, faderBlue);
            default -> getUserPage().go(ctrl);
        }
    }

    private boolean isUserPage() {
        return (currentScene.ordinal() - 3) >= 0;
    }

    private UserPage getUserPage() {
        int pos = currentScene.ordinal() - 3;
        if (pos < 0) throw new RuntimeException("Can't get user page of non-user scene"); // TODO Make exception
        return userPages.get(pos);
    }

    private void copyButton(APCController ctrl, Button button) {
        // Ignore the select command if the button is a scene launch or is a track and not on a user page
        if (button.getButtonType().equals(ButtonType.SCENE_LAUNCH)) return; // throw new RuntimeException("Cannot select colour from scene-launch button."); // TODO Make exception (maybe)
        if (button.getButtonType().equals(ButtonType.TRACK) && !isUserPage()) return;

        if (isUserPage()) copiedButton = getUserPage().selectButton(button);
        else {
            int colour = switch (currentScene) {
                case COLOURS_1 -> button.getLocalIdentifier();
                case COLOURS_2 -> button.getLocalIdentifier() + 64;
                case FADERS -> switch (button.getLocalIdentifier() % 8) {
                    case 0 -> 5; // RED COLUMN
                    case 1 -> 21; // GREEN COLUMN
                    case 2 -> 45; // BLUE COLUMN
                    case 3, 4 -> FaderGraphics.getCurrentColour();
                    default -> 0; // TODO Update later if feature for quick store on fader page added or other features added
                };
                default -> 0;
            };
            copiedButton = new Button(0x96, button.getIdentifier(), colour);
        }

        if (copyMode) setCopyMode(ctrl, false);
    }

    /*private void storeButton(APCController ctrl, Button button, int colour) {
        stored.put(button.getLocalIdentifier(), colour);
        ctrl.outputToButton(button.getLocalIdentifier(), colour);
    }*/


    @Override
    public void onFaderMove(APCController controller, Fader fader) {
        switch (fader.getFaderNum()) {
            case 1,2,3 -> {
                if (currentScene.equals(Scene.FADERS)) {
                    FaderGraphics.updateColumn(controller, fader);
                    FaderGraphics.updateTrackBlink(controller, true);
                }
                switch (fader.getFaderNum()) {
                    case 1 -> faderRed = fader.getStandardLevel();
                    case 2 -> faderGreen = fader.getStandardLevel();
                    case 3 -> faderBlue = fader.getStandardLevel();
                }
            }
            case 9 -> faderBrightness = fader.getPercentage();
        }
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {}

    @Override
    public void onClose() {
        for (UserPage page : userPages) {
            page.storeToFile();
        }
    }

    @Override
    public void setActive(APCController controller, boolean active) {
        super.setActive(controller, active);

        if (active) {
            new StartUpLayout().go(controller);
        } else {
            copyMode = false;
            currentScene = Scene.COLOURS_1;
            for (UserPage page : userPages) {
                page.storeToFile();
            }
        }
    }
}
