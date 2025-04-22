package com.example.fieldrental.service;

import com.example.fieldrental.dto.CoachDto;
import com.example.fieldrental.entity.Coach;

public interface CoachService {
    Coach getById(long id);

    Coach getByUserId(long userId);

    Coach createCoach(CoachDto coachDto);
}
