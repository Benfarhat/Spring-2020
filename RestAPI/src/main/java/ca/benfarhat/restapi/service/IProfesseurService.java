package ca.benfarhat.restapi.service;

import java.util.List;

import ca.benfarhat.restapi.model.Professeur;

public interface IProfesseurService {
	
	void ajouter(Professeur professeur);
	void editer(Long id, Professeur professeur);
	void retirer(Long id);
	
	Professeur findById(Long id);
	List<Professeur> findByNom(String nom);
	List<Professeur> findAll();

}
