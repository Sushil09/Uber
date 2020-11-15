package com.sjsushil09.services.drivermatching.filters;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Driver;

import java.util.List;

public interface DriverFilter {
    List<Driver> filter(List<Driver> drivers, Booking booking);
}
