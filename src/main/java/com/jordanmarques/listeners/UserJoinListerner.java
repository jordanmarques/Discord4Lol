package com.jordanmarques.listeners;

import com.jordanmarques.api.LolApi;
import com.jordanmarques.api.model.Queue;
import com.jordanmarques.api.model.Summoner;
import com.jordanmarques.api.model.Tier;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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

        manageTierRoles(user, server, tier);


    }

    private void manageTierRoles(IUser user, IGuild server, String tier){

        List<IRole> roles = removeUncorrectTierAndAddNewTier(user, server, tier);

        try {
            server.editUserRoles(user, roles.toArray(new IRole[roles.size()]));
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }

    private List<IRole> removeUncorrectTierAndAddNewTier(IUser user, IGuild server, String tier) {

        List<IRole> rolesForGuild = user.getRolesForGuild(server);
        for(Iterator<IRole> it = rolesForGuild.iterator(); it.hasNext();){
            IRole role = it.next();
            String currentRoleName = role.getName();
            if(Tier.getTier().containsKey(currentRoleName)){
                if(Tier.getTier().get(currentRoleName) != Tier.getTier().get(tier)){
                    it.remove();
                }
            }
        }

        List<IRole> roles = addTier(server, tier, rolesForGuild);

        return roles;
    }

    private List<IRole> addTier(IGuild server, String tier, List<IRole> rolesForGuild){

        rolesForGuild.add(getTierRole(tier, server.getRoles()));

        return rolesForGuild;
    }

    private IRole getTierRole(String tier, List<IRole> roles) {

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
            try(Stream<String> lines = Files.lines(Paths.get(System.getProperty("user.dir") + "/usernames.data"), Charset.defaultCharset())) {
                lines.forEachOrdered(line -> {
                    String[] username = line.split(":");
                    maptoreturn.put(username[0], username[1]);
                });
            }
        } catch (IOException e) {
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
