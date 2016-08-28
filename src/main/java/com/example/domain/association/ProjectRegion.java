package com.example.domain.association;

import javax.persistence.*;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "region")
@IdClass(value = ProjectRegionId.class)
public class ProjectRegion {

    @Id
    @Column(name = "region_code")
    private String regionCode;

    @Id
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumns({
            @JoinColumn(name = "project_id", referencedColumnName = "project_id"),
            @JoinColumn(name = "project_name", referencedColumnName = "project_name")
    })
    private Project project;

    public ProjectRegion() {
    }

    public ProjectRegion(String regionCode) {
        this.regionCode = regionCode;
    }

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


}
