package com.felipe.football_manager.repository;

import com.felipe.football_manager.domain.FormationPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormationPlayerRepository extends JpaRepository<FormationPlayer, Long> {
    void deleteAllByPlayerId(Long playerId);
}

