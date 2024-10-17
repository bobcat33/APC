package com.github.bobcat33.apc.animations;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;

import java.util.ArrayList;
import java.util.ListIterator;

public class SpreadFromPointAnimation {
    private static final int MOVE_DELAY_MILLIS = 100;

    private APCController ctrl;
    private int behaviour, colour;

    private final ArrayList<Button> up = new ArrayList<>(), down = new ArrayList<>();
    private Button left, right;

    public SpreadFromPointAnimation(APCController ctrl) {
        this(ctrl, 0);
    }

    public SpreadFromPointAnimation(APCController ctrl, int colour) {
        this.ctrl = ctrl;
        this.colour = colour;
        if (this.colour == 0) this.behaviour = 0x90;
        else this.behaviour = 0x96;
    }

    public void start(Button button) throws InvalidMessageException, InterruptedException {
        ctrl.output(Button.Out.createPadData(behaviour, button.getLocalIdentifier(), colour));
        left = button;
        right = button;
        // Add up and down if possible
        if (right.getLocalIdentifier() + 8 <= 63)
            up.add(Button.Out.createPadData(behaviour, right.getLocalIdentifier() + 8, colour));
        if (left.getLocalIdentifier() - 8 >= 0)
            down.add(Button.Out.createPadData(behaviour, left.getLocalIdentifier() - 8, colour));
        // Move left and right if possible
        if ((right.getLocalIdentifier()+1) % 8 != 0)
            right = Button.Out.createPadData(behaviour, right.getLocalIdentifier() + 1, colour);
        else right = null;
        if (left.getLocalIdentifier() % 8 != 0)
            left = Button.Out.createPadData(behaviour, left.getLocalIdentifier() - 1, colour);
        else left = null;
        Thread.sleep(MOVE_DELAY_MILLIS);
        triggerButtons();


        while (!moveButtons()) {
            Thread.sleep(MOVE_DELAY_MILLIS);
            triggerButtons();
        }
    }

    public boolean moveButtons() throws InvalidMessageException {
        boolean noMovement = true;
        ListIterator<Button> it = up.listIterator();
        while (it.hasNext()) {
            Button button = it.next();
            int nextPos = button.getLocalIdentifier() + 8;
            if (nextPos <= 63) {
                it.set(Button.Out.createPadData(behaviour, nextPos, colour));
                noMovement = false;
            }
            else it.remove();
        }

        it = down.listIterator();
        while (it.hasNext()) {
            Button button = it.next();
            int nextPos = button.getLocalIdentifier() - 8;
            if (nextPos >= 0) {
                it.set(Button.Out.createPadData(behaviour, nextPos, colour));
                noMovement = false;
            }
            else it.remove();
        }

        if (right != null && right.getLocalIdentifier() + 8 <= 63)
            up.add(Button.Out.createPadData(behaviour, right.getLocalIdentifier() + 8, colour));
        if (left != null && left.getLocalIdentifier() + 8 <= 63)
            up.add(Button.Out.createPadData(behaviour, left.getLocalIdentifier() + 8, colour));

        if (right != null && right.getLocalIdentifier() - 8 >= 0)
            down.add(Button.Out.createPadData(behaviour, right.getLocalIdentifier() - 8, colour));
        if (left != null && left.getLocalIdentifier() - 8 >= 0)
            down.add(Button.Out.createPadData(behaviour, left.getLocalIdentifier() - 8, colour));

        if (right != null && (right.getLocalIdentifier()+1) % 8 != 0) {
            right = Button.Out.createPadData(behaviour, right.getLocalIdentifier() + 1, colour);
            noMovement = false;
        }
        else right = null;

        if (left != null && left.getLocalIdentifier() % 8 != 0) {
            left = Button.Out.createPadData(behaviour, left.getLocalIdentifier() - 1, colour);
            noMovement = false;
        }
        else left = null;

        return noMovement;
    }

    public void triggerButtons() {
        for (Button button : up) ctrl.output(button);
        for (Button button : down) ctrl.output(button);
        if (left != null) ctrl.output(left);
        if (right != null) ctrl.output(right);
    }
}
