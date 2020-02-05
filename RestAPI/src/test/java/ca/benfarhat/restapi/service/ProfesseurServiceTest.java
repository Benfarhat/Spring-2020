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
import ca.benfarhat.restapi.model.Professeur;
import ca.benfarhat.restapi.model.Salle;
import ca.benfarhat.restapi.repository.ProfesseurRepository;
import ca.benfarhat.restapi.repository.SalleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RestApiApplication.class,
	H2Config.class})
class ProfesseurServiceTest {
	
	@Autowired
	ProfesseurRepository professeurRepository;
	
	@Autowired
	SalleRepository salleRepository;

	@Autowired
	ProfesseurService professeurService;

	private List<Professeur> professeurs = new ArrayList<>();
	private List<Salle> salles = new ArrayList<>();
	private List<Long> listId = new ArrayList<>();
	private List<String> listName = new ArrayList<>();
	private List<Long> listIdP = new ArrayList<>();
	private List<String> listNameP = new ArrayList<>();
	
	@BeforeEach
	void setup() throws Exception {
		for(int i = 1; i <= 100; i++) {
			Professeur professeur = new Professeur("Professeur " + i);
			if (i % 5 != 0) {
				professeur.setLastSalleId((long) i);
			} else {

				professeur.setLastSalleId(null);
			}
			professeurs.add(professeur);
		}
		
		professeurRepository.saveAll(professeurs);
		
		professeurs = professeurService.findAll();
		professeurs.stream().forEach(s -> {
			listId.add(s.getId());
			listName.add(s.getName());
		});

		

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
		salles = salleRepository.findAll();
		salles.stream().forEach(s -> {
			listIdP.add(s.getId());
			listNameP.add(s.getName());
		});
		

		
	}
	
	@AfterEach
	void tearDown() {
		professeurRepository.deleteAll();
		professeurs = new ArrayList<Professeur>();

		salleRepository.deleteAll();
		salles = new ArrayList<Salle>();
	}
	
	@Test
	void testContext() {
		assertThat(professeurService).isNotNull();
	}

	@Test
	void testAjouter() throws ProfesseurException {
		String name = "Test";
		Long id = professeurService.ajouter(new Professeur(name));
		
		Professeur professeur = professeurService.findById(id);
		assertThat(professeur).isNotNull();
		assertThat(professeur.getId()).isEqualTo(id);
		assertThat(professeur.getName()).isEqualTo(name);
		assertThat(professeur.getLastSalleId()).isNull();
	}

	@Test
	void testEditer() throws ProfesseurException {
		String name = "Lorem Ipsum";
		String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus feugiat enim non nibh lobortis tempor quis eu orci. Vivamus vel molestie nisl, vestibulum mattis eros. Fusce blandit tristique nisi nec consectetur. Quisque et consectetur lectus, id tristique diam. Morbi id elit bibendum, placerat est in, efficitur est. Maecenas a imperdiet ante. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum accumsan magna sed pharetra tempus. Praesent eget maximus nibh. Maecenas sollicitudin, tellus nec egestas efficitur, quam lectus accumsan nisl, at dignissim dui lorem a metus.";
		assertThat(professeurService.findByName(name)).isNull();

		assertThat(professeurService.findByName("Professeur 1").getDescription()).isNull();
		
		Professeur professeur = professeurService.findById(listId.get(30));
		professeur.setDescription(description);
		
		professeurService.editer(professeur);
		assertThat(professeurService.findByName(professeur.getName()).getDescription()).isNotNull();
		
	}

	@Test
	void testRetirer() throws ProfesseurException {
		Long id = professeurs.get(0).getId();
		Long otherId = professeurs.get(4).getId();
		assertThat(professeurService.findById(id)).isNotNull();
		
		Throwable thrown = catchThrowable(() -> professeurService.retirer(id) );
			

		assertThat(thrown).isInstanceOf(ProfesseurException.class)
		.hasNoCause()
		.hasStackTraceContaining("Professeur utilisé");              

			
		professeurService.retirer(otherId);
		assertThat(professeurService.findAll().size()).isEqualTo(99);
	}

	@Test
	void testFindById() throws ProfesseurException {
		Long id = professeurs.get(0).getId();
		assertThat(professeurService.findById(id)).isNotNull();
		
		professeurs.stream().map(Professeur::getId).forEach(
				index -> assertThatCode(() -> professeurService.findById(index)).doesNotThrowAnyException());
		
	}

	@Test
	void testFindByName() {
		String name = "Lorem Ipsum";
		professeurService.ajouter(new Professeur(name));
		assertThat(professeurService.findByName(name)).isNotNull();
	}

	@Test
	void testFindAll() {
		assertThat(professeurService.findAll().size()).isEqualTo(100);
	}

	@Test
	void testReserver() throws ProfesseurException, SalleException {
		professeurService.reserver(listId.get(50), listIdP.get(16));
		assertThat(professeurService.findById(listId.get(50)).getLastSalleId()).isEqualTo(listIdP.get(16));
	}

	@Test
	void testVerifier() throws ProfesseurException, SalleException {
		professeurService.reserver(listId.get(50), listIdP.get(51));
		assertThat(professeurService.verifier(listId.get(50), listIdP.get(51))).isTrue();
		assertThat(professeurService.verifier(listId.get(51), listIdP.get(51))).isFalse();
		assertThat(professeurService.verifier(listId.get(50), listIdP.get(50))).isFalse();
	}

	@Test
	void testLibre() throws ProfesseurException, SalleException {
		assertThat(professeurService.libre(listId.get(4))).isTrue();
		professeurService.reserver(listId.get(4), listIdP.get(16));
		assertThat(professeurService.libre(listId.get(4))).isFalse();
	}

	@Test
	void testLiberer() throws ProfesseurException, SalleException {
		assertThat(professeurService.libre(listId.get(8))).isFalse();
		assertThat(professeurService.libre(listId.get(9))).isTrue();
		professeurService.reserver(listId.get(9), listIdP.get(16));
		assertThat(professeurService.libre(listId.get(9))).isFalse();
		professeurService.liberer(listId.get(9));
		assertThat(professeurService.libre(listId.get(9))).isTrue();
	}

	@Test
	void testFindAllLibre() {
		assertThat(professeurService.findAllLibre().size()).isEqualTo(20);
	}

}
