package com.github.bobcat33.apc.programs.faderlevels;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.ShiftStopListener;
import com.github.bobcat33.apc.apcinterface.mididevice.MidiDeviceManager;

import javax.sound.midi.MidiUnavailableException;

public class FaderLevelsMain {

    public static void main(String[] args) throws MidiUnavailableException {
        APCController ctrl = MidiDeviceManager.createAPCController(6, 4);
        ctrl.addListener(new ListenFaders());
        ctrl.addListener(new ShiftStopListener());

        ctrl.start();
    }

}
