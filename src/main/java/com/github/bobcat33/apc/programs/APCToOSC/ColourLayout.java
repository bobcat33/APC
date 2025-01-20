package com.github.bobcat33.apc.programs.APCToOSC;

import com.github.bobcat33.apc.apcinterface.APCController;
import com.github.bobcat33.apc.apcinterface.graphics.APCColour;
import com.github.bobcat33.apc.apcinterface.graphics.Layout;

import java.awt.*;

public class ColourLayout implements Layout {
    @Override
    public void go(APCController controller) {
        controller.outputToButton(0, APCColour.getClosestColour(Color.RED));
        controller.outputToButton(1, APCColour.getClosestColour(Color.GREEN));
        controller.outputToButton(2, APCColour.getClosestColour(Color.BLUE));
    }
}
