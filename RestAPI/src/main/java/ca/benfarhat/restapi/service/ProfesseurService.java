package ca.benfarhat.restapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.benfarhat.restapi.model.Professeur;
import ca.benfarhat.restapi.repository.ProfesseurRepository;
import ca.benfarhat.restapi.repository.SalleRepository;

@Service
public class ProfesseurService implements IProfesseurService{
	
	@Autowired
	ProfesseurRepository professeurRepository;
	
	@Override
	public void ajouter(Professeur professeur) {
		professeurRepository.save(professeur);
		
	}

	@Override
	public void editer(Long id, Professeur professeur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retirer(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Professeur findById(Long id) {
		Professeur professeur = professeurRepository.findById(id).orElse(null);
		
		
		return null;
	}

	@Override
	public List<Professeur> findByNom(String nom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Professeur> findAll() {
		// TODO Auto-generated method stub
		return null;
	}



}
