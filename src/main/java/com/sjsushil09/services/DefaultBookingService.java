package com.sjsushil09.services;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.BookingStatus;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DefaultBookingService implements BookingService {


    @Autowired
    OTPService otpService;

    @Autowired
    SchedulingService schedulingService;

    @Override
    public void createBooking(Booking booking) {

        //if the ride is scheduled
        if(booking.getStartTime().after(new Date())){
            booking.setBookingStatus(BookingStatus.SCHEDULED);
            {
                //producer
                //use a task queue to push this task
                schedulingService.schedule(booking);
            }
        }
        else {
            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
            otpService.sendRideStartOTP(booking.getRideStartOTP());
        }
    }

    @Override
    public void cancelByDriver(Driver driver, Booking booking) {

    }

    @Override
    public void acceptBooking(Driver driver, Booking booking) {

    }

    @Override
    public void cancelByPassenger(Booking booking, Passenger passenger) {

    }
}
