package nl.hu.cisq1.lingo.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.security.filter.UserService;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import nl.hu.cisq1.lingo.security.data.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtFilterIntegrationTest {

    @Autowired UserService userService;
    @Autowired SpringUserRepository userRepository;
    @Autowired MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @ParameterizedTest
    @DisplayName("User can login")
    @MethodSource("randomUsers")
    void testUserCanLogin(String username, String password) throws Exception {

        /*Make sure user exists*/
        removeUser(username);
        registerUser(username, password);

        /*Create userdto*/
        Map<String,String> authdto = new HashMap<>();
        authdto.put("username", username);
        authdto.put("password", password);

        /*Perform post with form data*/
        mockMvc.perform(post("/login")
                .content(mapper.writeValueAsString(authdto))
                .with(request->{request.setRemoteAddr("192.168.0.1");return request;}) /*Required as not to get blocked*/
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String header = mvcResult.getResponse().getHeader("Authorization");
                    System.out.println(header);
                    assert header != null && header.contains("Bearer");
                });
    }

    @Test
    @DisplayName("Wrong password")
    void testUserWrongPassword() throws Exception {

        /*Make sure user exists*/
        removeUser("testuser");
        registerUser("testuser", "testpassword1");

        /*Create userdto*/
        Map<String,String> authdto = new HashMap<>();

        authdto.put("username", "testuser");
        authdto.put("password", "wrongpass");

        /*Perform post with form data*/
        mockMvc.perform(post("/login")
                .content(mapper.writeValueAsString(authdto))
                .with(request->{request.setRemoteAddr("192.168.0.2");return request;})
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("User gets blocked")
    void testUserGetsBlocked() throws Exception {

        /*Make sure user exists*/
        removeUser("testuser");
        registerUser("testuser", "testpassword1");

        /*Create userdto*/
        Map<String,String> authdto = new HashMap<>();
        authdto.put("username", "testuser");
        authdto.put("password", "wrongpass");

        /*10 wrong attempts*/
        IntStream.range(0, 10).forEach((n) -> {
            System.out.println("Attempt " + n);

            try {
                mockMvc.perform(post("/login")
                        .content(mapper.writeValueAsString(authdto))
                        .with(request->{request.setRemoteAddr("192.168.0.3");return request;})
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //right passw
        authdto.put("password", "testpassword1");

        /*Perform post with form data*/
        assertThrows(RuntimeException.class, () -> {
            mockMvc.perform(post("/login")
                    .content(mapper.writeValueAsString(authdto))
                    .with(request->{request.setRemoteAddr("192.168.0.3");return request;})
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        });
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