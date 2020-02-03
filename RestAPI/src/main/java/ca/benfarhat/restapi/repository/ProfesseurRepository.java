package ca.benfarhat.restapi.repository;

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

}
