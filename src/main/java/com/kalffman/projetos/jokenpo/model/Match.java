package com.kalffman.projetos.jokenpo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Match {
    private Integer matchID;
    private List<Move> moves;
    private Player winner;
    private String status;

    public Match(Integer matchID) {
        this.matchID = matchID;
        this.moves = new ArrayList<>();
    }

    public Integer getMatchID() {
        return matchID;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return matchID.equals(match.matchID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchID);
    }
}
