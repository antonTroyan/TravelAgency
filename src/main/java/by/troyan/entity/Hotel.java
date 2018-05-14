package by.troyan.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Hotel class. Extended from entity
 */

@javax.persistence.Entity
@Table(name = "hotels")
@Scope("prototype")
@Data
public class Hotel extends Entity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    @Length(min = 2, max = 50)
    @NotNull
    private String name;

    @Column(name = "phone")
    @Length(min = 2, max = 50)
    private String phone;

    @OneToOne
    @JoinColumn(name = "country_id")
    @NotNull
    private Country country;

    @Column(name = "stars")
    @Length(min = 1, max = 10)
    private Integer stars;
}
