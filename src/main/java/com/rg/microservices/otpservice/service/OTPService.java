package com.rg.microservices.otpservice.service;

import com.rg.microservices.otpservice.db.entity.TempOTP;
import com.rg.microservices.otpservice.db.repository.TempOTPRepository;
import com.rg.microservices.otpservice.dto.RegisterCheckDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Log4j2
@Service
public class OTPService {
    private final TempOTPRepository tempOTPRepository;

    @Autowired
    public OTPService(TempOTPRepository tempOTPRepository) {
        this.tempOTPRepository = tempOTPRepository;
    }

    public void requestOTP(RegisterCheckDto registerCheckDto) {
        String email = registerCheckDto.getEmail();

        // check OTP redis
        TempOTP otpByEmail = tempOTPRepository.getFirstByEmail(email);
        if (otpByEmail != null) {
            tempOTPRepository.delete(otpByEmail);
        }

        // generate random of number /otp
        String randomOTP = generateOTP();
        log.debug("random otp: {}", randomOTP);

        // save to redis
        TempOTP tempOTP = new TempOTP();
        tempOTP.setEmail(email);
        tempOTP.setOtp(randomOTP);

        tempOTPRepository.save(tempOTP);
    }

    private String generateOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }

}
