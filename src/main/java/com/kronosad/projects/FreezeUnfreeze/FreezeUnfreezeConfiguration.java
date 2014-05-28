package com.kronosad.projects.FreezeUnfreeze;

import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public class FreezeUnfreezeConfiguration {
    private Configuration config;

    private String serializedPlayersKey = "frozen.players";

    public FreezeUnfreezeConfiguration(Configuration config) {
        this.config = config;
    }

    public void serializePlayers(List<String> players) {
        FreezeUnfreeze.debug("Serializing: " + players.toString());
        config.set(serializedPlayersKey, players);
    }

    public List<String> getSerializedPlayers() {
        if (config.getList(serializedPlayersKey) != null) {
            FreezeUnfreeze.debug("Unserialized: " + config.getList(serializedPlayersKey).toString());
            return (List<String>)config.getList(serializedPlayersKey);
        } else {
            FreezeUnfreeze.debug("No serialized players found, creating new list!");
            return new ArrayList<String>();
        }

    }

    public void setConfig(Configuration config) {
        this.config = config;
    }
}
