package com.github.bobcat33.apc.programs.setup;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.mididevice.MidiDeviceManager;
import com.github.bobcat33.apc.layouts.AllOn;
import com.github.bobcat33.apc.listeners.ShiftStopListener;

import javax.sound.midi.MidiUnavailableException;

// FIXME WARNING THIS CLASS CAUSES PERMANENT HANGING, BLOCKS USB BUS
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting up with default IDs...");
        APCController controller = null;
        try {
            controller = MidiDeviceManager.createAPCController(6, 4); // FIXME CAUSES HANGING WHEN 6, 4 ARE USED AS PARAMETERS
            System.out.println("Default IDs successful.\nStarting controller...");
        } catch (MidiUnavailableException|ArrayIndexOutOfBoundsException ignored) {
            System.out.println("Failed to create APCController with default IDs, proceeding with manual creation.");
            while (controller == null) {
                try {
                    controller = MidiDeviceManager.createAPCController();
                    System.out.println("APCController manual creation successful.\nStarting controller...");
                } catch (MidiUnavailableException e) {
                    System.out.println("Failed to create APCController with the provided IDs, please try again.\nFull error: " + e.getMessage());
                }
            }
        }

        try {
            controller.start();
        } catch (MidiUnavailableException e) {
            System.out.println("Starting controller failed with exception: " + e.getMessage()
                    + "\nSetup exiting, please launch setup again to try again.");
            return;
        }

        System.out.println("Controller successfully started, proceeding with functionality tests.\n------------------------");
//        controller.close();
        testController(controller);
    }


    public static void testController(APCController controller) {
        System.out.println("Turning available LEDs on...");
        new AllOn().go(controller);
        System.out.println("All LEDs should now be on.\n------------------------\nStarting event listener...");

        controller.addListener(new ShiftStopListener(true));
        controller.addListener(new SetupInputListener());

        System.out.println("Event listener started.\n-> Press SHIFT on the APC at any time to end the test immediately.\n-> Press any section of buttons, if successfully connected the LEDs for that section should turn off.");
    }

}
