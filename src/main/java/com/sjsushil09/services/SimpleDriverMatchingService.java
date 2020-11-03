package com.sjsushil09.services;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.Passenger;
import org.springframework.stereotype.Service;

@Service
public class SimpleDriverMatchingService implements DriverMatchingService {
    @Override
    public void acceptBooking(Booking booking, Driver driver) {

    }

    @Override
    public void cancelByDriver(Booking booking, Driver driver) {

    }

    @Override
    public void cancelByPassenger(Passenger passenger, Booking booking) {

    }

    @Override
    public void assignDriver(Booking booking) {

    }

    public static void main(String[] args) {
        //acting as a consumer
    }
}
