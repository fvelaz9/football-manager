package com.felipe.football_manager.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormationDtos {
    private String name;
    private List<Long> playerIds;
}

