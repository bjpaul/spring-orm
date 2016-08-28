package com.example.domain.association;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "project")
@Cacheable
@org.hibernate.annotations.Cache(region = "project",usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "project_id")),
            @AttributeOverride(name = "name", column = @Column(name = "project_name"))
    })
    private ProjectId projectId;
    private String description;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "projects"
    )
    @org.hibernate.annotations.Cache(region = "employee",usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Employee> leads;
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "project"
    )
    private Set<Employee> employees;


    public Project() {
    }

    public Project(Long id, String name,String description) {
        this.projectId = new ProjectId(id, name);
        this.description = description;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ProjectId projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Employee> getLeads() {
        return leads;
    }

    public void setLeads(Set<Employee> leads) {
        this.leads = leads;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public void addLead(Employee employee) {
        if (this.leads == null) {
            this.leads = new HashSet<Employee>();
        }
        this.leads.add(employee);
        if (employee.getProjects() == null) {
            employee.setProjects(new HashSet<Project>());
        }
        employee.getProjects().add(this);
    }
    public void addEmployee(Employee employee) {
        if (this.getEmployees() == null) {
            this.setEmployees(new HashSet<Employee>());
        }
        this.getEmployees().add(employee);
        employee.setProject(this);
    }
    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", description='" + description + '\'' +
                '}';
    }
}
