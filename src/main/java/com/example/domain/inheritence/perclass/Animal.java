package com.example.domain.inheritence.perclass;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "animal")
@AttributeOverride(name = "speciesId", column = @Column(name = "animal_id"))
public class Animal extends Species {
    @Column(name = "hair_color")
    private String hairColor;

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }
}
