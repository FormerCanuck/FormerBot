package me.formercanuck.formerbot.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Random;

public class GetJsonData {

    private static String youtubeID = "AIzaSyBcZNuPkJyns4kHpeNELRM1HF6FdZpq5uE";

    private JsonParser json = new JsonParser();

    private static GetJsonData instance = new GetJsonData();

    private GetJsonData() {
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

//    public JsonElement getJson(String url) {
//        setProxy("127.0.0.1", getRandomNumberInRange(1000, 7000));
//        try {
//            return json.parse(getString(url));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private JsonElement getJsonFromYT(String url) {
        return json.parse(retrieveYoutubeJson(url));
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    public JsonElement getJson(String url) {
        InputStream is = null;
        try {
            URL u = new URL(url);
            String clientID = "pqi99elyam4p8ewyab8eyrxnb8urvw";
            URLConnection con = u.openConnection();
            con.setRequestProperty("Client-ID", clientID);
            is = con.getInputStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JsonElement j = json.parse(jsonText);
                return j;
            } finally {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String retrieveJson(String link) {
        try {

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            String clientID = "pqi99elyam4p8ewyab8eyrxnb8urvw";
            conn.setRequestProperty("Client-ID", clientID);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder str = new StringBuilder();
            String temp;

            while ((temp = br.readLine()) != null) {
                str.append(temp);
            }

            br.close();

            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getLastVideoTitle(String username) {
        if (getIDFromYoutube(username) == null) return null;
        return MiscUtils.strip(getInstance().getJsonFromYT("activities?part=snippet&channelId=" + getIDFromYoutube(username) + "&key=" + youtubeID).getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("snippet").getAsJsonObject().get("title").toString());
    }

    public Long daysSinceLastUpload(String username) {
        if (getIDFromYoutube(username) == null) return null;
        return MiscUtils.numberOfDaysBetweenDateAndNow(MiscUtils.strip(getJsonFromYT("activities?part=snippet&channelId=" + getIDFromYoutube(username) + "&key=" + youtubeID).getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("snippet").getAsJsonObject().get("publishedAt").toString()));
    }

    public String getLastVideoLink(String username) {
        if (getIDFromYoutube(username) == null) return null;
        return "https://www.youtube.com/watch?v=" + MiscUtils.strip(getJsonFromYT("activities?part=contentDetails&channelId=" + getIDFromYoutube(username) + "&key=" + youtubeID).getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("contentDetails").getAsJsonObject().get("upload").getAsJsonObject().get("videoId").toString());
    }

    public String getIDFromYoutube(String username) {
        JsonElement element = getInstance().getJsonFromYT("channels?part=contentDetails&forUsername=" + username + "&key=" + youtubeID);
        JsonArray array = element.getAsJsonObject().get("items").getAsJsonArray();
        if (array.size() == 0) return null;
        String id = array.get(0).getAsJsonObject().get("id").toString();
        return MiscUtils.strip(id);
    }

    private String retrieveYoutubeJson(String link) {
        try {
            String temp_link = "https://www.googleapis.com/youtube/v3/" + link;
//            String temp_link = "https://www.googleapis.com/youtube/v3/channels?part=contentDetails&forUsername=recanem&key="+youtubeID;
            URL url = new URL(temp_link);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder str = new StringBuilder();
            String temp;

            while ((temp = br.readLine()) != null) {
                str.append(temp);
            }

            br.close();
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static GetJsonData getInstance() {
        return instance;
    }
}
