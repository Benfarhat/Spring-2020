package ca.benfarhat.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.benfarhat.restapi.exception.ProfesseurException;
import ca.benfarhat.restapi.exception.SalleException;
import ca.benfarhat.restapi.model.Professeur;
import ca.benfarhat.restapi.model.Salle;
import ca.benfarhat.restapi.repository.ProfesseurRepository;
import ca.benfarhat.restapi.repository.SalleRepository;

@Service
public class SalleService implements IGenericService<Salle, SalleException, ProfesseurException>{
	
	@Autowired
	SalleRepository salleRepository;
	
	@Autowired
	ProfesseurRepository professeurRepository;

	@Override
	public Long ajouter(Salle salle) {
		return salleRepository.save(salle).getId();	
	}

	@Override
	public void editer(Salle salle) throws SalleException {
		salleRepository.saveAndFlush(salle);
	}

	@Override
	public void retirer(Long id) throws SalleException {
		if(!libre(id)) {
			throw new SalleException("Salle utilisée");
		}
		salleRepository.deleteById(id);	
	}

	@Override
	public Salle findById(Long id) throws SalleException {
		Salle salle = salleRepository.findById(id).orElse(null);
		if(salle == null) {
			throw new SalleException("Salle inexistante");
		} 
		return salle;
	}

	@Override
	public Salle findByName(String name) {
		return salleRepository.findByName(name).orElse(null);
	}

	@Override
	public List<Salle> findAll() {
		return salleRepository.findAll();
	}

	@Override
	public void reserver(Long salleId, Long profid) throws SalleException, ProfesseurException {
		Salle salle = findById(salleId);
		salle.setProfesseur(profid);
		editer(salle);
		
		if(!Objects.isNull(profid)) {
			Professeur professeur = professeurRepository.findById(profid).orElse(null);
			if(!Objects.isNull(professeur)) {
				professeur.setLastSalleId(salleId);
				professeurRepository.saveAndFlush(professeur);
			}
		}
	}

	@Override
	public boolean verifier(Long salleId, Long profid) throws SalleException {
		Salle salle = findById(salleId);
		return salle.getProfesseur().equals(profid);
	}

	@Override
	public boolean libre(Long salleId) throws SalleException {
		return findById(salleId).getProfesseur() == null;
	}

	@Override
	public void liberer(Long salleId) throws SalleException, ProfesseurException {
		reserver(salleId, null);		
	}

	@Override
	public List<Salle> findAllLibre() {
		return salleRepository.findByProfesseurNull().orElse(new ArrayList<Salle>());
	}
}
