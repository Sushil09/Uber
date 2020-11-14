package com.sjsushil09.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConstantService {

    final DBconstantRepository dBconstantRepository;

    final private Map<String,String> constants=new HashMap<>();

    private static final Integer TEN_MINUTES=60*10*1000;

    public ConstantService(DBconstantRepository dBconstantRepository){
        this.dBconstantRepository=dBconstantRepository;
        loadConstantFromDB();
    }

    @Scheduled(fixedRate = TEN_MINUTES)
    private void loadConstantFromDB(){
        dBconstantRepository.findAll().forEach(dbconstant->constants.put(dbconstant.getName(),dbconstant.getValue()));
    }

    public Integer getRideStartOTPExpiryMinutes(){
        return Integer.parseInt(constants.getOrDefault("rideStartOTPExpiryMinutes","3600000"));
    }

    public String getSchedulingTopicName() {
        return constants.getOrDefault("schedulingTopicName","SchedulingServiceTopic");

    }

    public String getDriverMatchingTopicName() {
        return constants.getOrDefault("driverMatchingTopicName","driverMatchingTopic");
    }

    public int getMaxWaitTimeForPreviousRide() {
        return Integer.parseInt(constants.getOrDefault("rideStartOTPExpiryMinutes","900000"));
    }

    public Integer getBookingProcessBeforeTime() {
        return Integer.parseInt(constants.getOrDefault("bookingProcessBeforeTime", "900000"));
    }
}
