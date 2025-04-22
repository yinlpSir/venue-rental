package com.example.fieldrental.repository;

import com.example.fieldrental.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachRepository extends JpaRepository<Coach , Long> {
    Coach getCoachByUserId(long userId);
}
