package com.jordanmarques.listeners;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.UserJoinEvent;

public class UserJoinListerner implements IListener<UserJoinEvent> {

    @Override
    public void handle(UserJoinEvent userJoinEvent) {
        String userName = userJoinEvent.getUser().getName();
        System.out.println(userName);
    }
}
