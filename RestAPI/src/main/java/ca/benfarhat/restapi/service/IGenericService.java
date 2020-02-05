package ca.benfarhat.restapi.service;

import java.util.List;

public interface IGenericService<T, S extends Exception, U extends Exception> {

	Long ajouter(T entity);
	void editer(T entity) throws S;
	void retirer(Long id) throws S;
	
	T findById(Long id) throws S;
	T findByName(String name);
	List<T> findAll();
	List<T> findAllLibre();
	
	void reserver(Long entityId, Long foreignId) throws S, U;
	boolean verifier(Long entityId, Long foreignId) throws S;
	boolean libre(Long entityId) throws S;
	void liberer(Long entityId) throws S, U;
}
