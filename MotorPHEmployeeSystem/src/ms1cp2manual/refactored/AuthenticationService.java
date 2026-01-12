package ms1cp2manual.refactored;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private Map<String, String> userCredentials;

    public AuthenticationService() {
        userCredentials = new HashMap<>();
        initializeCredentials();
    }

    private void initializeCredentials() {
        userCredentials.put("aljohn", "aljohn123");
        userCredentials.put("michael", "michael123");
    }
    // Check if credentials are valid
    public boolean authenticate(String username, String password) {
        return userCredentials.containsKey(username) 
            && userCredentials.get(username).equals(password);
    }

    public void addUser(String username, String password) {
        userCredentials.put(username, password);
    }
}