package com.sjsushil09.model;

import com.sjsushil09.exceptions.UnapprovedDriverExcepion;
import lombok.*;

import javax.persistence.*;
import java.util.*;

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

    private String phoneNumber;

    @OneToOne(mappedBy = "driver") // store this only in car table but not in driver tables
    private Car car;

    private String licenseDetails;

    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;

    @Enumerated(value = EnumType.STRING)
    private DriverApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "driver")
    private List<Booking> bookings;

    @ManyToMany(mappedBy = "notifiedDriver",cascade = CascadeType.PERSIST)
    private Set<Booking> acceptableBooking=new HashSet<>();

    private boolean isAvailable;

    private String activeCity;

    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private Booking activeBooking=null;

    @OneToOne
    private ExactLocation home;

    public void setAvailable(Boolean available){
        if(available && !approvalStatus.equals(DriverApprovalStatus.APPROVED))
            throw new UnapprovedDriverExcepion("Driver approval pending or denied" +getId());
        isAvailable=available;
    }

    public boolean canAcceptBooking(int maxWaitTimeForPreviousRide) {
        if(isAvailable && activeBooking==null)
            return true;
        return activeBooking.getExpectedCompletionTime().before(DateUtils.addMinutes(new Date(),maxWaitTimeForPreviousRide ));
        //
    }
}
