package com.sithirabimsara.chat.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findUsersByStatus(Status status);
}
