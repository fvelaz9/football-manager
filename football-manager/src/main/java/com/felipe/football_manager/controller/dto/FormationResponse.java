package com.felipe.football_manager.controller.dto;

import com.felipe.football_manager.domain.Formation;
import com.felipe.football_manager.domain.FormationPlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormationResponse {
    private Long id;
    private String name;
    private List<PlayerDto> players;

    public static FormationResponse fromEntity(Formation formation) {
        List<PlayerDto> players =
                formation.getFormationPlayers().stream()
                        .map(FormationPlayer::getPlayer)
                        .map(PlayerDto::fromEntity)
                        .collect(Collectors.toList());

        return FormationResponse.builder()
                .id(formation.getId())
                .name(formation.getName())
                .players(players)
                .build();
    }
}
