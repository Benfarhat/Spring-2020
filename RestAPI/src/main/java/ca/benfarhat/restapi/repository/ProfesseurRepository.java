package ca.benfarhat.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.benfarhat.restapi.model.Professeur;

/**
 * @author Benfarhat Elyes
 * 
 * @since 2020-02-01
 * @version 1.0.0
 *
 */
@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long>{
	Optional<List<Professeur>> findByLastSalleIdNull();
	Optional<Professeur> findByName(String name);
}
