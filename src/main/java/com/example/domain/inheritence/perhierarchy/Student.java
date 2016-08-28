package com.example.domain.inheritence.perhierarchy;

import javax.persistence.*;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "student")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "level",
        discriminatorType = DiscriminatorType.STRING
)
@DiscriminatorValue(value = "ENTRY")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roll;
    private String name;
    public Student(){}

    public Student(String name) {
        this.name = name;
    }

    public Long getRoll() {
        return roll;
    }

    public void setRoll(Long roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "roll=" + roll +
                ", name='" + name + '\'' +
                '}';
    }
}
