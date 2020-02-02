package ca.benfarhat.restapi.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entité <b>Salle</b>, relative aux salles d'enseignement
 * utilisé par les {@link Professeur }
 * 
 * @author Benfarhat Elyes
 * 
 * @apiNote Classe ajouter pour la gestion des paramètres utilisateurs
 *          dans la version 1.0.
 * @implSpec Ce simple POJO contient une trace du professeur qui l'utilise actuellement.
 * @implNote les ids de professeur peuvent être null
 * 
 * @since 2020-02-01
 * @version 1.0.0
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "classes")
public class Salle {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
	private Long id;

    @NotNull
    @Column(name = "nom")
	private String name;
    
    @Column(name = "currentProfessorId")
	private Long CurrentProfesseurId;
    

    @Column(name = "lastProfessorId")
	private Long LastProfesseurId;

}
