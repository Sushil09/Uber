package com.sjsushil09.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "paymentreceipt")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReceipt extends AuditTable{
    private Double amount;

    @ManyToOne
    private PaymentGateway paymentGateway;

    private String details;

}
