package ca.benfarhat.restapi.model;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@Entity
@Table(name = "classes")
public class Salle extends AbstractEntity{
    
    @Column(name = "currentProfessorId")
	private Long professeur;
    
    @Column(name = "listProfessorId")
    @ElementCollection(targetClass=Long.class, fetch = FetchType.EAGER)
	private List<Long> professeurs;

	public Long getProfesseur() {
		return professeur;
	}

	public void setProfesseur(Long professeur) {
		this.professeur = professeur;
	}

	public List<Long> getProfesseurs() {
		return professeurs;
	}

	public void setProfesseurs(List<Long> professeurs) {
		this.professeurs = professeurs;
	}
	
	public Salle(String name) {
		super();
		this.name = name;
		this.professeur = null;
		this.professeurs = new ArrayList<>();
	}
	
	public Salle(String name, Long professeur, List<Long> professeurs) {
		super();
		this.name = name;
		this.professeur = professeur;
		this.professeurs = professeurs;
	}
	
	

	public Salle(String name, String description, Long professeur, List<Long> professeurs) {
		super();
		this.name = name;
		this.description = description;
		this.professeur = professeur;
		this.professeurs = professeurs;
	}

	public Salle() {
		super();
	}

	@Override
	public String toString() {
		return "Salle [id=" + id + ", name=" + name + ", description=" + description + ", professeur=" + professeur
				+ "]";
	}

}
