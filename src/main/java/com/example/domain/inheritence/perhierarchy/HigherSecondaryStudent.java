package com.example.domain.inheritence.perhierarchy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by bijoy on 17/8/16.
 */
@Entity
@DiscriminatorValue(value = "XII")
public class HigherSecondaryStudent extends SecondaryStudent{
    @Temporal(value = TemporalType.DATE)
    @Column(name = "higher_secondary_pass_out_date")
    private Date higherSecondaryPassOutDate;

    public HigherSecondaryStudent(){}

    public HigherSecondaryStudent(String name, Date secondaryPassOutDate, Date higherSecondaryPassOutDate) {
        super(name, secondaryPassOutDate);
        this.higherSecondaryPassOutDate = higherSecondaryPassOutDate;
    }

    public Date getHigherSecondaryPassOutDate() {
        return higherSecondaryPassOutDate;
    }

    public void setHigherSecondaryPassOutDate(Date higherSecondaryPassOutDate) {
        this.higherSecondaryPassOutDate = higherSecondaryPassOutDate;
    }
}
