package com.github.bobcat33.apc.apcinterface;

import com.github.bobcat33.apc.apcinterface.listener.APCEventListener;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;
import com.github.bobcat33.apc.apcinterface.message.Message;

import javax.sound.midi.*;
import java.util.ArrayList;

public class APCController implements Receiver {
    private final MidiDevice inputStream, outputStream;
    private boolean active, closed;
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

        System.out.println(message);
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
/*
        // Want to make LED XX colour on 100% brightness
        // Channel 96 for 100%
        // Note is the same as the received message
        // Velocity colour variable

        ShortMessage msg = new ShortMessage();
        try {
            msg.setMessage(0x96, message.getMessage()[1], new Random().nextInt(127));
//            msg.setMessage(0x96, message.getMessage()[1], colour);
            output(msg);
        } catch (InvalidMidiDataException err) {
            System.out.println("Unable to send message as MIDI data is invalid: " + err.getMessage());
        } catch (MidiUnavailableException err) {
            System.out.println("Unable to send message as MIDI is unavailable: " + err.getMessage());
        }*/
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
        System.out.println("APCController Closed!");
    }

    public boolean isActive() {
        return !this.closed && this.active;
    }
}
