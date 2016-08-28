package com.example.domain.inheritence.superclass;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "gift_product")
public class GiftProduct extends Product{
    @Lob
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
