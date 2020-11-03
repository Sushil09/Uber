package com.sjsushil09.services;

import com.sjsushil09.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulingServiceImpl implements SchedulingService {
    @Autowired
    BookingService bookingService;

    @Override
    public void schedule(Booking booking) {
        //if it is time to assign a driver
        bookingService.acceptBooking(booking.getDriver(),booking);
    }

    public static void main(String[] args) {
        //consumer
        //These things will be running in separate process
    }
}
