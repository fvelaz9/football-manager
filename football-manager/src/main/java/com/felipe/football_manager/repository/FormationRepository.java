package com.felipe.football_manager.repository;

import com.felipe.football_manager.domain.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormationRepository extends JpaRepository<Formation, Long> {
}

