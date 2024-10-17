package com.github.bobcat33.apc.apcinterface.listener;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;
import com.github.bobcat33.apc.apcinterface.message.Message;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import java.util.ArrayList;

public class APCListener implements Receiver {
    private final APCController parentController;
    private final ArrayList<APCEventListener> listeners = new ArrayList<>();
    private final MidiDevice inputStream;
    private boolean active, closed;
    private boolean logs = false;

    public APCListener(APCController parent, MidiDevice inputStream) {
        this.parentController = parent;
        this.inputStream = inputStream;
    }

    public void start() throws MidiUnavailableException {
        if (closed || active) return;
        this.inputStream.getTransmitter().setReceiver(this);
        this.active = true;
        if (logs) System.out.println("APCListener Started");
    }

    public void setLogging(boolean logging) {
        this.logs = logging;
    }

    public void addListener(APCEventListener listener) {
        listeners.add(listener);
    }

    private void onReceive(Message message) {
        if (logs) System.out.println("IN: " + message);
        for (APCEventListener listener : listeners) listener.onMessage(parentController, message);
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (!isActive()) return;

        try {
            onReceive(Message.fromMIDIMessage(message));
        } catch (InvalidMessageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (!isActive()) return;
        this.closed = true;
        try {
            inputStream.getTransmitter().setReceiver(null);
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
        inputStream.close();
        for (APCEventListener listener : listeners) listener.onClose();
        this.active = false;

        if (logs) System.out.println("APCListener Closed!");
    }

    public boolean isActive() {
        return !this.closed && this.active;
    }
}
