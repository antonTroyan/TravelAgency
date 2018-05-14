package by.troyan.entity;

import lombok.Data;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Tour class. Extended from entity
 */

@javax.persistence.Entity
@Table(name = "tours")
@Scope("prototype")
@Data
public class Tour extends Entity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "photo")
    private Byte [] photo;

    @Temporal(value=TemporalType.DATE)
    @Column(name = "date")
    @NotNull
    private Date date;

    @Column(name = "duration")
    @NotNull
    private Integer duration;

    @OneToOne
    @JoinColumn(name = "country_id")
    @NotNull
    private Country country;

    @OneToOne
    @JoinColumn(name = "hotel_id")
    @NotNull
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    private TourType tourType;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    @NotNull
    private Double cost;
}
