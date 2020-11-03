package com.sjsushil09.repository;

import com.sjsushil09.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

    Optional<Driver> findFirstByAccount_UserName(String username);
}
