package com.github.bobcat33.apc.programs.apctoosc.basicshow;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonFaderEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Fader;
import com.github.bobcat33.apc.layouts.FileLayout;
import com.github.bobcat33.osc.OSCTransmitter;

public class InputListener extends APCButtonFaderEventListener {
    private final FileLayout fileLayout;
    private final OSCTransmitter transmitter;

    public InputListener(FileLayout fileLayout, OSCTransmitter transmitter) {
        this.fileLayout = fileLayout;
        this.transmitter = transmitter;
    }

    @Override
    public void onButtonDown(APCController controller, Button button) {
        if (button.getButtonType().equals(ButtonType.TRACK)) {
            // Toggle Sub On
            transmitter.flashFaderOn(button.getLocalIdentifier() + 1);
        }
        else if (button.getButtonType().equals(ButtonType.PAD)) {
            toggleFaderFromPad(button.getLocalIdentifier(), true);
        }
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {
        if (button.getButtonType().equals(ButtonType.TRACK)) {
            // Toggle fader button Off
            transmitter.flashFaderOut(button.getLocalIdentifier() + 1);
        }
        else if (button.getButtonType().equals(ButtonType.PAD)) {
            toggleFaderFromPad(button.getLocalIdentifier(), false);
        }
        else if (button.getButtonType().equals(ButtonType.SCENE_LAUNCH)) {
            if (button.getLocalIdentifier() == 0) {
                transmitter.sendBlackoutToggle();
            }
        }
    }

    @Override
    public void onFaderMove(APCController controller, Fader fader) {
        transmitter.sendFaderLevel(fader.getFaderNum(), fader.getPercentage());
    }

    @Override
    public void onClose() {

    }

    private void toggleFaderFromPad(int padId, boolean toggleOn) {
        int faderPage = ((padId / 8) * -1) + 9;
        int faderNum = (padId % 8) + 1;
        if (toggleOn) transmitter.flashFaderOn(faderPage, faderNum);
        else transmitter.flashFaderOut(faderPage, faderNum);
    }

    @Override
    public void setActive(APCController controller, boolean active) {
        super.setActive(controller, active);

        fileLayout.go(controller);
    }
}
