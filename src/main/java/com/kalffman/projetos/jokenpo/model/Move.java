package com.kalffman.projetos.jokenpo.model;

public class Move implements Comparable<Move> {
    private Player player;
    private MoveType play;
    private int points;

    public Move() {
    }

    public Move(Player player, MoveType play) {
        this.player = player;
        this.play = play;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPlay(MoveType play) {
        this.play = play;
    }

    public Player getPlayer() {
        return player;
    }

    public MoveType getPlay() {
        return play;
    }

    public void incrementPoint(int point){
        points += point;
    }

    public int getPoints() {
        return points;
    }

    public String describe() {
        return String.format("%s, jogada %s", player.getName(), play);
    }

    @Override
    public int compareTo(Move move) {
        return - Integer.compare(points, move.points); //ordenação decrescente
    }

    public enum MoveType {
        PEDRA,
        PAPEL,
        TESOURA,
        LAGARTO,
        SPOCK
    }
}
