package com.felipe.football_manager.controller;

import com.felipe.football_manager.controller.dto.FormationDtos;
import com.felipe.football_manager.controller.dto.FormationResponse;
import com.felipe.football_manager.domain.Formation;
import com.felipe.football_manager.service.FormationService;
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
@RequestMapping("/api/formations")
@RequiredArgsConstructor
public class FormationController {

    private final FormationService formationService;

    @GetMapping
    public List<FormationResponse> getAll() {
        return formationService.getAllFormations().stream()
                .map(FormationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FormationResponse getById(@PathVariable Long id) {
        try {
            Formation formation = formationService.getFormation(id);
            return FormationResponse.fromEntity(formation);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<FormationResponse> create(@RequestBody FormationDtos request) {
        try {
            Formation created =
                    formationService.createFormation(request.getName(), request.getPlayerIds());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(FormationResponse.fromEntity(created));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public FormationResponse update(@PathVariable Long id, @RequestBody FormationDtos request) {
        try {
            Formation updated =
                    formationService.updateFormation(id, request.getName(), request.getPlayerIds());
            return FormationResponse.fromEntity(updated);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formationService.deleteFormation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/players/{playerId}")
    public ResponseEntity<FormationResponse> removePlayer(
            @PathVariable Long id, @PathVariable Long playerId) {
        try {
            Formation updated = formationService.removePlayerFromFormation(id, playerId);
            return ResponseEntity.ok(FormationResponse.fromEntity(updated));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}

