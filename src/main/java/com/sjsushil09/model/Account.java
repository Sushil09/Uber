package com.sjsushil09.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AuditTable{
    private String userName;
    private String passWord;

    @ManyToMany
    private List<Role> roles=new ArrayList<>();
}
