package by.troyan.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The type Country.
 */

@javax.persistence.Entity
@Table(name = "countries")
@Scope("prototype")
@Data
public class Country extends Entity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    @NotNull
    @Length(min = 2, max = 50)
    private String name;
}
