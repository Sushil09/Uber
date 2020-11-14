package com.sjsushil09.services;

import com.sjsushil09.model.Booking;
import com.sjsushil09.model.DateUtils;
import com.sjsushil09.repository.BookingRepository;
import com.sjsushil09.services.messagequeue.MQMessage;
import com.sjsushil09.services.messagequeue.MessageQueue;
import com.sjsushil09.services.notifications.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class SchedulingService{
    @Autowired
    MessageQueue messageQueue;

    @Autowired
    ConstantService constantService;

    @Autowired
    LocationTrackingService locationTrackingService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingService bookingService;

    Set<Booking> scheduleBookings=new HashSet<>();

    @Scheduled(fixedRate = 1000)
    public void consume(){
        MQMessage passedMessage=messageQueue.consumeMessage(constantService.getDriverMatchingTopicName());
        if(passedMessage==null)
            return;
     Message message=(Message) passedMessage;
     schedule(message.getBooking());
    }

    private void schedule(Booking booking) {
        scheduleBookings.add(booking);
    }

    @Scheduled(fixedRate = 60000)
    public void process(){
        Set<Booking> newScheduledBooking=new HashSet<>();
        for(Booking booking:scheduleBookings){
            if(DateUtils.addMinutes(new Date(),constantService.getBookingProcessBeforeTime()).after(booking.getScheduledTime()))
                bookingService.acceptBooking(booking.getDriver(),booking);
            else
                newScheduledBooking.add(booking);
        }
        scheduleBookings=newScheduledBooking;
    }
    @Getter @Setter @AllArgsConstructor
    public static class Message implements MQMessage {
        private Booking booking;
    }

}
