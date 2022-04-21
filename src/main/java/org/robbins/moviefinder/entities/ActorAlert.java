package org.robbins.moviefinder.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "useractoralerts", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueUserAndActor", columnNames = { "fk_user", "actorId" }) })
@EntityListeners(AuditingEntityListener.class)
public class ActorAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user")
    private User user;
    
    private Long actorId;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private long modifiedDate;

    public ActorAlert() {
    }

    public ActorAlert(User user, Long actorId) {
        this.user = user;
        this.actorId = actorId;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Long getActorId() {
        return actorId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

}
