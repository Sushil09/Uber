package com.sjsushil09.model;

import com.sjsushil09.exceptions.InvalidActionForBooking;
import com.sjsushil09.exceptions.InvalidActionForBookingStateException;
import com.sjsushil09.exceptions.InvalidOTPException;
import lombok.*;

import javax.persistence.*;
import java.util.*;

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

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Driver> notifiedDrivers = new HashSet<>();

    @OneToOne
    private Review reviewByDriver;

    @OneToOne
    private PaymentReceipt paymentReceipt;

    private BookingStatus bookingStatus;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns =@JoinColumn(name = "exact_location_id"),
            indexes = {@Index(columnList = "booking_id")}
    )
    List<ExactLocation> routes=new ArrayList<>();

    @OrderColumn(name ="location_index")
    @OneToMany
    List<ExactLocation> completedRoute=new ArrayList<>();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;


    @Temporal(value = TemporalType.TIMESTAMP)
    private Date scheduledTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expectedCompletionTime;

    private Long totalDistanceMeters;//always mention the units while storing this

    @OneToOne
    private OTP rideStartOTP;

    public void startRide(OTP otp,int rideStartOTPExpiryMinutes) {
        if(!bookingStatus.equals(BookingStatus.CAR_ARRIVED))
            throw new InvalidActionForBooking("Cannot start the ride before the driver has arrived");

        //logic to start after correct otp has been entered
        if(!rideStartOTP.validateOTP(otp,rideStartOTPExpiryMinutes))//,RIDE_START_OTP_EXPIRY_MINUTES))//load this from db constants
            throw new InvalidOTPException();

        bookingStatus=BookingStatus.IN_RIDE;
    }

    public void endRide() {
        if(!bookingStatus.equals(BookingStatus.IN_RIDE))
            throw new InvalidActionForBooking("The ride hasn't started yet");
        driver.setActiveBooking(null);
        bookingStatus=BookingStatus.COMPLETED;
    }

    public boolean canChangeRoute() {
        return  bookingStatus.equals(BookingStatus.CAR_ARRIVED)
               || bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER)
               || bookingStatus.equals(BookingStatus.IN_RIDE)
               || bookingStatus.equals(BookingStatus.SCHEDULED)
               || bookingStatus.equals(BookingStatus.REACHING_PICKUP_LOCATION);
    }

    public boolean needsDriver() {
        return bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER);
    }

    public void cancel() throws InvalidActionForBookingStateException {
         if(bookingStatus.equals(BookingStatus.REACHING_PICKUP_LOCATION)
         || bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER)
         || bookingStatus.equals(BookingStatus.SCHEDULED)
         || bookingStatus.equals(BookingStatus.CAR_ARRIVED)){
             throw new InvalidActionForBookingStateException("Cannot cancel the booking now. If the ride is in progress, please ask the driver to cancel the ride");
         }
         bookingStatus=BookingStatus.CANCELLED;
         driver=null;
         notifiedDrivers.clear();

    }

    public ExactLocation getPickupLocation() {
        return routes.get(0);
    }
}
