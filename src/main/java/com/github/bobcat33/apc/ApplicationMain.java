package com.github.bobcat33.apc;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;
import com.github.bobcat33.apc.apcinterface.mididevice.MidiDeviceManager;
import com.github.bobcat33.apc.listeners.ShiftStopListener;
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

        ctrl.addListener(new APCButtonEventListener() {
            @Override
            public void onButtonDown(APCController controller, Button button) {
                if (button.getButtonType().equals(ButtonType.PAD)) controller.outputToButton(button.getIdentifier(), new Random().nextInt(127));
                else if (button.getButtonType().equals(ButtonType.SCENE_LAUNCH)) {
                    switch (button.getLocalIdentifier()) {
                        case 0 -> {
                            for (int i = 0; i < 64; i++) {
                                controller.outputToButton(i, 0);
                            }
                        }

                        case 1 -> {
                            for (int i = 0; i < 64; i++) {
                                controller.outputToButton(i, 56);
                            }
                        }
                    }

                }
            }

            @Override
            public void onButtonUp(APCController controller, Button button) {

            }

            @Override
            public void onClose() {

            }
        });

        ctrl.addListener(new ShiftStopListener());

        ctrl.start();


//        new StartupAnimation(ctrl).start();



//        ctrl.addListener(new APCButtonEventListener() {
//            boolean triggered = false;
//            @Override
//            public void onButtonDown(APCController controller, Button button) {
//                if (button.getButtonType().equals(ButtonType.SHIFT)) {
//                    controller.close();
//                    return;
//                }
//                try{
//                    new SpreadFromPointAnimation(controller, 0).start(button);
////                    new SpreadFromPointAnimation(controller, new Random().nextInt(127)).start(button);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            @Override
//            public void onButtonUp(APCController controller, Button button) {}
//            @Override
//            public void onClose() {}
//        });
    }
}