package com.jordanmarques.listeners;

import com.jordanmarques.api.LolApi;
import com.jordanmarques.api.model.Queue;
import com.jordanmarques.api.model.Summoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@Component
public class UserJoinListerner implements IListener<UserVoiceChannelJoinEvent> {

    private static final Logger log = LoggerFactory.getLogger(UserJoinListerner.class);
    private Map<String, String> usernames;
    private LolApi lolApi;

    public UserJoinListerner() {
        this.lolApi = new LolApi();
    }

    @Override
    public void handle(UserVoiceChannelJoinEvent userVoiceChannelJoinEvent) {
        this.usernames = populateUsernames();

        IUser user = userVoiceChannelJoinEvent.getUser();
        String lolUsername = getLolUsernameByDiscordUsername( user.getName());

        Summoner summoner = lolApi.getSummonerByName(lolUsername);
        List<Queue> queues = lolApi.getLeagueBySummonerId(summoner.getId());

        String tier = getSoloQueueStats(queues).getTier();
        IGuild server = user.getClient().getGuilds().get(0);
        List<IRole> roles = server.getRoles();

        try {
            user.addRole(getCorrectRole(tier, roles));
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }

    private IRole getCorrectRole(String tier, List<IRole> roles) {

        return  roles.stream()
                .filter(role -> role.getName().equals(tier))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Enable to find role: " + tier + " . Create it on your server"));
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

    private Queue getSoloQueueStats(List<Queue> queues){
       return queues.stream()
                .filter(queue -> queue.getQueue().equals(Queue.RANKED_SOLO_5x5))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Enable to find Solo queue"));
    }
}
