package com.gerald.backend.service;

import com.gerald.backend.model.Score;
import com.gerald.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    public Score createScore(Score score){
        return scoreRepository.save(score);
    }

    public Optional<Score> getScoreByID(UUID scoreId){
        return scoreRepository.findById(scoreId);
    }

    public List<Score> getAllScores(){
        return scoreRepository.findAll();
    }

    public List<Score> getRecentScores(){
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getScoreAboveValue(Integer minValue){
        return scoreRepository.findByPointGreaterThan(minValue);
    }

    public List<Score> getLeaderboard(Integer limit) {
        return scoreRepository.findTopScores(limit);
    }

    public void deleteScore(UUID scoreId) {
        Score score = scoreRepository.findById(scoreId).orElseThrow(() -> new RuntimeException("Score dengan ID " + scoreId + " tidak ditemukan"));
        scoreRepository.delete(score);
    }
}