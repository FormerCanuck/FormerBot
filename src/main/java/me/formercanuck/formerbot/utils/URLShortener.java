package me.formercanuck.formerbot.utils;

import net.swisstech.bitly.BitlyClient;
import net.swisstech.bitly.model.Response;
import net.swisstech.bitly.model.v3.ShortenResponse;

public class URLShortener {

    // ff12264818ebba5e20db365ac39aa4110230b4b7

    private static BitlyClient client = new BitlyClient("ff12264818ebba5e20db365ac39aa4110230b4b7");

    public static String shorten(String url) {
        Response<ShortenResponse> resp = client.shorten()
                .setLongUrl(url)
                .call();
        return resp.data.url;
    }
}
