package com.sjsushil09.model;

import com.sjsushil09.exceptions.InvalidOTPException;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "otp")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTP extends AuditTable{
    private String code;
    private String sendToNumber;

    public static OTP createOTP(String phoneNumber){
        return OTP.builder().
                code("0000").       //plugin random number generator
                sendToNumber(phoneNumber).
                build();
    }

    public boolean validateOTP(OTP otp,int rideStartOTPExpiryMinutes){
        if(!code.equals(otp.getCode()))
            return false;
        //if the createAt + expiryMinutes>currentTime, then valid
        //else not
        return true;
    }
}

//have a global expiry time