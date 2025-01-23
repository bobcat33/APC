package com.github.bobcat33.apc.apcinterface.communication;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.listener.APCEventListener;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;
import com.github.bobcat33.apc.apcinterface.message.Message;

import javax.sound.midi.*;
import java.util.ArrayList;

public class APCReceiver implements Receiver {
    private final APCController parentController;
    private final ArrayList<APCEventListener> listeners = new ArrayList<>();
    private final Transmitter inputStream;
    private boolean active, closed;
    private boolean logging = false;

    public APCReceiver(APCController parent, Transmitter transmitter) {
        this.parentController = parent;
        this.inputStream = transmitter;
    }

    public void start() {
        if (closed || active) return;
        this.inputStream.setReceiver(this);
        this.active = true;
        if (logging) System.out.println("APCListener Started");
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public void addListener(APCEventListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    private void onReceive(Message message) {
        if (logging) System.out.println(Thread.currentThread().threadId() + "\tIN: " + message);
        // TODO Modify so that it doesnt have to create a million new arrays
        ArrayList<APCEventListener> listenersCopy;
        synchronized (listeners) {
            listenersCopy = new ArrayList<>(listeners);
        }
        for (APCEventListener listener : listenersCopy) if (listener.isActive()) listener.onMessage(parentController, message);
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (!isActive()) return;

        try {
            // Run all received messages on a new thread to avoid blocking
            new Thread(() -> onReceive(Message.fromMIDIMessage(message))).start();
        } catch (InvalidMessageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (!isActive()) return;
        this.closed = true;
        inputStream.setReceiver(null);
        inputStream.close();
        for (APCEventListener listener : listeners) listener.onClose();
        this.active = false;

        if (logging) System.out.println("APCListener Closed!");
    }

    public boolean isActive() {
        return !this.closed && this.active;
    }
}
