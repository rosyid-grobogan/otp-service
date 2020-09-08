package com.rg.microservices.otpservice.controller;

import com.rg.microservices.otpservice.dto.RegisterCheckDto;
import com.rg.microservices.otpservice.service.OTPService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class OTPController {
    private final OTPService otpService;

    @Autowired
    public OTPController(OTPService otpService) {
        this.otpService = otpService;
    }


    @PostMapping("/request")
    public ResponseEntity<?> requestOTP(@RequestBody RegisterCheckDto register) {
        log.debug("request OTP: {}", register);
        otpService.requestOTP(register);

        return ResponseEntity.ok().build();
    }
}
