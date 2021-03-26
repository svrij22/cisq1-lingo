package nl.hu.cisq1.lingo.game.presentation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.SecurityConfig;
import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.game.data.GameRepository;
import nl.hu.cisq1.lingo.security.application.UserService;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
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
        this.authToken = SecurityConfig.getAuthToken(userRepository);
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
    void notSupportedWordLength() throws Exception {

        /*Do guesses*/
        IntStream.range(0, 10).forEach(i -> {

            /*TODO please remove this. Really ugly*/
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
    }

}