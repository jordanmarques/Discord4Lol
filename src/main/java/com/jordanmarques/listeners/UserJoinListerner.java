package com.jordanmarques.listeners;

import com.jordanmarques.api.LolApi;
import com.jordanmarques.model.Summoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.obj.IUser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


@Component
public class UserJoinListerner implements IListener<UserVoiceChannelJoinEvent> {

    private static final Logger log = LoggerFactory.getLogger(UserJoinListerner.class);
    private Map<String, String> usernames;
    private LolApi lolApi;

    public UserJoinListerner() {
        this.usernames = populateUsernames();
        this.lolApi = new LolApi();
    }

    @Override
    public void handle(UserVoiceChannelJoinEvent userVoiceChannelJoinEvent) {

        IUser discordUser = userVoiceChannelJoinEvent.getUser();
        String lolUsername = getLolUsernameByDiscordUsername( discordUser.getName());

        Summoner summoner = lolApi.getSummonerByName(lolUsername);
    }



    private String getLolUsernameByDiscordUsername(String discordUsername) {
        if(usernames.containsKey(discordUsername))
            return usernames.get(discordUsername);

        throw new RuntimeException("Enable to find Lol Username for Discord username: " + discordUsername);
    }

    private Map<String, String> populateUsernames(){

        Map<String, String> maptoreturn = new HashMap<>();

        try {
            try(Stream<String> lines = Files.lines(Paths.get(ClassLoader.getSystemResource("usernames.data").toURI()), Charset.defaultCharset())) {
                lines.forEachOrdered(line -> {
                    String[] username = line.split(":");
                    maptoreturn.put(username[0], username[1]);
                });
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return maptoreturn;
    }
}
