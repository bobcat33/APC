package com.github.bobcat33.apc.apcinterface;

import com.github.bobcat33.apc.apcinterface.communication.APCTransmitter;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.listener.APCEventListener;
import com.github.bobcat33.apc.apcinterface.communication.APCReceiver;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Message;

import javax.sound.midi.*;

public class APCController {
    private final MidiDevice outputStream, inputStream;
    private final APCReceiver receiver;
    private final APCTransmitter transmitter;

    private boolean active, closed;
    private boolean logging = false;

    public APCController(MidiDevice inputStream, MidiDevice outputStream) throws MidiUnavailableException {
        openDevice(inputStream);
        openDevice(outputStream);

        this.inputStream = inputStream;
        this.outputStream = outputStream;
        receiver = new APCReceiver(this, inputStream.getTransmitter());
        transmitter = new APCTransmitter();
    }

    public void addListener(APCEventListener listener) {
        receiver.addListener(listener);
    }

    public void start() throws MidiUnavailableException {
        if (closed || active) return;
        receiver.start();
        transmitter.setReceiver(outputStream.getReceiver());
        active = true;
        if (logging) System.out.println("APCController Started");
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
            transmitter.transmit(message);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    public void enableLogging() {
        setLogging(true, true, true);
    }
    public void disableLogging() {
        setLogging(false, false, false);
    }

    public void setLogging(boolean controller, boolean transmitter, boolean receiver) {
        logging = controller;
        this.transmitter.setLogging(transmitter);
        this.receiver.setLogging(receiver);
    }


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

    public void close() {
        if (!isActive()) return;
        this.closed = true;
        receiver.close();
        transmitter.close();
        inputStream.close();
        outputStream.close();
        this.active = false;
        if (logging) System.out.println("APCController Closed!");
    }

    public boolean isActive() {
        return !this.closed && this.active;
    }
}
