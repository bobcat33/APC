package com.github.bobcat33.apc.apcinterface.mididevice;

import com.github.bobcat33.apc.apcinterface.APCController;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.util.Scanner;

public class MidiDeviceManager {

    public static APCController createAPCController() throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (int i = 0; i < infos.length; i++) {
            MidiDevice.Info info = infos[i];
            System.out.println("--------\nID: " + i + "\nName: " + info.getName());
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter ID of device to read from: ");
        int from;
        while ((from = scanner.nextInt()) > infos.length || from < 0) System.out.print("Invalid ID, please try again: ");

        System.out.print("Enter ID of device to send to: ");
        int to;
        while ((to = scanner.nextInt()) > infos.length || to < 0) System.out.print("Invalid ID, please try again: ");

        scanner.close();

        return createAPCController(from, to);
    }

    public static APCController createAPCController(int inputDeviceID, int outputDeviceID) throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        MidiDevice inputDevice = MidiSystem.getMidiDevice(infos[inputDeviceID]);
        MidiDevice outputDevice = MidiSystem.getMidiDevice(infos[outputDeviceID]);

        return new APCController(inputDevice, outputDevice);
    }

}
