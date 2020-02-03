package ca.benfarhat.restapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.benfarhat.restapi.RestApiApplication;
import ca.benfarhat.restapi.exception.SalleException;
import ca.benfarhat.restapi.model.Salle;
import ca.benfarhat.restapi.repository.SalleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes=RestApiApplication.class)
class SalleServiceTest {
	
	@Autowired
	SalleRepository salleRepository;
	
	@Autowired
	SalleService salleService;
	
	@BeforeEach
	void setUp() throws Exception {
		IntStream.range(0, 100).forEach(
				nb -> System.out.println(salleService.ajouter(new Salle(String.join(" ", "Salle", String.valueOf(nb)))))
			);
		
	}
	
	@Test
	void contextLoads() {
		assertThat(salleService).isNotNull();
	}

	@Test
	void testAjouter() throws SalleException {
		String name = "Test";
		assertThat(salleService.findAll().size()).isEqualTo(100);
		Long id = salleService.ajouter(new Salle(name));
		
		Salle salle = salleService.findById(id);
		assertThat(salle).isNotNull();
		assertThat(salle.getId()).isEqualTo(id);
		assertThat(salle.getName()).isEqualTo(name);
		assertThat(salle.getProfesseur()).isNull();
		assertThat(salle.getProfesseurs()).isEmpty();
	}

	@Test
	void testEditer() {
		fail("Not yet implemented");
	}

	@Test
	void testRetirer() {
		fail("Not yet implemented");
	}

	@Test
	void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByNom() {
		fail("Not yet implemented");
	}

	@Test
	void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	void testReserver() {
		fail("Not yet implemented");
	}

	@Test
	void testVerifier() {
		fail("Not yet implemented");
	}

	@Test
	void testLibre() {
		fail("Not yet implemented");
	}

	@Test
	void testLiberer() {
		fail("Not yet implemented");
	}

	@Test
	void testFindAllLibre() {
		fail("Not yet implemented");
	}

}
