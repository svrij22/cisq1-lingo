package nl.hu.cisq1.lingo.security.filter;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 10;
    private Map<String, Integer> attemptsCache = new HashMap<>();
    Timer timer = new Timer();

    public LoginAttemptService() {
        super();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                attemptsCache = new HashMap<>();
            }
        }, 24*60*60*1000);
    }

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (Exception e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Integer> getAttemptsCache() {
        return attemptsCache;
    }
}