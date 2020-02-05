package ca.benfarhat.restapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.benfarhat.restapi.exception.ProfesseurException;
import ca.benfarhat.restapi.exception.SalleException;
import ca.benfarhat.restapi.model.Professeur;
import ca.benfarhat.restapi.repository.ProfesseurRepository;

@Service
public class ProfesseurService implements IGenericService<Professeur, ProfesseurException, SalleException>{

	@Autowired
	ProfesseurRepository professeurRepository;

	@Autowired
	SalleService salleService;

	@Override
	public Long ajouter(Professeur professeur) {
		return professeurRepository.save(professeur).getId();	
	}

	@Override
	public void editer(Professeur professeur) throws ProfesseurException {
		professeurRepository.saveAndFlush(professeur);
	}

	@Override
	public void retirer(Long id) throws ProfesseurException {
		if(!libre(id)) {
			throw new ProfesseurException("Professeur utilisé");
		}
		professeurRepository.deleteById(id);	
	}

	@Override
	public Professeur findById(Long id) throws ProfesseurException {
		Professeur professeur = professeurRepository.findById(id).orElse(null);
		if(professeur == null) {
			throw new ProfesseurException("Professeur inexistant");
		} 
		return professeur;
	}

	@Override
	public Professeur findByName(String name) {
		return professeurRepository.findByName(name).orElse(null);
	}

	@Override
	public List<Professeur> findAll() {
		return professeurRepository.findAll();
	}

	@Override
	public void reserver(Long professeurId, Long salleId) throws ProfesseurException, SalleException {
		Professeur professeur = findById(professeurId);
		professeur.setLastSalleId(salleId);
		editer(professeur);
		salleService.reserver(salleId, professeurId);
	}

	@Override
	public boolean verifier(Long professeurId, Long salleId) throws ProfesseurException {
		Professeur professeur = findById(professeurId);
		return professeur.getLastSalleId().equals(salleId);
	}

	@Override
	public boolean libre(Long professeurId) throws ProfesseurException {
		return findById(professeurId).getLastSalleId() == null;
	}

	@Override
	public void liberer(Long professeurId) throws ProfesseurException, SalleException {
		reserver(professeurId, null);		
	}

	@Override
	public List<Professeur> findAllLibre() {
		return professeurRepository.findByLastSalleIdNull().orElse(new ArrayList<Professeur>());
	}
}
