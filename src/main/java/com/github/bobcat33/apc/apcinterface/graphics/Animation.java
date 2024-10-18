package com.github.bobcat33.apc.apcinterface.graphics;

import com.github.bobcat33.apc.apcinterface.APCController;

import java.util.function.Consumer;

public abstract class Animation {

    protected final APCController controller;
    private Consumer<APCController> onFinish;

    public Animation(APCController controller) {
        this.controller = controller;
    }

    protected abstract void start();

    public void run() {
        start();
        if (onFinish != null) onFinish.accept(controller);
    }

    public void onFinish(Consumer<APCController> consumer) {
        this.onFinish = consumer;
    }

    protected void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
