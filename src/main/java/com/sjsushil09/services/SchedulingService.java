package com.sjsushil09.services;

import com.sjsushil09.model.Booking;
import org.springframework.stereotype.Service;

@Service
public interface SchedulingService {
    void schedule(Booking booking);
}
