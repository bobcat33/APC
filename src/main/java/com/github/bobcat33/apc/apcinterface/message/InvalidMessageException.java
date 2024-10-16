package com.github.bobcat33.apc.apcinterface.message;

public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException() {
        super("Invalid message parameters");
    }

    public InvalidMessageException(String str) {
        super("Invalid message: " + str);
    }

    public InvalidMessageException(Message m) {
        super("Invalid message data for message " + m.toString());
    }
}
