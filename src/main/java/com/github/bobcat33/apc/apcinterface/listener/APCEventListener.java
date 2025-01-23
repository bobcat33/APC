package com.github.bobcat33.apc.apcinterface.listener;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Message;

public abstract class APCEventListener {
    private boolean active = true;

    public abstract void onMessage(APCController controller, Message message);
    public abstract void onClose();

    public boolean isActive() {
        return active;
    }

    public void setActive(APCController controller, boolean active) {
        this.active = active;
    }
}
