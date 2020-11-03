package com.sjsushil09.services;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.Passenger;
import org.springframework.stereotype.Service;


public interface BookingService {

    void createBooking(Booking booking);

    void cancelByDriver(Driver driver, Booking booking);

    void acceptBooking(Driver driver, Booking booking);

    void cancelByPassenger(Booking booking, Passenger passenger);
}
