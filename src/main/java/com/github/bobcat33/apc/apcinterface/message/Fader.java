package com.github.bobcat33.apc.apcinterface.message;

public class Fader extends Message {

    public Fader(int identifier, int data) throws InvalidMessageException {
        super(-80, identifier, data);
    }

    public static boolean isFader(Message message) {
        return message.getChannel() == -80;
    }

    public int getFaderNum() {
        return super.getIdentifier()-47;
    }

    public int getStandardLevel() {
        return (int) ((getData()/127d)*255);
    }

    public int getPercentage() {
        return (int) ((getData()/127d)*100);
    }

    public static Fader fromMessage(Message message) {
        return new Fader(message.getIdentifier(), message.getData());
    }

    @Override
    public String toString() {
        return "Fader " + getFaderNum() + ": " + getData();
    }
}
