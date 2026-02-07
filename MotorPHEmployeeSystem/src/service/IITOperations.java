package service;

import model.User;

//Access restricted to IT personnel.
public interface IITOperations {    
    void manageSystemSecurity();    
    void performSystemMaintenance();   
    void backupDatabase();   
    void resetUserPassword(User user);    
    void configureSystemSettings();    
    void monitorSystemHealth();
}
