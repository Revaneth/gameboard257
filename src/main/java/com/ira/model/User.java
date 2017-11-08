package com.ira.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

import static com.ira.GameBoardApp.r;
import static com.ira.GameBoardApp.regex;

@ApiModel
(value = "User")
public class User {
    private static Logger logger = LoggerFactory.getLogger(User.class);

    @JsonProperty(value = "userId")
    private UUID id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("lastName")
    private String lastName = null;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    public User() {
        //
    }

    public User(
            String username, String firstName, String lastName,
            String email, String password
    ) {
        this.id = UUID.randomUUID();
        if (username != null) username = username.replaceAll(regex, r);
        if (firstName != null) firstName = firstName.replaceAll(regex, r);
        if (lastName != null) lastName = lastName.replaceAll(regex, r);
        if (email != null) email = email.replaceAll("[;%+ //\'^:,*{}/[/]]", r);
        logger.info("\n\n\n " + username + " " + firstName + " " + lastName + " " + email);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(UUID id, String username,
                String firstName, String lastName,
                String email, String password) {

        this.id = id;
        if (username != null) username = username.replaceAll(regex, r);
        if (firstName != null) firstName = firstName.replaceAll(regex, r);
        if (lastName != null) lastName = lastName.replaceAll(regex, r);
        if (email != null) email = email.replaceAll("[;%+ //\'^:,*{}/[/]]", r);
        logger.info("\n\n\n " + username + " " + firstName + " " + lastName + " " + email);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     * Get userId
     *
     * @return userId
     **/
    @ApiModelProperty(value = "User Id")
    public UUID getUserId() {
        return id;
    }

    /**
     * Get username
     *
     * @return username
     **/
    @ApiModelProperty(value = "User nickname/login/username", required = true)
    public String getUsername() {
        return username;
    }

    /**
     * Get firstName
     *
     * @return firstName
     **/
    @ApiModelProperty(value = "User first name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get lastName
     *
     * @return lastName
     **/
    @ApiModelProperty(value = "User last name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get email
     *
     * @return email
     **/
    @ApiModelProperty(value = "User email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get password
     *
     * @return password
     **/
    @ApiModelProperty(value = "User password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(this.id, user.id) &&
                Objects.equals(this.username, user.username) &&
                Objects.equals(this.firstName, user.firstName) &&
                Objects.equals(this.lastName, user.lastName) &&
                Objects.equals(this.email, user.email) &&
                Objects.equals(this.password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return "class User {\n" +
                "    userId: " + toIndentedString(id) + "\n" +
                "    username: " + toIndentedString(username) + "\n" +
                "    firstName: " + toIndentedString(firstName) + "\n" +
                "    lastName: " + toIndentedString(lastName) + "\n" +
                "    email: " + toIndentedString(email) + "\n" +
                "    password: " + toIndentedString(password) + "\n" +
                "}";
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}//class end