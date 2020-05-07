package com.kalffman.projetos.jokenpo.repository;

import com.kalffman.projetos.jokenpo.model.Match;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MatchRepository {
    private Set<Match> list = Collections.synchronizedSet(new HashSet<>());

    public Set<Match> findAll(){
        return list;
    }

    public Match save(Match match) {
        this.list.add(match);
        return match;
    }

    public Match findByID(Integer id) {
        return list.stream().filter(m -> m.getMatchID().equals(id)).findFirst().get();
    }
}
