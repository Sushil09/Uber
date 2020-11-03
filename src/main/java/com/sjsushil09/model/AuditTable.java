package com.sjsushil09.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

//implements serializible to transfer json obj via net

@MappedSuperclass // dont create table for this
@EntityListeners(AuditingEntityListener.class)// so that createAt and updateAt works
@Getter @Setter
public abstract class AuditTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP) // as per db use that->Jpa
    @LastModifiedDate // hibernate property
    //@PrePersist -hibernate(Creation time, id is assigned)
    //@PreUpdate ->hibernate only when a change is made in db
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTable that = (AuditTable) o;
        if(id==null || that.id ==null)
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id==null?0: id.hashCode();
    }
}
