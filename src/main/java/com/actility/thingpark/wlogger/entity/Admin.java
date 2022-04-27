package com.actility.thingpark.wlogger.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Admin extends EntityBase {

  @NotNull
  @Length(max = 255)
  @Column(nullable = false, length = 255)
  private String login;

  @NotNull
  @Length(max = 512)
  @Column(nullable = false, length = 512)
  private String password;

  public Admin(String login, String password) {
    this.login = login;
    this.password = password;
  }

  public Admin() {
  }

  public String getLogin() {
    return login;
  }

  public Admin setLogin(String login) {
    this.login = login;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public Admin setPassword(String password) {
    this.password = password;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
