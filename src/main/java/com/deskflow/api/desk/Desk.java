package com.deskflow.api.desk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "desk")
public class Desk {

    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String code;

    @Column(nullable = false)
    private Integer floor;

    @Column(name = "has_monitor", nullable = false)
    private boolean hasMonitor;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    protected Desk() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Integer getFloor() {
        return floor;
    }

    public boolean isHasMonitor() {
        return hasMonitor;
    }

    public boolean isActive() {
        return active;
    }
}