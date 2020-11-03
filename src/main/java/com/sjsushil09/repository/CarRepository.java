package com.sjsushil09.repository;

import com.sjsushil09.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository <Car,Long>{
}
