package com.jordanmarques.client;

import com.jordanmarques.listeners.UserJoinListerner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Instance{

    private static final Logger log = LoggerFactory.getLogger(Instance.class);

    public static volatile IDiscordClient CLIENT;
    private String email;
    private String password;

    public static String TOKEN;

    private final AtomicBoolean reconnect = new AtomicBoolean(true);

    public Instance() {
    }

    public Instance(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Instance(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public void login() throws DiscordException {
        if (TOKEN == null) {
            CLIENT = new ClientBuilder().withLogin(email, password).login();
        } else {
            CLIENT = new ClientBuilder().withToken(TOKEN).login();
        }
        EventDispatcher dispatcher = CLIENT.getDispatcher();
        dispatcher.registerListener(this);// Gets the EventDispatcher instance for this CLIENT instance
        dispatcher.registerListener(new UserJoinListerner());
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        log.info("*** Discord bot armed ***");
    }

    @EventSubscriber
    public void onDisconnect(DiscordDisconnectedEvent event) {
        CompletableFuture.runAsync(() -> {
            if (reconnect.get()) {
                log.info("Reconnecting bot");
                try {
                    login();
                } catch (DiscordException e) {
                    log.warn("Failed to reconnect bot", e);
                }
            }
        });
    }

    @EventSubscriber
    public void onMessage(MessageReceivedEvent e) {
        log.debug("Got message");
    }

    public void terminate() {
        reconnect.set(false);
        try {
            CLIENT.logout();
        } catch (RateLimitException | DiscordException e) {
            log.warn("Logout failed", e);
        }
    }

    public String getToken() {
        return TOKEN;
    }
    @Value("${discord.token}")
    public void setToken(String token) {
        TOKEN = token;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
