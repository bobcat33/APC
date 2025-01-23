package com.github.bobcat33.apc.layouts;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.Layout;
import com.github.bobcat33.apc.apcinterface.graphics.UIButtonBehaviour;
import com.github.bobcat33.apc.apcinterface.message.Button;
import com.github.bobcat33.apc.apcinterface.message.ButtonType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileLayout implements Layout {
    private static final String dataDelimiter = ",", padLinePrefix = "PAD #", trackLinePrefix = "TRACK #", sceneLinePrefix = "SCENE #";

    private final File file;
    private final HashMap<Integer, Button> padMap; // Store button so that support for different LED behaviour will be available later
    private final HashMap<Integer, UIButtonBehaviour> trackMap, sceneMap;


    public FileLayout(File layoutFile) {
        this.file = layoutFile;
        this.padMap = new HashMap<>();
        this.trackMap = new HashMap<>();
        this.sceneMap = new HashMap<>();
    }

    @Override
    public void go(APCController controller) {
        padMap.clear();
        trackMap.clear();
        try {
            padMap.putAll(loadPadDataFromFile(file));
            trackMap.putAll(loadTrackButtonBehaviourFromFile(file));
            sceneMap.putAll(loadSceneButtonBehaviourFromFile(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Display pad colours
        for (int i = 0; i < 64; i++) {
            Button button = padMap.get(i);
            controller.output(Objects.requireNonNullElse(button, new Button(0x90, i, 0)));
        }
        // Display track and scene behaviours
        for (int i = 0; i < 8; i++) {
            UIButtonBehaviour trackBehaviour = trackMap.get(i);
            UIButtonBehaviour sceneBehaviour = sceneMap.get(i);
            controller.outputToButton(ButtonType.TRACK, i, Objects.requireNonNullElse(trackBehaviour, UIButtonBehaviour.OFF));
            controller.outputToButton(ButtonType.SCENE_LAUNCH, i, Objects.requireNonNullElse(sceneBehaviour, UIButtonBehaviour.OFF));
        }
    }

    public static HashMap<Integer, Button> loadPadDataFromFile(File file) throws FileNotFoundException {
        if (file == null) throw new FileNotFoundException(); // TODO log error
        Scanner fileScanner;
        fileScanner = new Scanner(file);

        HashMap<Integer, Button> pads = new HashMap<>();

        int fileLineNum = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            fileLineNum++;

            if (!line.startsWith(padLinePrefix)) continue;

            String[] lineData = line.substring(padLinePrefix.length()).split(dataDelimiter);

            if (lineData.length != 3) continue; // TODO show error

            // Remove whitespace
            trimParts(lineData);

            // Load data
            int buttonId = Integer.parseInt(lineData[0]);
            int behaviour = Integer.parseInt(lineData[1]);
            int data = Integer.parseInt(lineData[2]);

            pads.put(buttonId, new Button(behaviour, ButtonType.PAD, buttonId, data));
        }
        fileScanner.close();

        return pads;
    }

    public static HashMap<Integer, UIButtonBehaviour> loadTrackButtonBehaviourFromFile(File file) throws FileNotFoundException {
        if (file == null) throw new FileNotFoundException(); // TODO log error
        Scanner fileScanner;
        fileScanner = new Scanner(file);

        HashMap<Integer, UIButtonBehaviour> trackButtons = new HashMap<>();

        int fileLineNum = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            fileLineNum++;

            if (!line.startsWith(trackLinePrefix)) continue;

            String[] lineData = line.substring(trackLinePrefix.length()).split(dataDelimiter);

            if (lineData.length != 2) continue; // TODO show error

            // Remove whitespace
            trimParts(lineData);

            // Load data
            int buttonId = Integer.parseInt(lineData[0]);
            UIButtonBehaviour behaviour = UIButtonBehaviour.from(Integer.parseInt(lineData[1]));

            trackButtons.put(buttonId, behaviour);
        }
        fileScanner.close();

        return trackButtons;
    }

    public static HashMap<Integer, UIButtonBehaviour> loadSceneButtonBehaviourFromFile(File file) throws FileNotFoundException {
        if (file == null) throw new FileNotFoundException(); // TODO log error
        Scanner fileScanner;
        fileScanner = new Scanner(file);

        HashMap<Integer, UIButtonBehaviour> sceneButtons = new HashMap<>();

        int fileLineNum = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            fileLineNum++;

            if (!line.startsWith(sceneLinePrefix)) continue;

            String[] lineData = line.substring(sceneLinePrefix.length()).split(dataDelimiter);

            if (lineData.length != 2) continue; // TODO show error

            // Remove whitespace
            trimParts(lineData);

            // Load data
            int buttonId = Integer.parseInt(lineData[0]);
            UIButtonBehaviour behaviour = UIButtonBehaviour.from(Integer.parseInt(lineData[1]));

            sceneButtons.put(buttonId, behaviour);
        }
        fileScanner.close();

        return sceneButtons;
    }

    /**
     * Remove leading and trailing whitespace from all strings in an array.
     * @param parts the array to trim
     */
    private static void trimParts(String[] parts) {
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
    }

    public static void storeToFile(File file, HashMap<Integer, Button> pads, HashMap<Integer, UIButtonBehaviour> tracks, HashMap<Integer, UIButtonBehaviour> scenes) throws IOException {
        FileWriter fileWriter;

        fileWriter = new FileWriter(file);

        for (Map.Entry<Integer, Button> pad : pads.entrySet()) {
            fileWriter.write(padDataToFileLine(pad.getKey(), pad.getValue()));
        }

        for (Map.Entry<Integer, UIButtonBehaviour> track : tracks.entrySet()) {
            fileWriter.write(trackDataToFileLine(track.getKey(), track.getValue()));
        }

        for (Map.Entry<Integer, UIButtonBehaviour> scene : scenes.entrySet()) {
            fileWriter.write(sceneDataToFileLine(scene.getKey(), scene.getValue()));
        }

        fileWriter.close();
    }

    private static String padDataToFileLine(Integer key, Button button) {
        return padLinePrefix + key + dataDelimiter + button.getBehaviour() + dataDelimiter + button.getData() + "\n";
    }

    private static String trackDataToFileLine(Integer key, UIButtonBehaviour behaviour) {
        return trackLinePrefix + key + dataDelimiter + behaviour.ordinal() + "\n";
    }

    private static String sceneDataToFileLine(Integer key, UIButtonBehaviour behaviour) {
        return sceneLinePrefix + key + dataDelimiter + behaviour.ordinal() + "\n";
    }
}
