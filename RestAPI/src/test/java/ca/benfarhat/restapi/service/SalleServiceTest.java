package ca.benfarhat.restapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.benfarhat.restapi.RestApiApplication;
import ca.benfarhat.restapi.config.H2Config;
import ca.benfarhat.restapi.exception.ProfesseurException;
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
	private List<Long> listId = new ArrayList<>();
	private List<String> listName = new ArrayList<>();
	
	@BeforeEach
	void setup() throws Exception {
		for(int i = 1; i <= 100; i++) {
			Salle salle = new Salle("Salle " + i);
			if (i % 5 != 0) {
				salle.setProfesseur((long) i);
			} else {

				salle.setProfesseur(null);
			}
			salles.add(salle);
		}
		
		salleRepository.saveAll(salles);
		
		salles = salleService.findAll();
		salles.stream().forEach(s -> {
			listId.add(s.getId());
			listName.add(s.getName());
		});
		
	}
	
	@AfterEach
	void tearDown() {
		salleRepository.deleteAll();
		salles = new ArrayList<Salle>();
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
		String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus feugiat enim non nibh lobortis tempor quis eu orci. Vivamus vel molestie nisl, vestibulum mattis eros. Fusce blandit tristique nisi nec consectetur. Quisque et consectetur lectus, id tristique diam. Morbi id elit bibendum, placerat est in, efficitur est. Maecenas a imperdiet ante. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum accumsan magna sed pharetra tempus. Praesent eget maximus nibh. Maecenas sollicitudin, tellus nec egestas efficitur, quam lectus accumsan nisl, at dignissim dui lorem a metus.";
		assertThat(salleService.findByName(name)).isNull();

		assertThat(salleService.findByName("Salle 1").getDescription()).isNull();
		
		Salle salle = salleService.findById(listId.get(30));
		salle.setDescription(description);
		
		salleService.editer(salle);
		assertThat(salleService.findByName(salle.getName()).getDescription()).isNotNull();
		
	}

	@Test
	void testRetirer() throws SalleException {
		Long id = salles.get(0).getId();
		Long otherId = salles.get(4).getId();
		assertThat(salleService.findById(id)).isNotNull();
		
		Throwable thrown = catchThrowable(() -> salleService.retirer(id) );
			

		assertThat(thrown).isInstanceOf(SalleException.class)
		.hasNoCause()
		.hasStackTraceContaining("Salle utilisé");              

			
		salleService.retirer(otherId);
		assertThat(salleService.findAll().size()).isEqualTo(99);
	}

	@Test
	void testFindById() throws SalleException {
		Long id = salles.get(0).getId();
		assertThat(salleService.findById(id)).isNotNull();
		
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
	void testReserver() throws SalleException, ProfesseurException {
		salleService.reserver(listId.get(50), 16L);
		assertThat(salleService.findById(listId.get(50)).getProfesseur()).isEqualTo(16L);
	}

	@Test
	void testVerifier() throws SalleException, ProfesseurException {
		salleService.reserver(listId.get(50), 51L);
		assertThat(salleService.verifier(listId.get(50), 51L)).isTrue();
		assertThat(salleService.verifier(listId.get(51), 51L)).isFalse();
		assertThat(salleService.verifier(listId.get(50), 50L)).isFalse();
	}

	@Test
	void testLibre() throws SalleException, ProfesseurException {
		assertThat(salleService.libre(listId.get(4))).isTrue();
		salleService.reserver(listId.get(4), 16L);
		assertThat(salleService.libre(listId.get(4))).isFalse();
	}

	@Test
	void testLiberer() throws SalleException, ProfesseurException {
		assertThat(salleService.libre(listId.get(8))).isFalse();
		assertThat(salleService.libre(listId.get(9))).isTrue();
		salleService.reserver(listId.get(9), 16L);
		assertThat(salleService.libre(listId.get(9))).isFalse();
		salleService.liberer(listId.get(9));
		assertThat(salleService.libre(listId.get(9))).isTrue();
	}

	@Test
	void testFindAllLibre() {
		assertThat(salleService.findAllLibre().size()).isEqualTo(20);
	}

}
