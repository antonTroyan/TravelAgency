package by.troyan.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Review class. Extended from entity
 */

@javax.persistence.Entity
@Table(name = "reviews")
@Scope("prototype")
@Data
public class Review extends Entity {

    @Id
    @Column(name = "id")
    private String id;

    @OneToOne
    @JoinColumn(name = "tour_id")
    @NotNull
    private Tour tour;

    @OneToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "content")
    @NotNull
    @Length(min = 50, max = 500)
    private String content;
}
