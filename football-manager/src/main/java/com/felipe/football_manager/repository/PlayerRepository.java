package com.felipe.football_manager.repository;

import com.felipe.football_manager.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}

