package com.example.domain.association;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "practice")
public class Practice {
    @Id
    private String name;
    @Lob
    private String description;

    @Transient
    @Formula(value = "Department + name")
    @Column(name = "department_name")
    private String departmentName;

    public Practice(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Practice{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
