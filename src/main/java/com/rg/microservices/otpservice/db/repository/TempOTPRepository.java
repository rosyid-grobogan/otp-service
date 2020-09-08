package com.rg.microservices.otpservice.db.repository;

import com.rg.microservices.otpservice.db.entity.TempOTP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempOTPRepository extends CrudRepository<TempOTP, String> {
    TempOTP getFirstByEmail(String email);
}
