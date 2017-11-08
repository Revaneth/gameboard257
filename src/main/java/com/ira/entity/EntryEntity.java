package com.ira.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "entries")
@NamedQueries({
        @NamedQuery(name = "entries.findAll", query = "SELECT e FROM EntryEntity e")
})
public class EntryEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntryEntity.class);

    @Id
    @Column(name = "entryId")
    @org.hibernate.annotations.Type(type="pg-uuid")
    private UUID id;

    @Column(name = "userId")
    @org.hibernate.annotations.Type(type="pg-uuid")
    private UUID userId;

    @Column(name = "gameId")
    @org.hibernate.annotations.Type(type="pg-uuid")
    private UUID gameId;

    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }

       public EntryEntity() {
       }

    public EntryEntity(UUID id, UUID userId, UUID gameId) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {return userId;}

    public UUID getGameId(){return gameId;}

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("userId", userId)
                .add("gameId", gameId)
                .toString();
    }

}