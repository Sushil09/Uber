package com.sjsushil09.services;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Passenger;
import org.springframework.stereotype.Service;


public interface PassengerMatchingService {

    public void cancelByPassenger(Booking booking, Passenger passenger);

}
