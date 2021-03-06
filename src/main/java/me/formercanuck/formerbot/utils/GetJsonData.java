package me.formercanuck.formerbot.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class GetJsonData {

    private static String youtubeID = "AIzaSyBcZNuPkJyns4kHpeNELRM1HF6FdZpq5uE";

    private JsonParser json = new JsonParser();

    private static GetJsonData instance = new GetJsonData();

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
        InputStream is;
        try {
            URL u = new URL(url);
            String clientID = "pqi99elyam4p8ewyab8eyrxnb8urvw";
            URLConnection con = u.openConnection();
            con.setRequestProperty("Client-ID", clientID);
            is = con.getInputStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                return json.parse(jsonText);
            } finally {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
