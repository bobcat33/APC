package com.github.bobcat33.apc.programs.colourpicker.scene;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.graphics.Layout;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class UserPage implements Layout {
    private final HashMap<Integer, Button> padMap; // Store button so that support for different LED behaviour will be available later
    private final HashMap<Integer, UIButtonBehaviour> trackMap;
    private File file = null;

    public UserPage() {
        padMap = new HashMap<>();
        trackMap = new HashMap<>();
    }

    public UserPage(File file) {
        this();
        this.file = file;
        // TODO Load from file
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
            return new Button(0x90, ButtonType.TRACK, pos, Objects.requireNonNullElse(trackMap.get(pos).ordinal(), 0));
        return null;
    }

    public void clear() {
        padMap.clear();
        trackMap.clear();
    }

    public void storeToFile() {
        // TODO Possibly store to default files even if loaded from different file (store to both) so that desk starts from where left off when restarted?
        if (file == null) return; // TODO Store to default file
        // TODO Store to loaded file
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
