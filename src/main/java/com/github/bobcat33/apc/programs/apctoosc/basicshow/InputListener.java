package com.github.bobcat33.apc.programs.apctoosc.basicshow;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonFaderEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Fader;
import com.github.bobcat33.osc.OSCTransmitter;

public class InputListener extends APCButtonFaderEventListener {
    private final OSCTransmitter transmitter;

    public InputListener(OSCTransmitter transmitter) {
        this.transmitter = transmitter;
    }

    @Override
    public void onButtonDown(APCController controller, Button button) {
        if (button.getButtonType().equals(ButtonType.TRACK)) {
            // Toggle Sub On
            transmitter.flashSubOn(button.getLocalIdentifier() + 1);
        }
    }

    @Override
    public void onButtonUp(APCController controller, Button button) {
        if (button.getButtonType().equals(ButtonType.TRACK)) {
            // Toggle Sub Off
            transmitter.flashSubOff(button.getLocalIdentifier() + 1);
        }
        else if (button.getButtonType().equals(ButtonType.PAD)) {
            // TODO TOGGLE EFFECT/COLOUR SUB
        }
    }

    @Override
    public void onFaderMove(APCController controller, Fader fader) {
        transmitter.sendFaderLevel(fader.getFaderNum(), fader.getPercentage());
    }

    @Override
    public void onClose() {

    }

}
