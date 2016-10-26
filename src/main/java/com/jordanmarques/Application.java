package com.jordanmarques;

import com.jordanmarques.client.Client;
import com.jordanmarques.listeners.UserJoinListerner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        IDiscordClient client = null; // Gets the client object (from the first example)
        try {
            client = Client.getClient("MjQwODg1NzI0MjUyMDc4MDgy.CvKJHw.Q0XtY0TQYTrvwvtvbPliker5Yzk", true);
            EventDispatcher dispatcher = client.getDispatcher(); // Gets the EventDispatcher instance for this client instance
            dispatcher.registerListener(new UserJoinListerner()); // Registers the IListener example class from above
        } catch (DiscordException e) {
            e.printStackTrace();
        }


    }
}
