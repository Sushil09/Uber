package com.sjsushil09.services.drivermatching.filters;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.ExactLocation;
import com.sjsushil09.services.ConstantService;
import com.sjsushil09.services.ETAService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ETABasedFilter implements DriverFilter{

    private final ETAService etaService;
    private final ConstantService constantService;

    public ETABasedFilter(ETAService etaService,ConstantService constantService){
        this.etaService=etaService;
        this.constantService=constantService;
    }

    public List<Driver> filter(List<Driver> drivers, Booking booking) {
//        if (!getConstants().getIsETABasedFilterEnabled()) return drivers;

        ExactLocation pickup = booking.getPickupLocation();
        return drivers.stream().filter(driver -> {
            return etaService.getETAMinutes(driver.getLastKnownLocation(), pickup) <= constantService.getMaxDriverETAMinutes();
        }).collect(Collectors.toList());
    }
}
