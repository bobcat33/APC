package com.github.bobcat33.apc.apcinterface;

import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.listener.APCEventListener;
import com.github.bobcat33.apc.apcinterface.listener.APCListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Message;

import javax.sound.midi.*;

public class APCController implements AutoCloseable {
    private final MidiDevice outputStream;
    private final APCListener inputStream;

    private boolean active, closed;
    private boolean logs = false;

    public APCController(MidiDevice inputStream, MidiDevice outputStream) throws MidiUnavailableException {
        openDevice(inputStream);
        openDevice(outputStream);

        this.inputStream = new APCListener(this, inputStream);
        this.outputStream = outputStream;
    }

    public void addListener(APCEventListener listener) {
        inputStream.addListener(listener);
    }

    public void start() throws MidiUnavailableException {
        if (closed || active) return;
        this.inputStream.start();
        this.active = true;
        if (logs) System.out.println("APCController Started");
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

        if (logs) System.out.println("OUT: " + message);
    }

    public void enableLogs() {
        logs = true;
        inputStream.setLogging(true);
    }
    public void disableLogs() {
        logs = false;
        inputStream.setLogging(false);
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

    @Override
    public void close() {
        if (!isActive()) return;
        this.closed = true;
        inputStream.close();
        outputStream.close();
        this.active = false;

        if (logs) System.out.println("APCController Closed!");
    }

    public boolean isActive() {
        return !this.closed && this.active;
    }
}
