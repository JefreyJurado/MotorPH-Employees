package dao;

import model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String USER_FILE = "users.csv";
    private final List<User> users;
    
    public UserRepository() {
        this.users = new ArrayList<>();
        loadFromCSV();
    }
    
    private void loadFromCSV() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            createDefaultCSV();
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { 
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4) {
                        String username = parts[0];
                        String password = parts[1];
                        String role = parts[2];
                        String employeeNumber = parts[3].isEmpty() ? null : parts[3];
                        
                        users.add(new User(username, password, role, employeeNumber));
                    }
                }
            }
        } catch (IOException e) {
        }
    }
    
    private void createDefaultCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            writer.println("Username,Password,Role,EmployeeNumber");
            // Default users
            writer.println("admin,admin123,Admin,");
            writer.println("hruser,hr123,HR,10001");
            writer.println("manager,manager123,HR,10002");
            writer.println("employee1,emp123,Employee,1");
            writer.println("employee2,emp456,Employee,2");
            writer.println("employee3,emp789,Employee,3");
            
            // Reload after creating
            loadFromCSV();
        } catch (IOException e) {
      }
    }
    
    public void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            writer.println("Username,Password,Role,EmployeeNumber");
            
            for (User user : users) {
                writer.printf("%s,%s,%s,%s%n",
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole(),
                    user.getEmployeeNumber() != null ? user.getEmployeeNumber() : ""
                );
            }
        } catch (IOException e) {
        }
    }
    
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public void addUser(User user) {
        users.add(user);
        saveToCSV();
    }
    
    public void updateUser(int index, User user) {
        if (index >= 0 && index < users.size()) {
            users.set(index, user);
            saveToCSV();
        }
    }
    
    public void deleteUser(int index) {
        if (index >= 0 && index < users.size()) {
            users.remove(index);
            saveToCSV();
        }
    }
    
    public boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }
}