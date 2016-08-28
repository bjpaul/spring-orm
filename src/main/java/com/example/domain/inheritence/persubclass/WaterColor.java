package com.example.domain.inheritence.persubclass;

import javax.persistence.*;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "water_color")
@PrimaryKeyJoinColumn(name = "color_id", referencedColumnName = "color_id")
public class WaterColor extends Color{
    @Lob
    @Column(name = "description")
    private String waterColorDescription;

    public String getWaterColorDescription() {
        return waterColorDescription;
    }

    public void setWaterColorDescription(String waterColorDescription) {
        this.waterColorDescription = waterColorDescription;
    }
}
