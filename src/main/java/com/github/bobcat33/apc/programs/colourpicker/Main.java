package com.github.bobcat33.apc.programs.colourpicker;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.mididevice.MidiDeviceManager;
import com.github.bobcat33.apc.listeners.ShiftStopListener;

import javax.sound.midi.MidiUnavailableException;

public class Main {

    public static void main(String[] args) throws MidiUnavailableException {
        APCController ctrl = MidiDeviceManager.createAPCController(6, 4);
        ctrl.enableLogs();
        ctrl.addListener(new ShiftStopListener(true));
        ctrl.addListener(new InputListener());
        ctrl.start();

        new StartUpLayout().go(ctrl);

    }

}
