package com.example.domain.inheritence.persubclass;

import javax.persistence.*;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "color")
@Inheritance(strategy = InheritanceType.JOINED)
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "color_id")
    private Long id;

    @Column(name = "color_name")
    private String colorName;

    public Color(){}

    public Color(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Color{" +
                "id=" + id +
                ", colorName='" + colorName + '\'' +
                '}';
    }
}
