package com.github.bobcat33.apc.apcinterface.communication;

import com.github.bobcat33.apc.apcinterface.message.Message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class APCTransmitter implements Transmitter {
    private Receiver outputStream;
    private boolean logging = false;
    private boolean active = true;

    public void transmit(Message message) throws InvalidMidiDataException {
        if (!active) return;
        if (logging) System.out.println("SENDING: " + message);
        if (outputStream != null) outputStream.send(message.toShortMessage(), -1);
        if (logging) System.out.println("SENT:    " + message);
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    @Override
    public void setReceiver(Receiver receiver) {
        if (!active) return;
        this.outputStream = receiver;
    }

    @Override
    public Receiver getReceiver() {
        if (!active) return null;
        return outputStream;
    }

    @Override
    public void close() {
        active = false;
        outputStream = null;
        if (logging) System.out.println("APCTransmitter Closed!");
    }
}
