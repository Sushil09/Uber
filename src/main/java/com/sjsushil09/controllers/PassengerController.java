package com.sjsushil09.controllers;

import com.sjsushil09.exceptions.InvalidBookingException;
import com.sjsushil09.exceptions.InvalidPassengerException;
import com.sjsushil09.model.*;
import com.sjsushil09.repository.BookingRepository;
import com.sjsushil09.repository.PassengerRepository;
import com.sjsushil09.repository.ReviewRepository;
import com.sjsushil09.services.BookingService;
import com.sjsushil09.services.drivermatching.DriverMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    final PassengerRepository passengerRepository;

    final BookingRepository bookingRepository;

    final DriverMatchingService driverMatchingService;

    final ReviewRepository reviewRepository;

    final BookingService bookingService;

    public PassengerController(PassengerRepository passengerRepository,
                               BookingRepository bookingRepository,
                               DriverMatchingService driverMatchingService,
                               ReviewRepository reviewRepository,
                               BookingService bookingService) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.driverMatchingService = driverMatchingService;
        this.reviewRepository = reviewRepository;
        this.bookingService = bookingService;
    }

    //->To get a particular passenger details
    @GetMapping("/{passengerId}")
    public Passenger getPassengerDetails(@RequestParam(name = "passengerId") Long passengerId){
        Passenger passenger=getPassengerFromId(passengerId);
        return passenger;
    }

    //->To retrieve all bookings of a passenger
    @GetMapping("/{passengerId}/bookings")
    public List<Booking> getAllBookings(@RequestParam(name = "passengerId") Long passengerId){
        Passenger passenger=getPassengerFromId(passengerId);
        return passenger.getBookings();
    }

    //->to get a particular booking of a passenger
    @GetMapping("/{passengerId}/booking/{bookingId}")
    public Booking getBooking(@RequestParam(name = "passengerId") Long passengerId,
                              @RequestParam(name = "bookingId") Long bookingId){

      Passenger passenger=getPassengerFromId(passengerId);
      Booking booking=getDerivedBookingFromId(bookingId,passenger);
      return booking;
    }

    //to accept a particular ride
    @PostMapping("/{passengerId}/bookings")
    public void requestBooking(@RequestParam(name = "passengerId") Long passengerId,
                              @RequestBody Booking data){
        Passenger passenger=getPassengerFromId(passengerId);
        List<ExactLocation> route=new ArrayList<>();
        data.getRoutes().forEach(location->{
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });

        Booking booking=Booking.builder()
                        .rideStartOTP(OTP.createOTP(passenger.getPhoneNumber()))
                        .routes(route)
                .passenger(passenger)
                .bookingType(data.getBookingType())
                .scheduledTime(data.getScheduledTime())
                        .build();
        bookingService.createBooking(booking);
//        bookingRepository.save(booking);
//        passengerRepository.save(passenger);
    }

    @PostMapping("/{passengerId}/bookings/{bookingid}")
    public void retryBooking(@RequestParam(name = "passengerId") Long passengerId,
                             @RequestParam(name = "bookingId") Long bookingId){
        Passenger passenger=getPassengerFromId(passengerId);
        Booking booking=getDerivedBookingFromId(bookingId,passenger);
        bookingService.retryBooking(booking);
    }

    //To change the route of a particular booking
    @PatchMapping("/{passengerId}/booking/{bookingId}")
    public void updateRoute(@RequestParam(name = "passengerId") Long passengerId,
                            @RequestParam(name = "bookingId") Long bookingId,
                            @RequestBody Booking data){

        Passenger passenger=getPassengerFromId(passengerId);
        Booking booking=getDerivedBookingFromId(bookingId,passenger);
        List<ExactLocation> route=new ArrayList<>();
        data.getRoutes().forEach(location->{
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });
        bookingService.updateRoute(booking,route);
    }


    //to cancel a booking
    @DeleteMapping("/{passengerId}/booking/{bookingId}")
    public void cancelbooking(@RequestParam(name = "passengerId") Long passengerId,
                              @RequestParam(name = "bookingId") Long bookingId){

        Passenger passenger=getPassengerFromId(passengerId);
        Booking booking=getDerivedBookingFromId(bookingId,passenger);
        bookingService.cancelByPassenger(booking,passenger);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/end")
    public void setReview(@RequestParam(name = "passengerId") Long passengerId,
                          @RequestParam(name = "bookingId") Long bookingId,
                          @RequestBody Review data){
        Passenger passenger=getPassengerFromId(passengerId);
        Booking booking=getDerivedBookingFromId(bookingId,passenger);
        Review review=Review.builder()
                      .note(data.getNote())
                      .ratingOutOfFive(data.getRatingOutOfFive())
                      .build();
        booking.setReviewByPassenger(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }

    //Utility function to get a particular passenger
    public Passenger getPassengerFromId(Long passengerId){
        Optional<Passenger> passenger=passengerRepository.findById(passengerId);
        if(passenger.isEmpty())
            throw new InvalidPassengerException("No such passenger with id="+passengerId);
        return passenger.get();
    }

    //Utility function to get a particular booking
    public Booking getDerivedBookingFromId(Long bookingId, Passenger passenger){
        Optional<Booking> optionalBooking=bookingRepository.findById(bookingId);
        if(optionalBooking.isEmpty())
            throw new InvalidBookingException("No such booking with id="+bookingId);
        Booking booking=optionalBooking.get();

        if(!booking.getPassenger().equals(passenger))
            throw new InvalidBookingException("Passenger has no booking associated to booking with id="+bookingId);
        return booking;
    }



}
