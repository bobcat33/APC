package com.github.bobcat33.osc;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCMessageInfo;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.OSCSerializerAndParserBuilder;
import com.illposed.osc.transport.NetworkProtocol;
import com.illposed.osc.transport.OSCPortOut;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class OSCTransmitter {

    private final OSCPortOut sender;

    public OSCTransmitter() throws IOException {

        InetSocketAddress destination = new InetSocketAddress("127.0.0.1", 8000);
        InetSocketAddress source = new InetSocketAddress("127.0.0.1", 8001);
        sender = new OSCPortOut(new OSCSerializerAndParserBuilder(), destination, source, NetworkProtocol.UDP);
        createFaderBanks();
    }

    private void createFaderBanks() {
        sendShort("/fader/1/config/10");
        sendShort("/fader/2/config/2/10");
        sendShort("/fader/3/config/3/10");
        sendShort("/fader/4/config/4/10");
        sendShort("/fader/5/config/5/10");
        sendShort("/fader/6/config/6/10");
        sendShort("/fader/7/config/7/10");
        sendShort("/fader/8/config/8/10");
        sendShort("/fader/9/config/9/10");
    }


    /**
     * Send an OSC string to EOS
     * @param data the OSC data to send to EOS, with an equals sign separating the path from the data
     */
    public void send(String data) {
        String[] dataParts = data.split("=");
        List<Object> dataArray = new ArrayList<>();
        if (dataParts.length == 2) {
            for (String part : dataParts[1].split(",")) {
                dataArray.add(part.trim());
            }
        }
        try {
            sender.send(new OSCMessage(dataParts[0], dataArray));
        } catch (OSCSerializeException | IOException e) {
            System.out.println("WARN: failed to execute OSC command \"" + data + "\"\nError: " + e.getMessage());
        }
    }

    /**
     * Adds "/eos/user/1" to the start of the data string passed to {@link #send(String)}
     * @param data the remaining OSC data to send to EOS, with an equals sign separating the path from the data
     */
    public void sendShort(String data) {
        send("/eos/user/1" + data);
    }

    public void sendChannelIntensity(double channelNum, double intensity) {
        sendShort("/chan/" + channelNum + "=" + intensity);
    }


    public void sendSubLevel(int sub, double level) {
        sendShort("/sub/" + sub + "=" + (level / 100));
    }

    public void sendFaderLevel(int fader, double level) {
        sendShort("/fader/1/" + fader + "=" + (level / 100d));
    }

    public void flashFaderOn(int fader) {
        flashFaderOn(1, fader);
    }

    public void flashFaderOut(int fader) {
        flashFaderOut(1, fader);
    }

    public void flashFaderOn(int page, int fader) {
        sendShort("/fader/" + page + "/" + fader + "/fire=1.0");
    }

    public void flashFaderOut(int page, int fader) {
        sendShort("/fader/" + page + "/" + fader + "/fire=0.0");
    }

    public void flashSub(int sub) {
        sendShort("/sub/" + sub + "/fire");
    }

    public void flashSubOn(int sub) {
        sendShort("/sub/" + sub + "/fire=1.0");
    }

    public void flashSubOff(int sub) {
        sendShort("/sub/" + sub + "/fire=0.0");
    }

    public void clearCmd() {
        sendShort("/key/clear_cmdline"); // TODO may have issues
    }

    public void sendBlackoutToggle() {
        sendShort("/key/blackout");
    }
}
