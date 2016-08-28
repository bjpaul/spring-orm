package com.example.domain.inheritence.perclass;

import javax.persistence.*;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "species")
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long speciesId;
    @Column(name = "species_name")
    private String name;

    public Long getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Long speciesId) {
        this.speciesId = speciesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
