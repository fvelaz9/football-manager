package com.felipe.football_manager.controller;

import com.felipe.football_manager.controller.dto.PlayerDto;
import com.felipe.football_manager.domain.Player;
import com.felipe.football_manager.service.PlayerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public List<PlayerDto> getAll() {
        return playerService.getAllPlayers().stream()
                .map(PlayerDto::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PlayerDto getById(@PathVariable Long id) {
        try {
            Player player = playerService.getPlayer(id);
            return PlayerDto.fromEntity(player);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<PlayerDto> create(@RequestBody PlayerDto request) {
        Player created = playerService.createPlayer(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerDto.fromEntity(created));
    }

    @PutMapping("/{id}")
    public PlayerDto update(@PathVariable Long id, @RequestBody PlayerDto request) {
        try {
            Player updated = playerService.updatePlayer(id, request.toEntity());
            return PlayerDto.fromEntity(updated);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}

