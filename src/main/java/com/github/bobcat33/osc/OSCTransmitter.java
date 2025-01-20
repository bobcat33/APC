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



    public void clearCmd() {
        System.out.println("Hi");
        sendShort("/key/chan");
        sendShort("/key/clear_cmdline");
    }

    public void setGrandMaster(double value) {
        // TODO get this sorted
    }
}
