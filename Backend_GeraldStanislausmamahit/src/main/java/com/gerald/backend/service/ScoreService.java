package com.gerald.backend.service;

import com.gerald.backend.model.Score;
import com.gerald.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO: Tambahkan anotasi yang membuat Spring mengenali class ini sebagai service layer
@Service
public class ScoreService {

    // TODO: Tambahkan anotasi untuk melakukan Dependency Injection dari Instance yang sudah ada (ScoreRepository)
    @Autowired
    // TODO: Tambahkan private field untuk ScoreRepository
    private ScoreRepository scoreRepository;

    // TODO: buat public method createScore yang dapat menerima parameter Score dan mengembalikan score yang telah dibuat
    public Score createScore(Score score){
        return scoreRepository.save(score);
    }

    // TODO: buat public method getScoreByID yang dapat menerima parameter UUID dan mengembalikan score sesuai dengan scoreId yang diberikan dengan parameter
    public Optional<Score> getScoreByID(UUID scoreId){
        return scoreRepository.findById(scoreId);
    }

    public List<Score> getAllScores(){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database kemudian kembalikan hasilnya
        return scoreRepository.findAll();
    }

    public List<Score> getRecentScores(){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database dengan urutan pembuatan terbaru kemudian kembalikan hasilnya
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getScoreAboveValue(Integer minValue){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database yang memiliki point di atas nilai tertentu
        return scoreRepository.findByPointGreaterThan(minValue);
    }

    public List<Score> getLeaderboard(Integer limit) {
        // TODO: Gunakan scoreRepository untuk mencari Top Scores dan berikan parameter yang sesuai
        return scoreRepository.findAllByOrderByPointDesc(PageRequest.of(0, limit)).getContent();
    }

    public void deleteScore(UUID scoreId) {
        // 1. Cari score yang ingin dihapus menggunakan scoreRepository kemudian simpan score tersebut
        // 2. Cek apakah score tersebut ditemukan atau tidak dengan orElseThrow
        Score score = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new RuntimeException("Score dengan ID " + scoreId + " tidak ditemukan"));

        // 3. Panggil delete() dari scoreRepository untuk menghapus score yang disimpan tadi
        scoreRepository.delete(score);
    }
}