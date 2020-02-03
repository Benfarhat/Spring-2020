package ca.benfarhat.restapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.benfarhat.restapi.exception.SalleException;
import ca.benfarhat.restapi.model.Salle;
import ca.benfarhat.restapi.repository.SalleRepository;

@Service
public class SalleService implements ISalleService{
	
	@Autowired
	SalleRepository salleRepository;

	@Override
	public Long ajouter(Salle salle) {
		return salleRepository.save(salle).getId();	
	}

	@Override
	public void editer(Long id, Salle salle) throws SalleException {
		Salle salleAModifier = findById(id);
		salleAModifier.copy(salle);
		salleRepository.flush();
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
	public void reserver(Long salleId, Long profid) throws SalleException {
		Salle salle = findById(salleId);
		salle.setProfesseur(profid);
		editer(salleId, salle);
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
	public void liberer(Long salleId) throws SalleException {
		reserver(salleId, null);		
	}

	@Override
	public List<Salle> findAllLibre() {
		return salleRepository.findByProfesseurNull().orElse(new ArrayList<Salle>());
	}
}
