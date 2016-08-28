package com.example.domain.association;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@Table(name = "address")
@Cacheable
@Cache(region = "address", usage = CacheConcurrencyStrategy.READ_WRITE)
@NaturalIdCache(region = "address_by_natural_id")
public class Address {
    @Id
    @Column(name = "employee_address_id")
    private Long id;
    @NaturalId
    private Long pincode;
    private String street;
    @NaturalId
    private String city;
    private String state;

    @MapsId
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch =  FetchType.LAZY,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "employee_fk",
            referencedColumnName = "employee_id"
    )
    @Cache(region = "employee",usage = CacheConcurrencyStrategy.READ_WRITE)
    private Employee employee;

    public Address(){}

    public Address(Long pincode, String street, String city, String state) {
        this.pincode = pincode;
        this.street = street;
        this.city = city;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", pincode=" + pincode +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public void addEmployee(Employee employee){
        this.setEmployee(employee);
        employee.setAddress(this);
    }
}
