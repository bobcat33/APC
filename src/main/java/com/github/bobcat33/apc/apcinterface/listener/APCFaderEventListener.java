package com.github.bobcat33.apc.apcinterface.listener;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Fader;
import com.github.bobcat33.apc.apcinterface.message.Message;

public abstract class APCFaderEventListener implements APCEventListener {

    public abstract void onFaderMove(APCController controller, Fader fader);

    @Override
    public void onMessage(APCController controller, Message message) {
        if (Fader.isFader(message)) onFaderMove(controller, Fader.fromMessage(message));
    }
}
