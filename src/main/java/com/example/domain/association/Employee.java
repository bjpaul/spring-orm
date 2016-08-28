package com.example.domain.association;

import com.example.domain.listners.EmployeeListners;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "employee")
@NamedQuery(name = "emp.empList",query = "select e.id, e.name from Employee e where e.id IN (:ids)")
@OptimisticLocking(type = OptimisticLockType.DIRTY)
//@EntityListeners(EmployeeListners.class)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id")
    private Long id;
    @Column(name = "employee_name")
    private String name;
    @Version
    private int version;
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "employee"
    )
    @org.hibernate.annotations.Cache(region = "address",usage = CacheConcurrencyStrategy.READ_WRITE)
    private Address address;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "lead_id",
            referencedColumnName = "employee_id"
    )
    private Employee lead;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "lead"
    )
    private Set<Employee> employees;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "project_leads",
            joinColumns = {
              @JoinColumn(name = "lead_id", referencedColumnName = "employee_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "owning_project_id", referencedColumnName = "project_id"),
                    @JoinColumn(name = "owning_project_name", referencedColumnName = "project_name")
            }
    )
    @org.hibernate.annotations.Cache(region = "project",usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Project> projects;
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "project_employees",
            joinColumns = {
                    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "project_id", referencedColumnName = "project_id"),
                    @JoinColumn(name = "project_name", referencedColumnName = "project_name")
            }
    )
    @org.hibernate.annotations.Cache(region = "project",usage = CacheConcurrencyStrategy.READ_WRITE)
    private Project project;
    public Employee(){}

    public Employee(String name) {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Employee getLead() {
        return lead;
    }

    public void setLead(Employee lead) {
        this.lead = lead;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @BatchSize(size = 1)
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void addAddress(Address address){
        this.setAddress(address);
        address.setEmployee(this);
    }

    public void addAncestor(Employee employee){
        this.setLead(employee);
        Set<Employee> employees =employee.getEmployees();
        if(employees == null){
            employees = new HashSet<Employee>();
        }
        employees.add(this);
    }

    public void addDescendant(Employee employee){
        if(this.employees == null){
            this.employees = new HashSet<Employee>();
        }
        this.employees.add(employee);
        employee.setLead(this);
    }

    public void addOwningProject(Project project) {
        if (this.getProjects() == null) {
            this.setProjects(new HashSet<Project>());
        }
        this.getProjects().add(project);
        if (project.getLeads() == null) {
            project.setLeads(new HashSet<Employee>());
        }
        project.getLeads().add(this);
    }
    public void addProject(Project project) {
        this.project = project;
        if (project.getEmployees() == null) {
            project.setEmployees(new HashSet<Employee>());
        }
        project.getEmployees().add(this);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}
