package com.example.domain.inheritence.perhierarchy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@DiscriminatorValue(value = "X")
public class SecondaryStudent extends Student{
    @Temporal(value = TemporalType.DATE)
    @Column(name = "secondary_pass_out_date")
    private Date secondaryPassOutDate;

    public SecondaryStudent(){}

    public SecondaryStudent(String name, Date secondaryPassOutDate) {
        super(name);
        this.secondaryPassOutDate = secondaryPassOutDate;
    }

    public Date getSecondaryPassOutDate() {
        return secondaryPassOutDate;
    }

    public void setSecondaryPassOutDate(Date secondaryPassOutDate) {
        this.secondaryPassOutDate = secondaryPassOutDate;
    }


}
