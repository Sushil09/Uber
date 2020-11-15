package com.sjsushil09.services;

import com.sjsushil09.exceptions.InvalidActionForBooking;
import com.sjsushil09.exceptions.InvalidActionForBookingStateException;
import com.sjsushil09.model.*;
import com.sjsushil09.repository.BookingRepository;
import com.sjsushil09.repository.DriverRepository;
import com.sjsushil09.repository.PassengerRepository;
import com.sjsushil09.services.drivermatching.DriverMatchingService;
import com.sjsushil09.services.messagequeue.MessageQueue;
import com.sjsushil09.services.notifications.NotificationService;
import com.sjsushil09.services.otp.OTPService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    OTPService otpService;

    @Autowired
    SchedulingService schedulingService;

    @Autowired
    MessageQueue messageQueue;

    @Autowired
    ConstantService constantService;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    DriverRepository driverRepository;

    public void createBooking(Booking booking) {

        //if the ride is scheduled
        if(booking.getStartTime().after(new Date())){
            booking.setBookingStatus(BookingStatus.SCHEDULED);
//                schedulingService.schedule(booking);
            //producer
            messageQueue.sendMessage(constantService.getSchedulingTopicName(),new SchedulingService.Message(booking));
        }
        else {
            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
            otpService.sendRideStartOTP(booking.getRideStartOTP());
            messageQueue.sendMessage(constantService.getDriverMatchingTopicName(),new DriverMatchingService.Message(booking));
        }
        bookingRepository.save(booking);
        passengerRepository.save(booking.getPassenger());
    }

    public void cancelByDriver(Driver driver, Booking booking) {
        booking.setDriver(null);
        driver.setActiveBooking(null);
        driver.getAcceptableBooking().remove(booking);
        notificationService.notify(booking.getPassenger().getPhoneNumber(),"Reassigning driver");
        notificationService.notify(booking.getDriver().getPhoneNumber(),"Booking has been cancelled");
        retryBooking(booking);

    }

    public void acceptBooking(Driver driver, Booking booking) {
        if(!booking.needsDriver()){
            return;
        }
        if(!driver.canAcceptBooking(constantService.getMaxWaitTimeForPreviousRide())){
            notificationService.notify(driver.getPhoneNumber(),"Cannot accept the booking");
            return;
        }
        booking.setDriver(driver);
        driver.setActiveBooking(booking);
        booking.getNotifiedDrivers().clear();
        driver.getAcceptableBooking().clear();
        notificationService.notify(booking.getPassenger().getPhoneNumber(),driver.getName()+" is arriving at pick-up location");
        notificationService.notify(booking.getDriver().getPhoneNumber(),"Booking accepted");
        bookingRepository.save(booking);
        driverRepository.save(driver);
    }

    @SneakyThrows
    public void cancelByPassenger(Booking booking, Passenger passenger) {
        try{
            booking.cancel();
            bookingRepository.save(booking);
        }
        catch (InvalidActionForBookingStateException inner) {
            notificationService.notify(booking.getPassenger().getPhoneNumber(),"Cannot cancel the ride now, if the ride is in progress, please ask the driver to end the ride");
            throw inner;
        }
    }

    public void updateRoute(Booking booking, List<ExactLocation> route) {
        if(!booking.canChangeRoute())
            throw new InvalidActionForBooking("Ride has already been completed or cancelled");

        booking.setRoutes(route);
        bookingRepository.save(booking);
        notificationService.notify(booking.getDriver().getPhoneNumber(),"Route has been updated");
    }

    public void retryBooking(Booking booking) {
        createBooking(booking);
    }
}
