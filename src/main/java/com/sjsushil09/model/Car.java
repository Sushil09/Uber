package com.sjsushil09.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "car")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car extends AuditTable{
    @ManyToOne
    private Color color;

    @OneToOne
    private Driver driver;

    private String plateNumber;

    private String brandModel;

    @Enumerated(value = EnumType.STRING)
    private CarType carType;

}
