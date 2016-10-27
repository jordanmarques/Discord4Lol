package com.jordanmarques.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.impl.obj.Role;
import sx.blah.discord.handle.obj.IUser;

import java.util.Map;

@Component
public class UserJoinListerner implements IListener<UserVoiceChannelJoinEvent> {

    private static final Logger log = LoggerFactory.getLogger(UserJoinListerner.class);

    private static Map<String,String> lolUsername;

    @Override
    public void handle(UserVoiceChannelJoinEvent userVoiceChannelJoinEvent) {
        log.debug("User Join");


        IUser user = userVoiceChannelJoinEvent.getUser();
        String lolPseudo = getLolPseudo( user.getName());
    }

    private String getLolPseudo(String discordUsername) {

        if(lolUsername.containsKey(discordUsername))
            return lolUsername.get(discordUsername);

        throw new RuntimeException("Enable to find Lol Username for Discord username: " + discordUsername);
    }

    public Map<String, String> getLolUsername() {
        return lolUsername;
    }
    @Value("#{${discord.lolusername}}")
    public void setLolUsername(Map<String, String> lolUsername) {
        this.lolUsername = lolUsername;
    }
}
