package com.github.bobcat33.apc.apcinterface.graphics;

import java.awt.*;
import java.util.*;

public class APCColour {

    /**
     * @deprecated temporarily deprecated until properly filled
     */
    private enum Name {
        OFF,                //   0: #000000
        ON,                 //   1: #1E1E1E
        GREY,               //   2: #7F7F7F
        WHITE,              //   3: #FFFFFF
        PALE_RED,           //   4: #FF4C4C
        RED,                //   5: #FF0000
        DIM_RED,            //   6: #590000
        DARK_RED,           //   7: #190000
        YELLOW,             //   8: #FFBD6C
        ORANGE,             //   9: #FF5400
        DIM_ORANGE,         //  10: #591D00
        DARK_ORANGE,        //  11: #271B00
        YELLOW_GREEN,       //  12: #FFFF4C
        BRIGHT_YELLOW,      //  13: #FFFF00
        DIM_YELLOW,         //  14: #595900
        DARK_YELLOW,        //  15: #191900
        PASTEL_GREEN,       //  16: #88FF4C
        BRIGHT_GREEN,       //  17: #54FF00
        DIM_BRIGHT_GREEN,   //  18: #1D5900
        DARK_BRIGHT_GREEN,  //  19: #142B00
        PALE_GREEN,         //  20: #4CFF4C
        GREEN,              //  21: #00FF00
        DIM_GREEN,          //  22: #005900
        DARK_GREEN,         //  23: #001900
        GREEN_1,            //  24: #4CFF5E
        GREEN_2,            //  25: #00FF19
        DIM_GREEN_2,        //  26: #00590D
        DARK_GREEN_2,       //  27: #001902
        GREEN_3,            //  28: #4CFF88
        GREEN_4,            //  29: #00FF55
        DIM_GREEN_4,        //  30: #00591D
        DARK_GREEN_4,       //  31: #001F12
        MINT_GREEN,         //  32: #4CFFB7
        GREEN_5,            //  33: #00FF99
        DIM_GREEN_5,        //  34: #005935
        DARK_GREEN_5,       //  35: #001912
        SKY_BLUE,           //  36: #4CC3FF
        DEEP_SKY_BLUE,      //  37: #00A9FF
        DIM_SKY_BLUE,       //  38: #004152
        DARK_SKY_BLUE,      //  39: #001019
        BLUE_2,             //  40: #4C88FF
        BLUE,               //  41: #0055FF
        DIM_BLUE,           //  42: #001D59
        DARK_BLUE,          //  43: #000819
        BLUE_3,             //  44: #4C4CFF
        SOLID_BLUE,         //  45: #0000FF
        DIM_SOLID_BLUE,     //  46: #000059
        DARK_SOLID_BLUE,    //  47: #000019
        VIOLET,             //  48: #874CFF
        BLUE_4,             //  49: #5400FF
        DIM_BLUE_4,         //  50: #190064
        DIM_BLUE_5,         //  51: #0F0030
        MAGENTA,            //  52: #FF4CFF
        SOLID_MAGENTA,      //  53: #FF00FF
        DIM_MAGENTA,        //  54: #590059
        DARK_MAGENTA,       //  55: #190019
        HOT_PINK,           //  56: #FF4C87
        RED_PINK,           //  57: #FF0054
        DIM_RED_PINK,       //  58: #59001D
        DARK_RED_PINK,      //  59: #220013
        SUNSET_ORANGE,      //  60: #FF1500
        RED_BROWN,          //  61: #993500
        BROWN,              //  62: #795100
        CAMO_GREEN,         //  63: #436400
        DIM_CAMO_GREEN,     //  64: #033900
        PALE_DIM_CAMO_GREEN,//  65: #005735
        INDIGO_BLUE,        //  66: #00547F
        SOLID_BLUE_COPY,    //  67: #0000FF
        DIM_INDIGO_BLUE,    //  68: #00454F
        BLUE_5,             //  69: #2500CC
        GRAY,               //  70: #7F7F7F
        DARK_GREY,          //  71: #202020
        SOLID_RED,          //  72: #FF0000
        LIGHT_PEA_GREEN,    //  73: #BDFF2D
        CHARTREUSE_GREEN,   //  74: #AFED06
        LIME_GREEN,         //  75: #64FF09
        DIM_LIME_GREEN,     //  76: #108B00
        MINT_GREEN_2,       //  77: #00FF87
        DEEP_SKY_BLUE_COPY, //  78: #00A9FF
        DEEPER_SKY_BLUE,    //  79: #002AFF
    }

