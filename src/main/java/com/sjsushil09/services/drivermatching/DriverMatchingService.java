package com.sjsushil09.services.drivermatching;


import com.sjsushil09.model.Booking;
import com.sjsushil09.model.Driver;
import com.sjsushil09.model.ExactLocation;
import com.sjsushil09.model.Passenger;
import com.sjsushil09.repository.BookingRepository;
import com.sjsushil09.services.ConstantService;
import com.sjsushil09.services.ETAService;
import com.sjsushil09.services.drivermatching.filters.DriverFilter;
import com.sjsushil09.services.drivermatching.filters.ETABasedFilter;
import com.sjsushil09.services.drivermatching.filters.GenderFilter;
import com.sjsushil09.services.locationtracking.LocationTrackingService;
import com.sjsushil09.services.messagequeue.MQMessage;
import com.sjsushil09.services.messagequeue.MessageQueue;
import com.sjsushil09.services.notifications.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverMatchingService {

    final MessageQueue messageQueue;

    final ConstantService constantService;

    final LocationTrackingService locationTrackingService;

    final NotificationService notificationService;

    final BookingRepository bookingRepository;

    final ETAService etaService;

    final List<DriverFilter> filters=new ArrayList<>();

    public DriverMatchingService(MessageQueue messageQueue,
                                 ConstantService constantService,
                                 LocationTrackingService locationTrackingService,
                                 NotificationService notificationService,
                                 BookingRepository bookingRepository,
                                 ETAService etaService) {
        this.messageQueue = messageQueue;
        this.constantService = constantService;
        this.locationTrackingService = locationTrackingService;
        this.notificationService = notificationService;
        this.bookingRepository = bookingRepository;
        this.etaService=etaService;
        filters.add(new ETABasedFilter(etaService,constantService));
        filters.add(new GenderFilter());
    }

    @Scheduled(fixedRate = 1000)
    public void consume(){
        MQMessage passedMessage=messageQueue.consumeMessage(constantService.getDriverMatchingTopicName());
        if(passedMessage==null)
            return;
        Message message=(Message) passedMessage;
        findNearbyDrivers(message.getBooking());
    }

    private void findNearbyDrivers(Booking booking) {
        ExactLocation pickup=booking.getPickupLocation();
        List<Driver> drivers=locationTrackingService.getDriversNearLocation(pickup);
        if(drivers.isEmpty()){
            //add surge fee and send notifiaction to driver
            notificationService.notify(booking.getPassenger().getPhoneNumber(),"No cabs near you!");
            return;
        }
        notificationService.notify(booking.getPassenger().getPhoneNumber(),String.format("Contacting %s cabs near you",drivers.size()));

        //filter the driver

        drivers.forEach(driver -> {
            notificationService.notify(driver.getPhoneNumber(),"Booking near you "+booking.toString());
            driver.getAcceptableBooking().add(booking);
        });
        bookingRepository.save(booking);

    }


    public void acceptBooking(Booking booking, Driver driver) {

    }


    public void cancelByDriver(Booking booking, Driver driver) {

    }


    public void cancelByPassenger(Passenger passenger, Booking booking) {

    }


    public void assignDriver(Booking booking) {

    }

    public static void main(String[] args) {
        //acting as a consumer
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message implements MQMessage{
        private Booking booking;

        @Override
        public String toString() {
            return String.format("Need to find driver for % booking",booking.toString());
        }
    }


}
