package com.sjsushil09.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AuditTable{

    @Column(unique = true,nullable = false)
    private String name;

    private String description;
}


//Role based authentication
//permission based authentication