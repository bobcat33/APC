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

    public Button at(int identifier) {
        return new Button(getBehaviour(), getButtonType(), identifier, getData());
    }

    public Button as(ButtonType type) {
        if (getButtonType().equals(type)) return new Button(getBehaviour(), getIdentifier(), getData());
        if (getButtonType().equals(ButtonType.PAD)) {
            if (getData() == 0) return new Button(0x90, type, 0, 0);
            if (getBehaviour() <= 0x96) return new Button(0x90, type, 0, 1);
            return new Button(0x90, type, 0, 2);
        }
        if (type.equals(ButtonType.PAD)) {
            if (getData() == 0) return new Button(0x90, 0, 0);
            int colour = (getButtonType().equals(ButtonType.SCENE_LAUNCH)) ? 21 : 5; // Green if scene launch is copied, red otherwise
            if (getData() == 1) return new Button(0x96, 0, colour);
            return new Button(0x9F, 0, colour); // Blinking 1/2
        }
        return new Button(getBehaviour(), type, 0, getData());
    }

    public Button copy() {
        return new Button(getBehaviour(), getIdentifier(), getData());
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

        public boolean isUp() {
            return getData() == 0;
        }

        public boolean isDown() {
            return !isUp();
        }

        @Override
        public String toString() {
            return "IN: " + getButtonType().toString() + " #" + getLocalIdentifier() + ": " + ((isDown()) ? "Is Down" : "Is Up");
        }
    }

    public static class Out {
        public static Button createPadData(int behaviour, int localIdentifier, int colour) throws InvalidMessageException {
            return new Button(behaviour, ButtonType.PAD, localIdentifier, colour);
        }

        public static Button createUIButtonData(ButtonType type, int localIdentifier, int behaviour) throws InvalidMessageException {
            if (type == ButtonType.PAD) throw new InvalidMessageException();
            return new Button(0x90, type, localIdentifier, behaviour);
        }
    }
}
