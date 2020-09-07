package com.rg.microservices.otpservice.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(value = "otp", timeToLive = 300)
public class TempOTP {
    @Id
    private String id;
    private String otp;
    private String email;
}
