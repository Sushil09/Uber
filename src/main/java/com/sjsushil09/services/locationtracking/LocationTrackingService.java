package com.sjsushil09.services.locationtracking;

import com.sjsushil09.model.Driver;
import com.sjsushil09.model.ExactLocation;
import com.sjsushil09.repository.DriverRepository;
import com.sjsushil09.services.ConstantService;
import com.sjsushil09.services.messagequeue.MQMessage;
import com.sjsushil09.services.messagequeue.MessageQueue;
import com.sjsushil09.services.quadtree.QuadTree;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationTrackingService {

    @Autowired
    MessageQueue messageQueue;

    @Autowired
    ConstantService constantService;

    @Autowired
    DriverRepository driverRepository;

    QuadTree world=new QuadTree();

    public List<Driver> getDriversNearLocation(ExactLocation pickup) {

        return world.findNeighboursIds(
          pickup.getLatitude(),
          pickup.getLongitude(),
          constantService.getMaxDistanceForDriverMatching())
          .stream()
          .map(driverId->driverRepository.findById(driverId).orElseThrow())
          .collect(Collectors.toList());
    }

    public void updateDriverLocation(Driver driver, ExactLocation location) {
        world.removeNeighbour(driver.getId());
        world.addNeighbour(driver.getId(),location.getLatitude(),location.getLongitude());
        driver.setLastKnownLocation(location);
        driverRepository.save(driver);
    }

    @Scheduled(fixedRate = 1000)
    public void consume(){
        MQMessage passedMessage=messageQueue.consumeMessage(constantService.getDriverMatchingTopicName());
        if(passedMessage==null)
            return;
        Message message=(Message) passedMessage;
        updateDriverLocation(message.getDriver(),message.getLocation());
    }

    @Getter @Setter @AllArgsConstructor
    public static class Message implements MQMessage{
        private Driver driver;
        private ExactLocation location;
    }

}