    /*
        Colours accepted by APC and their associated velocity:
          0: #000000
          1: #1E1E1E
          2: #7F7F7F
          3: #FFFFFF
          4: #FF4C4C
          5: #FF0000
          6: #590000
          7: #190000
          8: #FFBD6C
          9: #FF5400
         10: #591D00
         11: #271B00
         12: #FFFF4C
         13: #FFFF00
         14: #595900
         15: #191900
         16: #88FF4C
         17: #54FF00
         18: #1D5900
         19: #142B00
         20: #4CFF4C
         21: #00FF00
         22: #005900
         23: #001900
         24: #4CFF5E
         25: #00FF19
         26: #00590D
         27: #001902
         28: #4CFF88
         29: #00FF55
         30: #00591D
         31: #001F12
         32: #4CFFB7
         33: #00FF99
         34: #005935
         35: #001912
         36: #4CC3FF
         37: #00A9FF
         38: #004152
         39: #001019
         40: #4C88FF
         41: #0055FF
         42: #001D59
         43: #000819
         44: #4C4CFF
         45: #0000FF
         46: #000059
         47: #000019
         48: #874CFF
         49: #5400FF
         50: #190064
         51: #0F0030
         52: #FF4CFF
         53: #FF00FF
         54: #590059
         55: #190019
         56: #FF4C87
         57: #FF0054
         58: #59001D
         59: #220013
         60: #FF1500
         61: #993500
         62: #795100
         63: #436400
         64: #033900
         65: #005735
         66: #00547F
         67: #0000FF
         68: #00454F
         69: #2500CC
         70: #7F7F7F
         71: #202020
         72: #FF0000
         73: #BDFF2D
         74: #AFED06
         75: #64FF09
         76: #108B00
         77: #00FF87
         78: #00A9FF
         79: #002AFF
         80: #3F00FF
         81: #7A00FF
         82: #B21A7D
         83: #402100
         84: #FF4A00
         85: #88E106
         86: #72FF15
         87: #00FF00
         88: #3BFF26
         89: #59FF71
         90: #38FFCC
         91: #5B8AFF
         92: #3151C6
         93: #877FE9
         94: #D31DFF
         95: #FF005D
         96: #FF7F00
         97: #B9B000
         98: #90FF00
         99: #835D07
        100: #392b00
        101: #144C10
        102: #0D5038
        103: #15152A
        104: #16205A
        105: #693C1C
        106: #A8000A
        107: #DE513D
        108: #D86A1C
        109: #FFE126
        110: #9EE12F
        111: #67B50F
        112: #1E1E30
        113: #DCFF6B
        114: #80FFBD
        115: #9A99FF
        116: #8E66FF
        117: #404040
        118: #757575
        119: #E0FFFF
        120: #A00000
        121: #350000
        122: #1AD000
        123: #074200
        124: #B9B000
        125: #3F3100
        126: #B35F00
        127: #4B1502

     */

    private static final HashMap<Integer, Integer> availableColours = new HashMap<>();

