package com.github.bobcat33.apc.programs.test;

import com.github.bobcat33.apc.apcinterface.message.APCColour;

import java.awt.*;
import java.util.Scanner;

public class MainTest {

    public static void main(String[] args) {
        System.out.println(APCColour.getClosestColour(Color.decode("#4b60a3")));
    }

   /* public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a value to convert: ");
        String lineIn;
        while (!(lineIn = input.nextLine()).equalsIgnoreCase("q")) {
            int v;
            try {
                v = Integer.parseInt(lineIn);
                System.out.println("Percentage: " + getPercentage(v) + "%");
                System.out.println("Level: " + getStandardLevel(v));
            } catch (NumberFormatException ignored) {}
            System.out.print("Enter a value to convert: ");
        }
    }

    private static int getStandardLevel(int value) {
        return (int) ((value/127d)*255);
    }

    private static int getPercentage(int value) {
        return (int) ((value/127d)*100);
    }*/

}
