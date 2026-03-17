package com.felipe.football_manager.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Indica que esta clase es una tabla en la base de datos
@Entity 
// Configura el nombre de la tabla y añade una restricción: 
// No puede haber dos filas con la misma combinación de formación y jugador
@Table(
        name = "formation_players",
        uniqueConstraints = @UniqueConstraint(columnNames = {"formation_id", "player_id"}))
// LOMBOK: Genera automáticamente Getters, Setters, toString, equals y hashCode
@Data 
// LOMBOK: Crea un constructor vacío (obligatorio para que JPA funcione)
@NoArgsConstructor 
// LOMBOK: Crea un constructor con todos los atributos de la clase
@AllArgsConstructor 
// LOMBOK: Permite crear objetos de forma fluida: FormationPlayer.builder().player(p).build()
@Builder 
public class FormationPlayer {

    // Define la llave primaria (PK)
    @Id 
    // Indica que el ID se genera solo en la DB (Auto-increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    // Relación: Muchos registros de esta tabla apuntan a una sola formación
    @ManyToOne 
    // Crea la columna FK física llamada "formation_id"
    @JoinColumn(name = "formation_id", nullable = false) 
    // LOMBOK: Evita que esta relación aparezca en el método toString (evita bucles infinitos)
    @ToString.Exclude 
    // LOMBOK: Evita usar este objeto para comparar igualdad (mejora rendimiento/estabilidad)
    @EqualsAndHashCode.Exclude 
    private Formation formation;

    // Relación: Muchos registros de esta tabla apuntan a un solo jugador
    @ManyToOne 
    // Crea la columna FK física llamada "player_id"
    @JoinColumn(name = "player_id", nullable = false) 
    private Player player;
}

