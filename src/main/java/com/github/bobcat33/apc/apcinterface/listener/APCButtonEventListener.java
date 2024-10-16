package com.github.bobcat33.apc.apcinterface.listener;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.Fader;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;
import com.github.bobcat33.apc.apcinterface.message.Message;

public abstract class APCButtonEventListener implements APCEventListener {

    public abstract void onButtonDown(APCController controller, Button button);
    public abstract void onButtonUp(APCController controller, Button button);


    @Override
    public void onMessage(APCController controller, Message message) {
        if (Fader.isFader(message)) return;

        Button.In button = new Button.In(message.getChannel(), message.getIdentifier(), message.getData());

        if (button.isDown()) onButtonDown(controller, button);
        else onButtonUp(controller, button);
    }
}
