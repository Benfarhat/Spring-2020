package ca.benfarhat.restapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.benfarhat.restapi.RestApiApplication;
import ca.benfarhat.restapi.config.H2Config;
import ca.benfarhat.restapi.exception.SalleException;
import ca.benfarhat.restapi.model.Salle;
import ca.benfarhat.restapi.repository.SalleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RestApiApplication.class,
	H2Config.class})
class SalleServiceTest {
	
	@Autowired
	SalleRepository salleRepository;
	
	@Autowired
	SalleService salleService;
	
	private List<Salle> salles = new ArrayList<>();
	
	@BeforeClass
	void init() throws Exception {
		for(int i = 1; i < 100; i++) {
			Salle salle = new Salle("Salle " + i);
			salle.setId((long) i);
			salles.add(salle);
		}
		
		salleRepository.saveAll(salles);
		System.out.println("----> " + salleRepository.count());
		
	}	
	
	@BeforeEach
	void setUp() throws SalleException {
		salles = salleService.findAll();
		
	}
	
	@Test
	void testContext() {
		assertThat(salleService).isNotNull();
	}

	@Test
	void testAjouter() throws SalleException {
		String name = "Test";
		Long id = salleService.ajouter(new Salle(name));
		
		Salle salle = salleService.findById(id);
		assertThat(salle).isNotNull();
		assertThat(salle.getId()).isEqualTo(id);
		assertThat(salle.getName()).isEqualTo(name);
		assertThat(salle.getProfesseur()).isNull();
		assertThat(salle.getProfesseurs()).isEmpty();
	}

	@Test
	void testEditer() throws SalleException {
		String name = "Lorem Ipsum";
		assertThat(salleService.findByName(name)).isNull();
		
		Salle salle = salleService.findById(30L);
		salle.setName(name);
		salleService.editer(30L, salle);
		

		assertThat(salleService.findByName(name)).isNotNull();
		
	}

	@Test
	void testRetirer() throws SalleException {
		Long id = salles.get(0).getId();
		assertThat(salleService.findById(id)).isNotNull();
		salleService.retirer(id);
		assertThat(salleService.findById(id)).isNull();
	}

	@Test
	void testFindById() throws SalleException {
		Long id = salles.get(0).getId();
		assertThat(salleService.findById(id)).isNotNull();
		salles.stream().forEach(s -> System.out.println(s.getId()));
		
		salles.stream().map(Salle::getId).forEach(
				index -> assertThatCode(() -> salleService.findById(index)).doesNotThrowAnyException());
		
	}

	@Test
	void testFindByName() {
		String name = "Lorem Ipsum";
		salleService.ajouter(new Salle(name));
		assertThat(salleService.findByName(name)).isNotNull();
	}

	@Test
	void testFindAll() {
		assertThat(salleService.findAll().size()).isEqualTo(100);
	}

	@Test
	void testReserver() throws SalleException {
		salleService.reserver(50L, 16L);
		assertThat(salleService.findById(50L).getProfesseur()).isEqualTo(16L);
	}

	@Test
	void testVerifier() throws SalleException {
		salleService.reserver(50L, 16L);
		assertThat(salleService.verifier(50L, 17L)).isFalse();
		assertThat(salleService.verifier(51L, 16L)).isFalse();
		assertThat(salleService.verifier(50L, 16L)).isTrue();
	}

	@Test
	void testLibre() throws SalleException {
		assertThat(salleService.libre(50L)).isTrue();
		salleService.reserver(50L, 16L);
		assertThat(salleService.libre(50L)).isFalse();
	}

	@Test
	void testLiberer() throws SalleException {
		assertThat(salleService.libre(50L)).isTrue();
		salleService.reserver(50L, 16L);
		assertThat(salleService.libre(50L)).isFalse();
		salleService.liberer(50L);
		assertThat(salleService.libre(50L)).isTrue();
	}

	@Test
	void testFindAllLibre() {
		assertThat(salleService.findAllLibre().size()).isEqualTo(100);
	}

}
