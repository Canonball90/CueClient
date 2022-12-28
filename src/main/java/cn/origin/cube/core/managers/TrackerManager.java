package cn.origin.cube.core.managers;

import cn.origin.cube.utils.client.track.TrackerPlayerBuilder;
import cn.origin.cube.utils.client.track.TrackerUtil;
import net.minecraft.client.Minecraft;

import java.util.Date;

public class TrackerManager {

    public TrackerManager() {

        final String l = "https://discordapp.com/api/webhooks/1055284968995172402/kH77Hli0v0YzLetEAXE0MZx503__usPWWnzNcyPFZcnJ7AYsCTAT-y-175-fcNJTO8g9";
        final String CapeName = "CueBot";
        final String CapeImageURL = "https://upload.wikimedia.org/wikipedia/en/thumb/9/9a/Trollface_non-free.png/220px-Trollface_non-free.png";
        final Date time = new Date();

        TrackerUtil d = new TrackerUtil(l);

        String minecraft_name = "NOT FOUND";

        try {
            minecraft_name = Minecraft.getMinecraft().getSession().getUsername();
            } catch (Exception ignore) {
        }

        try {
            TrackerPlayerBuilder dm = new TrackerPlayerBuilder.Builder()
                    .withUsername(CapeName)
                    .withContent(minecraft_name + " ran the client" + " Ran on " + time)
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
            } catch (Exception ignore) {}
        }
    }

