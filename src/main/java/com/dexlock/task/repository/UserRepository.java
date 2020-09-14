package com.dexlock.task.repository;

import com.dexlock.task.models.Comments;
import com.dexlock.task.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;


public interface UserRepository extends JpaRepository<User, Long> {
  @Query(
          value = "Select * from users where username=?",
          nativeQuery = true
  )
  public User findByUsername(String username);
  Boolean existsByUsername(String username);
  Boolean existsByEmail(String email);


}
