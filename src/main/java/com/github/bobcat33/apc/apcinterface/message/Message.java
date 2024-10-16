package com.github.bobcat33.apc.apcinterface.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

public class Message {
    private final int channel, identifier, data;
    public Message(int channel, int identifier, int data) throws InvalidMessageException {
//        if (channel < 0 || channel > 127) throw new InvalidMessageException("Channel out of bounds (" + channel + ")");
//        if (identifier < 0 || identifier > 127) throw new InvalidMessageException("Identifier out of bounds (" + identifier + ")");
//        if (data < 0 || data > 127) throw new InvalidMessageException("Data out of bounds (" + data + ")");

        this.channel = channel;
        this.identifier = identifier;
        this.data = data;
    }

    public int getChannel() {
        return channel;
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        return "[" + channel + ", " + identifier + ", " + data + "]";
    }

    public ShortMessage toShortMessage() throws InvalidMidiDataException {
        ShortMessage msg = new ShortMessage();
        msg.setMessage(getChannel(), getIdentifier(), getData());
        return msg;
    }

    public static Message fromMIDIMessage(MidiMessage message) throws InvalidMessageException {
        if (message.getLength() != 3) throw new InvalidMessageException("MIDIMessage contains invalid number of values");

        byte[] messageContents = message.getMessage();

        return new Message(messageContents[0], messageContents[1], messageContents[2]);
    }
}

