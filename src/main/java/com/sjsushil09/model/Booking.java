package com.sjsushil09.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "booking")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends AuditTable{

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Driver driver;

    @Enumerated(value = EnumType.STRING)
    private BookingType bookingType;

    @OneToOne
    private Review reviewByUser;

    @OneToOne
    private Review reviewByDriver;

    @OneToOne
    private PaymentReceipt paymentReceipt;

    private BookingStatus bookingStatus;

    @OneToMany
    List<ExactLocation> routes=new ArrayList<>();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;

    private Long totalDistanceMeters;//always mention the units while storing this

    @OneToOne
    private OTP rideStartOTP;


}
