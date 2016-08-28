package com.example.domain.listners;

import com.example.domain.association.Employee;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by bijoy on 18/8/16.
 */
@Component
public class EmployeeListners {

    @PrePersist
    public void beforePersist(Employee employee){
        System.out.println("Prepersist -> "+employee);
    }

    @PostPersist
    public void afterPersist(Employee employee){
        System.out.println("Postpersist -> "+employee);
    }

    @PreUpdate
    public void beforeUpdate(Employee employee){
        System.out.println("beforeUpdate -> "+employee);
    }

    @PostUpdate
    public void afterUpdate(Employee employee){
        System.out.println("afterUpdate -> "+employee);
    }
}
