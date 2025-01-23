package com.github.bobcat33.apc.programs.apctoosc.basicshow;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonEventListener;
import com.github.bobcat33.apc.apcinterface.listener.APCEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

public class ListenerSwitch extends APCButtonEventListener {
    private APCEventListener activeListener;
    private APCEventListener standbyListener;

    public ListenerSwitch(APCController controller, APCEventListener active, APCEventListener standby) {
        activeListener = active;
        standbyListener = standby;

        activeListener.setActive(controller, true);
        standbyListener.setActive(controller, false);
    }

    @Override
    public void onButtonDown(APCController controller, Button button) {

    }

    @Override
    public void onButtonUp(APCController controller, Button button) {
        if (button.getButtonType().equals(ButtonType.SHIFT)) {
            switchActive(controller);
        }
    }

    @Override
    public void onClose() {

    }

    public void switchActive(APCController controller) {
        activeListener.setActive(controller, false);
        standbyListener.setActive(controller, true);
        APCEventListener oldActiveListener = activeListener;
        activeListener = standbyListener;
        standbyListener = oldActiveListener;
    }
}
