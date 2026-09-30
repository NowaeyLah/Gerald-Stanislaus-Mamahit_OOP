package com.gerald.backend.controller;

import com.gerald.backend.model.Score;
import com.gerald.backend.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO: tambah anotasi yang menandakan class ini merupakan REST API Controller
// TODO: tambahkan anotasi untuk mapping api dengan "/api/scores"
@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {

    // TODO: Tambahkan anotasi untuk melakukan Dependency Injection dari Instance yang sudah ada (ScoreService)
    @Autowired
    // TODO: Tambahkan private field untuk ScoreService
    private ScoreService scoreService;

    // GET /api/scores/{scoreId}
    // TODO: tambahkan anotasi untuk maps HTTP GET ke method ini dengan path "/{scoreId}"
    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById(@PathVariable UUID scoreId) {
        // TODO: buat variabel score untuk menyimpan score yang didapat dari scoreService
        Optional<Score> score = scoreService.getScoreByID(scoreId);

        // cek apakah variabel score ada isinya dengan isPresent()
        if (score.isPresent()) {
            // jika ya kembalikan score yang dipost
            return ResponseEntity.ok(score.get());
        } else {
            // jika tidak maka kembalikan status NOT_FOUND dengan keterangan body error yang sesuai
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score with ID " + scoreId + " not found");
        }
    }

    // POST /api/scores
    // TODO: Tambahkan anotsi untuk maps HTTP POST ke method ini
    @PostMapping
    public ResponseEntity<?> createScore(
            /* TODO: berikan @RequestBody untuk bind JSON body dari request ke object score di sini */
            @RequestBody Score score){
        try{
            // TODO: Buat instance score baru menggunakakan scoreService dengan data yang ada dari parameter
            Score newScore = scoreService.createScore(score);

            // TODO: kembalikan response data score baru dengan status CREATED
            return ResponseEntity.status(HttpStatus.CREATED).body(newScore);
        } catch (RuntimeException e){
            // TODO: kembalikan response error dengan status BAD_REQUEST beserta body error yang sesuai
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create score: " + e.getMessage());
        }
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping
    public ResponseEntity<List<Score>> getAllScores() {
        // 2. Gunakan scoreService untuk memanggil getAllScores() dan simpan scores tersebut ke suatu variabel menggunakan List
        List<Score> scores = scoreService.getAllScores();

        // 3. Kembalikan variabel berisi scores tersebut
        return ResponseEntity.ok(scores);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboardByPoint(
            // 2. tambahkan parameter'@RequestParam' dengan defaultValue 10
            // 3. serta Integer limit
            @RequestParam(defaultValue = "10") Integer limit) {

        // 4. Gunakan scoreService untuk memanggil getLeaderboard() dengan parameter yang sesuai
        //    dan simpan scores tersebut ke suatu variabel menggunakan List
        List<Score> scores = scoreService.getLeaderboard(limit);

        // 5. Kembalikan variabel berisi scores tersebut
        return ResponseEntity.ok(scores);
    }

    // Soal 8 – Buat endpoint GET /api/scores/above/{minValue}
    @GetMapping("/above/{minValue}")
    public ResponseEntity<List<Score>> getScoresAboveValue(
            @PathVariable Integer minValue) {
        List<Score> scores = scoreService.getScoreAboveValue(minValue);
        return ResponseEntity.ok(scores);
    }

    // Soal 9 – Buat endpoint GET /api/scores/recent
    @GetMapping("/recent")
    public ResponseEntity<List<Score>> getRecentScores(){
        List<Score> scores = scoreService.getRecentScores();
        return ResponseEntity.ok(scores);
    }

    // Soal 10 – Buat endpoint DELETE /api/scores/{scoreId}
    @DeleteMapping("/{scoreId}")
    public ResponseEntity<?> deleteScore(
            @PathVariable UUID scoreId) {
        try {
            scoreService.deleteScore(scoreId);
            return ResponseEntity.ok().body("Score berhasil dihapus");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}