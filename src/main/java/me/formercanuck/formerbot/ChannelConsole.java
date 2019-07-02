package me.formercanuck.formerbot;

import me.fc.console.Console;
import me.formercanuck.formerbot.twitch.Channel;

import java.awt.*;
import java.awt.event.WindowEvent;

public class ChannelConsole extends Console {

    private Channel channel;

    public ChannelConsole(String channelName, Channel channel) {
        super(channelName);
        this.channel = channel;
    }

    public void print(String s, String user, Color... colors) {
        String regex = "\\s*\\b" + user + "\\b\\s*";
        s = s.replaceAll(regex, ">");

        String[] temp = s.split(">");

        print(temp[0], false, colors[0]);
        print(user, false, colors[1]);
        println(temp[1], false, colors[0]);
    }

    @Override
    public void onWindowClose(WindowEvent e) {
        channel.leaveChannel();
    }
}
