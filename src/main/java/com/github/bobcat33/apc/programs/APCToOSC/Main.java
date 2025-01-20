package com.github.bobcat33.apc.programs.APCToOSC;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.mididevice.MidiDeviceManager;
import com.github.bobcat33.apc.listeners.ShiftStopListener;
import com.github.bobcat33.osc.OSCTransmitter;

import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws MidiUnavailableException, IOException {
        APCController ctrl = MidiDeviceManager.createAPCController(6, 4);
        ctrl.addListener(new ShiftStopListener(true));
        ctrl.start();

        new ColourLayout().go(ctrl);

        ctrl.addListener(new InputListener(new OSCTransmitter()));

    }

}
