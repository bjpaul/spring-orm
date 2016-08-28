package com.example.domain.inheritence.persubclass;

import javax.persistence.*;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "powder_color")
@PrimaryKeyJoinColumn(name = "color_id", referencedColumnName = "color_id")
public class PowderColor extends Color{
    @Lob
    @Column(name = "description")
    private String powderColorDescription;

    public String getPowderColorDescription() {
        return powderColorDescription;
    }

    public void setPowderColorDescription(String powderColorDescription) {
        this.powderColorDescription = powderColorDescription;
    }
}
