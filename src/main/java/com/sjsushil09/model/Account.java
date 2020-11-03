package com.sjsushil09.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AuditTable{

    @Column(unique = true,nullable = false)
    private String userName;
    private String passWord;

    //get all the roles when someone fetches an account
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles=new ArrayList<>();
}
