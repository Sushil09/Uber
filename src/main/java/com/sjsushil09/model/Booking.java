package com.sjsushil09.model;

import com.sjsushil09.exceptions.InvalidActionForBooking;
import com.sjsushil09.exceptions.InvalidOTPException;
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
@Table(name = "booking", indexes = {
        @Index(columnList = "passenger_id"),
        @Index(columnList = "driver_id")
})
public class Booking extends AuditTable{

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Driver driver;

    @Enumerated(value = EnumType.STRING)
    private BookingType bookingType;

    @OneToOne
    private Review reviewByPassenger;

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

    public void startRide(OTP otp) {
        if(!bookingStatus.equals(BookingStatus.CAR_ARRIVED))
            throw new InvalidActionForBooking("Cannot start the ride before the driver has arrived");

        //logic to start after correct otp has been entered
        if(!rideStartOTP.validateOTP(otp))//,RIDE_START_OTP_EXPIRY_MINUTES))//load this from db constants
            throw new InvalidOTPException();

        bookingStatus=BookingStatus.IN_RIDE;

    }

    public void endRide() {
        if(!bookingStatus.equals(BookingStatus.IN_RIDE))
            throw new InvalidActionForBooking("The ride hasn't started yet");
        bookingStatus=BookingStatus.COMPLETED;
    }
}
