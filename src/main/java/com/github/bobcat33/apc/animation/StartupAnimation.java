package com.github.bobcat33.apc.animation;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class StartupAnimation {

    private APCController ctrl;

    public StartupAnimation(APCController ctrl) {
        this.ctrl = ctrl;
    }

    public void start() throws InvalidMessageException, MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ctrl.output(Button.Out.createPadData(0x96, (x+(y*8)), 5));
                Thread.sleep(50);
            }
        }
        Thread.sleep(50);

        for (int i = 7; i >= 0; i--) {
            ctrl.output(Button.Out.createUIButtonData(ButtonType.TRACK, i, 1));
            ctrl.output(Button.Out.createUIButtonData(ButtonType.SCENE_LAUNCH, i, 1));
            Thread.sleep(100);
        }

        for (int i = 7; i >= 0; i--) {
            ctrl.output(Button.Out.createUIButtonData(ButtonType.TRACK, i, 0));
            ctrl.output(Button.Out.createUIButtonData(ButtonType.SCENE_LAUNCH, i, 0));
            Thread.sleep(30);
        }
    }

}
