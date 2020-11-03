package com.sjsushil09.controllers;

import com.sjsushil09.exceptions.InvalidBookingException;
import com.sjsushil09.exceptions.InvalidDriverException;
import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.OTP;
import com.sjsushil09.model.Review;
import com.sjsushil09.repository.BookingRepository;
import com.sjsushil09.repository.DriverRepository;
import com.sjsushil09.repository.ReviewRepository;
import com.sjsushil09.services.BookingService;
import com.sjsushil09.services.DriverMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    BookingRepository bookingRepository;

//    @Autowired
//    DriverMatchingService driverMatchingService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    BookingService bookingService;

    //->To get a particular driver details
    @GetMapping("/{driverId}")
    public Driver getDriverDetails(@RequestParam(name = "driverId") Long driverId){
        Driver driver=getDriverFromId(driverId);
        return driver;
    }

    //->to update the driver availability
    @PutMapping("/{driverId}")
    public void changeAvailability(@RequestParam(name = "driverId") Long driverId,
                                   @RequestParam(name = "available") Boolean available){

       Driver driver=getDriverFromId(driverId);
       driver.setAvailable(available);
       driverRepository.save(driver);
    }

    //->To retrieve all bookings of a driver
    @GetMapping("/{driverId}/bookings")
    public List<Booking> getAllBookings(@RequestParam(name = "driverId") Long driverId){
        Driver driver=getDriverFromId(driverId);
        return driver.getBookings();
    }

    //->to get a particular booking of a driver
    @GetMapping("/{driverId}/booking/{bookingId}")
    public Booking getBooking(@RequestParam(name = "driverId") Long driverId,
                              @RequestParam(name = "bookingId") Long bookingId){

      Driver driver=getDriverFromId(driverId);
      Booking booking=getDerivedBookingFromId(bookingId,driver);
      return booking;
    }

    //to accept a particular ride
    @PutMapping("/{driverId}/booking/{bookingId}")
    public void acceptbooking(@RequestParam(name = "driverId") Long driverId,
                              @RequestParam(name = "bookingId") Long bookingId){
        Driver driver=getDriverFromId(driverId);
        Booking booking=getDerivedBookingFromId(bookingId,driver);
        bookingService.acceptBooking(driver,booking);
//        driverMatchingService.acceptBooking(booking,driver);
    }

    //to cancel a booking
    @DeleteMapping("/{driverId}/booking/{bookingId}")
    public void cancelbooking(@RequestParam(name = "driverId") Long driverId,
                              @RequestParam(name = "bookingId") Long bookingId){

        Driver driver=getDriverFromId(driverId);
        Booking booking=getDerivedBookingFromId(bookingId,driver);
        //push this to task queue-producer->resposibilty of bookingService
        bookingService.cancelByDriver(driver,booking);
//        driverMatchingService.cancelByDriver(booking,driver);
    }

    //to strat the ride only after confirming the otp
    @PatchMapping("{driverId}/bookings/{bookingId}/start")
    public void startRide(@RequestParam(name = "driverId") Long driverId,
                          @RequestParam(name = "bookingId") Long bookingId,
                          @RequestBody OTP otp){
        Driver driver=getDriverFromId(driverId);
        Booking booking=getDerivedBookingFromId(bookingId,driver);
        booking.startRide(otp);
        bookingRepository.save(booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/end")
    public void endRide(@RequestParam(name = "driverId") Long driverId,
                        @RequestParam(name = "bookingId") Long bookingId,
                        @RequestBody OTP otp){
        Driver driver=getDriverFromId(driverId);
        Booking booking=getDerivedBookingFromId(bookingId,driver);
        booking.endRide();
        bookingRepository.save(booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/review")
    public void setReview(@RequestParam(name = "driverId") Long driverId,
                          @RequestParam(name = "bookingId") Long bookingId,
                          @RequestBody Review data){
        Driver driver=getDriverFromId(driverId);
        Booking booking=getDerivedBookingFromId(bookingId,driver);
        Review review=Review.builder()
                      .note(data.getNote())
                      .ratingOutOfFive(data.getRatingOutOfFive())
                      .build();
        booking.setReviewByDriver(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }

    //Utility function to get a particular driver
    public Driver getDriverFromId(Long driverId){
        Optional<Driver> driver=driverRepository.findById(driverId);
        if(driver.isEmpty())
            throw new InvalidDriverException("No such driver with id="+driverId);
        return driver.get();
    }

    //Utility function to get a particular booking
    public Booking getDerivedBookingFromId(Long bookingId, Driver driver){
        Optional<Booking> optionalBooking=bookingRepository.findById(bookingId);
        if(optionalBooking.isEmpty())
            throw new InvalidBookingException("No such booking with id="+bookingId);
        Booking booking=optionalBooking.get();

        if(!booking.getDriver().equals(driver))
            throw new InvalidBookingException("Driver has no booking associated to booking with id="+bookingId);
        return booking;
    }



}
