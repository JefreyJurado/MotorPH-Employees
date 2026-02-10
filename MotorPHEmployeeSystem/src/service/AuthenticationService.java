package service;

import model.User;
import dao.UserRepository;

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