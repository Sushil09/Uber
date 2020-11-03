package com.sjsushil09.services;

import com.sjsushil09.model.OTP;
import org.springframework.stereotype.Service;


public interface OTPService {
    void sendPhoneNumberConfirmationOTP(OTP otp);
    void sendRideStartOTP(OTP otp);
}
