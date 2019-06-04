package me.formercanuck.formerbot.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetJsonData {

    private String clientID = "pqi99elyam4p8ewyab8eyrxnb8urvw";

    private JsonParser json = new JsonParser();

    private static GetJsonData instance = new GetJsonData();

    private GetJsonData() {
    }

    public JsonElement getJson(String url) {
        return json.parse(retrieveJson(url));
    }

    private String retrieveJson(String link) {
        try {

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setRequestProperty("Client-ID", clientID);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String str = "";
            String temp = "";

            while ((temp = br.readLine()) != null) {
                str += temp;
            }

            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String retJson(String link) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            con.setRequestProperty("Client-ID", clientID);
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = in.readLine()) != null) {
                    contentBuilder.append(line);
                }
                return contentBuilder.toString();
            } finally {
                con.getInputStream().close();
                con.getOutputStream().close();
            } // Closes in.
        } catch (IOException e) {
//            log.warn("Could not connect", e);
        }
        return "";
    }

    public static GetJsonData getInstance() {
        return instance;
    }
}