    // Add all available APC colours to internal map
    static {
        availableColours.put(0x000000,   0);
        availableColours.put(0x1E1E1E,   1);
        availableColours.put(0x7F7F7F,   2);
        availableColours.put(0xFFFFFF,   3);
        availableColours.put(0xFF4C4C,   4);
        availableColours.put(0xFF0000,   5);
        availableColours.put(0x590000,   6);
        availableColours.put(0x190000,   7);
        availableColours.put(0xFFBD6C,   8);
        availableColours.put(0xFF5400,   9);
        availableColours.put(0x591D00,  10);
        availableColours.put(0x271B00,  11);
        availableColours.put(0xFFFF4C,  12);
        availableColours.put(0xFFFF00,  13);
        availableColours.put(0x595900,  14);
        availableColours.put(0x191900,  15);
        availableColours.put(0x88FF4C,  16);
        availableColours.put(0x54FF00,  17);
        availableColours.put(0x1D5900,  18);
        availableColours.put(0x142B00,  19);
        availableColours.put(0x4CFF4C,  20);
        availableColours.put(0x00FF00,  21);
        availableColours.put(0x005900,  22);
        availableColours.put(0x001900,  23);
        availableColours.put(0x4CFF5E,  24);
        availableColours.put(0x00FF19,  25);
        availableColours.put(0x00590D,  26);
        availableColours.put(0x001902,  27);
        availableColours.put(0x4CFF88,  28);
        availableColours.put(0x00FF55,  29);
        availableColours.put(0x00591D,  30);
        availableColours.put(0x001F12,  31);
        availableColours.put(0x4CFFB7,  32);
        availableColours.put(0x00FF99,  33);
        availableColours.put(0x005935,  34);
        availableColours.put(0x001912,  35);
        availableColours.put(0x4CC3FF,  36);
        availableColours.put(0x00A9FF,  37);
        availableColours.put(0x004152,  38);
        availableColours.put(0x001019,  39);
        availableColours.put(0x4C88FF,  40);
        availableColours.put(0x0055FF,  41);
        availableColours.put(0x001D59,  42);
        availableColours.put(0x000819,  43);
        availableColours.put(0x4C4CFF,  44);
        availableColours.put(0x0000FF,  45);
        availableColours.put(0x000059,  46);
        availableColours.put(0x000019,  47);
        availableColours.put(0x874CFF,  48);
        availableColours.put(0x5400FF,  49);
        availableColours.put(0x190064,  50);
        availableColours.put(0x0F0030,  51);
        availableColours.put(0xFF4CFF,  52);
        availableColours.put(0xFF00FF,  53);
        availableColours.put(0x590059,  54);
        availableColours.put(0x190019,  55);
        availableColours.put(0xFF4C87,  56);
        availableColours.put(0xFF0054,  57);
        availableColours.put(0x59001D,  58);
        availableColours.put(0x220013,  59);
        availableColours.put(0xFF1500,  60);
        availableColours.put(0x993500,  61);
        availableColours.put(0x795100,  62);
        availableColours.put(0x436400,  63);
        availableColours.put(0x033900,  64);
        availableColours.put(0x005735,  65);
        availableColours.put(0x00547F,  66);
        availableColours.put(0x00454F,  68);
        availableColours.put(0x2500CC,  69);
        availableColours.put(0x202020,  71);
        availableColours.put(0xBDFF2D,  73);
        availableColours.put(0xAFED06,  74);
        availableColours.put(0x64FF09,  75);
        availableColours.put(0x108B00,  76);
        availableColours.put(0x00FF87,  77);
        availableColours.put(0x002AFF,  79);
        availableColours.put(0x3F00FF,  80);
        availableColours.put(0x7A00FF,  81);
        availableColours.put(0xB21A7D,  82);
        availableColours.put(0x402100,  83);
        availableColours.put(0xFF4A00,  84);
        availableColours.put(0x88E106,  85);
        availableColours.put(0x72FF15,  86);
        availableColours.put(0x3BFF26,  88);
        availableColours.put(0x59FF71,  89);
        availableColours.put(0x38FFCC,  90);
        availableColours.put(0x5B8AFF,  91);
        availableColours.put(0x3151C6,  92);
        availableColours.put(0x877FE9,  93);
        availableColours.put(0xD31DFF,  94);
        availableColours.put(0xFF005D,  95);
        availableColours.put(0xFF7F00,  96);
        availableColours.put(0xB9B000,  97);
        availableColours.put(0x90FF00,  98);
        availableColours.put(0x835D07,  99);
        availableColours.put(0x392b00, 100);
        availableColours.put(0x144C10, 101);
        availableColours.put(0x0D5038, 102);
        availableColours.put(0x15152A, 103);
        availableColours.put(0x16205A, 104);
        availableColours.put(0x693C1C, 105);
        availableColours.put(0xA8000A, 106);
        availableColours.put(0xDE513D, 107);
        availableColours.put(0xD86A1C, 108);
        availableColours.put(0xFFE126, 109);
        availableColours.put(0x9EE12F, 110);
        availableColours.put(0x67B50F, 111);
        availableColours.put(0x1E1E30, 112);
        availableColours.put(0xDCFF6B, 113);
        availableColours.put(0x80FFBD, 114);
        availableColours.put(0x9A99FF, 115);
        availableColours.put(0x8E66FF, 116);
        availableColours.put(0x404040, 117);
        availableColours.put(0x757575, 118);
        availableColours.put(0xE0FFFF, 119);
        availableColours.put(0xA00000, 120);
        availableColours.put(0x350000, 121);
        availableColours.put(0x1AD000, 122);
        availableColours.put(0x074200, 123);
        availableColours.put(0x3F3100, 125);
        availableColours.put(0xB35F00, 126);
        availableColours.put(0x4B1502, 127);
    }

    public static int getClosestColour(Color colour) {
        int matchedColour = 0;
        double matchedColourDistance = Double.MAX_VALUE;
        double colourDistance;

        for (Map.Entry<Integer, Integer> e : availableColours.entrySet()) {
            colourDistance = colourDistance(colour, e.getKey());
//            System.out.println(e.getKey());
            if (colourDistance < matchedColourDistance) {
                matchedColourDistance = colourDistance;
                matchedColour = e.getValue();
            }
        }

        return matchedColour;
    }

    /**
     * Taken from <a href="https://stackoverflow.com/questions/6334311/whats-the-best-way-to-round-a-color-object-to-the-nearest-color-constant">stack overflow</a>
     * @param colour colour to compare against
     * @param hex hex code to compare against
     * @return the distance between the colour and the hex colour
     */
    private static double colourDistance(Color colour, Integer hex) {
        int hexR = hex >>> 16;
        int hexG = (0x00FF00 & hex) >>> 8;
        int hexB = 0x0000FF & hex;

        int red1 = colour.getRed();
        int rmean = (red1 + hexR) >> 1;
        int r = red1 - hexR;
        int g = colour.getGreen() - hexG;
        int b = colour.getBlue() - hexB;
        return (((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8);
    }

}
