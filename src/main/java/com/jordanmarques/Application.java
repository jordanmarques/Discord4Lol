package com.jordanmarques;

import com.jordanmarques.client.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sx.blah.discord.util.DiscordException;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        Instance bot = new Instance();
        try {
            bot.login();
        } catch (DiscordException e) {
            log.warn("Bot could not start", e);
        }

    }
}
