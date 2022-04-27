package com.actility.thingpark.wlogger.auth;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

  private final UserType type;
  private final String id;
  private final String firstName;
  private final String lastName;
  private final String language;
  private final String oidcUserID;

  public User(UserType userType, String id, String firstName, String lastName, String language, String oidcUserID) {
    this.type = userType;
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.language = language;
    this.oidcUserID = oidcUserID;
  }

  public String getId() {
    return id;
  }

  public String getOidcUserID() {
    return oidcUserID;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String getLanguage() {
    return language;
  }

  public UserType getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) &&
            Objects.equals(firstName, user.firstName) &&
            Objects.equals(lastName, user.lastName) &&
            Objects.equals(language, user.language);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, language);
  }
}
