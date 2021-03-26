package nl.hu.cisq1.lingo.game.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.SecurityConfig;
import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

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
        gameService.getGameForUser("user4");
    }

    @Test
    @DisplayName("Request leaderboard")
    void requestNewGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/users/leaderboard")
                .header("Authorization", authToken);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content().string(containsString("{\"username\":\"user4\",\"score\":0.0}")))
                .andExpect(status().is2xxSuccessful());
    }

}