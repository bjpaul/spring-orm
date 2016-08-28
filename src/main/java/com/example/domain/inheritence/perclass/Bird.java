package com.example.domain.inheritence.perclass;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "bird")
@AttributeOverride(name = "speciesId", column = @Column(name = "bird_id"))
public class Bird extends Species {

    @Column(name = "wing_size")
    private String wingSize;

    public String getWingSize() {
        return wingSize;
    }

    public void setWingSize(String wingSize) {
        this.wingSize = wingSize;
    }
}
