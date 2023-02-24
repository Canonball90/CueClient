package cn.origin.cube;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class Discord {
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        String discordID = "1078729515007737897";
        DiscordRPC.INSTANCE.Discord_Initialize(discordID, eventHandlers, true, null);
        Discord.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        Discord.discordRichPresence.details = "Cue da best frl";
        Discord.discordRichPresence.largeImageText = "Cue";
        Discord.discordRichPresence.state = null;
        DiscordRPC.INSTANCE.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stopRPC() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
        DiscordRPC.INSTANCE.Discord_ClearPresence();
    }
}

