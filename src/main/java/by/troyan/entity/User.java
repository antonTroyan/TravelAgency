package by.troyan.entity;

import lombok.Data;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User class. Extends from entity
 */

@javax.persistence.Entity
@Table(name = "users")
@Scope("prototype")
@Data
public class User extends Entity {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "login")
  @NotNull
  private String login;

  @Column(name = "password")
  @NotNull
  private String password;

  @OneToMany
  @JoinColumn(name = "tours")
  private List<Tour> tours;

  @OneToMany
  @JoinColumn(name = "reviews")
  private List<Review> reviews;
}
