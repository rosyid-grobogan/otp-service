package com.rg.microservices.otpservice.service;

import com.rg.microservices.otpservice.db.entity.TempOTP;
import com.rg.microservices.otpservice.db.repository.TempOTPRepository;
import com.rg.microservices.otpservice.dto.EmailDto;
import com.rg.microservices.otpservice.dto.RegisterCheckDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Log4j2
@Service
public class OTPService {
    private final TempOTPRepository tempOTPRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;

    @Autowired
    public OTPService(TempOTPRepository tempOTPRepository, RedisTemplate redisTemplate, ChannelTopic channelTopic) {
        this.tempOTPRepository = tempOTPRepository;
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
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

        // send message broker
        sendEmail(email, "Kode verifikasi Anda: " + randomOTP);
    }

    private void sendEmail(String to, String body) {
        log.debug("to: {}, body: {}", to, body);
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(to);
        emailDto.setSubject("Kode Verifikasi");
        emailDto.setBody(body);
        redisTemplate.convertAndSend(channelTopic.getTopic(), emailDto);
    }

    private String generateOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }

}
