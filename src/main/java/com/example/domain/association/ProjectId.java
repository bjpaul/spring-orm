package com.example.domain.association;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by bijoy on 17/8/16.
 */
@Embeddable
public class ProjectId implements Serializable{
    private Long id;
    private String name;

    public ProjectId(){}

    public ProjectId(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectId)) return false;

        ProjectId projectId = (ProjectId) o;

        if (!getId().equals(projectId.getId())) return false;
        return getName().equals(projectId.getName());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProjectId{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
