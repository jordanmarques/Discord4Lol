package com.jordanmarques.api.model;

import java.util.List;

public class Queue {
    private String name;
    private String tier;
    private String queue;
    private List<Entry> entries;

    public static final String RANKED_SOLO_5x5 = "RANKED_SOLO_5x5";
    public static final String RANKED_TEAM_5x5 = "RANKED_TEAM_5x5";

    public Queue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

}
