package nl.hu.cisq1.lingo.game.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.SecurityConfigHelper;
import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

import static nl.hu.cisq1.lingo.game.domain.enums.GameState.STARTED;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    SpringUserRepository userRepository;
    @Autowired
    GameService gameService;

    String authToken;

    @BeforeAll
    public void setupUser() {
        this.authToken = SecurityConfigHelper.getAuthToken(userRepository);
    }

    @Test
    @DisplayName("Request a new game")
    void requestNewGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/game")
                .header("Authorization", authToken);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content().string(containsString("\"state\":\"STARTED\"")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Try guess at existing game")
    void testGuessExistingGameExpectWordLengthError() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/guess")
                .header("Authorization", authToken)
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
    @Transactional
    @DisplayName("Try right guess")
    void testRightGuess() throws Exception {
        String word = gameService.getGameForUser("user4").getCurrentRound().getWordToGuess().getValue();

        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/guess")
                .header("Authorization", authToken)
                .queryParam("guess", word);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(mvcResult -> {
                    assertTrue(mvcResult.getResponse().getContentAsString().contains(word));
                });
    }


    @Test
    @DisplayName("Reset game without finishing it")
    void testExpectErrorWhenResetting() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/game/reset")
                .header("Authorization", authToken);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> {
                    String errorMsg = mvcResult.getResponse().getErrorMessage();
                    assert errorMsg != null;
                    assert errorMsg.contains("Game is still running");
                });
    }

    @Test
    @DisplayName("Reset game after failing")
    @Transactional
    void notSupportedWordLength() throws Exception {

        /*Do guesses*/
        IntStream.range(0, 10).forEach(i -> {
            try{ gameService.gameDoGuess("user4", "xxxxx"); }catch (Exception ignored){}
            try{ gameService.gameDoGuess("user4", "xxxxxx"); }catch (Exception ignored){}
            try{ gameService.gameDoGuess("user4", "xxxxxxx"); }catch (Exception ignored){}
        });

        RequestBuilder request = MockMvcRequestBuilders
                .get("/game/reset")
                .header("Authorization", authToken);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        /*Check if game is reset*/
        /*Transactional required*/

        Game game = gameService.getGameForUser("user4");
        assert !game.getCurrentRound().isCorrect();
        assert game.getState() == STARTED;
    }

}