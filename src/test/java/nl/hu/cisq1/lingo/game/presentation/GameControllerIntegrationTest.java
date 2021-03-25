package nl.hu.cisq1.lingo.game.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"security.basic.enabled=false", "management.security.enabled=false"})
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class })
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    public int getGameId() throws Exception {
        final Integer[] gameId = new Integer[1];

        RequestBuilder request = MockMvcRequestBuilders
                .get("/game/new");

        mockMvc.perform(request)
                .andDo(mvcResult -> {
                    String testStr = mvcResult.getResponse().getContentAsString().split("[:,]")[1];
                    gameId[0] = Integer.valueOf(testStr);
                });

        return gameId[0];
    }

    @Test
    @DisplayName("Request a new game")
    void requestNewGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/game/new");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content().string(containsString("\"state\":\"STARTED\"")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Try guess at existing game")
    void testGuessExistingGame() throws Exception {
        int gameId = getGameId();

        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/guess")
                .queryParam("id", String.valueOf(gameId))
                .queryParam("guess", "xxxxxxxx");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> {
                    String errorMsg = mvcResult.getResponse().getErrorMessage();
                    assert errorMsg != null;
                    assert errorMsg.contains("Word lengths not equal");
                });
    }


    @Test
    @DisplayName("Do a guess on non-existant game")
    void notSupportedWordLength() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/guess")
                .queryParam("id", "-1")
                .queryParam("guess", "xxxxx");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> {
                    String errorMsg = mvcResult.getResponse().getErrorMessage();
                    assert errorMsg != null;
                    assert errorMsg.contains("Game Id Not Found");
                });
    }

}