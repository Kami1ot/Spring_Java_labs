package com.example.Barber_lab6.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String initials;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime sessionDateTime;
    private String service_name;
    private String master_name;

    public Clients(String initials, LocalDateTime sessionDateTime, String service_name, String master_name) {
        this.initials = initials;
        this.sessionDateTime = sessionDateTime;
        this.service_name = service_name;
        this.master_name = master_name;
    }

    public Clients() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String setinitials() {
        return initials;
    }

    public void setinitials(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public LocalDateTime getSessionDateTime() {
        return sessionDateTime;
    }

    public void setSessionDateTime(LocalDateTime sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }
}

