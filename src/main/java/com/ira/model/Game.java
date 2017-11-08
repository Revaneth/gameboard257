package com.ira.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import java.util.UUID;

import static com.ira.GameBoardApp.r;
import static com.ira.GameBoardApp.regex;

/**
 * Game
 */
@ApiModel
(value = "Game")
public class Game   {
  @JsonProperty("gameId")
  private UUID id;

  @JsonProperty("titleOfGame")
  private String title;

  @JsonProperty("typeOfGame")
  private String type = null;

  public Game (){}

  public Game( String titleOfGame, String typeOfGame) {
    this.id = UUID.randomUUID();
    if (titleOfGame != null) titleOfGame = titleOfGame.replaceAll(regex, r);
    if (typeOfGame != null) typeOfGame = typeOfGame.replaceAll(regex, r);
    this.title = titleOfGame;
    this.type = typeOfGame;
  }

    public Game(UUID id,
                String titleOfGame, String typeOfGame) {
      if (titleOfGame != null) titleOfGame = titleOfGame.replaceAll(regex, r);
      if (typeOfGame != null) typeOfGame = typeOfGame.replaceAll(regex, r);
      this.id = id;
      this.title = titleOfGame;
      this.type = typeOfGame;
    }
  /**
   * Get gameId
   * @return gameId
  **/
  @ApiModelProperty(required = true)
  public UUID getGameId() {
    return id;
  }

   /**
   * Get titleOfGame
   * @return titleOfGame
  **/
  @ApiModelProperty(required = true)
  public String getTitleOfGame() {
    return title;
  }

  public void setTitleOfGame(String titleOfGame) {
    this.title = titleOfGame;
  }

   /**
   * Get typeOfGame
   * @return typeOfGame
  **/
  @ApiModelProperty
  public String getTypeOfGame() {
    return type;
  }

  public void setTypeOfGame(String typeOfGame) {
    this.type = typeOfGame;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Game game = (Game) o;
    return Objects.equals(this.id, game.id) &&
            Objects.equals(this.title, game.title) &&
            Objects.equals(this.type, game.type);
  }

  @Override
  public int hashCode() {
      return Objects.hash(id, title, type);
  }

  @Override
  public String toString() {
      return  "class Game {\n" +
              "    gameId: " + toIndentedString(id) + "\n" +
              "    titleOfGame: " + toIndentedString(title) + "\n" +
              "    typeOfGame: " + toIndentedString(type) + "\n" +
              "}";
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}//class end

