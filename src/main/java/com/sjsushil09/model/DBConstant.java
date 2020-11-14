package com.sjsushil09.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dbconstant")
public class DBConstant extends AuditTable{

    @Column(unique = true,nullable = false)
    private String name;
    private String value;
}
