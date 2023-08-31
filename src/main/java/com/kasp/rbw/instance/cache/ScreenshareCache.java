package com.kasp.rbw.instance.cache;

import com.kasp.rbw.instance.ScreenShare;

import java.util.HashMap;

public class ScreenshareCache {

    // channelID, screenshare
    private static HashMap<String, ScreenShare> screenshares = new HashMap<>();

    public static ScreenShare getScreenshare(String ID) {
        return screenshares.get(ID);
    }

    public static void addScreenshare(ScreenShare ss) {
        screenshares.put(ss.getChannelID(), ss);

        System.out.println("Screenshare for " + ss.getTarget().getIgn() + " has been loaded into memory");
    }

    public static void removeScreenshare(String ID) {
        screenshares.remove(ID);
    }

    public static boolean containsScreenshare(ScreenShare ss) {
        return screenshares.containsValue(ss);
    }

    public static void initializeScreenshare(ScreenShare ss) {
        if (!containsScreenshare(ss))
            addScreenshare(ss);
    }

    public static HashMap<String, ScreenShare> getScreenshares() {
        return screenshares;
    }
}
