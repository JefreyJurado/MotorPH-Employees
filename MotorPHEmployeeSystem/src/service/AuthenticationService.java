package service;

import model.User;
import repository.UserRepository;

public class AuthenticationService {
    private final UserRepository userRepository;
    
    public AuthenticationService() {
        this.userRepository = new UserRepository();
    }
    
    public User authenticate(String username, String password) {
        return userRepository.authenticate(username, password);
    }
    
    public UserRepository getUserRepository() {
        return userRepository;
    }
}