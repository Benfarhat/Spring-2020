package ca.benfarhat.restapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité <b>Professeur</b>
 * Au niveau métierm les professeurs agiront sur {@link Salle.class }
 * 
 * @author Benfarhat Elyes
 * 
 * @apiNote Classe ajouter pour la gestion des paramètres utilisateurs
 *          dans la version 1.0.
 * @implSpec Ce simple POJO contient une trace du professeur qui l'utilise actuellement.
 * @implNote l'id d'une salle ne veut pas dire que cette dernière est libre
 * 
 * @since 2020-02-01
 * @version 1.0.0
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "professor")
public class Professeur extends AbstractEntity{

    @Column(name = "lastProfessorId")
	private Long lastSalleId;

	public Long getLastSalleId() {
		return lastSalleId;
	}

	public void setLastSalleId(Long lastSalleId) {
		this.lastSalleId = lastSalleId;
	}

}
