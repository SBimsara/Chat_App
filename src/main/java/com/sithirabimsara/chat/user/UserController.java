package com.sithirabimsara.chat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @MessageMapping("/user.saveUser")
    @SendTo("/user/topic")
    public User saveUser(@Payload User user) {
        return userService.saveUser(user);
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public User disconnectUser(@Payload User user) {
        return userService.disconnectUser(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

}
