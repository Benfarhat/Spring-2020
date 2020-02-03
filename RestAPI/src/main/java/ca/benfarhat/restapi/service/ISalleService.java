package ca.benfarhat.restapi.service;

import java.util.List;

import ca.benfarhat.restapi.exception.SalleException;
import ca.benfarhat.restapi.model.Salle;

public interface ISalleService {

	Long ajouter(Salle salle);
	void editer(Long id, Salle salle) throws SalleException;
	void retirer(Long id) throws SalleException;
	
	Salle findById(Long id) throws SalleException;
	Salle findByName(String name);
	List<Salle> findAll();
	List<Salle> findAllLibre();
	
	void reserver(Long salleId, Long profid) throws SalleException;
	boolean verifier(Long salleId, Long profid) throws SalleException;
	boolean libre(Long salleId) throws SalleException;
	void liberer(Long salleId) throws SalleException;
}
