package com.felipe.football_manager.service;

import com.felipe.football_manager.domain.Player;
import com.felipe.football_manager.repository.PlayerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayer(Long id) {
        return playerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + id));
    }

    public Player createPlayer(Player player) {
        player.setId(null);
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, Player player) {
        Player existing = getPlayer(id);
        existing.setName(player.getName());
        existing.setPosition(player.getPosition());
        existing.setNumber(player.getNumber());
        return playerRepository.save(existing);
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}

