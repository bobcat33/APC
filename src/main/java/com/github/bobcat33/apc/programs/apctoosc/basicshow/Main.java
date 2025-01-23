package com.github.bobcat33.apc.programs.apctoosc.basicshow;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCEventListener;
import com.github.bobcat33.apc.apcinterface.mididevice.MidiDeviceManager;
import com.github.bobcat33.apc.layouts.FileLayout;
import com.github.bobcat33.apc.listeners.ShiftHoldStopListener;
import com.github.bobcat33.apc.programs.colourpicker.scene.UserPage;
import com.github.bobcat33.osc.OSCTransmitter;

import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws MidiUnavailableException, IOException {
        APCController ctrl = MidiDeviceManager.createAPCController(6, 4);
        ctrl.addListener(new ShiftHoldStopListener(3));
        ctrl.start();

//        new ButtonGraphics().go(ctrl);

        File layoutFile = new File("src/main/java/com/github/bobcat33/apc/programs/apctoosc/basicshow/layout.txt");

        FileLayout layout = new FileLayout(layoutFile);

        APCEventListener lightingListener = new InputListener(layout, new OSCTransmitter());
        APCEventListener colourPickerListener = new com.github.bobcat33.apc.programs.colourpicker.InputListener(new UserPage(layoutFile));

        APCEventListener switcher = new ListenerSwitch(ctrl, lightingListener, colourPickerListener);
        ctrl.addListener(switcher);
        ctrl.addListener(lightingListener);
        ctrl.addListener(colourPickerListener);


    }

}
