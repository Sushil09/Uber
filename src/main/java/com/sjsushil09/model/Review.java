package com.sjsushil09.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;


@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review extends AuditTable{

    private Integer ratingOutOfFive;

    private String note;
}
