package com.jordanmarques.model;

public class Entry {
    private String leaguePoints;
    private Boolean isFreshBlood;
    private Boolean isHotStreak;
    private String division;
    private Boolean isInactive;
    private Boolean isVeteran;
    private String playstyle;
    private String losses;
    private String playerOrTeamName;
    private String playerOrTeamId;
    private String wins;

    public String getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(String leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public Boolean getFreshBlood() {
        return isFreshBlood;
    }

    public void setFreshBlood(Boolean freshBlood) {
        isFreshBlood = freshBlood;
    }

    public Boolean getHotStreak() {
        return isHotStreak;
    }

    public void setHotStreak(Boolean hotStreak) {
        isHotStreak = hotStreak;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Boolean getInactive() {
        return isInactive;
    }

    public void setInactive(Boolean inactive) {
        isInactive = inactive;
    }

    public Boolean getVeteran() {
        return isVeteran;
    }

    public void setVeteran(Boolean veteran) {
        isVeteran = veteran;
    }

    public String getPlaystyle() {
        return playstyle;
    }

    public void setPlaystyle(String playstyle) {
        this.playstyle = playstyle;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public String getPlayerOrTeamName() {
        return playerOrTeamName;
    }

    public void setPlayerOrTeamName(String playerOrTeamName) {
        this.playerOrTeamName = playerOrTeamName;
    }

    public String getPlayerOrTeamId() {
        return playerOrTeamId;
    }

    public void setPlayerOrTeamId(String playerOrTeamId) {
        this.playerOrTeamId = playerOrTeamId;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }
}
