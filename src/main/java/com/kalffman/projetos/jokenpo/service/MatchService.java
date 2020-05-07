package com.kalffman.projetos.jokenpo.service;

import com.kalffman.projetos.jokenpo.dto.MoveDTO;
import com.kalffman.projetos.jokenpo.model.Match;
import com.kalffman.projetos.jokenpo.model.Move;
import com.kalffman.projetos.jokenpo.model.Player;
import com.kalffman.projetos.jokenpo.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MatchRepository repository;

    private final PlayerService playerService;

    public MatchService(MatchRepository repository, PlayerService playerService) {
        this.repository = repository;
        this.playerService = playerService;
    }

    public Set<Match> findAll() {
        return repository.findAll();
    }

    public Match save(Match match) {
        log.info("MatchID[{}] - Salvando partida.", match.getMatchID());
        return repository.save(match);
    }

    public Match play(Match match) {
        log.info("MatchID[{}] - Jogar.", match.getMatchID());

        if (match.getMoves().size() > 1) {
            List<Move> moves = match.getMoves();
            for (Move playerMove : moves) {
                List<Move> rivals = moves.stream().filter(m -> !m.equals(playerMove)).collect(Collectors.toList());
                for (Move rivalMove : rivals) {
                    playerMove.incrementPoint(whoWin(playerMove.getPlay(), rivalMove.getPlay()));
                }
            }

            Collections.sort(moves);

        } else {
            throw new RuntimeException(String.format("Movimentos insuficientes para concluir a partida. Total de jogadores: %d", match.getMoves().size()));
        }

        electWinner(match);

        log.info("MatchID[{}] - Finalizando partida.", match.getMatchID());
        return match;
    }

    /**
     * Retorna uma nova instancia de partida com o seu ID
     *
     * @return Nova partida
     */
    public Match newMatch() {
        Integer matchID = findAll().size() + 1;
        Match match = new Match(matchID);
        log.info("MatchID[{}] - Nova partida criada.", match.getMatchID());
        return save(match);
    }

    /**
     * Realiza a adição de uma jogada a partida.
     * Pesquisa se existe jogador na partida, se sim, remove a sua jogada atual e adiciona a nova, se não, adiciona-a à partida.
     *
     * @param match Partida
     * @param move  Jogada
     */
    public void addMove(Match match, Move move) {
        if (match.getStatus() == null) {
            log.info("MatchID[{}] / ADD - {}.", match.getMatchID(), move.describe());
            List<Move> moves = match.getMoves();

            moves.removeIf(m -> m.getPlayer().equals(move.getPlayer()));

            moves.add(move);

        } else {
            throw new RuntimeException("A partida não pode ser modificada, pois a partida está finalizada.");
        }
    }

    public Match findByID(Integer id) {
        try {
            return repository.findByID(id);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(String.format("Partida com o id \"%d\" não foi encontrada.", id));
        }
    }

    /**
     * Método para eleger ganhador da partida.
     * Se houver somente um, atribui-se ganhador, se não, permanece como está
     * e detalha a descrição dos ganhadores.
     *
     * @param match Partida
     */
    private void electWinner(Match match) {
        log.info("MatchID[{}] - Elegendo ganhador.", match.getMatchID());
        List<Move> moves = match.getMoves();

        // Retornar o movimento com maior pontuação
        Move hScorer = moves.stream().reduce((atual, prox) ->
                atual.getPoints() == prox.getPoints() ? atual :
                atual.getPoints() > prox.getPoints() ? atual : prox
        ).get();

        // Prepara a lista de ganhadores
        List<Player> winners = moves.stream()
                .filter(m -> m.getPoints() == hScorer.getPoints())
                .map(Move::getPlayer)
                .collect(Collectors.toList());

        if(winners.size() == 1){
            Player winner = winners.get(0);
            match.setWinner(winner);
            match.setStatus(String.format("GANHADOR(ES) DA PARTIDA: %s", winner.getName()));
        } else {
            match.setStatus(String.format("GANHADOR(ES) DA PARTIDA: %s", winners.stream().map(Player::getName).collect(Collectors.toList())));
        }
    }

    /**
     * Trabalha a lógica de quem ganha entre o jogador e o adversário.
     * Vide {@link Move.MoveType}
     *
     * @param player Jogador primário
     * @param rival adversário
     * @return 1 para jogador, 0 para empate e -1 para o adversário
     */
    private int whoWin(Move.MoveType player, Move.MoveType rival) {
        switch (player) {
            case PEDRA:
                switch (rival) {
                    case PEDRA: return 0;
                    case TESOURA:
                    case LAGARTO: return 1;
                    case SPOCK:
                    case PAPEL: return -1;
                }
                break;
            case PAPEL:
                switch (rival) {
                    case PAPEL: return 0;
                    case PEDRA:
                    case SPOCK: return 1;
                    case TESOURA:
                    case LAGARTO: return -1;
                }
                break;
            case TESOURA:
                switch (rival) {
                    case TESOURA: return 0;
                    case PAPEL:
                    case LAGARTO: return 1;
                    case PEDRA:
                    case SPOCK: return -1;
                }
                break;
            case LAGARTO:
                switch (rival) {
                    case LAGARTO: return 0;
                    case PAPEL:
                    case SPOCK: return 1;
                    case PEDRA:
                    case TESOURA: return -1;
                }
                break;
            case SPOCK:
                switch (rival) {
                    case SPOCK: return 0;
                    case PEDRA:
                    case TESOURA: return 1;
                    case PAPEL:
                    case LAGARTO: return -1;
                }
        }
        throw new IllegalArgumentException("Java... Você falhou...");
    }

    public Move convertDTOToEntity(MoveDTO dto) {
        Move move = new Move();

        Player player = playerService.findByName(dto.getName());

        move.setPlayer(player);
        move.setPlay(dto.getPlay());

        return move;
    }

    public void delMove(Match match, Move move) {
        if(match.getStatus() == null){
            log.warn("MatchID[{}] / DEL - {}.", match.getMatchID(), move.describe());
            List<Move> moves = match.getMoves();

            moves.removeIf(m -> m.getPlayer().equals(move.getPlayer()));
        } else {
            throw new RuntimeException("A partida não pode ser modificada, pois a partida está finalizada.");
        }
    }
}
