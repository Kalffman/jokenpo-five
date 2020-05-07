package com.kalffman.projetos.jokenpo.service;

import com.kalffman.projetos.jokenpo.model.Player;
import com.kalffman.projetos.jokenpo.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public Set<Player> findAll() {
        return repository.findAll();
    }

    public Player save(Player player) {
        return repository.save(player);
    }

    public Player newPlayer(String name){
        Integer playerID = findAll().size() + 1;
        Player player = new Player(playerID, name);
        log.info("PlayerID[{}] - Novo jogador criado.", player.getPlayerID());
        return save(player);
    }

    public Player findByName(String name) {
        try {
            return repository.findByName(name);
        } catch (NoSuchElementException ex) {
            log.warn("Usuário não encontrado. QUERY: name={}", name);
            return newPlayer(name);
        }
    }
}
