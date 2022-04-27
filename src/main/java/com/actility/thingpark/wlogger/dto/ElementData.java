package com.actility.thingpark.wlogger.dto;

public class ElementData implements Element {
  public final String name;
  public final String value;

  public ElementData(Builder builder) {
    this.name = builder.name;
    this.value = builder.value;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String name;
    private String value;

    private Builder() {}

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder value(String value) {
      this.value = value;
      return this;
    }

    public ElementData build() {
      return new ElementData(this);
    }
  }
}
