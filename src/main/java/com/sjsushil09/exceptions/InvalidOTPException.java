package com.sjsushil09.exceptions;

public class InvalidOTPException extends UberException {
    public InvalidOTPException(){
        super("Invalid OTP");
    }
}
