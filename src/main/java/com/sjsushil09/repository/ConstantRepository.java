package com.sjsushil09.repository;

import com.sjsushil09.model.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstantRepository  extends JpaRepository<Constants,Long> {
}
