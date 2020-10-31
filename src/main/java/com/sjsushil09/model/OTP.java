package com.sjsushil09.model;

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
}

//have a global expiry time