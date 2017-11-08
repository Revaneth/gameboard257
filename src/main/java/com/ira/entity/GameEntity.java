package com.ira.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "games")
@NamedQueries({
        @NamedQuery(name = "games.findAll", query = "SELECT g FROM GameEntity g") })
public class GameEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEntity.class);

    @Id
    @Column(name = "gameId")
    @org.hibernate.annotations.Type(type="pg-uuid")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }

    public GameEntity() {
    }

    public GameEntity(UUID id, String titleOfGame, String typeOfGame ) {
        this.id = id;
        this.title = titleOfGame;
        this.type = typeOfGame;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("gameTitle", title)
                .add("gameType", type)
                .toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

}
