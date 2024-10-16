package com.github.bobcat33.apc.programs.faderlevels;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCFaderEventListener;
import com.github.bobcat33.apc.apcinterface.message.Fader;

public class ListenFaders extends APCFaderEventListener {
    @Override
    public void onFaderMove(APCController controller, Fader fader) {
        AnimateButtons.animateColumn(controller, fader.getFaderNum()-1, fader.getData());
    }

    @Override
    public void onClose() {}
}
