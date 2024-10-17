package com.github.bobcat33.apc.apcinterface;

import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.listener.APCEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;
import com.github.bobcat33.apc.apcinterface.message.Message;

import javax.sound.midi.*;
import java.util.ArrayList;

public class APCController implements Receiver {
    private final MidiDevice inputStream, outputStream;
    private boolean active, closed;

    private boolean logs = false;
    private ArrayList<APCEventListener> listeners = new ArrayList<>();

    public APCController(MidiDevice inputStream, MidiDevice outputStream) throws MidiUnavailableException {
        openDevice(inputStream);
        openDevice(outputStream);

        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void addListener(APCEventListener listener) {
        listeners.add(listener);
    }

    // Currently causes ConcurrentModificationException
//    public void removeListener(APCEventListener listener) {
//        listeners.remove(listener);
//    }

    public ArrayList<APCEventListener> getListeners() {
        return listeners;
    }

    public void start() throws MidiUnavailableException {
        if (closed || active) return;
        this.inputStream.getTransmitter().setReceiver(this);
        this.active = true;
    }

    private void openDevice(MidiDevice device) throws MidiUnavailableException {
        if (!(device.isOpen())) {
            device.open();
        } else {
            throw new MidiUnavailableException("Device is already in use, please close device and try again.");
        }
    }

    public void output(Message message) {
        if (!isActive()) return;

        try {
            outputStream.getReceiver().send(message.toShortMessage(), -1);
        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            throw new RuntimeException(e);
        }

        if (logs) System.out.println(message);
    }

    public void enableLogs() {logs = true;}
    public void disableLogs() {logs = false;}

    public void outputToButton(int position, int colour) {
        int behaviour = 0x96;
        if (colour == 0) behaviour = 0x90;

        output(new Button(behaviour, position, colour));
    }

    public void outputToButton(ButtonType type, int localPosition, int colour) {
        int behaviour;
        if (type.equals(ButtonType.PAD) && colour != 0) behaviour = 0x96;
        else behaviour = 0x90;

        output(new Button(behaviour, type, localPosition, colour));
    }

    public void outputToButton(ButtonType type, int localPosition, UIButtonBehaviour behaviour) {
        if (type.equals(ButtonType.PAD)) throw new RuntimeException("UIButtonBehaviour cannot be applied to button of type PAD"); // TODO make exception
        outputToButton(type, localPosition, behaviour.ordinal());
    }

    private void onReceive(Message message) {
        for (APCEventListener listener : listeners) listener.onMessage(this, message);
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
        } catch (MidiUnavailableException ignored) {
        }
        inputStream.close();
        outputStream.close();
        this.active = false;

        for (APCEventListener listener : listeners) listener.onClose();
        if (logs) System.out.println("APCController Closed!");
    }

    public boolean isActive() {
        return !this.closed && this.active;
    }
}
