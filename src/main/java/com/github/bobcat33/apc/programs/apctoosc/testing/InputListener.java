package com.github.bobcat33.apc.programs.apctoosc.testing;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.listener.APCButtonFaderEventListener;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;
import com.github.bobcat33.apc.apcinterface.message.Fader;
import com.github.bobcat33.osc.OSCTransmitter;

public class InputListener extends APCButtonFaderEventListener {

    private final OSCTransmitter oscTransmitter;
    private boolean[] selected = new boolean[8];
    private boolean selecting = true;

    public InputListener(OSCTransmitter oscTransmitter) {
        this.oscTransmitter = oscTransmitter;
    }

    @Override
    public void onButtonDown(APCController controller, Button button) {
        System.out.println("button down: " + button.toString());

    }

    @Override
    public void onButtonUp(APCController controller, Button button) {
        System.out.println("button up: " + button.toString());

        if (button.getButtonType().equals(ButtonType.TRACK)) {
            // If the user wasn't currently selecting a new set reset the selection
            if (!selecting) {
                resetSelection(controller);
                return; // TODO temporary solution to issue where user selects a channel, changes colour, then if they reselect that channel it is loaded as -<channel> rather that +<channel>
                // Temp solution requires 2 presses, 1 to clear selection and the second press to select
            }

            // If the channel is already selected
            if (selected[button.getLocalIdentifier()]) {
                // Unselect on the client
                selected[button.getLocalIdentifier()] = false;

                // Subtract from command line
                oscTransmitter.sendShort("/chan=" + (button.getLocalIdentifier() + 1));

                // Turn off the track button LED
                controller.outputToButton(ButtonType.TRACK, button.getLocalIdentifier(), UIButtonBehaviour.OFF);

                // If nothing is being selected anymore clear command line and stop selection process
                if (!isSelecting()) {
                    selecting = false;
                    oscTransmitter.clearCmd();
                }
            } else {

                // Add selection to command line
                oscTransmitter.sendShort("/chan=" + (button.getLocalIdentifier() + 1));

                // Turn track button LED on
                controller.outputToButton(ButtonType.TRACK, button.getLocalIdentifier(), UIButtonBehaviour.ON);

                // Set channel to selected on the client
                selected[button.getLocalIdentifier()] = true;
            }
        }

        else {
            selecting = false;
            updateTrackToBlinking(controller);
            if (button.getButtonType().equals(ButtonType.PAD)) {
                if (button.getLocalIdentifier() < 8) {
                    switch (button.getLocalIdentifier()) {
                        case 0 -> {
                            oscTransmitter.sendShort("/param/green=0");
                            oscTransmitter.sendShort("/param/blue=0");
                            oscTransmitter.sendShort("/param/red=255");
                        }
                        case 1 -> {
                            oscTransmitter.sendShort("/param/blue=0");
                            oscTransmitter.sendShort("/param/red=0");
                            oscTransmitter.sendShort("/param/green=255");
                        }
                        case 2 -> {
                            oscTransmitter.sendShort("/param/red=0");
                            oscTransmitter.sendShort("/param/green=0");
                            oscTransmitter.sendShort("/param/blue=255");
                        }
                        case 3 -> {
                            oscTransmitter.sendShort("/param/red=255");
                            oscTransmitter.sendShort("/param/green=255");
                            oscTransmitter.sendShort("/param/blue=255");
                            oscTransmitter.sendShort("/fx=917");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onFaderMove(APCController controller, Fader fader) {
        oscTransmitter.sendChannelIntensity(fader.getFaderNum(), fader.getPercentage());
    }

    @Override
    public void onClose() {
        
    }


    /**
     * @return true if anything is currently being selected
     */
    private boolean isSelecting() {
        for (boolean b : selected) {
            if (b) return true;
        }
        return false;
    }

    /**
     * Update all track buttons that have been selected to blink
     */
    private void updateTrackToBlinking(APCController controller) {
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                controller.outputToButton(ButtonType.TRACK, i, UIButtonBehaviour.BLINK);
            }
        }
    }

    /**
     * Turn all track buttons off
     */
    private void updateTrackToOff(APCController controller) {
        for (int i = 0; i < selected.length; i++) {
            controller.outputToButton(ButtonType.TRACK, i, UIButtonBehaviour.OFF);
        }
    }

    /**
     * Clear command line, set selecting to tru and turn off track button LEDs
     */
    private void resetSelection(APCController controller) {
        oscTransmitter.clearCmd();
        selecting = true;
        updateTrackToOff(controller);
        selected = new boolean[8];
    }

    /**
     * Clear command line and reselect previously selected values
     */
    private void reselect() {
        oscTransmitter.clearCmd();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                oscTransmitter.sendShort("/chan=" + (i + 1));
            }
        }
    }
}
