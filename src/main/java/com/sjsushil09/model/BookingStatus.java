package com.sjsushil09.model;

public enum BookingStatus {
    CANCELLED("The booking has been camcelled due to some reason"),
    SCHEDULED("The booking has been scheduled for sometime"),
    ASSIGNING_DRIVER("The passenger gas requested for a booking"),
    CAR_ARRIVED("The driver has arrived at the pick-up location"),
    IN_RIDE("The ride is currently in progress"),
    COMPLETED("The ride has already been completed");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }

}
