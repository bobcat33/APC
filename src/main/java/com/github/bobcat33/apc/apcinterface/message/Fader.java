package com.github.bobcat33.apc.apcinterface.message;

public class Fader extends Message {

    public Fader(int identifier, int data) throws InvalidMessageException {
        super(-80, identifier, data);
    }

    public static boolean isFader(Message message) {
        return message.getChannel() == -80;
    }

    public int getFaderNum() {
        return getIdentifier() + 1;
    }

    @Override
    public String toString() {
        return "Fader " + getFaderNum() + ": " + getData();
    }
}
