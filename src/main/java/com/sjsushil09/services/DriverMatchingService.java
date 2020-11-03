package com.sjsushil09.services;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.Passenger;
import org.springframework.stereotype.Service;

@Service
public interface DriverMatchingService {
    void acceptBooking(Booking booking, Driver driver);
    void cancelByDriver(Booking booking, Driver driver);
    void cancelByPassenger(Passenger passenger, Booking booking);

    //figure out nearby drivers
    //send notifications to user
    void assignDriver(Booking booking);

}
