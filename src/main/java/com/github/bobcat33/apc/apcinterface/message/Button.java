package com.github.bobcat33.apc.apcinterface.message;

public class Button extends Message {
    public Button(int behaviour, int identifier, int data) throws InvalidMessageException {
        super(behaviour, identifier, data);
    }

    public Button(int behaviour, ButtonType type, int localIdentifier, int data) throws InvalidMessageException {
        super(behaviour, switch (type) {
            case PAD -> localIdentifier;
            case TRACK -> 0x64 + localIdentifier;
            case SCENE_LAUNCH -> 0x70 + localIdentifier;
            case SHIFT -> 0x7A;
        }, data);
    }

    public int getBehaviour() {
        return getChannel();
    }

    public int getLocalIdentifier() {
        return getLocalIdentifier(getIdentifier());
    }

    public static int getLocalIdentifier(int identifier) {
        if (identifier <= 0x3F) return identifier;
        if (identifier >= 0x64 && identifier <= 0x6B) return identifier - 0x64;
        if (identifier >= 0x70 && identifier <= 0x77) return identifier & 0x0F;
        return 0;
    }

    public ButtonType getButtonType() {
        return getButtonType(getIdentifier());
    }

    public static boolean isButton(Message message) {
        return message.getChannel() != -80;
    }

    public static ButtonType getButtonType(int identifier) {
        if (identifier >= 0x00 && identifier <= 0x3F) return ButtonType.PAD;
        if (identifier >= 0x64 && identifier <= 0x6B) return ButtonType.TRACK;
        if (identifier >= 0x70 && identifier <= 0x77) return ButtonType.SCENE_LAUNCH;
        return ButtonType.SHIFT;
    }

    @Override
    public String toString() {
        return getButtonType().toString() + " #" + getLocalIdentifier() + ": " + getData();
    }

    public static class In extends Button {
        public In(int behaviour, int identifier, int data) throws InvalidMessageException {
            super(behaviour, identifier, data);
        }

        public In(int behaviour, ButtonType type, int localIdentifier, int data) throws InvalidMessageException {
            super(behaviour, type, localIdentifier, data);
        }

        public In(int behaviour, int identifier, boolean down) throws InvalidMessageException {
            super(behaviour, identifier, down ? 0 : 127);
        }

        public In(int behaviour, ButtonType type, int localIdentifier, boolean down) throws InvalidMessageException {
            super(behaviour, type, localIdentifier, down ? 0 : 127);
        }

        public boolean isDown() {
            return getData() == 0;
        }

        public boolean isUp() {
            return !isDown();
        }

        @Override
        public String toString() {
            return "IN: " + getButtonType().toString() + " #" + getLocalIdentifier() + ": " + ((isDown()) ? "Is Down" : "Is Up");
        }
    }

    public static class Out extends Button {
        public Out(int behaviour, int identifier, int data) throws InvalidMessageException {
            super(behaviour, identifier, data);
        }

        public Out(int behaviour, ButtonType type, int localIdentifier, int data) throws InvalidMessageException {
            super(behaviour, type, localIdentifier, data);
        }

        public static Out createPadData(int behaviour, int localIdentifier, int colour) throws InvalidMessageException {
            return new Out(behaviour, ButtonType.PAD, localIdentifier, colour);
        }

        public static Out createUIButtonData(ButtonType type, int localIdentifier, int behaviour) throws InvalidMessageException {
            if (type == ButtonType.PAD) throw new InvalidMessageException();
            return new Out(0x90, type, localIdentifier, behaviour);
        }

        @Override
        public String toString() {
            return "OUT: " + super.toString() + (getButtonType().equals(ButtonType.PAD) ? " | " + getBehaviour() : "");
        }
    }
}
