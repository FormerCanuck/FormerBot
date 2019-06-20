package me.formercanuck.formerbot.command.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.formercanuck.formerbot.Main;
import me.formercanuck.formerbot.command.Command;
import me.formercanuck.formerbot.utils.GetJsonData;

import java.util.HashMap;

public class TopClips extends Command {
    @Override
    public String getName() {
        return "clips";
    }

    @Override
    public void onCommand(String sender, String channel, String[] args) {
        String id = Main.getInstance().getBot().getChannel().getChannelID();
        sender = sender.toLowerCase();

        JsonElement temp = GetJsonData.getInstance().getJson(String.format("https://api.twitch.tv/helix/clips?broadcaster_id=%s&first=5", id));

        JsonArray jsonArray = temp.getAsJsonObject().get("data").getAsJsonArray();

        if (args.length == 0 && !Main.getInstance().getBot().getChannel().isMod(sender)) {
            Main.getInstance().getBot().getChannel().messageChannel("Usage: !topclips <1-5>");
            return;
        }

        if (args[0].equalsIgnoreCase("top") && Main.getInstance().getBot().getChannel().isMod(sender) || Main.getInstance().getBot().getChannel().isWhiteListed(sender)) {
            Main.getInstance().getBot().getChannel().messageChannel("Here are the top 5 clips:");

            for (int i = 0; i < 5; i++) { // Integer.parseInt(args[1]) - 1
                Main.getInstance().getBot().getChannel().messageChannel(String.format("Clip name: %s and the link: %s", jsonArray.get(i).getAsJsonObject().get("title").getAsString(), jsonArray.get(i).getAsJsonObject().get("url").getAsString()));
            }
            return;
        } else if (args.length > 0) {
            try {
                Main.getInstance().getBot().getChannel().messageChannel(String.format("Clip name: %s and the link: %s", jsonArray.get(Integer.parseInt(args[0])).getAsJsonObject().get("title").getAsString(), jsonArray.get(Integer.parseInt(args[0])).getAsJsonObject().get("url").getAsString()));
            } catch (NumberFormatException er) {
                if (!Main.getInstance().getBot().getChannel().isMod(sender)) return;

                HashMap<String, HashMap<String, String>> clips;
                if (!Main.getInstance().getBot().getBotFile().contains("clips")) clips = new HashMap<>();
                else
                    clips = (HashMap<String, HashMap<String, String>>) Main.getInstance().getBot().getBotFile().get("clips");

                HashMap<String, String> sendersClips = new HashMap<>();

                if (clips.containsKey(sender) && !clips.get(sender).isEmpty()) sendersClips = clips.get(sender);

                if (clips.containsKey(args[0].toLowerCase())) {
                    sendersClips = clips.get(args[0]);
                    if (sendersClips == null) sendersClips = new HashMap<>();
                    System.out.println("HEY");
                    Main.getInstance().getBot().getChannel().messageChannel(String.format("%s here are %s's favorite clips: ", sender, args[0]));
                    for (String s : sendersClips.keySet()) {
                        Main.getInstance().getBot().getChannel().messageChannel(String.format("%s", sendersClips.get(s)));
                    }
                } else {
                    sendersClips = clips.get(sender);
                    if (sendersClips == null) sendersClips = new HashMap<>();

                    StringBuilder clipName = new StringBuilder();
                    for (String s : args) {
                        clipName.append(s).append(" ");
                    }
                    temp = GetJsonData.getInstance().getJson(String.format("https://api.twitch.tv/helix/clips?broadcaster_id=%s", id));

                    jsonArray = temp.getAsJsonObject().get("data").getAsJsonArray();

                    for (JsonElement e : jsonArray) {
                        JsonObject o = e.getAsJsonObject();

                        if (o.get("title").toString().replace("\"", " ").trim().equalsIgnoreCase(clipName.toString().trim())) {
                            sendersClips.put(o.get("title").toString().replace("\"", " ").trim(), o.get("url").getAsString());
                            clips.put(sender, sendersClips);
                            Main.getInstance().getBot().getBotFile().set("clips", clips);
                            Main.getInstance().getBot().getChannel().messageChannel(String.format("%s you have saved %s to your favorite clips!", sender, o.get("url").getAsString()));
                            break;
                        }

                    }
                }
            }
        }
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
