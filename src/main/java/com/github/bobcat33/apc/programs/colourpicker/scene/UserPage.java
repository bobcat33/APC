package com.github.bobcat33.apc.programs.colourpicker.scene;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.graphics.Layout;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.layouts.FileLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class UserPage implements Layout {
    private final HashMap<Integer, Button> padMap; // Store button so that support for different LED behaviour will be available later
    private final HashMap<Integer, UIButtonBehaviour> trackMap, sceneMap;
    private File file = null;

    public UserPage() {
        padMap = new HashMap<>();
        trackMap = new HashMap<>();
        sceneMap = new HashMap<>();
    }

    public UserPage(File file) {
        this();
        this.file = file;
        try {
            padMap.putAll(FileLayout.loadPadDataFromFile(file));
            trackMap.putAll(FileLayout.loadTrackButtonBehaviourFromFile(file));
            sceneMap.putAll(FileLayout.loadSceneButtonBehaviourFromFile(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeButton(Button button) {
        if (button.getButtonType().equals(ButtonType.PAD)) padMap.put(button.getIdentifier(), button);
        else if (button.getButtonType().equals(ButtonType.TRACK)) trackMap.put(button.getLocalIdentifier(), UIButtonBehaviour.from(button.getData()));
    }

    public Button selectButton(Button button) {
        int pos = button.getLocalIdentifier();
        // If PAD selected
        if (button.getButtonType().equals(ButtonType.PAD)) {
            Button pad = padMap.get(pos);
            if (pad == null) return new Button(0x90, pos, 0);
            return pad.copy();
        }

        // If TRACK selected
        if (button.getButtonType().equals(ButtonType.TRACK))
            return new Button(0x90, ButtonType.TRACK, pos, trackMap.get(pos).ordinal());
        return null;
    }

    public void clear() {
        padMap.clear();
        trackMap.clear();
    }

    public void storeToFile() {
        if (file == null) return; // TODO Store to default file

        try {
            FileLayout.storeToFile(file, padMap, trackMap, sceneMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void go(APCController controller) {
        // Display pad colours
        for (int i = 0; i < 64; i++) {
            Button button = padMap.get(i);
            controller.output(Objects.requireNonNullElse(button, new Button(0x90, i, 0)));
        }
        // Display track behaviours
        for (int i = 0; i < 8; i++) {
            UIButtonBehaviour behaviour = trackMap.get(i);
            controller.outputToButton(ButtonType.TRACK, i, Objects.requireNonNullElse(behaviour, UIButtonBehaviour.OFF));
        }
    }

}
