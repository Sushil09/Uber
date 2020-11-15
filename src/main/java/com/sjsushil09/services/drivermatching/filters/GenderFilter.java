package com.sjsushil09.services.drivermatching.filters;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.Gender;

import java.util.List;
import java.util.stream.Collectors;

public class GenderFilter implements DriverFilter{
    public List<Driver> filter(List<Driver> drivers, Booking booking){
        Gender passengerGender=booking.getPassenger().getGender();

        return drivers.stream().filter(driver -> {
           Gender driverGender=driver.getGender();
           return !driverGender.equals(Gender.MALE) || passengerGender.equals(Gender.MALE);
        }).collect(Collectors.toList());
    }
}
