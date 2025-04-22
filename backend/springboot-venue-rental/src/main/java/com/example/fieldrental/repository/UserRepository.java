package com.example.fieldrental.repository;

import com.example.fieldrental.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository extends JpaRepository<User,Long> {

    User getUserById (long id);

    User getUserByUsername (String username);

    List<User> getUserByEmail (String email);

    Page<User> findAll(Pageable pageable);
}
