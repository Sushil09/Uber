package com.sjsushil09.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "driver")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends AuditTable{

    @OneToOne
    private Account account;

    private Gender gender;
    private String name;

    @OneToOne(mappedBy = "driver") // store this only in car table but not in driver tables
    private Car car;

    private String licenseDetails;

    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;

    @Enumerated(value = EnumType.STRING)
    private DriverApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "driver")
    private List<Booking> booking=new ArrayList<>();

    private boolean isAvailable;

    private String activeCity;

    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private ExactLocation home;

}
