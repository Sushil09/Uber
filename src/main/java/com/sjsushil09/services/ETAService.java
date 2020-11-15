package com.sjsushil09.services;

import com.sjsushil09.model.ExactLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ETAService {
    @Autowired
    private ConstantService constantService;

    public int getETAMinutes(ExactLocation lastKnownLocation, ExactLocation pickup) {
        return (int) (60.0 * lastKnownLocation.distanceKm(pickup) / constantService.getDefaultETASpeedKmph());
    }
}
