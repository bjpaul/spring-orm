package com.example.domain.association;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by bijoy on 17/8/16.
 */
@Embeddable
public class ProjectRegionId implements Serializable {
    private String regionCode;
    private Project project;

    public ProjectRegionId(){}

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "ProjectRegionId{" +
                "regionCode='" + regionCode + '\'' +
                ", project=" + project +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectRegionId)) return false;

        ProjectRegionId that = (ProjectRegionId) o;

        if (!getRegionCode().equals(that.getRegionCode())) return false;
        return getProject().equals(that.getProject());

    }

    @Override
    public int hashCode() {
        int result = getRegionCode().hashCode();
        result = 31 * result + getProject().hashCode();
        return result;
    }
}
