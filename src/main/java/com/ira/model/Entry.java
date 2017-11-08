package com.ira.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import java.util.UUID;

/**
 * Entry
 */
public class Entry   {
  @JsonProperty("id")
  private UUID id;

  @JsonProperty("userId")
  private UUID userId;

    @JsonProperty("gameId")
  private UUID gameId;

    public Entry ( UUID userId, UUID gameId) {
    this.id = UUID.randomUUID();
    this.userId=userId;
    this.gameId= gameId;
  }

public Entry(){}
  public Entry ( UUID entryId , UUID userId, UUID gameId) {
    this.id = entryId;
    this.userId = userId;
    this.gameId = gameId;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "UUID id")
  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entry entry = (Entry) o;
    return Objects.equals(this.id, entry.id) &&
            Objects.equals(this.userId, entry.userId) &&
            Objects.equals(this.gameId, entry.gameId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
      return  "class Entry {\n" +
              "    entryId: " + toIndentedString(id) + "\n" +
              "    userId: " + toIndentedString(userId) + "\n" +
              "    gameId: " + toIndentedString(gameId) + "\n" +
              "}";
  }

  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

    public UUID getUserId() {
        return userId;
    }

    public UUID getGameId() {
        return gameId;
    }

}//class end

