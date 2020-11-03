package com.sjsushil09.repository;

import com.sjsushil09.model.NamedLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NamedLocationRepository extends JpaRepository<NamedLocation,Long> {
}
