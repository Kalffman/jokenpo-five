package com.kalffman.projetos.jokenpo.repository;

import com.kalffman.projetos.jokenpo.model.Player;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PlayerRepository {
    private Set<Player> repo = Collections.synchronizedSet(new HashSet<>());

    public Set<Player> findAll() {
        return repo;
    }

    public Player findByName(String name) {
        return repo.stream().filter(p -> p.getName().equals(name)).findFirst().get();
    }

    public Player save(Player player) {
        repo.add(player);
        return player;
    }

    public boolean delete(Player player){
        return repo.remove(player);
    }
}
