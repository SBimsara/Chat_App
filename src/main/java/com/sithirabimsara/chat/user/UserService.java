package com.sithirabimsara.chat.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepo userRepo;

    // Save user to database
    public User saveUser(User user) {
        user.setStatus(Status.ONLINE);
        return userRepo.save(user);
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
    public User disconnectUser(User user) {
        User exitingUser = userRepo.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        exitingUser.setStatus(Status.OFFLINE);
        return userRepo.save(exitingUser);

    }

    public List<User> findConnectedUsers() {
        List<User> connectedUsersList = userRepo.findUsersByStatus(Status.ONLINE);
        return connectedUsersList;
    }
}
