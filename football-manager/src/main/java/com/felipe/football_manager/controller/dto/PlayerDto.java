package com.felipe.football_manager.controller.dto;

import com.felipe.football_manager.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {

    private Long id;
    private String name;
    private String position;
    private Integer number;

    public static PlayerDto fromEntity(Player player) {
        return PlayerDto.builder()
                .id(player.getId())
                .name(player.getName())
                .position(player.getPosition())
                .number(player.getNumber())
                .build();
    }

    public Player toEntity() {
        return Player.builder().id(id).name(name).position(position).number(number).build();
    }
}

