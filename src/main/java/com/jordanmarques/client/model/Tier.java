package com.jordanmarques.client.model;

import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RoleBuilder;

import java.awt.*;
import java.util.EnumSet;

public class Tier {

    public static final String BRONZE = "BRONZE";
    public static final String SILVER = "SILVER";
    public static final String GOLD = "GOLD";
    public static final String PLATINUM = "PLATINUM";
    public static final String DIAMOND = "DIAMOND";
    public static final String MASTER = "MASTER";
    public static final String CHALLENGER = "CHALLENGER";

    public static IRole getTier(String role, IGuild guild){
        Color color;
        String name;

        switch (role){

            case BRONZE:
                color = new Color(207, 91, 0);
                name = "Bronze";
                break;

            case SILVER:
                color = new Color(130, 124, 118);
                name = "Silver";
                break;

            case GOLD:
                color = new Color(232, 193, 11);
                name = "Gold";
                break;

            case PLATINUM:
                color = new Color(0, 198, 103);
                name = "Platinium";
                break;

            case DIAMOND:
                color = new Color(18, 134, 221);
                name = "Diamond";
                break;

            case MASTER:
                color = new Color(126, 83, 216);
                name = "Master";
                break;

            case CHALLENGER:
                color = new Color(255, 30, 0);
                name = "Chalenger";
                break;

            default:
                color = new Color(0,0,0);
                name = "U MAD BRO";
                break;
        }

        try {
            return new RoleBuilder(guild)
                    .withPermissions(EnumSet.of(Permissions.CREATE_INVITE,
                            Permissions.READ_MESSAGES,
                            Permissions.SEND_MESSAGES,
                            Permissions.SEND_TTS_MESSAGES,
                            Permissions.EMBED_LINKS,
                            Permissions.ATTACH_FILES,
                            Permissions.READ_MESSAGE_HISTORY,
                            Permissions.MENTION_EVERYONE,
                            Permissions.USE_EXTERNAL_EMOJIS,
                            Permissions.VOICE_CONNECT,
                            Permissions.VOICE_SPEAK,
                            Permissions.VOICE_USE_VAD))
                    .setMentionable(false)
                    .setHoist(true)
                    .withName(name)
                    .withColor(color)
                    .build();

        } catch (DiscordException | RateLimitException | MissingPermissionsException e) {
            throw new RuntimeException(e);
        }
    }
}
