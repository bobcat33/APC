package com.github.bobcat33.apc.apcinterface.listener;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Message;

public interface APCEventListener {

    void onMessage(APCController controller, Message message);
    void onClose();

}
