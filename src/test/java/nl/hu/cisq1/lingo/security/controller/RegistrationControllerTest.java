package nl.hu.cisq1.lingo.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.security.filter.UserService;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import nl.hu.cisq1.lingo.security.data.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerTest {

    @Autowired UserService userService;
    @Autowired SpringUserRepository userRepository;
    @Autowired MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @ParameterizedTest
    @DisplayName("Creates a new user")
    @MethodSource("randomUsers")
    void testRegisterService(String username, String password) throws Exception {

        /*Make sure user doesn't exist*/
        removeUser(username);

        /*Create userdto*/
        Map<String,String> authdto = new HashMap<>();
        authdto.put("username", username);
        authdto.put("password", password);

        /*Perform post with form data*/
        mockMvc.perform(post("/register")
                .content(mapper.writeValueAsString(authdto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User user = userService.loadUserByUsername(username);
        assert user!=null;

    }

    @ParameterizedTest
    @DisplayName("User already exists")
    @MethodSource("randomUsers")
    void testRegisterServiceAlreadyExists(String username, String password) throws Exception {

        /*Make sure user exists*/
        removeUser(username);
        registerUser(username, password);

        /*Create userdto*/
        Map<String,String> authdto = new HashMap<>();
        authdto.put("username", username);
        authdto.put("password", password);

        /*Perform post with form data*/
        mockMvc.perform(post("/register")
                .content(mapper.writeValueAsString(authdto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    public void removeUser(String username){
        try{
            User user = userService.loadUserByUsername(username);
            userRepository.delete(user);
        }catch (Exception ignored){}
    }

    public void registerUser(String username, String password){
        try{
            userService.register(username,password);
        }catch (Exception ignored){}
    }

    static Stream<Arguments> randomUsers() {
        return Stream.of(
                Arguments.of("user1", "pwd55"),
                Arguments.of("user2", "pwd55"),
                Arguments.of("user3", "pwd55"),
                Arguments.of("user4", "pwd55"),
                Arguments.of("user5", "pwd55")
        );
    }
}