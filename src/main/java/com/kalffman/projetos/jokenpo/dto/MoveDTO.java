package com.kalffman.projetos.jokenpo.dto;

import com.kalffman.projetos.jokenpo.model.Move;

public class MoveDTO {
    private String name;
    private Move.MoveType play;

    public MoveDTO() {
    }

    public MoveDTO(String name) {
        this.name = name;
    }

    public MoveDTO(String name, Move.MoveType play) {
        this.name = name;
        this.play = play;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Move.MoveType getPlay() {
        return play;
    }

    public void setPlay(Move.MoveType play) {
        this.play = play;
    }
}
