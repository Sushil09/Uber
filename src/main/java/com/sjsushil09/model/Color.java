package com.sjsushil09.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "color")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Color  extends AuditTable{
    @Column(unique = true,nullable = false) //unique column
    private String name;
}
