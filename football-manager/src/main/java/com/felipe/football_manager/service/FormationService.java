package com.felipe.football_manager.service;

import com.felipe.football_manager.domain.Formation;
import com.felipe.football_manager.domain.FormationPlayer;
import com.felipe.football_manager.domain.Player;
import com.felipe.football_manager.repository.FormationPlayerRepository;
import com.felipe.football_manager.repository.FormationRepository;
import com.felipe.football_manager.repository.PlayerRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormationService {


    private final FormationRepository formationRepository;
    private final PlayerRepository playerRepository;
    private final FormationPlayerRepository formationPlayerRepository;

    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    public Formation getFormation(Long id) {
        return formationRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Formation not found: " + id));
    }

    @Transactional
    public Formation createFormation(String name, List<Long> playerIds) {

        List<Player> players = getPlayersByIds(playerIds);
        Formation formation = Formation.builder().name(name).build();

        List<FormationPlayer> links = new ArrayList<>();
        for (Player player : players) {
            FormationPlayer link =
                    FormationPlayer.builder().formation(formation).player(player).build();
            links.add(link);
        }
        formation.setFormationPlayers(links);

        return formationRepository.save(formation);
    }

    @Transactional
    public Formation updateFormation(Long id, String name, List<Long> playerIds) {

        Formation formation = getFormation(id);
        List<Player> players = getPlayersByIds(playerIds);

        formation.setName(name);
        formation.getFormationPlayers().clear();

        List<FormationPlayer> links = new ArrayList<>();
        for (Player player : players) {
            FormationPlayer link =
                    FormationPlayer.builder().formation(formation).player(player).build();
            links.add(link);
        }
        formation.getFormationPlayers().addAll(links);

        return formationRepository.save(formation);
    }

    public void deleteFormation(Long id) {
        formationRepository.deleteById(id);
    }

    @Transactional
    public Formation removePlayerFromFormation(Long formationId, Long playerId) {
        Formation formation = getFormation(formationId);
        formation.getFormationPlayers().removeIf(fp -> fp.getPlayer().getId().equals(playerId));
        return formationRepository.save(formation);
    }



    private List<Player> getPlayersByIds(List<Long> playerIds) {
        List<Player> players = playerRepository.findAllById(playerIds);
        if (players.size() != playerIds.size()) {
            throw new IllegalArgumentException("One or more players do not exist.");
        }
        return players;
    }
}

