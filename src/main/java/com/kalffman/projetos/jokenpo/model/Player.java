package com.kalffman.projetos.jokenpo.model;

import java.util.Objects;

public class Player {
    private Integer playerID;
    private String name;

    public Player(Integer playerID, String name) {
        this.playerID = playerID;
        this.name = name;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerID.equals(player.playerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID);
    }
}
