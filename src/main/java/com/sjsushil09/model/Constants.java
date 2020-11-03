package com.sjsushil09.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "constants")
public class Constants extends AuditTable{
    private String name;
    private String value;

    public Long getNumbers(){
        return Long.parseLong(value);
    }

}
