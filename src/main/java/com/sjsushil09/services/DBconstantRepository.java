package com.sjsushil09.services;

import com.sjsushil09.model.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBconstantRepository extends JpaRepository<Constants,String> {
}
