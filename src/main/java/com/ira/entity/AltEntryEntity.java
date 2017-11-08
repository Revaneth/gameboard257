package com.ira.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class AltEntryEntity {

    @Id
    @Column(name = "entryId")
    @org.hibernate.annotations.Type(type="pg-uuid")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "title")
    private String title;

    public AltEntryEntity(){}

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }
}
