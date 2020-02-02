package ca.benfarhat.restapi.model;
@MappedSuperclass
public class AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Override
  public boolean equals(Object obj) { … }

  @Override
  public int hashCode() { … }
}