package com.sjsushil09.repository;

import com.sjsushil09.model.ExactLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExactLocationRepository extends JpaRepository<ExactLocation,Long> {
}
