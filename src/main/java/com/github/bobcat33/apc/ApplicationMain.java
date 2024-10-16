package com.github.bobcat33.apc;

import com.github.bobcat33.apc.animation.SpreadFromPointAnimation;
import com.github.bobcat33.apc.animation.StartupAnimation;
import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;
import com.github.bobcat33.apc.apcinterface.mididevice.MidiDeviceManager;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.Random;

public class ApplicationMain extends Application {
    @Override
    public void start(Stage stage) {}

    public static void main(String[] args) throws MidiUnavailableException, InvalidMessageException, InvalidMidiDataException, InterruptedException {
//        launch();

        APCController ctrl = MidiDeviceManager.createAPCController(6, 4);
        ctrl.start();

        new StartupAnimation(ctrl).start();

        ctrl.addListener(new APCButtonEventListener() {
            boolean triggered = false;
            @Override
            public void onButtonDown(APCController controller, Button button) {
                if (button.getButtonType().equals(ButtonType.SHIFT)) {
                    controller.close();
                    return;
                }
                try{
                    new SpreadFromPointAnimation(controller, new Random().nextInt(127)).start(button);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onButtonUp(APCController controller, Button button) {}
            @Override
            public void onClose() {}
        });
    }
}