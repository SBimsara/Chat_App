package com.sithirabimsara.chat.user;

import java.util.List;

public class UserService {

    private UserRepo userRepo;

    // Save user to database
    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        userRepo.save(user);
    }

    public void deleteUser(User user) {
        // Delete user from database
    }

    public User getUser(String id) {
        // Get user from database
        return null;
    }

    public void updateUser(User user) {
        // Update user in database
    }

    // Disconnect user from chat
    public void disconnectUser(User user) {
        User exitingUser = userRepo.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        exitingUser.setStatus(Status.OFFLINE);
        userRepo.save(exitingUser);

    }

    public List<User> findConnectedUsers() {
        List<User> connectedUsersList = userRepo.findUsersByStatus(Status.ONLINE);
        return connectedUsersList;
    }
}
