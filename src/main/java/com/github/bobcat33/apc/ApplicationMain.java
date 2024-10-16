package com.github.bobcat33.apc;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;
import com.github.bobcat33.apc.apcinterface.mididevice.MidiDeviceManager;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class ApplicationMain extends Application {
    @Override
    public void start(Stage stage) {}

    public static void main(String[] args) throws MidiUnavailableException, InvalidMessageException, InvalidMidiDataException {
//        launch();

        APCController ctrl = MidiDeviceManager.createAPCController(6, 4);
        ctrl.start();

        ctrl.output(Button.Out.createUIButtonData(ButtonType.SHIFT, 0, 1));

        ctrl.close();

    }
}