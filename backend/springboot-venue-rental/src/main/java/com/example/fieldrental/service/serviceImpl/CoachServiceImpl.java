package com.example.fieldrental.service.serviceImpl;

import com.example.fieldrental.dto.CoachDto;
import com.example.fieldrental.entity.Coach;
import com.example.fieldrental.repository.CoachRepository;
import com.example.fieldrental.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("coachService")
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachRepository coachRepository ;

    @Override
    public Coach getById(long id) {
        return coachRepository.findById(id).orElse(new Coach());
    }

    @Override
    public Coach getByUserId(long userId) {
        Coach coachByUserId = coachRepository.getCoachByUserId(userId);
        if (coachByUserId == null) {
            throw new RuntimeException("Couldn't find" + userId + "in coachRepository");
        }
        else
        return coachByUserId;
    }

    @Override
    public Coach createCoach(CoachDto coachDto) {
        return null;
    }
}