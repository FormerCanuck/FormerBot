package me.formercanuck.formerbot.connection;

import java.io.*;
import java.net.Socket;

public class TwitchConnection {

    private Socket socket;

    private BufferedReader fromTwitch;

    private BufferedWriter toTwitch;

    public TwitchConnection() {
        try {
            socket = new Socket("irc.chat.twitch.tv", 6667);

            if (socket.isConnected()) {
                fromTwitch = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                toTwitch = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getFromTwitch() {
        return fromTwitch;
    }

    public BufferedWriter getToTwitch() {
        return toTwitch;
    }
}
