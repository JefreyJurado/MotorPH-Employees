package service;

import model.User;

/**
 * Interface for IT-specific operations
 * Only IT employees can perform these operations
 */
public interface IITOperations {
    
    /**
     * Manages system security settings and protocols
     */
    void manageSystemSecurity();
    
    /**
     * Performs routine system maintenance tasks
     */
    void performSystemMaintenance();
    
    /**
     * Creates backup of system database
     */
    void backupDatabase();
    
    /**
     * Resets a user's password
     * @param user The user whose password needs reset
     */
    void resetUserPassword(User user);
    
    /**
     * Configures system-wide settings
     */
    void configureSystemSettings();
    
    /**
     * Monitors system performance and health
     */
    void monitorSystemHealth();
}