package com.github.bobcat33.apc.animations;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.Animation;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.InvalidMessageException;

import java.util.ArrayList;
import java.util.ListIterator;

public class SpreadFromPointAnimation extends Animation {
    private static final int MOVE_DELAY_MILLIS = 100;

    private final int behaviour = 0x96, colour;

    private final Button button;

    private final ArrayList<Button> up = new ArrayList<>(), down = new ArrayList<>();
    private Button left, right;

    public SpreadFromPointAnimation(APCController controller) {
        this(controller, new Button(0x90, 7, 0));
    }

    public SpreadFromPointAnimation(APCController controller, Button button) {
        this(controller, button.getData(), button);
    }

    public SpreadFromPointAnimation(APCController ctrl, int colour, Button button) {
        super(ctrl);
        this.colour = colour;
        this.button = button;
    }

    @Override
    protected void start() throws InvalidMessageException {
        controller.output(Button.Out.createPadData(behaviour, button.getLocalIdentifier(), colour));
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
        sleep(MOVE_DELAY_MILLIS);
        triggerButtons();


        while (!moveButtons()) {
            sleep(MOVE_DELAY_MILLIS);
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
        for (Button button : up) controller.output(button);
        for (Button button : down) controller.output(button);
        if (left != null) controller.output(left);
        if (right != null) controller.output(right);
    }
}
