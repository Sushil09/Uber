package com.sjsushil09.controllers;

import com.sjsushil09.exceptions.InvalidDriverException;
import com.sjsushil09.exceptions.InvalidPassengerException;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.ExactLocation;
import com.sjsushil09.model.Passenger;
import com.sjsushil09.repository.DriverRepository;
import com.sjsushil09.repository.PassengerRepository;
import com.sjsushil09.services.ConstantService;
import com.sjsushil09.services.locationtracking.LocationTrackingService;
import com.sjsushil09.services.messagequeue.MQMessage;
import com.sjsushil09.services.messagequeue.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationTrackingController {

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    LocationTrackingService locationTrackingService;

    @Autowired
    MessageQueue messageQueue;

    @Autowired
    ConstantService constantService;

    @PutMapping("/driver/{driverId}")
    public void updateDriverLocation(@PathVariable Long driverId,
                                     @RequestBody ExactLocation data){
    Driver driver=getDriverFromId(driverId);
    ExactLocation location=ExactLocation.builder()
                           .longitude(data.getLongitude())
                           .latitude(data.getLatitude())
                           .build();
    messageQueue.sendMessage(constantService.getLocationTrackingTopicName(),
                             new LocationTrackingService.Message(driver,location));
        locationTrackingService.updateDriverLocation(driver,location);
    }

    @PutMapping("/passenger/{passengerId}")
    public void updatePassengerLocation(@PathVariable Long passengerId,
                                        @RequestBody ExactLocation location){
        Passenger passenger=getPassengerFromId(passengerId);
        passenger.setLastKnownLocation(ExactLocation.builder()
                                       .longitude(location.getLongitude())
                                       .latitude(location.getLatitude())
                                        .build());

        passengerRepository.save(passenger);
    }


    //Utility function to get a particular driver
    public Driver getDriverFromId(Long driverId){
        Optional<Driver> driver=driverRepository.findById(driverId);
        if(driver.isEmpty())
            throw new InvalidDriverException("No such driver with id="+driverId);
        return driver.get();
    }

    //Utility function to get a particular passenger
    public Passenger getPassengerFromId(Long passengerId){
        Optional<Passenger> passenger=passengerRepository.findById(passengerId);
        if(passenger.isEmpty())
            throw new InvalidPassengerException("No such passenger with id="+passengerId);
        return passenger.get();
    }
}
