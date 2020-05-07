package com.kalffman.projetos.jokenpo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalffman.projetos.jokenpo.dto.MoveDTO;
import com.kalffman.projetos.jokenpo.model.Match;
import com.kalffman.projetos.jokenpo.model.Move;
import com.kalffman.projetos.jokenpo.service.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class JokenpoApplicationPlayTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MatchService matchService;

    @Test
    void test() throws Exception {
        List<Match> matches = new ArrayList<>();
        matches.add(new Match(1));

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(get("/match"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(matches)));
    }

    @Test
    void initPlay() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        MoveDTO move1 = new MoveDTO("Jogador 1", Move.MoveType.PEDRA);
        MoveDTO move2 = new MoveDTO("Jogador 2", Move.MoveType.TESOURA);
        MoveDTO move3 = new MoveDTO("Jogador 3", Move.MoveType.TESOURA);

        mockMvc
            .perform(
                post("/match")
                    .header("Content-type","application/json")
                    .content(mapper.writeValueAsString(move1)))
            .andExpect(status().isCreated());

        mockMvc
            .perform(
                post("/match")
                    .header("Content-type","application/json")
                    .content(mapper.writeValueAsString(move2)))
            .andExpect(status().isCreated());

        mockMvc
            .perform(
                post("/match")
                    .header("Content-type","application/json")
                    .content(mapper.writeValueAsString(move3)))
            .andExpect(status().isCreated());

        mockMvc
            .perform(post("/match/play"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("GANHADOR(ES) DA PARTIDA: Jogador 1"));

    }

}
