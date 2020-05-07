package com.kalffman.projetos.jokenpo.controller;

import com.kalffman.projetos.jokenpo.dto.MoveDTO;
import com.kalffman.projetos.jokenpo.handler.ApiError;
import com.kalffman.projetos.jokenpo.model.Match;
import com.kalffman.projetos.jokenpo.model.Move;
import com.kalffman.projetos.jokenpo.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    private Match openMatch;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostConstruct
    public void init() {
        openMatch = matchService.newMatch();
    }

    @PostMapping("/new")
    public ResponseEntity<?> newMatch() {
        return ResponseEntity.ok(matchService.newMatch());
    }

    @GetMapping("/play")
    public ResponseEntity<?> play() {
        try {
            openMatch = matchService.play(openMatch);

            Match savedMatch = matchService.save(openMatch);

            init();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(savedMatch.getStatus());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.buildError(ApiError.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("/play/{id}")
    public ResponseEntity<?> play(@PathVariable Integer id) {
        try {
            Match match = matchService.findByID(id);

            match = matchService.play(match);

            match = matchService.save(match);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(match.getStatus());

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.buildError(ApiError.NOT_FOUND, e.getMessage()));

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.buildError(ApiError.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getMatches() {
        return ResponseEntity.ok(matchService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMatch(@PathVariable Integer id) {
        try {
            Match match = matchService.findByID(id);

            return ResponseEntity.ok(match);

        } catch (NoSuchElementException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.buildError(ApiError.NOT_FOUND, ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addMove(@RequestBody MoveDTO dto) {
        try {
            Move move = matchService.convertDTOToEntity(dto);

            matchService.addMove(openMatch, move);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.buildError(ApiError.BAD_REQUEST, e.getMessage()));
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addMove(@PathVariable Integer id, @RequestBody MoveDTO dto) {
        try {
            Match match = matchService.findByID(id);

            Move move = matchService.convertDTOToEntity(dto);

            matchService.addMove(match, move);

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.buildError(ApiError.NOT_FOUND, e.getMessage()));
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.buildError(ApiError.BAD_REQUEST, e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delMove(@RequestBody MoveDTO dto) {
        try {
            Move move = matchService.convertDTOToEntity(dto);

            matchService.delMove(openMatch, move);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.buildError(ApiError.BAD_REQUEST, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delMove(@PathVariable Integer id, @RequestBody MoveDTO dto) {
        try {
            Match match = matchService.findByID(id);

            Move move = matchService.convertDTOToEntity(dto);

            matchService.delMove(match, move);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiError.buildError(ApiError.NOT_FOUND, e.getMessage()));
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.buildError(ApiError.BAD_REQUEST, e.getMessage()));
        }
    }
}
