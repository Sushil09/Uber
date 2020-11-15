package com.sjsushil09.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="passenger")
public class Passenger extends AuditTable{

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @OneToOne
    private Booking activeBooking = null;

    @OneToMany(mappedBy = "passenger")//pls don't store the info of booking inside passenger table
    //rather inside booking table i will have a column called passenger_id
    private List<Booking> bookings=new ArrayList<>();

    @Temporal(value=TemporalType.DATE)
    private Date dateOfBirth;

    private String phoneNumber;

    @OneToOne
    private ExactLocation home;
    @OneToOne
    private ExactLocation work;
    @OneToOne
    private ExactLocation lastKnownLocation;


}
